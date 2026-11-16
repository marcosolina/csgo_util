/*
package com.ixigo.demmanager.validators.cmd;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.charts.CmdGetPlayersAvgScorePerMap;
import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.enums.ScoreType;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.validators.IxigoValidator;

@Component
public class ValidatorCmdGetPlayersAvgScorePerMap extends IxigoValidator<CmdGetPlayersAvgScorePerMap>{
	@Autowired
    private IxigoMessageResource msgSource;

	@Override
	public void rules() {
		// @formatter:off
		ruleFor(CmdGetPlayersAvgScorePerMap::getSteamIds)
	        .must(list -> list != null && !list.isEmpty())
	        .withMessage(msgSource.getMessage(ErrorCodes.ERROR_MANDATORY))
	        .withCode(ErrorCodes.ERROR_MANDATORY)
	        .withAttempedValue(CmdGetPlayersAvgScorePerMap::getSteamIds)
	        .withFieldName(this.getJsonPropertyName(CmdGetPlayersAvgScorePerMap.Fields.steamIds));
		
		ruleFor(CmdGetPlayersAvgScorePerMap::getScoreType)
	        .must(not(stringEmptyOrNull()))
	        .withMessage(msgSource.getMessage(ErrorCodes.ERROR_MANDATORY))
	        .withCode(ErrorCodes.ERROR_MANDATORY)
	        .withAttempedValue(CmdGetPlayersAvgScorePerMap::getScoreType)
	        .withFieldName(this.getJsonPropertyName(CmdGetPlayersAvgScorePerMap.Fields.scoreType));
		
		ruleFor(CmdGetPlayersAvgScorePerMap::getMatchesToConsider)
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
	        .withAttempedValue(CmdGetPlayersAvgScorePerMap::getMatchesToConsider)
	        .withFieldName(this.getJsonPropertyName(CmdGetPlayersAvgScorePerMap.Fields.matchesToConsider));
		
		ruleFor(CmdGetPlayersAvgScorePerMap::getMatchesToConsider)
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
	        .withAttempedValue(CmdGetPlayersAvgScorePerMap::getMatchesToConsider)
	        .withFieldName(this.getJsonPropertyName(CmdGetPlayersAvgScorePerMap.Fields.matchesToConsider));
		
		ruleFor(CmdGetPlayersAvgScorePerMap::getScoreType)
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
	        .withAttempedValue(CmdGetPlayersAvgScorePerMap::getScoreType)
	        .withFieldName(this.getJsonPropertyName(CmdGetPlayersAvgScorePerMap.Fields.scoreType));
		// @formatter:on
		
	}
}
*/