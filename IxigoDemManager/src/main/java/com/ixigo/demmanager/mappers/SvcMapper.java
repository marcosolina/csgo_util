package com.ixigo.demmanager.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ixigo.demmanager.models.database.UsersDto;
import com.ixigo.demmanager.models.svc.demdata.SvcUser;

@Mapper(componentModel = "spring")
public interface SvcMapper {
	@Mapping(source = "steam_id", target = "steamId")
	@Mapping(source = "user_name", target = "userName")
	public SvcUser fromDtoToSvc(UsersDto dto);
}
