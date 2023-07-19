package com.ixigo.demmanager.mappers;

import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;

import com.ixigo.demmanager.models.svc.SvcFileInfo;
import com.ixigo.demmanager.models.svc.demdata.data.SvcDemProcessQueue;
import com.ixigo.demmanager.models.svc.demdata.data.SvcEntryKillStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcEntryKillStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcEntryKillStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcEntryKillStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMapHitStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMapHitStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMapPlayerWeaponStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMapPlayerWeaponStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMapShotStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMapShotStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchHitStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchHitStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchPlayerWeaponStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchPlayerWeaponStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchResults;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchResultsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchShotStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchShotStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcOverallHitStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcOverallHitStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcOverallPlayerWeaponStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcOverallPlayerWeaponStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcOverallShotStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcOverallShotStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerClutchStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerClutchStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerKillCount;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerKillCountCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMapMatchStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMapMatchStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMapStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMapStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMapStatsExtendedExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMapStatsExtendedExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchKillCount;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchKillCountCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchResults;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchResultsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerOverallMatchStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerOverallMatchStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerOverallStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerOverallStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerOverallStatsExtendedExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerOverallStatsExtendedExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundAssistStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundAssistStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundDamageStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundDamageStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundDeathStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundDeathStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundEventStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundEventStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundExtendedStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundExtendedStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundFlashAssistStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundFlashAssistStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundKillStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundKillStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundUtilityStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundUtilityStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponMapKills;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponMapKillsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponMapRanking;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponMapRankingCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponMatchKills;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponMatchKillsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponOverallKills;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponOverallKillsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponRanking;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerWeaponRankingCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundHitEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundHitEventsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundHitEventsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundHitStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundHitStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundKillEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundKillEventsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundKillEventsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundPlayerWeaponStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundPlayerWeaponStatsCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundScorecard;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundScorecardCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundShotEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundShotStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundShotStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundStatsExtendedCache;
import com.ixigo.demmanager.models.svc.demdata.data.SvcUsers;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestDemProcessQueue;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestEntryKillStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestEntryKillStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestEntryKillStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestEntryKillStatsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMapHitStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMapHitStatsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMapPlayerWeaponStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMapPlayerWeaponStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMapShotStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMapShotStatsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchHitStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchHitStatsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchPlayerWeaponStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchPlayerWeaponStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchResults;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchResultsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchShotStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchShotStatsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestMatchStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestOverallHitStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestOverallHitStatsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestOverallPlayerWeaponStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestOverallPlayerWeaponStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestOverallShotStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestOverallShotStatsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerClutchStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerClutchStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerKillCount;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerKillCountCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMapMatchStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMapMatchStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMapStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMapStatsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMapStatsExtendedExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMapStatsExtendedExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchKillCount;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchKillCountCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchResults;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchResultsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchStatsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerOverallMatchStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerOverallMatchStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerOverallStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerOverallStatsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerOverallStatsExtendedExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerOverallStatsExtendedExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundAssistStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundAssistStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundDamageStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundDamageStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundDeathStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundDeathStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundEventStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundEventStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundExtendedStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundExtendedStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundFlashAssistStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundFlashAssistStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundKillStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundKillStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundUtilityStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerRoundUtilityStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponMapKills;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponMapKillsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponMapRanking;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponMapRankingCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponMatchKills;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponMatchKillsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponOverallKills;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponOverallKillsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponRanking;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerWeaponRankingCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundEvents;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundHitEvents;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundHitEventsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundHitEventsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundHitStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundHitStatsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundKillEvents;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundKillEventsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundKillEventsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundPlayerWeaponStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundPlayerWeaponStatsCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundScorecard;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundScorecardCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundShotEvents;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundShotStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundShotStatsExtendedCache;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundStats;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestRoundStatsExtendedCache;
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

	public RestEntryKillStatsCache fromSvcToRest(SvcEntryKillStatsCache svc);

	public SvcEntryKillStatsCache fromRestToSvc(RestEntryKillStatsCache rest);

	public RestEntryKillStatsExtendedCache fromSvcToRest(SvcEntryKillStatsExtendedCache svc);

	public SvcEntryKillStatsExtendedCache fromRestToSvc(RestEntryKillStatsExtendedCache rest);

	public RestMapHitStatsExtendedCache fromSvcToRest(SvcMapHitStatsExtendedCache svc);

	public SvcMapHitStatsExtendedCache fromRestToSvc(RestMapHitStatsExtendedCache rest);

	public RestMapPlayerWeaponStatsCache fromSvcToRest(SvcMapPlayerWeaponStatsCache svc);

	public SvcMapPlayerWeaponStatsCache fromRestToSvc(RestMapPlayerWeaponStatsCache rest);

	public RestMapShotStatsExtendedCache fromSvcToRest(SvcMapShotStatsExtendedCache svc);

	public SvcMapShotStatsExtendedCache fromRestToSvc(RestMapShotStatsExtendedCache rest);

	public RestMatchHitStatsExtendedCache fromSvcToRest(SvcMatchHitStatsExtendedCache svc);

	public SvcMatchHitStatsExtendedCache fromRestToSvc(RestMatchHitStatsExtendedCache rest);

	public RestMatchPlayerWeaponStatsCache fromSvcToRest(SvcMatchPlayerWeaponStatsCache svc);

	public SvcMatchPlayerWeaponStatsCache fromRestToSvc(RestMatchPlayerWeaponStatsCache rest);

	public RestMatchResultsCache fromSvcToRest(SvcMatchResultsCache svc);

	public SvcMatchResultsCache fromRestToSvc(RestMatchResultsCache rest);

	public RestMatchShotStatsExtendedCache fromSvcToRest(SvcMatchShotStatsExtendedCache svc);

	public SvcMatchShotStatsExtendedCache fromRestToSvc(RestMatchShotStatsExtendedCache rest);

	public RestMatchStats fromSvcToRest(SvcMatchStats svc);

	public SvcMatchStats fromRestToSvc(RestMatchStats rest);

	public RestOverallHitStatsExtendedCache fromSvcToRest(SvcOverallHitStatsExtendedCache svc);

	public SvcOverallHitStatsExtendedCache fromRestToSvc(RestOverallHitStatsExtendedCache rest);

	public RestOverallPlayerWeaponStatsCache fromSvcToRest(SvcOverallPlayerWeaponStatsCache svc);

	public SvcOverallPlayerWeaponStatsCache fromRestToSvc(RestOverallPlayerWeaponStatsCache rest);

	public RestOverallShotStatsExtendedCache fromSvcToRest(SvcOverallShotStatsExtendedCache svc);

	public SvcOverallShotStatsExtendedCache fromRestToSvc(RestOverallShotStatsExtendedCache rest);

	public RestPlayerClutchStatsCache fromSvcToRest(SvcPlayerClutchStatsCache svc);

	public SvcPlayerClutchStatsCache fromRestToSvc(RestPlayerClutchStatsCache rest);

	public RestPlayerKillCountCache fromSvcToRest(SvcPlayerKillCountCache svc);

	public SvcPlayerKillCountCache fromRestToSvc(RestPlayerKillCountCache rest);

	public RestPlayerMapMatchStatsCache fromSvcToRest(SvcPlayerMapMatchStatsCache svc);

	public SvcPlayerMapMatchStatsCache fromRestToSvc(RestPlayerMapMatchStatsCache rest);

	public RestPlayerMapStatsExtendedCache fromSvcToRest(SvcPlayerMapStatsExtendedCache svc);

	public SvcPlayerMapStatsExtendedCache fromRestToSvc(RestPlayerMapStatsExtendedCache rest);

	public RestPlayerMapStatsExtendedExtendedCache fromSvcToRest(SvcPlayerMapStatsExtendedExtendedCache svc);

	public SvcPlayerMapStatsExtendedExtendedCache fromRestToSvc(RestPlayerMapStatsExtendedExtendedCache rest);

	public RestPlayerMatchKillCountCache fromSvcToRest(SvcPlayerMatchKillCountCache svc);

	public SvcPlayerMatchKillCountCache fromRestToSvc(RestPlayerMatchKillCountCache rest);

	public RestPlayerMatchResultsCache fromSvcToRest(SvcPlayerMatchResultsCache svc);

	public SvcPlayerMatchResultsCache fromRestToSvc(RestPlayerMatchResultsCache rest);

	public RestPlayerMatchStatsCache fromSvcToRest(SvcPlayerMatchStatsCache svc);

	public SvcPlayerMatchStatsCache fromRestToSvc(RestPlayerMatchStatsCache rest);

	public RestPlayerMatchStatsExtendedCache fromSvcToRest(SvcPlayerMatchStatsExtendedCache svc);

	public SvcPlayerMatchStatsExtendedCache fromRestToSvc(RestPlayerMatchStatsExtendedCache rest);

	public RestPlayerOverallMatchStatsCache fromSvcToRest(SvcPlayerOverallMatchStatsCache svc);

	public SvcPlayerOverallMatchStatsCache fromRestToSvc(RestPlayerOverallMatchStatsCache rest);

	public RestPlayerOverallStatsExtendedCache fromSvcToRest(SvcPlayerOverallStatsExtendedCache svc);

	public SvcPlayerOverallStatsExtendedCache fromRestToSvc(RestPlayerOverallStatsExtendedCache rest);

	public RestPlayerOverallStatsExtendedExtendedCache fromSvcToRest(SvcPlayerOverallStatsExtendedExtendedCache svc);

	public SvcPlayerOverallStatsExtendedExtendedCache fromRestToSvc(RestPlayerOverallStatsExtendedExtendedCache rest);

	public RestPlayerRoundAssistStatsCache fromSvcToRest(SvcPlayerRoundAssistStatsCache svc);

	public SvcPlayerRoundAssistStatsCache fromRestToSvc(RestPlayerRoundAssistStatsCache rest);

	public RestPlayerRoundDamageStatsCache fromSvcToRest(SvcPlayerRoundDamageStatsCache svc);

	public SvcPlayerRoundDamageStatsCache fromRestToSvc(RestPlayerRoundDamageStatsCache rest);

	public RestPlayerRoundDeathStatsCache fromSvcToRest(SvcPlayerRoundDeathStatsCache svc);

	public SvcPlayerRoundDeathStatsCache fromRestToSvc(RestPlayerRoundDeathStatsCache rest);

	public RestPlayerRoundEventStatsCache fromSvcToRest(SvcPlayerRoundEventStatsCache svc);

	public SvcPlayerRoundEventStatsCache fromRestToSvc(RestPlayerRoundEventStatsCache rest);

	public RestPlayerRoundExtendedStatsCache fromSvcToRest(SvcPlayerRoundExtendedStatsCache svc);

	public SvcPlayerRoundExtendedStatsCache fromRestToSvc(RestPlayerRoundExtendedStatsCache rest);

	public RestPlayerRoundFlashAssistStatsCache fromSvcToRest(SvcPlayerRoundFlashAssistStatsCache svc);

	public SvcPlayerRoundFlashAssistStatsCache fromRestToSvc(RestPlayerRoundFlashAssistStatsCache rest);

	public RestPlayerRoundKillStatsCache fromSvcToRest(SvcPlayerRoundKillStatsCache svc);

	public SvcPlayerRoundKillStatsCache fromRestToSvc(RestPlayerRoundKillStatsCache rest);

	public RestPlayerRoundStats fromSvcToRest(SvcPlayerRoundStats svc);

	public SvcPlayerRoundStats fromRestToSvc(RestPlayerRoundStats rest);

	public RestPlayerRoundUtilityStatsCache fromSvcToRest(SvcPlayerRoundUtilityStatsCache svc);

	public SvcPlayerRoundUtilityStatsCache fromRestToSvc(RestPlayerRoundUtilityStatsCache rest);

	public RestPlayerStats fromSvcToRest(SvcPlayerStats svc);

	public SvcPlayerStats fromRestToSvc(RestPlayerStats rest);

	public RestPlayerWeaponMapKillsCache fromSvcToRest(SvcPlayerWeaponMapKillsCache svc);

	public SvcPlayerWeaponMapKillsCache fromRestToSvc(RestPlayerWeaponMapKillsCache rest);

	public RestPlayerWeaponMapRankingCache fromSvcToRest(SvcPlayerWeaponMapRankingCache svc);

	public SvcPlayerWeaponMapRankingCache fromRestToSvc(RestPlayerWeaponMapRankingCache rest);

	public RestPlayerWeaponMatchKillsCache fromSvcToRest(SvcPlayerWeaponMatchKillsCache svc);

	public SvcPlayerWeaponMatchKillsCache fromRestToSvc(RestPlayerWeaponMatchKillsCache rest);

	public RestPlayerWeaponOverallKillsCache fromSvcToRest(SvcPlayerWeaponOverallKillsCache svc);

	public SvcPlayerWeaponOverallKillsCache fromRestToSvc(RestPlayerWeaponOverallKillsCache rest);

	public RestPlayerWeaponRankingCache fromSvcToRest(SvcPlayerWeaponRankingCache svc);

	public SvcPlayerWeaponRankingCache fromRestToSvc(RestPlayerWeaponRankingCache rest);

	public RestRoundEvents fromSvcToRest(SvcRoundEvents svc);

	public SvcRoundEvents fromRestToSvc(RestRoundEvents rest);

	public RestRoundHitEvents fromSvcToRest(SvcRoundHitEvents svc);

	public SvcRoundHitEvents fromRestToSvc(RestRoundHitEvents rest);

	public RestRoundHitEventsExtendedCache fromSvcToRest(SvcRoundHitEventsExtendedCache svc);

	public SvcRoundHitEventsExtendedCache fromRestToSvc(RestRoundHitEventsExtendedCache rest);

	public RestRoundHitStatsExtendedCache fromSvcToRest(SvcRoundHitStatsExtendedCache svc);

	public SvcRoundHitStatsExtendedCache fromRestToSvc(RestRoundHitStatsExtendedCache rest);

	public RestRoundKillEvents fromSvcToRest(SvcRoundKillEvents svc);

	public SvcRoundKillEvents fromRestToSvc(RestRoundKillEvents rest);

	public RestRoundKillEventsExtendedCache fromSvcToRest(SvcRoundKillEventsExtendedCache svc);

	public SvcRoundKillEventsExtendedCache fromRestToSvc(RestRoundKillEventsExtendedCache rest);

	public RestRoundPlayerWeaponStatsCache fromSvcToRest(SvcRoundPlayerWeaponStatsCache svc);

	public SvcRoundPlayerWeaponStatsCache fromRestToSvc(RestRoundPlayerWeaponStatsCache rest);

	public RestRoundScorecardCache fromSvcToRest(SvcRoundScorecardCache svc);

	public SvcRoundScorecardCache fromRestToSvc(RestRoundScorecardCache rest);

	public RestRoundShotEvents fromSvcToRest(SvcRoundShotEvents svc);

	public SvcRoundShotEvents fromRestToSvc(RestRoundShotEvents rest);

	public RestRoundShotStatsExtendedCache fromSvcToRest(SvcRoundShotStatsExtendedCache svc);

	public SvcRoundShotStatsExtendedCache fromRestToSvc(RestRoundShotStatsExtendedCache rest);

	public RestRoundStats fromSvcToRest(SvcRoundStats svc);

	public SvcRoundStats fromRestToSvc(RestRoundStats rest);

	public RestRoundStatsExtendedCache fromSvcToRest(SvcRoundStatsExtendedCache svc);

	public SvcRoundStatsExtendedCache fromRestToSvc(RestRoundStatsExtendedCache rest);

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
