package com.ixigo.demmanager.validators.cmd;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.charts.CmdGetPlayersAvgScorePerMap;
import com.ixigo.demmanager.commands.charts.CmdGetTeamsAvgScorePerMap;
import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.enums.ScoreType;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.validators.IxigoValidator;

@Component
public class ValidatorCmdGetTeamsAvgScorePerMap extends IxigoValidator<CmdGetTeamsAvgScorePerMap>{
	@Autowired
    private IxigoMessageResource msgSource;

	@Override
	public void rules() {
		// @formatter:off		
		ruleFor(CmdGetTeamsAvgScorePerMap::getScoreType)
	        .must(not(stringEmptyOrNull()))
	        .withMessage(msgSource.getMessage(ErrorCodes.ERROR_MANDATORY))
	        .withCode(ErrorCodes.ERROR_MANDATORY)
	        .withAttempedValue(CmdGetTeamsAvgScorePerMap::getScoreType)
	        .withFieldName(this.getJsonPropertyName(CmdGetTeamsAvgScorePerMap.Fields.scoreType));
		
		ruleFor(CmdGetTeamsAvgScorePerMap::getMatchesToConsider)
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
	        .withAttempedValue(CmdGetTeamsAvgScorePerMap::getMatchesToConsider)
	        .withFieldName(this.getJsonPropertyName(CmdGetPlayersAvgScorePerMap.Fields.matchesToConsider));
		
		ruleFor(CmdGetTeamsAvgScorePerMap::getMatchesToConsider)
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
	        .withAttempedValue(CmdGetTeamsAvgScorePerMap::getMatchesToConsider)
	        .withFieldName(this.getJsonPropertyName(CmdGetTeamsAvgScorePerMap.Fields.matchesToConsider));
		
		ruleFor(CmdGetTeamsAvgScorePerMap::getScoreType)
	        .must(strScoreType -> {
	        	try {
		        	ScoreType.valueOf(strScoreType);
		        	return true;
	        	}catch (Exception e) {
	        		return false;
	        	}
	        })
	        .when(not(stringEmptyOrNull()))
	        .withMessage(msgSource.getMessage(ErrorCodes.ERROR_SCORE_TYPE_INVALID))
	        .withCode(ErrorCodes.ERROR_SCORE_TYPE_INVALID)
	        .withAttempedValue(CmdGetTeamsAvgScorePerMap::getScoreType)
	        .withFieldName(this.getJsonPropertyName(CmdGetPlayersAvgScorePerMap.Fields.scoreType));
		// @formatter:on
		
	}
}
