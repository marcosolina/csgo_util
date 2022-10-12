package com.ixigo.demmanager.mappers;

import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;

import com.ixigo.demmanager.models.rest.RestFileInfo;
import com.ixigo.demmanager.models.svc.SvcFileInfo;

@Mapper(componentModel = "spring")
public interface RestMapper {
	// Dem files manager
	public RestFileInfo fromSvcTorRest(SvcFileInfo fileInfo);

	public List<RestFileInfo> fromSvcToRest(List<SvcFileInfo> list);

	public Map<String, List<RestFileInfo>> fromSvcToRest(Map<String, List<SvcFileInfo>> map);
}
