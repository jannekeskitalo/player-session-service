package net.jannekeskitalo.unity.playersessionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;

@SpringBootApplication
public class PlayerSessionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlayerSessionServiceApplication.class, args);
    }

}
