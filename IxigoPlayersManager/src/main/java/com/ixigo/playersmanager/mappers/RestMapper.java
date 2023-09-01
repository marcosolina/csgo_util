package com.ixigo.playersmanager.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchStatsExtended;
import com.ixigo.playersmanager.models.svc.SvcMapStats;
import com.ixigo.playersmanager.models.svc.SvcTeam;
import com.ixigo.playersmanager.models.svc.SvcUserAvgScore;
import com.ixigo.playersmanagercontract.models.rest.RestTeam;
import com.ixigo.playersmanagercontract.models.rest.RestUserAvgScore;

@Mapper(componentModel = "spring")
public interface RestMapper {
	
	@Mapping(source = "kills", target = "kills")
	@Mapping(source = "ff", target = "teamKillFriendlyFire")
	@Mapping(source = "hltv_rating", target = "halfLifeTelevisionRating")
	@Mapping(source = "bd", target = "bombDefused")
	@Mapping(source = "_1v3", target = "oneVersusThree")
	@Mapping(source = "_1v2", target = "oneVersusTwo")
	@Mapping(source = "kast", target = "kast")
	@Mapping(source = "_1v1", target = "oneVersusOne")
	@Mapping(source = "hr", target = "hostageRescued")
	@Mapping(source = "match_date", target = "matchDate")
	@Mapping(source = "bp", target = "bombPlanted")
	@Mapping(source = "ud", target = "fireDamage")
	@Mapping(source = "kasttotal", target = "kasttotal")
	@Mapping(source = "rws", target = "roundWinShare")
	@Mapping(source = "score", target = "score")
	@Mapping(source = "headshots", target = "headShots")
	@Mapping(source = "assists", target = "assists")
	@Mapping(source = "_4k", target = "fourKills")
	@Mapping(source = "last_round_team", target = "lastRoundTeam")
	@Mapping(source = "_2k", target = "twoKills")
	@Mapping(source = "usernames", target = "userName")
	@Mapping(source = "_1v5", target = "oneVersusFive")
	@Mapping(source = "_1v4", target = "oneVersusFour")
	@Mapping(source = "headshot_percentage", target = "headShotsPercentage")
	@Mapping(source = "deaths", target = "deaths")
	@Mapping(source = "rounds_on_team2", target = "roundsOnTeam2")
	@Mapping(source = "rounds_on_team1", target = "roundsOnTeam1")
	@Mapping(source = "ffd", target = "teammateDamageHealth")
	@Mapping(source = "roundsplayed", target = "roundsplayed")
	@Mapping(source = "ek", target = "entryKill")
	@Mapping(source = "mvp", target = "mostValuablePlayer")
	@Mapping(source = "match_id", target = "matchId")
	@Mapping(source = "dpr", target = "deathPerRound")
	@Mapping(source = "rwstotal", target = "roundWinShareTotal")
	@Mapping(source = "kpr", target = "killPerRound")
	@Mapping(source = "adr", target = "averageDamagePerRound")
	@Mapping(source = "steamid", target = "steamId")
	@Mapping(source = "td", target = "tradeDeath")
	@Mapping(source = "tda", target = "totalDamageArmor")
	@Mapping(source = "_5k", target = "fiveKills")
	@Mapping(source = "_3k", target = "threeKills")
	@Mapping(source = "ebt", target = "opponentBlindTime")
	@Mapping(source = "tk", target = "tradeKill")
	@Mapping(source = "kdr", target = "killDeathRatio")
	@Mapping(source = "_1k", target = "oneKill")
	@Mapping(source = "tdh", target = "totalDamageHealth")
	@Mapping(source = "fbt", target = "teammateBlindTime")
	@Mapping(source = "fa", target = "flashAssists")
	public SvcMapStats fromRestMapStatsToSvc(RestPlayerMatchStatsExtended rest);
	public List<SvcMapStats> fromListRestMapStatsToSvcList(List<RestPlayerMatchStatsExtended> rest);
	
	@Mapping(source = "kills", target = "kills")
	@Mapping(source = "teamKillFriendlyFire", target = "ff")
	@Mapping(source = "halfLifeTelevisionRating", target = "hltv_rating")
	@Mapping(source = "bombDefused", target = "bd")
	@Mapping(source = "oneVersusThree", target = "_1v3")
	@Mapping(source = "oneVersusTwo", target = "_1v2")
	@Mapping(source = "kast", target = "kast")
	@Mapping(source = "oneVersusOne", target = "_1v1")
	@Mapping(source = "hostageRescued", target = "hr")
	@Mapping(source = "matchDate", target = "match_date")
	@Mapping(source = "bombPlanted", target = "bp")
	@Mapping(source = "fireDamage", target = "ud")
	@Mapping(source = "kasttotal", target = "kasttotal")
	@Mapping(source = "roundWinShare", target = "rws")
	@Mapping(source = "score", target = "score")
	@Mapping(source = "headShots", target = "headshots")
	@Mapping(source = "assists", target = "assists")
	@Mapping(source = "fourKills", target = "_4k")
	@Mapping(source = "lastRoundTeam", target = "last_round_team")
	@Mapping(source = "twoKills", target = "_2k")
	@Mapping(source = "userName", target = "usernames")
	@Mapping(source = "oneVersusFive", target = "_1v5")
	@Mapping(source = "oneVersusFour", target = "_1v4")
	@Mapping(source = "headShotsPercentage", target = "headshot_percentage")
	@Mapping(source = "deaths", target = "deaths")
	@Mapping(source = "roundsOnTeam2", target = "rounds_on_team2")
	@Mapping(source = "roundsOnTeam1", target = "rounds_on_team1")
	@Mapping(source = "teammateDamageHealth", target = "ffd")
	@Mapping(source = "roundsplayed", target = "roundsplayed")
	@Mapping(source = "entryKill", target = "ek")
	@Mapping(source = "mostValuablePlayer", target = "mvp")
	@Mapping(source = "matchId", target = "match_id")
	@Mapping(source = "deathPerRound", target = "dpr")
	@Mapping(source = "roundWinShareTotal", target = "rwstotal")
	@Mapping(source = "killPerRound", target = "kpr")
	@Mapping(source = "averageDamagePerRound", target = "adr")
	@Mapping(source = "steamId", target = "steamid")
	@Mapping(source = "tradeDeath", target = "td")
	@Mapping(source = "totalDamageArmor", target = "tda")
	@Mapping(source = "fiveKills", target = "_5k")
	@Mapping(source = "threeKills", target = "_3k")
	@Mapping(source = "opponentBlindTime", target = "ebt")
	@Mapping(source = "tradeKill", target = "tk")
	@Mapping(source = "killDeathRatio", target = "kdr")
	@Mapping(source = "oneKill", target = "_1k")
	@Mapping(source = "totalDamageHealth", target = "tdh")
	@Mapping(source = "teammateBlindTime", target = "fbt")
	@Mapping(source = "flashAssists", target = "fa")
	public RestPlayerMatchStatsExtended fromSvcMapStatsToRest(SvcMapStats svc);
	public List<RestPlayerMatchStatsExtended> fromListSvcMapStatsToRestList(List<SvcMapStats> svc);
	
	public RestTeam fromSvcTeamToRest(SvcTeam svc);
	public List<RestTeam> fromListSvcTeamToRestList(List<SvcTeam> svc);
	
	public RestUserAvgScore fromSvcUserAvgScoreToRest(SvcUserAvgScore svc);
	public List<RestUserAvgScore> fromListSvcUserAvgScoreToRestList(List<SvcUserAvgScore> svc);
}
