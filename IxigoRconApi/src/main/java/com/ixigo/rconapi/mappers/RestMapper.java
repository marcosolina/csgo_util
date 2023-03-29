package com.ixigo.rconapi.mappers;

import org.mapstruct.Mapper;

import com.ixigo.rconapi.models.rest.RestRconRequest;
import com.ixigo.rconapi.models.service.SvcRconRequest;

/**
 * Mapper used between the controller layer and service layer
 * 
 * @author Marco
 *
 */
@Mapper(componentModel = "spring")
public interface RestMapper {
	SvcRconRequest fromRestToSvc(RestRconRequest request);
	RestRconRequest fromSvcToRest(SvcRconRequest request);
}
