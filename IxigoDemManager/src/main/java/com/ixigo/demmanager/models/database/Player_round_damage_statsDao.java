package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_round_damage_statsDao extends IxigoDao<Player_round_damage_statsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_round_damage_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_round_damage_stats";
	private Player_round_damage_statsDto dto = null;

	public Player_round_damage_statsDao() {
		this.setSqlViewName(tableName);
	// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Player_round_damage_statsDto.Fields.steamid,
			Player_round_damage_statsDto.Fields.he_damage,
			Player_round_damage_statsDto.Fields.round,
			Player_round_damage_statsDto.Fields.fire_damage,
			Player_round_damage_statsDto.Fields.match_id,
			Player_round_damage_statsDto.Fields.total_damage_armour,
			Player_round_damage_statsDto.Fields.opponents_flashed,
			Player_round_damage_statsDto.Fields.opponent_blindtime,
			Player_round_damage_statsDto.Fields.teammate_damage_health,
			Player_round_damage_statsDto.Fields.total_damage_health,
			Player_round_damage_statsDto.Fields.teammate_blindtime,
		});
	// @formatter:on
		this.dto = new Player_round_damage_statsDto();
	}

	@Override
	public Player_round_damage_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		return this.genericMappingFunction(new Player_round_damage_statsDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public Long getHe_damage() {
		return dto.getHe_damage();
	}

	public void setHe_damage(Long he_damage) {
		this.dto.setHe_damage(he_damage);
	}

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
	}

	public Long getFire_damage() {
		return dto.getFire_damage();
	}

	public void setFire_damage(Long fire_damage) {
		this.dto.setFire_damage(fire_damage);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Long getTotal_damage_armour() {
		return dto.getTotal_damage_armour();
	}

	public void setTotal_damage_armour(Long total_damage_armour) {
		this.dto.setTotal_damage_armour(total_damage_armour);
	}

	public Long getOpponents_flashed() {
		return dto.getOpponents_flashed();
	}

	public void setOpponents_flashed(Long opponents_flashed) {
		this.dto.setOpponents_flashed(opponents_flashed);
	}

	public BigDecimal getOpponent_blindtime() {
		return dto.getOpponent_blindtime();
	}

	public void setOpponent_blindtime(BigDecimal opponent_blindtime) {
		this.dto.setOpponent_blindtime(opponent_blindtime);
	}

	public Long getTeammate_damage_health() {
		return dto.getTeammate_damage_health();
	}

	public void setTeammate_damage_health(Long teammate_damage_health) {
		this.dto.setTeammate_damage_health(teammate_damage_health);
	}

	public Long getTotal_damage_health() {
		return dto.getTotal_damage_health();
	}

	public void setTotal_damage_health(Long total_damage_health) {
		this.dto.setTotal_damage_health(total_damage_health);
	}

	public BigDecimal getTeammate_blindtime() {
		return dto.getTeammate_blindtime();
	}

	public void setTeammate_blindtime(BigDecimal teammate_blindtime) {
		this.dto.setTeammate_blindtime(teammate_blindtime);
	}

	public Player_round_damage_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_round_damage_statsDto dto) {
		this.dto = dto;
	}

}
