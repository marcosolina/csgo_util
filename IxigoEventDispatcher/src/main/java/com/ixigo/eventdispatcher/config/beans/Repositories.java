package com.ixigo.eventdispatcher.config.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.eventdispatcher.config.properties.PostgresProps;
import com.ixigo.eventdispatcher.repositories.implementations.RepoEntityEventListenerPostgres;
import com.ixigo.eventdispatcher.repositories.interfaces.RepoEntityEventListener;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
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
			                .database("event_dispatcher")
			                .username(postrgresProps.getUser())
			                .password(postrgresProps.getPassword())
//			                .codecRegistrar(
//		                		EnumCodec.builder()
//			                		.withEnum("process_status", DemProcessStatus.class)
//			                		.build()
//		            		)
			                .build()
					// @formatter:on
		);
	}

	@Bean
	public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
		var cp = ConnectionPoolConfiguration.builder(connectionFactory).initialSize(2).maxSize(20).build();
		ConnectionPool pool = new ConnectionPool(cp);
		return DatabaseClient.builder().connectionFactory(pool).build();
	}

	@Bean
	public RepoEntityEventListener getRepoEntityEventListener() {
		return new RepoEntityEventListenerPostgres();
	}
}
