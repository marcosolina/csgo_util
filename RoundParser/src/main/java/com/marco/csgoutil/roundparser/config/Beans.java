package com.marco.csgoutil.roundparser.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;

import com.fasterxml.classmate.TypeResolver;
import com.marco.csgoutil.roundparser.enums.Environment;
import com.marco.csgoutil.roundparser.repositories.implementations.RepoUserPostgres;
import com.marco.csgoutil.roundparser.repositories.implementations.RepoUserScorePostgres;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUser;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUserScore;
import com.marco.csgoutil.roundparser.services.implementations.RoundFileServiceMarco;
import com.marco.csgoutil.roundparser.services.implementations.CsgoRoundFileParser.CsgoRoundFileParserRasp;
import com.marco.csgoutil.roundparser.services.implementations.CsgoRoundFileParser.CsgoRoundFileParserWindows;
import com.marco.csgoutil.roundparser.services.implementations.partitionteams.PartitionTeamsDynamicSearchTree;
import com.marco.csgoutil.roundparser.services.implementations.partitionteams.PartitionTeamsIxigo;
import com.marco.csgoutil.roundparser.services.implementations.roundsservice.RoundsServiceRasp;
import com.marco.csgoutil.roundparser.services.implementations.roundsservice.RoundsServiceWindows;
import com.marco.csgoutil.roundparser.services.interfaces.CsgoRoundFileParser;
import com.marco.csgoutil.roundparser.services.interfaces.PartitionTeams;
import com.marco.csgoutil.roundparser.services.interfaces.RoundFileService;
import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Standard SpringBoot Configuration class
 * 
 * @author Marco
 *
 */
@Configuration
public class Beans {

	@Value("${com.marco.csgoutil.roundparser.version}")
	private String appVersion;

	@Value("${com.marco.csgoutil.roundparser.environment}")
	private Environment environment;

	/*
	 * ############################################################### Services
	 * ###############################################################
	 */
	@Bean
	public RoundsService getRoundsService() {
		switch (environment) {
		case WINDOWS:
			return new RoundsServiceWindows();
		case RASP:
			return new RoundsServiceRasp();
		default:
			return new RoundsServiceWindows();
		}
	}

	@Bean
	public RoundFileService getRoundFileService() {
		return new RoundFileServiceMarco();
	}

	@Bean
	public CsgoRoundFileParser getCsgoRoundFileParser() {
		switch (environment) {
		case WINDOWS:
			return new CsgoRoundFileParserWindows();
		case RASP:
			return new CsgoRoundFileParserRasp();
		default:
			return new CsgoRoundFileParserWindows();
		}

	}

	@Bean(name = "Simple")
	public PartitionTeams getSimplePartitionTeams() {
		return new PartitionTeamsDynamicSearchTree();
	}

	@Bean(name = "IxiGO")
	public PartitionTeams getIxigoPartitionTeams() {
		return new PartitionTeamsIxigo();
	}

	/*
	 * ############################################################### DB
	 * Repositories ###############################################################
	 */
	@Bean
	public RepoUser getRepoUser() {
		return new RepoUserPostgres();
	}

	@Bean
	public RepoUserScore getRepoUserScore() {
		return new RepoUserScorePostgres();
	}

	/*
	 * ############################################################### Misc
	 * ###############################################################
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:/messages/errorCodes");

		return messageSource;
	}

	@Bean
	public Docket swaggerConfiguration() {
		TypeResolver resolver = new TypeResolver();
		return new Docket(DocumentationType.SWAGGER_2)
				.alternateTypeRules(
						AlternateTypeRules.newRule(resolver.resolve(List.class, LocalDate.class),
								resolver.resolve(List.class, String.class), Ordered.HIGHEST_PRECEDENCE),
						AlternateTypeRules.newRule(resolver.resolve(List.class, LocalDateTime.class),
								resolver.resolve(List.class, String.class), Ordered.HIGHEST_PRECEDENCE))
				.select().apis(RequestHandlerSelectors.basePackage("com.marco.csgoutil.roundparser.controllers"))
				.paths(PathSelectors.regex("/.*")).build().apiInfo(apiEndPointsInfo());
	}

	private ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder().title("Round Parser").description(
				"I have created this project to parse the \".dem\" files generated from the CS:GO server that I use with my friends")
				.contact(new Contact("Marco Solina", "", "")).version(appVersion).build();
	}
}
