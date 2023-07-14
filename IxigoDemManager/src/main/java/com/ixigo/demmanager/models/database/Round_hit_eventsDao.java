package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Round_hit_eventsDao extends IxigoDao<Round_hit_eventsDto> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(Round_hit_eventsDao.class);
	public static final String tableName = "round_hit_events";
	private static final long serialVersionUID = 1L;
	private Round_hit_eventsDto dto = null;

	public Round_hit_eventsDao() {
		// @formatter:off
		this.setSqlViewName(tableName);
		this.setSqlKeys(new String[] {});
		this.setSqlFields(new String[] {
			Round_hit_eventsDto.Fields.steamid,
			Round_hit_eventsDto.Fields.weapon,
			Round_hit_eventsDto.Fields.damagearmour,
			Round_hit_eventsDto.Fields.round,
			Round_hit_eventsDto.Fields.victimsteamid,
			Round_hit_eventsDto.Fields.hitgroup,
			Round_hit_eventsDto.Fields.match_id,
			Round_hit_eventsDto.Fields.damagehealth,
			Round_hit_eventsDto.Fields.eventtime,
			Round_hit_eventsDto.Fields.blindtime,
		});
		this.dto = new Round_hit_eventsDto();
		// @formatter:on
	}
	
	@Override
	public Round_hit_eventsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		return this.genericMappingFunction(new Round_hit_eventsDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public String getWeapon() {
		return dto.getWeapon();
	}

	public void setWeapon(String weapon) {
		this.dto.setWeapon(weapon);
	}

	public Long getDamagearmour() {
		return dto.getDamagearmour();
	}

	public void setDamagearmour(Long damagearmour) {
		this.dto.setDamagearmour(damagearmour);
	}

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
	}

	public String getVictimsteamid() {
		return dto.getVictimsteamid();
	}

	public void setVictimsteamid(String victimsteamid) {
		this.dto.setVictimsteamid(victimsteamid);
	}

	public Long getHitgroup() {
		return dto.getHitgroup();
	}

	public void setHitgroup(Long hitgroup) {
		this.dto.setHitgroup(hitgroup);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Long getDamagehealth() {
		return dto.getDamagehealth();
	}

	public void setDamagehealth(Long damagehealth) {
		this.dto.setDamagehealth(damagehealth);
	}

	public BigDecimal getEventtime() {
		return dto.getEventtime();
	}

	public void setEventtime(BigDecimal eventtime) {
		this.dto.setEventtime(eventtime);
	}

	public BigDecimal getBlindtime() {
		return dto.getBlindtime();
	}

	public void setBlindtime(BigDecimal blindtime) {
		this.dto.setBlindtime(blindtime);
	}

	public Round_hit_eventsDto getDto() {
		return this.dto;
	}

	public void setDto(Round_hit_eventsDto dto) {
		this.dto = dto;
	}

}
