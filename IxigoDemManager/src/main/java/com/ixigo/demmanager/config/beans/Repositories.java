package com.ixigo.demmanager.config.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.demmanager.config.properties.PostgresProps;
import com.ixigo.demmanager.repositories.implementations.RepoProcessQueuePostgres;
import com.ixigo.demmanager.repositories.implementations.RepoUserPostgres;
import com.ixigo.demmanager.repositories.implementations.RepoUserScorePostgres;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.ixigo.demmanager.repositories.interfaces.RepoUser;
import com.ixigo.demmanager.repositories.interfaces.RepoUserScore;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;

/**
 * Repositories configuration class
 * 
 * @author Marco
 *
 */
@Configuration
public class Repositories {
	private static final Logger _LOGGER = LoggerFactory.getLogger(Repositories.class);
	
	@Autowired
	private PostgresProps postrgresProps;

	@Bean
	public ConnectionFactory connectionFactory() {
		
		_LOGGER.info("##############################################################");
		_LOGGER.info("##############################################################");
		_LOGGER.info("##############################################################");
		_LOGGER.info(String.format("Creating postgres bean with: %s", postrgresProps.toString()));
		_LOGGER.info("##############################################################");
		_LOGGER.info("##############################################################");
		_LOGGER.info("##############################################################");
		
		return new PostgresqlConnectionFactory(
				// @formatter:off
			        PostgresqlConnectionConfiguration.builder()
			                .host(postrgresProps.getHost())
			                .database("demfiles")
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
		var cp = ConnectionPoolConfiguration.builder(connectionFactory)
	            .initialSize(2)
	            .maxSize(20)
	            .build();
		ConnectionPool pool = new ConnectionPool(cp);
		return DatabaseClient.builder().connectionFactory(pool).build();
	}

	@Bean
	public RepoProcessQueue getRepoProcessQueue() {
		return new RepoProcessQueuePostgres();
	}

	@Bean
	public RepoUser getRepoUserPostgres() {
		return new RepoUserPostgres();
	}

	@Bean
	public RepoUserScore getRepoUserScore() {
		return new RepoUserScorePostgres();
	}
}
