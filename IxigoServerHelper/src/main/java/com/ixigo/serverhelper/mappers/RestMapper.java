package com.ixigo.serverhelper.mappers;

import org.mapstruct.Mapper;

import com.ixigo.serverhelper.models.rest.RestServerMap;
import com.ixigo.serverhelper.models.svc.SvcServerMap;

@Mapper(componentModel = "spring")
public interface RestMapper {
	RestServerMap fromSvcToRest(SvcServerMap svc);
}
