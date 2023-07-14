package com.ixigo.demmanager.models.database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_round_statsDao extends IxigoDao<Player_round_statsDto> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_round_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_round_stats";
	private Player_round_statsDto dto = null;

	// @formatter:off
	public Player_round_statsDao() {
		this.setSqlViewName(tableName);
		this.setSqlKeys(new String[] {});
		this.setSqlFields(new String[] { 
			Player_round_statsDto.Fields.steamid,
			Player_round_statsDto.Fields.round,
			Player_round_statsDto.Fields.clutchchance,
			Player_round_statsDto.Fields.moneyspent,
			Player_round_statsDto.Fields.clutchsuccess,
			Player_round_statsDto.Fields.mvp,
			Player_round_statsDto.Fields.match_id,
			Player_round_statsDto.Fields.equipmentvalue,
			Player_round_statsDto.Fields.team,
			Player_round_statsDto.Fields.survived,
			Player_round_statsDto.Fields.username
		});
		this.dto = new Player_round_statsDto();
	}
	// @formatter:on
	
	@Override
	public Player_round_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		return this.genericMappingFunction(new Player_round_statsDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
	}

	public BigDecimal getClutchchance() {
		return dto.getClutchchance();
	}

	public void setClutchchance(BigDecimal clutchchance) {
		this.dto.setClutchchance(clutchchance);
	}

	public Long getMoneyspent() {
		return dto.getMoneyspent();
	}

	public void setMoneyspent(Long moneyspent) {
		this.dto.setMoneyspent(moneyspent);
	}

	public Boolean getClutchsuccess() {
		return dto.getClutchsuccess();
	}

	public void setClutchsuccess(Boolean clutchsuccess) {
		this.dto.setClutchsuccess(clutchsuccess);
	}

	public Boolean getMvp() {
		return dto.getMvp();
	}

	public void setMvp(Boolean mvp) {
		this.dto.setMvp(mvp);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Long getEquipmentvalue() {
		return dto.getEquipmentvalue();
	}

	public void setEquipmentvalue(Long equipmentvalue) {
		this.dto.setEquipmentvalue(equipmentvalue);
	}

	public Long getTeam() {
		return dto.getTeam();
	}

	public void setTeam(Long team) {
		this.dto.setTeam(team);
	}

	public Boolean getSurvived() {
		return dto.getSurvived();
	}

	public void setSurvived(Boolean survived) {
		this.dto.setSurvived(survived);
	}

	public String getUsername() {
		return dto.getUsername();
	}

	public void setUsername(String username) {
		this.dto.setUsername(username);
	}

	public Player_round_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_round_statsDto dto) {
		this.dto = dto;
	}
}
