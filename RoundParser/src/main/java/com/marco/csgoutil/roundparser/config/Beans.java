package com.marco.csgoutil.roundparser.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.marco.csgoutil.roundparser.repositories.implementations.RepoUserPostgres;
import com.marco.csgoutil.roundparser.repositories.implementations.RepoUserScorePostgres;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUser;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUserScore;
import com.marco.csgoutil.roundparser.services.implementations.CsgoRoundFileParserMarco;
import com.marco.csgoutil.roundparser.services.implementations.RoundFileServiceMarco;
import com.marco.csgoutil.roundparser.services.implementations.RoundsServiceMarco;
import com.marco.csgoutil.roundparser.services.interfaces.CsgoRoundFileParser;
import com.marco.csgoutil.roundparser.services.interfaces.RoundFileService;
import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;

@Configuration
public class Beans {

	/*###############################################################
	 * Services
	 ###############################################################*/
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
		return new CsgoRoundFileParserMarco();
	}
	
	/*###############################################################
	 * DB Repositories
	 ###############################################################*/
	@Bean
	public RepoUser getRepoUser() {
		return new RepoUserPostgres();
	}
	
	@Bean
	public RepoUserScore getRepoUserScore() {
		return new RepoUserScorePostgres();
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:/messages/errorCodes");

		return messageSource;
	}
}
