package com.ixigo.demmanager.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ixigo.demmanager.models.database.Match_statsDto;
import com.ixigo.demmanager.models.database.Player_round_statsDto;
import com.ixigo.demmanager.models.database.Player_statsDto;
import com.ixigo.demmanager.models.database.Round_eventsDto;
import com.ixigo.demmanager.models.database.Round_hit_eventsDto;
import com.ixigo.demmanager.models.database.Round_kill_eventsDto;
import com.ixigo.demmanager.models.database.Round_shot_eventsDto;
import com.ixigo.demmanager.models.database.Round_statsDto;
import com.ixigo.demmanager.models.database.UsersDto;
import com.ixigo.demmanager.models.svc.demdata.SvcUser;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcMapFileStats;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcPlayerRoundStats;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcPlayerStats;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcRoundEvent;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcRoundHitEvent;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcRoundKillEvent;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcRoundShotEvent;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcRoundStats;

/**
 * Simple mapper to map the models between the Service and Repository layers
 * 
 * @author marco
 *
 */
@Mapper(componentModel = "spring")
public interface SvcMapper {
	@Mapping(source = "steam_id", target = "steamId")
	@Mapping(source = "user_name", target = "userName")
	public SvcUser fromDtoToSvc(UsersDto dto);
	
	@Mapping(source = "steamId", target = "steam_id")
	@Mapping(source = "userName", target = "user_name")
	public UsersDto fromSvcToDto(SvcUser dto);
	
	@Mapping(source = SvcMapFileStats.Fields.date, target = Match_statsDto.Fields.match_date)
	@Mapping(source = SvcMapFileStats.Fields.mapName, target = Match_statsDto.Fields.mapname)
	@Mapping(source = SvcMapFileStats.Fields.fileName, target = Match_statsDto.Fields.match_filename)
	//@Mapping(source = "matchId", target = "match_id")
	public Match_statsDto fromSvcToDto(SvcMapFileStats svc);
	
	@Mapping(source = SvcPlayerStats.Fields.userName, target = Player_statsDto.Fields.username)
	@Mapping(source = SvcPlayerStats.Fields.steamID, target = Player_statsDto.Fields.steamid)
	@Mapping(source = SvcPlayerStats.Fields.fileName, target = Player_statsDto.Fields.match_filename)
	@Mapping(source = SvcPlayerStats.Fields.score, target = Player_statsDto.Fields.score)
	public Player_statsDto fromSvcToDto(SvcPlayerStats svc);
	
	@Mapping(source = SvcRoundStats.Fields.roundNumber, target = Round_statsDto.Fields.roundnumber)
	@Mapping(source = SvcRoundStats.Fields.winnerSide, target = Round_statsDto.Fields.winnerside)
	@Mapping(source = SvcRoundStats.Fields.reasonEndRound, target = Round_statsDto.Fields.reasonendround)
	@Mapping(source = SvcRoundStats.Fields.fileName, target = Round_statsDto.Fields.match_filename)
	public Round_statsDto fromSvcToDto(SvcRoundStats svc);
	
	@Mapping(source = SvcPlayerRoundStats.Fields.clutchChance, target = Player_round_statsDto.Fields.clutchchance)
	@Mapping(source = SvcPlayerRoundStats.Fields.clutchSuccess, target = Player_round_statsDto.Fields.clutchsuccess)
	@Mapping(source = SvcPlayerRoundStats.Fields.equipmentValue, target = Player_round_statsDto.Fields.equipmentvalue)
	@Mapping(source = SvcPlayerRoundStats.Fields.fileName, target = Player_round_statsDto.Fields.match_filename)
	@Mapping(source = SvcPlayerRoundStats.Fields.moneySpent, target = Player_round_statsDto.Fields.moneyspent)
	@Mapping(source = SvcPlayerRoundStats.Fields.mvp, target = Player_round_statsDto.Fields.mvp)
	@Mapping(source = SvcPlayerRoundStats.Fields.round, target = Player_round_statsDto.Fields.round)
	@Mapping(source = SvcPlayerRoundStats.Fields.steamID, target = Player_round_statsDto.Fields.steamid)
	@Mapping(source = SvcPlayerRoundStats.Fields.survived, target = Player_round_statsDto.Fields.survived)
	@Mapping(source = SvcPlayerRoundStats.Fields.team, target = Player_round_statsDto.Fields.team)
	@Mapping(source = SvcPlayerRoundStats.Fields.userName, target = Player_round_statsDto.Fields.username)
	public Player_round_statsDto fromSvcToDto(SvcPlayerRoundStats svc);
	
