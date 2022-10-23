package com.ixigo.demmanager.mappers;

import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;

import com.ixigo.demmanager.models.rest.demdata.RestMapPlayed;
import com.ixigo.demmanager.models.rest.demdata.RestMapStats;
import com.ixigo.demmanager.models.rest.demdata.RestUser;
import com.ixigo.demmanager.models.rest.demfilesmanager.RestFileInfo;
import com.ixigo.demmanager.models.svc.SvcFileInfo;
import com.ixigo.demmanager.models.svc.demdata.SvcMapPlayedCounter;
import com.ixigo.demmanager.models.svc.demdata.SvcMapStats;
import com.ixigo.demmanager.models.svc.demdata.SvcUser;

@Mapper(componentModel = "spring")
public interface RestMapper {
	// Dem files manager
	public RestFileInfo fromSvcTorRest(SvcFileInfo fileInfo);
	public List<RestFileInfo> fromSvcToRest(List<SvcFileInfo> list);
	public Map<String, List<RestFileInfo>> fromSvcToRest(Map<String, List<SvcFileInfo>> map);
	
	// Dem data
	public RestMapPlayed fromSvcToRest(SvcMapPlayedCounter svc);
	public List<RestMapPlayed> fromSvcToRestMapPlayedList(List<SvcMapPlayedCounter> svc);
	public RestUser fromSvcToRest(SvcUser svc);
	public List<RestUser> fromSvcToRestUserList(List<SvcUser> svc);
	public List<RestMapStats> fromSvcToRestMapStats(List<SvcMapStats> svc);
	
}
