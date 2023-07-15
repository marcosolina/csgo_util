package com.ixigo.demmanager.models.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_match_resultsDao extends IxigoDao<Player_match_resultsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_match_resultsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_match_results";
	private Player_match_resultsDto dto = null;

	public Player_match_resultsDao() {
		_LOGGER.trace("Instanciating Player_match_resultsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Player_match_resultsDto.Fields.steamid,
			Player_match_resultsDto.Fields.score_against,
			Player_match_resultsDto.Fields.match_filename,
			Player_match_resultsDto.Fields.rounds_on_team2,
			Player_match_resultsDto.Fields.rounds_on_team1,
			Player_match_resultsDto.Fields.match_result,
			Player_match_resultsDto.Fields.score_for,
			Player_match_resultsDto.Fields.last_round_team,
		});
		// @formatter:on
		this.dto = new Player_match_resultsDto();
	}

	@Override
	public Player_match_resultsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_match_resultsDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public Long getScore_against() {
		return dto.getScore_against();
	}

	public void setScore_against(Long score_against) {
		this.dto.setScore_against(score_against);
	}

	public String getMatch_filename() {
		return dto.getMatch_filename();
	}

	public void setMatch_filename(String match_filename) {
		this.dto.setMatch_filename(match_filename);
	}

	public Long getRounds_on_team2() {
		return dto.getRounds_on_team2();
	}

	public void setRounds_on_team2(Long rounds_on_team2) {
		this.dto.setRounds_on_team2(rounds_on_team2);
	}

	public Long getRounds_on_team1() {
		return dto.getRounds_on_team1();
	}

	public void setRounds_on_team1(Long rounds_on_team1) {
		this.dto.setRounds_on_team1(rounds_on_team1);
	}

	public String getMatch_result() {
		return dto.getMatch_result();
	}

	public void setMatch_result(String match_result) {
		this.dto.setMatch_result(match_result);
	}

	public Long getScore_for() {
		return dto.getScore_for();
	}

	public void setScore_for(Long score_for) {
		this.dto.setScore_for(score_for);
	}

	public String getLast_round_team() {
		return dto.getLast_round_team();
	}

	public void setLast_round_team(String last_round_team) {
		this.dto.setLast_round_team(last_round_team);
	}

	public Player_match_resultsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_match_resultsDto dto) {
		this.dto = dto;
	}

}