	@Mapping(source = SvcRoundKillEvent.Fields.assister, target = Round_kill_eventsDto.Fields.assister)
	@Mapping(source = SvcRoundKillEvent.Fields.fileName, target = Round_kill_eventsDto.Fields.match_filename)
	@Mapping(source = SvcRoundKillEvent.Fields.flashAssister, target = Round_kill_eventsDto.Fields.flashassister)
	@Mapping(source = SvcRoundKillEvent.Fields.headshot, target = Round_kill_eventsDto.Fields.headshot)
	@Mapping(source = SvcRoundKillEvent.Fields.isFirstKill, target = Round_kill_eventsDto.Fields.isfirstkill)
	@Mapping(source = SvcRoundKillEvent.Fields.isTradeDeath, target = Round_kill_eventsDto.Fields.istradedeath)
	@Mapping(source = SvcRoundKillEvent.Fields.isTradeKill, target = Round_kill_eventsDto.Fields.istradekill)
	@Mapping(source = SvcRoundKillEvent.Fields.killerFlashed, target = Round_kill_eventsDto.Fields.killerflashed)
	@Mapping(source = SvcRoundKillEvent.Fields.round, target = Round_kill_eventsDto.Fields.round)
	@Mapping(source = SvcRoundKillEvent.Fields.steamID, target = Round_kill_eventsDto.Fields.steamid)
	@Mapping(source = SvcRoundKillEvent.Fields.time, target = Round_kill_eventsDto.Fields.eventtime)
	@Mapping(source = SvcRoundKillEvent.Fields.victimSteamId, target = Round_kill_eventsDto.Fields.victimsteamid)
	@Mapping(source = SvcRoundKillEvent.Fields.weapon, target = Round_kill_eventsDto.Fields.weapon)
	public Round_kill_eventsDto fromSvcToDto(SvcRoundKillEvent svc);
	
	@Mapping(source = SvcRoundShotEvent.Fields.eventType, target = Round_shot_eventsDto.Fields.eventtype)
	@Mapping(source = SvcRoundShotEvent.Fields.fileName, target = Round_shot_eventsDto.Fields.match_filename)
	@Mapping(source = SvcRoundShotEvent.Fields.round, target = Round_shot_eventsDto.Fields.round)
	@Mapping(source = SvcRoundShotEvent.Fields.steamID, target = Round_shot_eventsDto.Fields.steamid)
	@Mapping(source = SvcRoundShotEvent.Fields.time, target = Round_shot_eventsDto.Fields.eventtime)
	@Mapping(source = SvcRoundShotEvent.Fields.weapon, target = Round_shot_eventsDto.Fields.weapon)
	public Round_shot_eventsDto fromSvcToDto(SvcRoundShotEvent svc);
	
