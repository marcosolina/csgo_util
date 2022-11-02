package com.ixigo.demmanager.services.implementations.demprocessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.ixigo.demmanager.constants.RoundParserUtils;
import com.ixigo.demmanager.models.svc.demdata.SvcUserGotvScore;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class CmdExecuter {
	private static final Logger _LOGGER = LoggerFactory.getLogger(CmdExecuter.class);

	public Flux<SvcUserGotvScore> extractPlayersScore(List<String> cmd) throws IxigoException {
		var mono = Mono.fromSupplier(() -> {
			List<SvcUserGotvScore> usersStats = new ArrayList<>();

			try {
				_LOGGER.trace(String.format("Executing command: %s", cmd.toString()));

				Process p = Runtime.getRuntime().exec(cmd.toArray(new String[cmd.size()]));
				p.waitFor();

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

				String line = null;
				String err = null;
				boolean dataAvailableInTheFile = false;
				boolean headingLine = true;

				while ((line = stdInput.readLine()) != null) {
					if (headingLine) {
						// The first line is the csv heading
						headingLine = false;
						continue;
					}
					
					if(line.contains("NaN")) {
						_LOGGER.error(String.format("Line read: %s", line));
						continue;
					}
					
					String[] tmp = line.split(";");
					SvcUserGotvScore ums = new SvcUserGotvScore();

					ums.setUserName(tmp[RoundParserUtils.DEM_COL_USER_NAME]);
					ums.setSteamID(tmp[RoundParserUtils.DEM_COL_STEAM_ID]);
					ums.setKills(Long.parseLong(tmp[RoundParserUtils.DEM_COL_KILLS]));
					ums.setAssists(Long.parseLong(tmp[RoundParserUtils.DEM_COL_ASSISTS]));
					ums.setDeaths(Long.parseLong(tmp[RoundParserUtils.DEM_COL_DEATHS]));
					ums.setTotalDamageHealth(Long.parseLong(tmp[RoundParserUtils.DEM_COL_TOTAL_DAMAGE_HEALTH]));
					ums.setTotalDamageArmor(Long.parseLong(tmp[RoundParserUtils.DEM_COL_TOTAL_DAMAGE_ARMOR]));
					ums.setOneVersusOne(Long.parseLong(tmp[RoundParserUtils.DEM_COL_ONE_VERSUS_ONE]));
					ums.setOneVersusTwo(Long.parseLong(tmp[RoundParserUtils.DEM_COL_ONE_VERSUS_TWO]));
					ums.setOneVersusThree(Long.parseLong(tmp[RoundParserUtils.DEM_COL_ONE_VERSUS_THREE]));
					ums.setOneVersusFour(Long.parseLong(tmp[RoundParserUtils.DEM_COL_ONE_VERSUS_FOUR]));
					ums.setOneVersusFive(Long.parseLong(tmp[RoundParserUtils.DEM_COL_ONE_VERSUS_FIVE]));
					ums.setGrenadesThrownCount(Long.parseLong(tmp[RoundParserUtils.DEM_COL_GRENADES_THROWN_COUNT]));
					ums.setFlashesThrownCount(Long.parseLong(tmp[RoundParserUtils.DEM_COL_FLASHES_THROWN_COUNT]));
					ums.setSmokesThrownCount(Long.parseLong(tmp[RoundParserUtils.DEM_COL_SMOKES_THROWN_COUNT]));
					ums.setFireThrownCount(Long.parseLong(tmp[RoundParserUtils.DEM_COL_FIRE_THROWN_COUNT]));
					ums.setHighExplosiveDamage(Long.parseLong(tmp[RoundParserUtils.DEM_COL_HIGH_EXPLOSIVE_DAMAGE]));
					ums.setFireDamage(Long.parseLong(tmp[RoundParserUtils.DEM_COL_FIRE_DAMAGE]));
					ums.setFiveKills(Long.parseLong(tmp[RoundParserUtils.DEM_COL_FIVE_KILLS]));
					ums.setFourKills(Long.parseLong(tmp[RoundParserUtils.DEM_COL_FOUR_KILLS]));
					ums.setThreeKills(Long.parseLong(tmp[RoundParserUtils.DEM_COL_THREE_KILLS]));
					ums.setTwoKills(Long.parseLong(tmp[RoundParserUtils.DEM_COL_TWO_KILLS]));
					ums.setOneKill(Long.parseLong(tmp[RoundParserUtils.DEM_COL_ONE_KILL]));
					ums.setTradeKill(Long.parseLong(tmp[RoundParserUtils.DEM_COL_TRADE_KILL]));
					ums.setTradeDeath(Long.parseLong(tmp[RoundParserUtils.DEM_COL_TRADE_DEATH]));
					ums.setTeamKillFriendlyFire(Long.parseLong(tmp[RoundParserUtils.DEM_COL_TEAM_KILL_FRIENDLY_FIRE]));
					ums.setEntryKill(Long.parseLong(tmp[RoundParserUtils.DEM_COL_ENTRY_KILL]));
					ums.setBombPLanted(Long.parseLong(tmp[RoundParserUtils.DEM_COL_BOMB_PLANTED]));
					ums.setBombDefused(Long.parseLong(tmp[RoundParserUtils.DEM_COL_BOMB_DEFUSED]));
					ums.setMostValuablePlayer(Long.parseLong(tmp[RoundParserUtils.DEM_COL_MOST_VALUABLE_PLAYER]));
					ums.setScore(Long.parseLong(tmp[RoundParserUtils.DEM_COL_SCORE]));
					ums.setHeadShots(Long.parseLong(tmp[RoundParserUtils.DEM_COL_HEAD_SHOTS]));

					ums.setRoundWinShare(parseDouble(tmp[RoundParserUtils.DEM_COL_ROUND_WIN_SHARE]));
					ums.setKillDeathRation(parseDouble(tmp[RoundParserUtils.DEM_COL_KILL_DEATH_RATION]));
					ums.setHeadShotsPercentage(parseDouble(tmp[RoundParserUtils.DEM_COL_HEAD_SHOTS_PERC]));
					ums.setHalfLifeTelevisionRating(parseDouble(tmp[RoundParserUtils.DEM_COL_HALF_LIVE_TELEVISION_RATING]));
					ums.setKillPerRound(parseDouble(tmp[RoundParserUtils.DEM_COL_KILL_PER_ROUND]));
					ums.setAssistsPerRound(parseDouble(tmp[RoundParserUtils.DEM_COL_ASSIST_PER_ROUND]));
					ums.setDeathPerRound(parseDouble(tmp[RoundParserUtils.DEM_COL_DEATH_PER_ROUND]));
					ums.setAverageDamagePerRound(parseDouble(tmp[RoundParserUtils.DEM_COL_AVERAGE_DAMAGE_PER_ROUND]));
					ums.setMatchPlayed(parseDouble(tmp[RoundParserUtils.DEM_COL_MATCH_PLAYED]));

					usersStats.add(ums);
					dataAvailableInTheFile = true;
				}

				StringBuilder sb = new StringBuilder();
				while ((err = stdError.readLine()) != null) {
					sb.append(err);
				}

				if (sb.length() > 0) {
					throw new IxigoException(HttpStatus.BAD_GATEWAY, sb.toString());
				}
				if (!dataAvailableInTheFile) {
					return null;
				}

			} catch (IOException | InterruptedException | NumberFormatException e) {
				if (_LOGGER.isTraceEnabled()) {
					e.printStackTrace();
				}
				throw new IxigoException(HttpStatus.BAD_GATEWAY, e.getMessage());
			}
			return usersStats;
		}).subscribeOn(Schedulers.boundedElastic());

		return mono.flatMapIterable(list -> list).log();
	}

	private Double parseDouble(String s) {
		s = s.replace(',', '.');
		Double d = Double.parseDouble(s);
		return d;
	}
}
