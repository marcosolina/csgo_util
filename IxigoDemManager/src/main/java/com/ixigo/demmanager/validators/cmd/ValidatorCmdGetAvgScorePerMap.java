package com.ixigo.demmanager.validators.cmd;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.charts.CmdGetAvgScorePerMap;
import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.enums.ScoreType;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.validators.IxigoValidator;

@Component
public class ValidatorCmdGetAvgScorePerMap extends IxigoValidator<CmdGetAvgScorePerMap>{
	@Autowired
    private IxigoMessageResource msgSource;

	@Override
	public void rules() {
		// @formatter:off
		ruleFor(CmdGetAvgScorePerMap::getSteamIds)
	        .must(list -> list != null && !list.isEmpty())
	        .withMessage(msgSource.getMessage(ErrorCodes.ERROR_MANDATORY))
	        .withCode(ErrorCodes.ERROR_MANDATORY)
	        .withAttempedValue(CmdGetAvgScorePerMap::getSteamIds)
	        .withFieldName(this.getJsonPropertyName(CmdGetAvgScorePerMap.Fields.steamIds));
		
		ruleFor(CmdGetAvgScorePerMap::getScoreType)
	        .must(not(stringEmptyOrNull()))
	        .withMessage(msgSource.getMessage(ErrorCodes.ERROR_MANDATORY))
	        .withCode(ErrorCodes.ERROR_MANDATORY)
	        .withAttempedValue(CmdGetAvgScorePerMap::getScoreType)
	        .withFieldName(this.getJsonPropertyName(CmdGetAvgScorePerMap.Fields.scoreType));
		
		ruleFor(CmdGetAvgScorePerMap::getScoreType)
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
	        .withAttempedValue(CmdGetAvgScorePerMap::getScoreType)
	        .withFieldName(this.getJsonPropertyName(CmdGetAvgScorePerMap.Fields.scoreType));
		// @formatter:on
		
	}
}