	@Mapping(source = SvcRoundHitEvent.Fields.blindTime, target = Round_hit_eventsDto.Fields.blindtime)
	@Mapping(source = SvcRoundHitEvent.Fields.damageArmour, target = Round_hit_eventsDto.Fields.damagearmour)
	@Mapping(source = SvcRoundHitEvent.Fields.damageHealth, target = Round_hit_eventsDto.Fields.damagehealth)
	@Mapping(source = SvcRoundHitEvent.Fields.fileName, target = Round_hit_eventsDto.Fields.match_filename)
	@Mapping(source = SvcRoundHitEvent.Fields.hitGroup, target = Round_hit_eventsDto.Fields.hitgroup)
	@Mapping(source = SvcRoundHitEvent.Fields.round, target = Round_hit_eventsDto.Fields.round)
	@Mapping(source = SvcRoundHitEvent.Fields.steamID, target = Round_hit_eventsDto.Fields.steamid)
	@Mapping(source = SvcRoundHitEvent.Fields.time, target = Round_hit_eventsDto.Fields.eventtime)
	@Mapping(source = SvcRoundHitEvent.Fields.victimSteamId, target = Round_hit_eventsDto.Fields.victimsteamid)
	@Mapping(source = SvcRoundHitEvent.Fields.weapon, target = Round_hit_eventsDto.Fields.weapon)
	public Round_hit_eventsDto fromSvcToDto(SvcRoundHitEvent svc);
	
	@Mapping(source = SvcRoundEvent.Fields.eventType, target = Round_eventsDto.Fields.eventtype)
	@Mapping(source = SvcRoundEvent.Fields.fileName, target = Round_eventsDto.Fields.match_filename)
	@Mapping(source = SvcRoundEvent.Fields.round, target = Round_eventsDto.Fields.round)
	@Mapping(source = SvcRoundEvent.Fields.steamID, target = Round_eventsDto.Fields.steamid)
	@Mapping(source = SvcRoundEvent.Fields.time, target = Round_eventsDto.Fields.eventtime)
	public Round_eventsDto fromSvcToDto(SvcRoundEvent svc);
	
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

		ums.setRws(RoundParserUtils.doubleToBigDecimal(userScore.getRoundWinShare(), 2));
		ums.setKdr(RoundParserUtils.doubleToBigDecimal(userScore.getKillDeathRatio(), 2));
		ums.setHsp(RoundParserUtils.doubleToBigDecimal(userScore.getHeadShotsPercentage(), 2));
		ums.setHltv(RoundParserUtils.doubleToBigDecimal(userScore.getHalfLifeTelevisionRating(), 3));
		ums.setKpr(RoundParserUtils.doubleToBigDecimal(userScore.getKillPerRound(), 2));
		ums.setApr(RoundParserUtils.doubleToBigDecimal(userScore.getAssistsPerRound(), 2));
		ums.setDpr(RoundParserUtils.doubleToBigDecimal(userScore.getDeathPerRound(), 2));
		ums.setAdr(RoundParserUtils.doubleToBigDecimal(userScore.getAverageDamagePerRound(), 2));
		ums.setMp(RoundParserUtils.doubleToBigDecimal(userScore.getMatchPlayed(), 2));
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

		gotvScore.setRoundWinShare(RoundParserUtils.bigDecimalToDouble(userScore.getRws(), 2));
		gotvScore.setKillDeathRatio(RoundParserUtils.bigDecimalToDouble(userScore.getKdr(), 2));
		gotvScore.setHeadShotsPercentage(RoundParserUtils.bigDecimalToDouble(userScore.getHsp(), 2));
		gotvScore.setHalfLifeTelevisionRating(RoundParserUtils.bigDecimalToDouble(userScore.getHltv(), 3));
		gotvScore.setKillPerRound(RoundParserUtils.bigDecimalToDouble(userScore.getKpr(), 2));
		gotvScore.setAssistsPerRound(RoundParserUtils.bigDecimalToDouble(userScore.getApr(), 2));
		gotvScore.setDeathPerRound(RoundParserUtils.bigDecimalToDouble(userScore.getDpr(), 2));
		gotvScore.setAverageDamagePerRound(RoundParserUtils.bigDecimalToDouble(userScore.getAdr(), 2));
		gotvScore.setMatchPlayed(RoundParserUtils.bigDecimalToDouble(userScore.getMp(), 2));

		mapStats.addUserMapStats(gotvScore);
		return mapStats;
	}
	*/
}
