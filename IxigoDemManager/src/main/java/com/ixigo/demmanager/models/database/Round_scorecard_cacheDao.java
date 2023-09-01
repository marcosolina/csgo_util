package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Round_scorecard_cacheDao extends IxigoDao<Round_scorecard_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Round_scorecard_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "round_scorecard_cache";
	private Round_scorecard_cacheDto dto = null;

	public Round_scorecard_cacheDao() {
		_LOGGER.trace("Instanciating Round_scorecard_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Round_scorecard_cacheDto.Fields.total_equipment_value,
			Round_scorecard_cacheDto.Fields.ct_score,
			Round_scorecard_cacheDto.Fields.round_type,
			Round_scorecard_cacheDto.Fields.match_id,
			Round_scorecard_cacheDto.Fields.round_end_reason,
			Round_scorecard_cacheDto.Fields.team,
			Round_scorecard_cacheDto.Fields.player_count,
			Round_scorecard_cacheDto.Fields.round,
			Round_scorecard_cacheDto.Fields.death_count,
			Round_scorecard_cacheDto.Fields.t_score,
			Round_scorecard_cacheDto.Fields.team2_score,
			Round_scorecard_cacheDto.Fields.total_money_spent,
			Round_scorecard_cacheDto.Fields.winner_team,
			Round_scorecard_cacheDto.Fields.team1_score,
		});
		// @formatter:on
		this.dto = new Round_scorecard_cacheDto();
	}

	@Override
	public Round_scorecard_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Round_scorecard_cacheDto(), row, rowMetaData);
	}

	public Long getTotal_equipment_value() {
		return dto.getTotal_equipment_value();
	}

	public void setTotal_equipment_value(Long total_equipment_value) {
		this.dto.setTotal_equipment_value(total_equipment_value);
	}

	public Long getCt_score() {
		return dto.getCt_score();
	}

	public void setCt_score(Long ct_score) {
		this.dto.setCt_score(ct_score);
	}

	public String getRound_type() {
		return dto.getRound_type();
	}

	public void setRound_type(String round_type) {
		this.dto.setRound_type(round_type);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Long getRound_end_reason() {
		return dto.getRound_end_reason();
	}

	public void setRound_end_reason(Long round_end_reason) {
		this.dto.setRound_end_reason(round_end_reason);
	}

	public Long getTeam() {
		return dto.getTeam();
	}

	public void setTeam(Long team) {
		this.dto.setTeam(team);
	}

	public Long getPlayer_count() {
		return dto.getPlayer_count();
	}

	public void setPlayer_count(Long player_count) {
		this.dto.setPlayer_count(player_count);
	}

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
	}

	public Long getDeath_count() {
		return dto.getDeath_count();
	}

	public void setDeath_count(Long death_count) {
		this.dto.setDeath_count(death_count);
	}

	public Long getT_score() {
		return dto.getT_score();
	}

	public void setT_score(Long t_score) {
		this.dto.setT_score(t_score);
	}

	public Long getTeam2_score() {
		return dto.getTeam2_score();
	}

	public void setTeam2_score(Long team2_score) {
		this.dto.setTeam2_score(team2_score);
	}

	public Long getTotal_money_spent() {
		return dto.getTotal_money_spent();
	}

	public void setTotal_money_spent(Long total_money_spent) {
		this.dto.setTotal_money_spent(total_money_spent);
	}

	public Long getWinner_team() {
		return dto.getWinner_team();
	}

	public void setWinner_team(Long winner_team) {
		this.dto.setWinner_team(winner_team);
	}

	public Long getTeam1_score() {
		return dto.getTeam1_score();
	}

	public void setTeam1_score(Long team1_score) {
		this.dto.setTeam1_score(team1_score);
	}

	public Round_scorecard_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Round_scorecard_cacheDto dto) {
		this.dto = dto;
	}

}
