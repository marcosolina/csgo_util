package com.ixigo.demmanager.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.demmanager.repositories.implementations.RepoProcessQueuePostgres;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactory;

@Configuration
public class Repositories {
	@Bean
	public ConnectionFactory connectionFactory() {

		// 1. using ConnectionFactories.get from url
		// ConnectionFactory factory =
		// ConnectionFactories.get("r2dbc:h2:mem:///test?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

		// 2. using r2dbc drivers provided tools to create a connection factory.

		// H2
		// see: https://github.com/spring-projects/spring-data-r2dbc/issues/269
//	        return new H2ConnectionFactory(
//	                H2ConnectionConfiguration.builder()
//	                        //.inMemory("testdb")
//	                        .file("./testdb")
//	                        .username("user")
//	                        .password("password").build()
//	        );
		//
//	        return H2ConnectionFactory.inMemory("testdb");

		// postgres
		return new PostgresqlConnectionFactory(
		// @formatter:off
	        PostgresqlConnectionConfiguration.builder()
	                .host("localhost")
	                .database("demfiles")
	                .username("REPLACE_ME")
	                .password("REPLACE_ME")
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
		        // .bindMarkers(() -> BindMarkersFactory.named(":", "", 20).create())
		        .namedParameters(true).build();
	}
	
	@Bean
	public RepoProcessQueue getRepoProcessQueue() {
		return new RepoProcessQueuePostgres();
	}
}
