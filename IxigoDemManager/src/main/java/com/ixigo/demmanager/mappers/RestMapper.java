package com.ixigo.demmanager.mappers;

import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;

import com.ixigo.demmanager.models.svc.SvcFileInfo;
import com.ixigo.demmanager.models.svc.demdata.SvcMapStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcUser;
import com.ixigo.demmanagercontract.models.rest.demdata.RestMapStats;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUser;
import com.ixigo.demmanagercontract.models.rest.demfilesmanager.RestFileInfo;

/**
 * Simple mapper class used to map the models between the REST and Service layer
 * 
 * @author marco
 *
 */
@Mapper(componentModel = "spring")
public interface RestMapper {
	// Dem files manager
	public RestFileInfo fromSvcTorRest(SvcFileInfo fileInfo);

	public List<RestFileInfo> fromSvcToRest(List<SvcFileInfo> list);

	public Map<String, List<RestFileInfo>> fromSvcToRest(Map<String, List<SvcFileInfo>> map);

	public RestUser fromSvcToRest(SvcUser svc);

	public List<RestUser> fromSvcToRestUserList(List<SvcUser> svc);

	public List<RestMapStats> fromSvcToRestMapStats(List<SvcMapStats> svc);

}
