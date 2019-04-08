package net.jannekeskitalo.unity.playersessionservice.config;

import com.datastax.driver.core.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;

@Configuration
public class CassandraConfig {
    @Bean
    AsyncCassandraTemplate asyncCassandraTemplate(Session session) {
        return new AsyncCassandraTemplate(session);
    }
}
