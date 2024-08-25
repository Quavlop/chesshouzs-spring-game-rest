package com.chesshouzs.server.config.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableCassandraRepositories(basePackages = "com.chesshouzs.server.repository.cassandra")
public class Cassandra extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keySpace;

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints; 

    @Value("${spring.data.cassandra.port}")
    private int port;

    
    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }

    @Override
    protected int getPort() {
        return port;
    }

}
