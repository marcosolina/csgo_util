package com.ixigo.rconapi.validators.service;
import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.validators.IxigoValidator;
import com.ixigo.rconapi.models.service.SvcRconRequest;

@Component
public class ValidatorSvcRconCommand extends IxigoValidator<SvcRconRequest> {
	@Autowired
    private IxigoMessageResource msgSource;
	
	@Override
	public void rules() {
		// @formatter:off
		ruleFor(SvcRconRequest::getRconHost)
	        .must(not(stringEmptyOrNull()))
	        .withMessage(msgSource.getMessage("RCON00001"))
	        .withCode("RCON00001")
	        .withAttempedValue(SvcRconRequest::getRconHost)
	        .withFieldName(this.getJsonPropertyName(SvcRconRequest.Fields.rconHost));
		
		ruleFor(SvcRconRequest::getRconPort)
	        .must(port -> port != 0)
	        .withMessage(msgSource.getMessage("RCON00002"))
	        .withCode("RCON00002")
	        .withAttempedValue(SvcRconRequest::getRconPort)
	        .withFieldName(this.getJsonPropertyName(SvcRconRequest.Fields.rconPort));
		
		ruleFor(SvcRconRequest::getRconPass)
	        .must(not(stringEmptyOrNull()))
	        .withMessage(msgSource.getMessage("RCON00003"))
	        .withCode("RCON00003")
	        .withAttempedValue(SvcRconRequest::getRconPass)
	        .withFieldName(this.getJsonPropertyName(SvcRconRequest.Fields.rconPass));
		
		ruleFor(SvcRconRequest::getRconCmd)
	        .must(not(stringEmptyOrNull()))
	        .withMessage(msgSource.getMessage("RCON00004"))
	        .withCode("RCON00004")
	        .withAttempedValue(SvcRconRequest::getRconCmd)
	        .withFieldName(this.getJsonPropertyName(SvcRconRequest.Fields.rconCmd));
		// @formatter:on
	}

}
