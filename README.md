# Design document - player-session-service

A service that exposes a RESTful API to allow ingestion and querying of session events. Service uses Cassandra database.

***

## How to run

Instructions how to run and test this service.

### Requirements

* Docker installed and working
* JDK 8 installed

### Startup

```bash
# From repo root -folder...
./mvnw package
docker network create --driver bridge test
cd docker
docker-compose build && docker-compose up
```

### Manual API testing

* Open web browser and go to ```localhost:8080/swagger-ui.html```
* Go to "Ingestion" and upload the test data file or generate data
    * __Generator only creates data for session country search__
    * __File upload expects the format of the given test data file__
* Ingest event batches manually
* Try the query API with some player_id or country
* Testing bigger resulsets is best done with _curl_ on command line

### Running in IDE without docker

* A cassandra service needs to be available on localhost
* TEST -keyspace needs to exist in cassandra
* If cassandra is remote, configure connection properties in application.yml accordingly

### Troubleshooting docker-compose setup

* Check docker networking is working. Stop VPN.
* If Cassandra container is too slow to startup, increase sleep time in docker/Dockerfile & run compose build & up -again

***

## The Assignment

* Use ~~Python~~ and Cassandra
    * __I was given an option to use Java and I decided to do that__
* All endpoints are REST APIs
* API for receiving event batches (1-10 events / batch)
* API for fetching session starts for the last X (X is defined by the user) hours for each country
* API for fetching last 20 complete sessions for a given player
* Data older than 1 year should be discarded


## API first -approach

The design starts from the API requirements which are the following:
```
1) Post event batches (1-10 events / batch)
2) Get session starts for the last X (X is defined by the user) hours for each country
3) Get last 20 complete sessions for a given player
```

Implementation was done following the requirements. For requirement #2, getting session starts for a country, I would implement another API that allows fetching the data in one minute slices. Clients of this particular endpoint are most likely batch processing systems and they could easily initiate parallel queries to fetch the data for a full hour. Splitting the data to smaller chunks would make loading faster and more resilient. In case of failures, the client needs to reload less data due to the smaller chunk size.

## Assumptions

As the assignment doesn't give details of the access load patterns, following is assumed:

* Total event write throughput ~5M/min -> +80k/s
* Distributed to ~200 countries with very high skew
* Big reads of country session starts are infrequent and batch oriented
* Reads of last completed sessions per player are more frequent, but still significantly less than writes

### Example events

```
{
"event": "start",
"country": "FI",
"player_id": "0a2d12a1a7e145de8bae44c0c6e06629",
"session_id": "4a0c43c9-c43a-42ff-ba55-67563dfa35d4",
"ts": "2016-12-02T12:48:05.520022"
}

{
"event": "end",
"player_id": "0a2d12a1a7e145de8bae44c0c6e06629",
"session_id": "4a0c43c9-c43a-42ff-ba55-67563dfa35d4",
"ts": "2016-12-02T12:49:05.520022"
}
```

Example data,
https://cdn.unityads.unity3d.com/assignments/assignment_data.jsonl.bz2

***

## Database model

Tables and partition strategy. Without knowing the exact details of the load, the design is based on the aforementioned assumptions.

### Tables

The API requires two separate tables to effectively query the data. However, the data comes in two different events at different times which adds some complexity to the model and processing.

The first two tables are designed to fit the search queries. The third table was only used during development. __Old data can be expired by setting TTL for the tables or splitting the data to multiple temporal tables. Expiration was not implemented in this version.__

```sql
// Last sessions started by country
CREATE TABLE session_started_by_country (
    country text,
    start_hour_ts timestamp,
    bucket int,
    start_ts timestamp,
    session_id uuid,
    player_id text,
    PRIMARY KEY ((country, start_hour_ts, bucket), start_ts, session_id)
) WITH CLUSTERING ORDER BY (start_ts DESC, session_id ASC);
                       

// Session events by player. Used to search for complete player sessions.
CREATE TABLE session_by_player_id (
    player_id text,
    ts timestamp,
    country text,
    event text,
    session_id uuid,
    PRIMARY KEY (player_id, ts)
) WITH CLUSTERING ORDER BY (ts DESC);

// Table used during development to get insight on session events.
CREATE TABLE test.session_by_id (
      session_id uuid,
      start_hour timestamp,
      country text,
      end_hour timestamp,
      end_ts timestamp,
      player_id text,
      start_ts timestamp,
      PRIMARY KEY (session_id, start_hour)
  ) WITH CLUSTERING ORDER BY (start_hour DESC);

```

### Partitioning with dynamic bucketing for time-series

The used partitioning strategy is designed to maintain proper partition sizing and to avoid creating hot nodes. Usually reads should touch only one or a few partitions, but in this case the amount of data read in the country -search is quite substantial and it's unfeasible or even impossible to create single partition of that size. The required write throughput also calls for many partitions.

Event volume per country also varies significantly. Without knowing the exact numbers one can assume that a country like US alone constitutes a quarter or even more of all events. This makes it difficult to design a static time-series partitioning strategy that would always work, spread the load evenly and result to perfectly sized partitions.

To combat the heavy data skew a dynamic bucketing solution is proposed for time-series tables. The leading edge of the partition key is chosen based on the primary search predicate (_country / player_id_) which is accompanied by a time component of suitable granularity (_hour_). This alone will create one partition per _country/hour_ and introduces hot spots into the cluster, hence a dynamically adjustable bucketing-factor is required.

A third column _"bucket"_ is added to the partition key. Bucket identifier is derived from the player_id, being something like:

    player_id>>64 % BUCKETING_FACTOR
    
Bucketing factor is adjusted dynamically in certain bounds. A metric is maintained of the current event throughput and simple algorithm adjusts the factor as needed. Also an endpoint is added to the API to allow manual adjustment.

As the bucket distributes the load more evenly, it also makes querying the data a bit more complicated. For example fetching data for the last hour requires visiting all the buckets and the service needs to know which buckets exist. This could be done by defining the set of possible buckets as finite range from 1 to 1000 and querying all of them always, or maintaining a separate table for bucket metadata.

__The implemented bucketing uses a fixed size range.__

_At peak load a 25% share of the total events (US) would result to partition row count of 75k when using a bucketing factor of 1000. This falls inside the recommended limits for Cassandra._

## Production deployment considerations

Production deployment requires a load balancer in front of the service instances. For example in AWS one would create an ECS service with an application load balancer in front of the container instances. This can be configured with auto scaling to allow automatic adaptation to varying load.

Deployment of the service and the underlying infrastructure should be automated and represented as code in a version control system. Good option for cloud deployments is Terraform that supports many different cloud vendors. It can be integrated with CICD -tool like Jenkins for further automation and ease of use.

***

## Alternative architectures

Alternative implementation of this service could be done using serverless functions and API gateway. Cassandra could be replaced with a managed db service that features similar scaling properties, most notably DynamoDB or Bigtable. One could also apply the CQRS pattern and put a performant message bus like Kafka behind the rest api and use Kafka Streams or Spark Streaming to build materialized views of the event data into multiple data stores that cater for different access patterns. Domain specific realtime dashboards would query data from Cassandra while more complex analytical use cases would be a better fit for Bigquery, Snowflake or similar distributed column stores.

***

## Things to do

* Improve exception handling
* Improve rest input validation
* Add more robust data quality validation
* Timestamp handling with full event precision
* Add unit- and integration -tests to get proper test coverage
* Performance testing with real Cassandra cluster

