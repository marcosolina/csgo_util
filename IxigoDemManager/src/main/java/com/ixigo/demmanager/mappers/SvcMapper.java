package com.ixigo.demmanager.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.ixigo.demmanager.models.database.Match_statsDto;
import com.ixigo.demmanager.models.database.Player_round_statsDto;
import com.ixigo.demmanager.models.database.Player_statsDto;
import com.ixigo.demmanager.models.database.Round_eventsDto;
import com.ixigo.demmanager.models.database.Round_hit_eventsDto;
import com.ixigo.demmanager.models.database.Round_kill_eventsDto;
import com.ixigo.demmanager.models.database.Round_shot_eventsDto;
import com.ixigo.demmanager.models.database.Round_statsDto;
import com.ixigo.demmanager.models.database.UsersDto;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMapStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundEvent;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundHitEvent;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundKillEvent;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundShotEvent;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcUser;

/**
 * Simple mapper to map the models between the Service and Repository layers
 * 
 * @author marco
 *
 */
@Mapper(componentModel = "spring")
public interface SvcMapper {
	@Mappings({
	@Mapping(source = "steam_id", target = "steamId"),
	@Mapping(source = "user_name", target = "userName")
	})
	public SvcUser fromDtoToSvc(UsersDto dto);
	
	@Mappings({
	@Mapping(source = "steamId", target = "steam_id"),
	@Mapping(source = "userName", target = "user_name")
	})
	public UsersDto fromSvcToDto(SvcUser dto);
	
	@Mappings({
		@Mapping(source = "eventType", target = "eventtype"),
		@Mapping(source = "matchId", target = "match_id"),
		@Mapping(source = "round", target = "round"),
		@Mapping(source = "steamID", target = "steamid"),
		@Mapping(source = "time", target = "eventtime")
	})
	public Round_eventsDto fromSvcToDto(SvcRoundEvent svc);

	@Mappings({
	@Mapping(source = "date", target = "match_date"),
	@Mapping(source = "mapName", target = "mapname"),
	@Mapping(source = "fileName", target = "match_filename"),
	@Mapping(source = "matchId", target = "match_id")
	})
	public Match_statsDto fromSvcToDto(SvcMapStats svc);
	
	@Mappings({
	@Mapping(source = "userName", target = "username"),
	@Mapping(source = "steamID", target = "steamid"),
	@Mapping(source = "matchId", target = "match_id"),
	@Mapping(source = "score", target = "score")
	})
	public Player_statsDto fromSvcToDto(SvcPlayerStats svc);
	
	@Mappings({
	@Mapping(source = "roundNumber", target = "roundnumber"),
	@Mapping(source = "winnerSide", target = "winnerside"),
	@Mapping(source = "reasonEndRound", target = "reasonendround"),
	@Mapping(source = "matchId", target = "match_id")
	})
	public Round_statsDto fromSvcToDto(SvcRoundStats svc);
	
	@Mappings({
	@Mapping(source = "clutchChance", target = "clutchchance"),
	@Mapping(source = "clutchSuccess", target = "clutchsuccess"),
	@Mapping(source = "equipmentValue", target = "equipmentvalue"),
	@Mapping(source = "matchId", target = "match_id"),
	@Mapping(source = "moneySpent", target = "moneyspent"),
	@Mapping(source = "mvp", target = "mvp"),
	@Mapping(source = "round", target = "round"),
	@Mapping(source = "steamID", target = "steamid"),
	@Mapping(source = "survived", target = "survived"),
	@Mapping(source = "team", target = "team"),
	@Mapping(source = "userName", target = "username")
	})
	public Player_round_statsDto fromSvcToDto(SvcPlayerRoundStats svc);
	
	@Mappings({
	@Mapping(source = "assister", target = "assister"),
	@Mapping(source = "matchId", target = "match_id"),
	@Mapping(source = "flashAssister", target = "flashassister"),
	@Mapping(source = "headshot", target = "headshot"),
	@Mapping(source = "isFirstKill", target = "isfirstkill"),
	@Mapping(source = "isTradeDeath", target = "istradedeath"),
	@Mapping(source = "isTradeKill", target = "istradekill"),
	@Mapping(source = "killerFlashed", target = "killerflashed"),
	@Mapping(source = "round", target = "round"),
	@Mapping(source = "steamID", target = "steamid"),
	@Mapping(source = "time", target = "eventtime"),
	@Mapping(source = "victimSteamId", target = "victimsteamid"),
	@Mapping(source = "weapon", target = "weapon")
	})
	public Round_kill_eventsDto fromSvcToDto(SvcRoundKillEvent svc);
	
