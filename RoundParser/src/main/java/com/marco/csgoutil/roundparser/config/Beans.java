package com.marco.csgoutil.roundparser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marco.csgoutil.roundparser.services.implementations.CsgoRoundFileParserMarco;
import com.marco.csgoutil.roundparser.services.implementations.RoundFileServiceMarco;
import com.marco.csgoutil.roundparser.services.implementations.RoundsServiceMarco;
import com.marco.csgoutil.roundparser.services.interfaces.CsgoRoundFileParser;
import com.marco.csgoutil.roundparser.services.interfaces.RoundFileService;
import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;

@Configuration
public class Beans {

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
}
