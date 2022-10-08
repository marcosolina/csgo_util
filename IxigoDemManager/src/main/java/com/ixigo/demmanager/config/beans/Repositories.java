package com.ixigo.demmanager.config.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.demmanager.config.properties.PostgresProps;
import com.ixigo.demmanager.repositories.implementations.RepoProcessQueuePostgres;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;

@Configuration
public class Repositories {
	
	@Autowired
	private PostgresProps postrgresProps;
	
	@Bean
	public ConnectionFactory connectionFactory() {

		return new PostgresqlConnectionFactory(
		// @formatter:off
	        PostgresqlConnectionConfiguration.builder()
	                .host(postrgresProps.getHost())
	                .database("demfiles")
	                .username(postrgresProps.getUser())
	                .password(postrgresProps.getPassword())
//	                .codecRegistrar(
//                		EnumCodec.builder()
//	                		.withEnum("process_status", DemProcessStatus.class)
//	                		.build()
//            		)
	                .build()
			// @formatter:on
		);
	}

	@Bean
	public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
		return DatabaseClient.builder().connectionFactory(connectionFactory)
		        .namedParameters(true).build();
	}
	
	@Bean
	public RepoProcessQueue getRepoProcessQueue() {
		return new RepoProcessQueuePostgres();
	}
}
