package com.ixigo.playersmanager.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ixigo.demmanagercontract.models.rest.demdata.RestMapStats;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUserGotvScore;
import com.ixigo.playersmanager.models.svc.SvcMapStats;
import com.ixigo.playersmanager.models.svc.SvcTeam;
import com.ixigo.playersmanager.models.svc.SvcUserAvgScore;
import com.ixigo.playersmanager.models.svc.SvcUserMapStats;
import com.ixigo.playersmanagercontract.models.rest.RestTeam;
import com.ixigo.playersmanagercontract.models.rest.RestUserAvgScore;

@Mapper(componentModel = "spring")
public interface RestMapper {
	public SvcMapStats fromRestToSvc(RestMapStats rest);
	public SvcUserMapStats fromRestUserGotvScoreToSvc(RestUserGotvScore rest);
	public List<SvcMapStats> fromRestToSvcList(List<RestMapStats> rest);
	public List<SvcUserMapStats> fromRestUserListToSvcList(List<RestUserGotvScore> rest);
	
	public RestMapStats fromSvcToRest(SvcMapStats svc);
	public List<RestMapStats> fromSvcMapStatsToRestList(List<SvcMapStats> svc);
	
	public RestTeam fromSvcToRest(SvcTeam svc);
	public List<RestTeam> fromSvcTeamToRestList(List<SvcTeam> svc);
	
	public RestUserAvgScore fromSvcToRest(SvcUserAvgScore svc);
	public List<RestUserAvgScore> fromSvcUserAvgScoreToRestList(List<SvcUserAvgScore> svc);
}
