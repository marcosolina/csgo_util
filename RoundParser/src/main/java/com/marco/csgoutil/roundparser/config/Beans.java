package com.marco.csgoutil.roundparser.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.classmate.TypeResolver;
import com.marco.csgoutil.roundparser.enums.Environment;
import com.marco.csgoutil.roundparser.repositories.implementations.RepoErrorMapSentPostgres;
import com.marco.csgoutil.roundparser.repositories.implementations.RepoUserPostgres;
import com.marco.csgoutil.roundparser.repositories.implementations.RepoUserScorePostgres;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoErrorMapSent;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUser;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUserScore;
import com.marco.csgoutil.roundparser.services.implementations.RconServiceMarcoRconApi;
import com.marco.csgoutil.roundparser.services.implementations.RoundFileServiceMarco;
import com.marco.csgoutil.roundparser.services.implementations.CsgoRoundFileParser.CsgoRoundFileParserRasp;
import com.marco.csgoutil.roundparser.services.implementations.CsgoRoundFileParser.CsgoRoundFileParserWindows;
import com.marco.csgoutil.roundparser.services.implementations.notifications.EmailNotificationService;
import com.marco.csgoutil.roundparser.services.implementations.notifications.TelegramNotificationService;
import com.marco.csgoutil.roundparser.services.implementations.partitionteams.PartitionTeamsDynamicSearchTree;
import com.marco.csgoutil.roundparser.services.implementations.partitionteams.PartitionTeamsIxigo;
import com.marco.csgoutil.roundparser.services.implementations.roundsservice.RoundsServiceMarco;
import com.marco.csgoutil.roundparser.services.interfaces.CsgoRoundFileParser;
import com.marco.csgoutil.roundparser.services.interfaces.NotificationService;
import com.marco.csgoutil.roundparser.services.interfaces.PartitionTeams;
import com.marco.csgoutil.roundparser.services.interfaces.RconService;
import com.marco.csgoutil.roundparser.services.interfaces.RoundFileService;
import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;
import com.marco.utils.network.MarcoNetworkUtils;
import com.marco.utils.network.MarcoNetworkUtilsWebFlux;

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
	@Value("${spring.mail.username}")
	private String emailUser;
	@Value("${spring.mail.password}")
	private String emailPassw;

	/*
	 * ############################################################### 
	 * Services
	 * ###############################################################
	 */
	@Bean
	public RoundsService getRoundsService() {
		return new RoundsServiceMarco();
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
	
	@Bean(name = "email")
	public NotificationService getEmailNotificationService() {
		return new EmailNotificationService();
	}
	
	@Bean(name = "telegram")
	public NotificationService getTelegramNotificationService() {
		return new TelegramNotificationService();
	}
	
	@Bean
    @LoadBalanced
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }
    
    @Bean
    public MarcoNetworkUtils getMarcoNetworkUtils() {
        return new MarcoNetworkUtilsWebFlux(getWebClientBuilder());
    }
	
	@Bean 
	public RconService getRconService() {
		return new RconServiceMarcoRconApi();
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
		/*
		 * Send email using your GMAIL account
		 */
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername(emailUser);
		mailSender.setPassword(emailPassw);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

	/*
	 * ############################################################### 
	 * DB Repositories 
	 * ###############################################################
	 */
	@Bean
	public RepoUser getRepoUser() {
		return new RepoUserPostgres();
	}

	@Bean
	public RepoUserScore getRepoUserScore() {
		return new RepoUserScorePostgres();
	}
	
	@Bean
	public RepoErrorMapSent getRepoErrorMapSent() {
	    return new RepoErrorMapSentPostgres();
	}

	/*
	 * ############################################################### 
	 * Misc
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
