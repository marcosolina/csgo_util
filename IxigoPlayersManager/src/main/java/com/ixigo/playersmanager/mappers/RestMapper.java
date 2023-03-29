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
	public SvcMapStats fromRestMapStatsToSvc(RestMapStats rest);
	public SvcUserMapStats fromRestUserGotvScoreToSvc(RestUserGotvScore rest);
	public List<SvcMapStats> fromListRestMapStatsToSvcList(List<RestMapStats> rest);
	public List<SvcUserMapStats> fromListRestUserGotvScoreToSvcList(List<RestUserGotvScore> rest);
	
	public RestMapStats fromSvcMapStatsToRest(SvcMapStats svc);
	public List<RestMapStats> fromListSvcMapStatsToRestList(List<SvcMapStats> svc);
	
	public RestTeam fromSvcTeamToRest(SvcTeam svc);
	public List<RestTeam> fromListSvcTeamToRestList(List<SvcTeam> svc);
	
	public RestUserAvgScore fromSvcUserAvgScoreToRest(SvcUserAvgScore svc);
	public List<RestUserAvgScore> fromListSvcUserAvgScoreToRestList(List<SvcUserAvgScore> svc);
}
