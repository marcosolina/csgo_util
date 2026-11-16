package com.ixigo.discordbot.config.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import com.ixigo.discordbot.config.properties.PostgresProps;
import com.ixigo.discordbot.repositories.implementations.RepoBotConfigPostgres;
import com.ixigo.discordbot.repositories.implementations.RepoUsersMapPostgres;
import com.ixigo.discordbot.repositories.interfaces.RepoBotConfig;
import com.ixigo.discordbot.repositories.interfaces.RepoUsersMap;

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
                .database("discordbot")
                .username(postrgresProps.getUser())
                .password(postrgresProps.getPassword())
                .build()
		// @formatter:on
		);
	}

	@Bean
	public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
		//// @formatter:off
		var cp = ConnectionPoolConfiguration.builder(connectionFactory)
	            .initialSize(2)
	            .maxSize(20)
	            .build();
		// @formatter:on
		ConnectionPool pool = new ConnectionPool(cp);
		return DatabaseClient.builder().connectionFactory(pool).build();
	}

	@Bean
	public RepoBotConfig getRepoBotConfig() {
		return new RepoBotConfigPostgres();
	}

	@Bean
	public RepoUsersMap getRepoUsersMap() {
		return new RepoUsersMapPostgres();
	}
}