	@Mappings({
	@Mapping(source = "eventType", target = "eventtype"),
	@Mapping(source = "matchId", target = "match_id"),
	@Mapping(source = "round", target = "round"),
	@Mapping(source = "steamID", target = "steamid"),
	@Mapping(source = "time", target = "eventtime"),
	@Mapping(source = "weapon", target = "weapon")
	})
	public Round_shot_eventsDto fromSvcToDto(SvcRoundShotEvent svc);
	
	@Mappings({
	@Mapping(source = "blindTime", target = "blindtime"),
	@Mapping(source = "damageArmour", target = "damagearmour"),
	@Mapping(source = "damageHealth", target = "damagehealth"),
	@Mapping(source = "matchId", target = "match_id"),
	@Mapping(source = "hitGroup", target = "hitgroup"),
	@Mapping(source = "round", target = "round"),
	@Mapping(source = "steamID", target = "steamid"),
	@Mapping(source = "time", target = "eventtime"),
	@Mapping(source = "victimSteamId", target = "victimsteamid"),
	@Mapping(source = "weapon", target = "weapon")
	})
	public Round_hit_eventsDto fromSvcToDto(SvcRoundHitEvent svc);
	
	
	/*
	public default Users_scoresDto fromUserMapStatsToEntityUserScore(SvcMapStats ms, SvcUserGotvScore userScore) {
		Users_scoresDto ums = new Users_scoresDto();

		ums.setGame_date(ms.getPlayedOn());
		ums.setMap(ms.getMapName());
		ums.setSteam_id(userScore.getSteamID());

		ums.setKills(userScore.getKills());
		ums.setAssists(userScore.getAssists());
		ums.setDeaths(userScore.getDeaths());
		ums.setTdh(userScore.getTotalDamageHealth());
		ums.setTda(userScore.getTotalDamageArmor());
		ums.set_1v1(userScore.getOneVersusOne());
		ums.set_1v2(userScore.getOneVersusTwo());
		ums.set_1v3(userScore.getOneVersusThree());
		ums.set_1v4(userScore.getOneVersusFour());
		ums.set_1v5(userScore.getOneVersusFive());
		ums.setGrenades(userScore.getGrenadesThrownCount());
		ums.setFlashes(userScore.getFlashesThrownCount());
		ums.setSmokes(userScore.getSmokesThrownCount());
		ums.setFire(userScore.getFireThrownCount());
		ums.setHed(userScore.getHighExplosiveDamage());
		ums.setFd(userScore.getFireDamage());
		ums.set_5k(userScore.getFiveKills());
		ums.set_4k(userScore.getFourKills());
		ums.set_3k(userScore.getThreeKills());
		ums.set_2k(userScore.getTwoKills());
		ums.set_1k(userScore.getOneKill());
		ums.setTk(userScore.getTradeKill());
		ums.setTd(userScore.getTradeDeath());
		ums.setFf(userScore.getTeamKillFriendlyFire());
		ums.setEk(userScore.getEntryKill());
		ums.setBp(userScore.getBombPlanted());
		ums.setBd(userScore.getBombDefused());
		ums.setMvp(userScore.getMostValuablePlayer());
		ums.setScore(userScore.getScore());
		ums.setHs(userScore.getHeadShots());
		ums.setSide(userScore.getSide());

		ums.setRws(RoundParserUtils.doubleToBigDecimal(userScore.getRoundWinShare("), 2));
		ums.setKdr(RoundParserUtils.doubleToBigDecimal(userScore.getKillDeathRatio("), 2));
		ums.setHsp(RoundParserUtils.doubleToBigDecimal(userScore.getHeadShotsPercentage("), 2));
		ums.setHltv(RoundParserUtils.doubleToBigDecimal(userScore.getHalfLifeTelevisionRating("), 3));
		ums.setKpr(RoundParserUtils.doubleToBigDecimal(userScore.getKillPerRound("), 2));
		ums.setApr(RoundParserUtils.doubleToBigDecimal(userScore.getAssistsPerRound("), 2));
		ums.setDpr(RoundParserUtils.doubleToBigDecimal(userScore.getDeathPerRound("), 2));
		ums.setAdr(RoundParserUtils.doubleToBigDecimal(userScore.getAverageDamagePerRound("), 2));
		ums.setMp(RoundParserUtils.doubleToBigDecimal(userScore.getMatchPlayed("), 2));
		return ums;
	}

	public default SvcMapStats fromUsersScoreDtoToSvcMapStata(UsersDto userDto, Users_scoresDto userScore) {
		SvcMapStats mapStats = new SvcMapStats();
		mapStats.setMapName(userScore.getMap());
		mapStats.setPlayedOn(userScore.getGame_date());

		SvcUserGotvScore gotvScore = new SvcUserGotvScore();

		gotvScore.setUserName(userDto.getUser_name());
		gotvScore.setSteamID(userDto.getSteam_id());
		gotvScore.setKills(userScore.getKills());
		gotvScore.setAssists(userScore.getAssists());
		gotvScore.setDeaths(userScore.getDeaths());
		gotvScore.setTotalDamageHealth(userScore.getTdh());
		gotvScore.setTotalDamageArmor(userScore.getTda());
		gotvScore.setOneVersusOne(userScore.get_1v1());
		gotvScore.setOneVersusTwo(userScore.get_1v2());
		gotvScore.setOneVersusThree(userScore.get_1v3());
		gotvScore.setOneVersusFour(userScore.get_1v4());
		gotvScore.setOneVersusFive(userScore.get_1v5());
		gotvScore.setGrenadesThrownCount(userScore.getGrenades());
		gotvScore.setFlashesThrownCount(userScore.getFlashes());
		gotvScore.setSmokesThrownCount(userScore.getSmokes());
		gotvScore.setFireThrownCount(userScore.getFire());
		gotvScore.setHighExplosiveDamage(userScore.getHed());
		gotvScore.setFireDamage(userScore.getFd());
		gotvScore.setFiveKills(userScore.get_5k());
		gotvScore.setFourKills(userScore.get_4k());
		gotvScore.setThreeKills(userScore.get_3k());
		gotvScore.setTwoKills(userScore.get_2k());
		gotvScore.setOneKill(userScore.get_1k());
		gotvScore.setTradeKill(userScore.getTk());
		gotvScore.setTradeDeath(userScore.getTd());
		gotvScore.setTeamKillFriendlyFire(userScore.getFf());
		gotvScore.setEntryKill(userScore.getEk());
		gotvScore.setBombPlanted(userScore.getBp());
		gotvScore.setBombDefused(userScore.getBd());
		gotvScore.setMostValuablePlayer(userScore.getMvp());
		gotvScore.setScore(userScore.getScore());
		gotvScore.setHeadShots(userScore.getHs());
		gotvScore.setSide(userScore.getSide());

		gotvScore.setRoundWinShare(RoundParserUtils.bigDecimalToDouble(userScore.getRws("), 2));
		gotvScore.setKillDeathRatio(RoundParserUtils.bigDecimalToDouble(userScore.getKdr("), 2));
		gotvScore.setHeadShotsPercentage(RoundParserUtils.bigDecimalToDouble(userScore.getHsp("), 2));
		gotvScore.setHalfLifeTelevisionRating(RoundParserUtils.bigDecimalToDouble(userScore.getHltv("), 3));
		gotvScore.setKillPerRound(RoundParserUtils.bigDecimalToDouble(userScore.getKpr("), 2));
		gotvScore.setAssistsPerRound(RoundParserUtils.bigDecimalToDouble(userScore.getApr("), 2));
		gotvScore.setDeathPerRound(RoundParserUtils.bigDecimalToDouble(userScore.getDpr("), 2));
		gotvScore.setAverageDamagePerRound(RoundParserUtils.bigDecimalToDouble(userScore.getAdr("), 2));
		gotvScore.setMatchPlayed(RoundParserUtils.bigDecimalToDouble(userScore.getMp("), 2));

		mapStats.addUserMapStats(gotvScore);
		return mapStats;
	}
	*/
}
