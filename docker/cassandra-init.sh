cat >/import.cql <<EOF
CREATE KEYSPACE IF NOT EXISTS test WITH REPLICATION = {'class':'SimpleStrategy', 'replication_factor' : 1};
EOF

echo "Creating keyspace"
# You may add some other conditionals that fits your stuation here
until cqlsh -f /import.cql; do
  echo "cqlsh: Cassandra is unavailable to initialize - will retry later"
  sleep 2
done &

echo "Running entrypoint"
exec /docker-entrypoint.sh "$@"