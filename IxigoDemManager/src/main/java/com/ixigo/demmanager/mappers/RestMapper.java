package com.ixigo.demmanager.mappers;

import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;

import com.ixigo.demmanager.models.svc.SvcFileInfo;
import com.ixigo.demmanager.models.svc.demdata.data.SvcDemProcessQueue;
import com.ixigo.demmanager.models.svc.demdata.data.SvcEntryKillStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcEntryKillStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMapHitStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMapPlayerWeaponStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMapShotStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchHitStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchPlayerWeaponStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchResults;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchShotStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcOverallHitStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcOverallPlayerWeaponStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcOverallShotStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerClutchStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerKillCount;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMapMatchStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMapStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMapStatsExtendedExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchKillCount;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchResults;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerOverallMatchStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerOverallStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerOverallStatsExtendedExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundAssistStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundDamageStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundDeathStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundEventStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundExtendedStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundFlashAssistStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundKillStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundUtilityStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponMapKills;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponMapRanking;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponMatchKills;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponOverallKills;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponRanking;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundHitEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundHitEventsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundHitStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundKillEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundKillEventsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundPlayerWeaponStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundScorecard;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundShotEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundShotStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcUsers;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestDemProcessQueue;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestEntryKillStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestEntryKillStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMapHitStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMapPlayerWeaponStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMapShotStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchHitStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchPlayerWeaponStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchResults;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchShotStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestOverallHitStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestOverallPlayerWeaponStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestOverallShotStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerClutchStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerKillCount;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMapMatchStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMapStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMapStatsExtendedExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchKillCount;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchResults;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerOverallMatchStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerOverallStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerOverallStatsExtendedExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundAssistStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundDamageStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundDeathStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundEventStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundExtendedStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundFlashAssistStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundKillStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundUtilityStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponMapKills;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponMapRanking;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponMatchKills;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponOverallKills;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponRanking;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundEvents;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundHitEvents;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundHitEventsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundHitStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundKillEvents;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundKillEventsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundPlayerWeaponStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundScorecard;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundShotEvents;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundShotStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestUsers;
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

	public RestDemProcessQueue fromSvcToRest(SvcDemProcessQueue svc);

	public SvcDemProcessQueue fromRestToSvc(RestDemProcessQueue rest);

	public RestMatchStats fromSvcToRest(SvcMatchStats svc);

	public SvcMatchStats fromRestToSvc(RestMatchStats rest);

	public RestPlayerRoundStats fromSvcToRest(SvcPlayerRoundStats svc);

	public SvcPlayerRoundStats fromRestToSvc(RestPlayerRoundStats rest);

	public RestPlayerStats fromSvcToRest(SvcPlayerStats svc);

	public SvcPlayerStats fromRestToSvc(RestPlayerStats rest);

	public RestRoundEvents fromSvcToRest(SvcRoundEvents svc);

	public SvcRoundEvents fromRestToSvc(RestRoundEvents rest);

	public RestRoundHitEvents fromSvcToRest(SvcRoundHitEvents svc);

	public SvcRoundHitEvents fromRestToSvc(RestRoundHitEvents rest);

	public RestRoundKillEvents fromSvcToRest(SvcRoundKillEvents svc);

	public SvcRoundKillEvents fromRestToSvc(RestRoundKillEvents rest);

	public RestRoundShotEvents fromSvcToRest(SvcRoundShotEvents svc);

	public SvcRoundShotEvents fromRestToSvc(RestRoundShotEvents rest);

	public RestRoundStats fromSvcToRest(SvcRoundStats svc);

	public SvcRoundStats fromRestToSvc(RestRoundStats rest);

	public RestUsers fromSvcToRest(SvcUsers svc);

	public SvcUsers fromRestToSvc(RestUsers rest);

	public RestEntryKillStats fromSvcToRest(SvcEntryKillStats svc);

	public SvcEntryKillStats fromRestToSvc(RestEntryKillStats rest);

	public RestEntryKillStatsExtended fromSvcToRest(SvcEntryKillStatsExtended svc);

	public SvcEntryKillStatsExtended fromRestToSvc(RestEntryKillStatsExtended rest);

	public RestMapHitStatsExtended fromSvcToRest(SvcMapHitStatsExtended svc);

	public SvcMapHitStatsExtended fromRestToSvc(RestMapHitStatsExtended rest);

	public RestMapPlayerWeaponStats fromSvcToRest(SvcMapPlayerWeaponStats svc);

	public SvcMapPlayerWeaponStats fromRestToSvc(RestMapPlayerWeaponStats rest);

	public RestMapShotStatsExtended fromSvcToRest(SvcMapShotStatsExtended svc);

	public SvcMapShotStatsExtended fromRestToSvc(RestMapShotStatsExtended rest);

	public RestMatchHitStatsExtended fromSvcToRest(SvcMatchHitStatsExtended svc);

	public SvcMatchHitStatsExtended fromRestToSvc(RestMatchHitStatsExtended rest);

	public RestMatchPlayerWeaponStats fromSvcToRest(SvcMatchPlayerWeaponStats svc);

	public SvcMatchPlayerWeaponStats fromRestToSvc(RestMatchPlayerWeaponStats rest);

	public RestMatchResults fromSvcToRest(SvcMatchResults svc);

	public SvcMatchResults fromRestToSvc(RestMatchResults rest);

	public RestMatchShotStatsExtended fromSvcToRest(SvcMatchShotStatsExtended svc);

	public SvcMatchShotStatsExtended fromRestToSvc(RestMatchShotStatsExtended rest);

	public RestOverallHitStatsExtended fromSvcToRest(SvcOverallHitStatsExtended svc);

	public SvcOverallHitStatsExtended fromRestToSvc(RestOverallHitStatsExtended rest);

	public RestOverallPlayerWeaponStats fromSvcToRest(SvcOverallPlayerWeaponStats svc);

	public SvcOverallPlayerWeaponStats fromRestToSvc(RestOverallPlayerWeaponStats rest);

	public RestOverallShotStatsExtended fromSvcToRest(SvcOverallShotStatsExtended svc);

	public SvcOverallShotStatsExtended fromRestToSvc(RestOverallShotStatsExtended rest);

	public RestPlayerClutchStats fromSvcToRest(SvcPlayerClutchStats svc);

	public SvcPlayerClutchStats fromRestToSvc(RestPlayerClutchStats rest);

	public RestPlayerKillCount fromSvcToRest(SvcPlayerKillCount svc);

	public SvcPlayerKillCount fromRestToSvc(RestPlayerKillCount rest);

	public RestPlayerMapMatchStats fromSvcToRest(SvcPlayerMapMatchStats svc);

	public SvcPlayerMapMatchStats fromRestToSvc(RestPlayerMapMatchStats rest);

	public RestPlayerMapStatsExtended fromSvcToRest(SvcPlayerMapStatsExtended svc);

	public SvcPlayerMapStatsExtended fromRestToSvc(RestPlayerMapStatsExtended rest);

	public RestPlayerMapStatsExtendedExtended fromSvcToRest(SvcPlayerMapStatsExtendedExtended svc);

	public SvcPlayerMapStatsExtendedExtended fromRestToSvc(RestPlayerMapStatsExtendedExtended rest);

	public RestPlayerMatchKillCount fromSvcToRest(SvcPlayerMatchKillCount svc);

	public SvcPlayerMatchKillCount fromRestToSvc(RestPlayerMatchKillCount rest);

	public RestPlayerMatchResults fromSvcToRest(SvcPlayerMatchResults svc);

	public SvcPlayerMatchResults fromRestToSvc(RestPlayerMatchResults rest);

	public RestPlayerMatchStats fromSvcToRest(SvcPlayerMatchStats svc);

	public SvcPlayerMatchStats fromRestToSvc(RestPlayerMatchStats rest);

	public RestPlayerMatchStatsExtended fromSvcToRest(SvcPlayerMatchStatsExtended svc);

	public SvcPlayerMatchStatsExtended fromRestToSvc(RestPlayerMatchStatsExtended rest);

	public RestPlayerOverallMatchStats fromSvcToRest(SvcPlayerOverallMatchStats svc);

	public SvcPlayerOverallMatchStats fromRestToSvc(RestPlayerOverallMatchStats rest);

	public RestPlayerOverallStatsExtended fromSvcToRest(SvcPlayerOverallStatsExtended svc);

	public SvcPlayerOverallStatsExtended fromRestToSvc(RestPlayerOverallStatsExtended rest);

	public RestPlayerOverallStatsExtendedExtended fromSvcToRest(SvcPlayerOverallStatsExtendedExtended svc);

	public SvcPlayerOverallStatsExtendedExtended fromRestToSvc(RestPlayerOverallStatsExtendedExtended rest);

	public RestPlayerRoundAssistStats fromSvcToRest(SvcPlayerRoundAssistStats svc);

	public SvcPlayerRoundAssistStats fromRestToSvc(RestPlayerRoundAssistStats rest);

	public RestPlayerRoundDamageStats fromSvcToRest(SvcPlayerRoundDamageStats svc);

	public SvcPlayerRoundDamageStats fromRestToSvc(RestPlayerRoundDamageStats rest);

	public RestPlayerRoundDeathStats fromSvcToRest(SvcPlayerRoundDeathStats svc);

	public SvcPlayerRoundDeathStats fromRestToSvc(RestPlayerRoundDeathStats rest);

	public RestPlayerRoundEventStats fromSvcToRest(SvcPlayerRoundEventStats svc);

	public SvcPlayerRoundEventStats fromRestToSvc(RestPlayerRoundEventStats rest);

	public RestPlayerRoundExtendedStats fromSvcToRest(SvcPlayerRoundExtendedStats svc);

	public SvcPlayerRoundExtendedStats fromRestToSvc(RestPlayerRoundExtendedStats rest);

	public RestPlayerRoundFlashAssistStats fromSvcToRest(SvcPlayerRoundFlashAssistStats svc);

	public SvcPlayerRoundFlashAssistStats fromRestToSvc(RestPlayerRoundFlashAssistStats rest);

	public RestPlayerRoundKillStats fromSvcToRest(SvcPlayerRoundKillStats svc);

	public SvcPlayerRoundKillStats fromRestToSvc(RestPlayerRoundKillStats rest);

	public RestPlayerRoundUtilityStats fromSvcToRest(SvcPlayerRoundUtilityStats svc);

	public SvcPlayerRoundUtilityStats fromRestToSvc(RestPlayerRoundUtilityStats rest);

	public RestPlayerWeaponMapKills fromSvcToRest(SvcPlayerWeaponMapKills svc);

	public SvcPlayerWeaponMapKills fromRestToSvc(RestPlayerWeaponMapKills rest);

	public RestPlayerWeaponMapRanking fromSvcToRest(SvcPlayerWeaponMapRanking svc);

	public SvcPlayerWeaponMapRanking fromRestToSvc(RestPlayerWeaponMapRanking rest);

	public RestPlayerWeaponMatchKills fromSvcToRest(SvcPlayerWeaponMatchKills svc);

	public SvcPlayerWeaponMatchKills fromRestToSvc(RestPlayerWeaponMatchKills rest);

	public RestPlayerWeaponOverallKills fromSvcToRest(SvcPlayerWeaponOverallKills svc);

	public SvcPlayerWeaponOverallKills fromRestToSvc(RestPlayerWeaponOverallKills rest);

	public RestPlayerWeaponRanking fromSvcToRest(SvcPlayerWeaponRanking svc);

	public SvcPlayerWeaponRanking fromRestToSvc(RestPlayerWeaponRanking rest);

	public RestRoundHitEventsExtended fromSvcToRest(SvcRoundHitEventsExtended svc);

	public SvcRoundHitEventsExtended fromRestToSvc(RestRoundHitEventsExtended rest);

	public RestRoundHitStatsExtended fromSvcToRest(SvcRoundHitStatsExtended svc);

	public SvcRoundHitStatsExtended fromRestToSvc(RestRoundHitStatsExtended rest);

	public RestRoundKillEventsExtended fromSvcToRest(SvcRoundKillEventsExtended svc);

	public SvcRoundKillEventsExtended fromRestToSvc(RestRoundKillEventsExtended rest);

	public RestRoundPlayerWeaponStats fromSvcToRest(SvcRoundPlayerWeaponStats svc);

	public SvcRoundPlayerWeaponStats fromRestToSvc(RestRoundPlayerWeaponStats rest);

	public RestRoundScorecard fromSvcToRest(SvcRoundScorecard svc);

	public SvcRoundScorecard fromRestToSvc(RestRoundScorecard rest);

	public RestRoundShotStatsExtended fromSvcToRest(SvcRoundShotStatsExtended svc);

	public SvcRoundShotStatsExtended fromRestToSvc(RestRoundShotStatsExtended rest);

	public RestRoundStatsExtended fromSvcToRest(SvcRoundStatsExtended svc);

	public SvcRoundStatsExtended fromRestToSvc(RestRoundStatsExtended rest);
}
