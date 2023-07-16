package com.ixigo.demmanager.models.database;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Player_match_resultsDto.Fields.rounds_on_team2,
			Player_match_resultsDto.Fields.rounds_on_team1,
			Player_match_resultsDto.Fields.team1_wins_as_ct,
			Player_match_resultsDto.Fields.match_id,
			Player_match_resultsDto.Fields.total_t_wins,
			Player_match_resultsDto.Fields.match_date,
			Player_match_resultsDto.Fields.steamid,
			Player_match_resultsDto.Fields.score_against,
			Player_match_resultsDto.Fields.match_result,
			Player_match_resultsDto.Fields.score_for,
			Player_match_resultsDto.Fields.last_round_team,
			Player_match_resultsDto.Fields.team2_wins_as_ct,
			Player_match_resultsDto.Fields.team2_wins_as_t,
			Player_match_resultsDto.Fields.total_ct_wins,
			Player_match_resultsDto.Fields.team1_wins_as_t,
		});
		// @formatter:on
		this.dto = new Player_match_resultsDto();
	}

	@Override
	public Player_match_resultsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_match_resultsDto(), row, rowMetaData);
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

	public Long getTeam1_wins_as_ct() {
		return dto.getTeam1_wins_as_ct();
	}

	public void setTeam1_wins_as_ct(Long team1_wins_as_ct) {
		this.dto.setTeam1_wins_as_ct(team1_wins_as_ct);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Long getTotal_t_wins() {
		return dto.getTotal_t_wins();
	}

	public void setTotal_t_wins(Long total_t_wins) {
		this.dto.setTotal_t_wins(total_t_wins);
	}

	public LocalDateTime getMatch_date() {
		return dto.getMatch_date();
	}

	public void setMatch_date(LocalDateTime match_date) {
		this.dto.setMatch_date(match_date);
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

	public Long getTeam2_wins_as_ct() {
		return dto.getTeam2_wins_as_ct();
	}

	public void setTeam2_wins_as_ct(Long team2_wins_as_ct) {
		this.dto.setTeam2_wins_as_ct(team2_wins_as_ct);
	}

	public Long getTeam2_wins_as_t() {
		return dto.getTeam2_wins_as_t();
	}

	public void setTeam2_wins_as_t(Long team2_wins_as_t) {
		this.dto.setTeam2_wins_as_t(team2_wins_as_t);
	}

	public Long getTotal_ct_wins() {
		return dto.getTotal_ct_wins();
	}

	public void setTotal_ct_wins(Long total_ct_wins) {
		this.dto.setTotal_ct_wins(total_ct_wins);
	}

	public Long getTeam1_wins_as_t() {
		return dto.getTeam1_wins_as_t();
	}

	public void setTeam1_wins_as_t(Long team1_wins_as_t) {
		this.dto.setTeam1_wins_as_t(team1_wins_as_t);
	}

	public Player_match_resultsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_match_resultsDto dto) {
		this.dto = dto;
	}

}
