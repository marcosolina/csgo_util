package com.ixigo.demmanager.validators.cmd;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.charts.CmdGetTeamsScorePerMap;
import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.validators.IxigoValidator;

@Component
public class ValidatorCmdGetTeamsScorePerMap extends IxigoValidator<CmdGetTeamsScorePerMap>{
	@Autowired
    private IxigoMessageResource msgSource;

	@Override
	public void rules() {
		// @formatter:off
		ruleFor(CmdGetTeamsScorePerMap::getMap)
	        .must(list -> list != null && !list.isEmpty())
	        .withMessage(msgSource.getMessage(ErrorCodes.ERROR_MANDATORY))
	        .withCode(ErrorCodes.ERROR_MANDATORY)
	        .withAttempedValue(CmdGetTeamsScorePerMap::getMap)
	        .withFieldName(this.getJsonPropertyName(CmdGetTeamsScorePerMap.Fields.map));
		
		ruleFor(CmdGetTeamsScorePerMap::getMatchesToConsider)
	        .must(number -> {
	        	try {
	        		Integer.parseInt(number);
	        	}catch (NumberFormatException e) {
	        		return false;
	        	}
	        	return true;
	        })
	        .when(not(stringEmptyOrNull()))
	        .withMessage(msgSource.getMessage(ErrorCodes.ERROR_MUST_BE_AN_INTEGER))
	        .withCode(ErrorCodes.ERROR_MUST_BE_AN_INTEGER)
	        .withAttempedValue(CmdGetTeamsScorePerMap::getMatchesToConsider)
	        .withFieldName(this.getJsonPropertyName(CmdGetTeamsScorePerMap.Fields.matchesToConsider));
		
		ruleFor(CmdGetTeamsScorePerMap::getMatchesToConsider)
	        .must(number -> Integer.parseInt(number) > -1)
	        .when(number -> {
	        	if(number != null && !number.trim().isEmpty()) {
	        		try {
		        		Integer.parseInt(number);
		        	}catch (NumberFormatException e) {
		        		return false;
		        	}
		        	return true;
	        	}
	        	return false;
	        })
	        .withMessage(msgSource.getMessage(ErrorCodes.ERROR_MUST_NOT_BE_NEGATIVE))
	        .withCode(ErrorCodes.ERROR_MUST_NOT_BE_NEGATIVE)
	        .withAttempedValue(CmdGetTeamsScorePerMap::getMatchesToConsider)
	        .withFieldName(this.getJsonPropertyName(CmdGetTeamsScorePerMap.Fields.matchesToConsider));
		// @formatter:on
		
	}
}
