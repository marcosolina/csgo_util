package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Round_hit_events_extended_cacheDao extends IxigoDao<Round_hit_events_extended_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Round_hit_events_extended_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "round_hit_events_extended_cache";
	private Round_hit_events_extended_cacheDto dto = null;

	public Round_hit_events_extended_cacheDao() {
		_LOGGER.trace("Instanciating Round_hit_events_extended_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Round_hit_events_extended_cacheDto.Fields.steamid,
			Round_hit_events_extended_cacheDto.Fields.weapon,
			Round_hit_events_extended_cacheDto.Fields.damagearmour,
			Round_hit_events_extended_cacheDto.Fields.round,
			Round_hit_events_extended_cacheDto.Fields.victimsteamid,
			Round_hit_events_extended_cacheDto.Fields.hitgroup,
			Round_hit_events_extended_cacheDto.Fields.match_id,
			Round_hit_events_extended_cacheDto.Fields.damagehealth,
			Round_hit_events_extended_cacheDto.Fields.eventtime,
			Round_hit_events_extended_cacheDto.Fields.attacker_team,
			Round_hit_events_extended_cacheDto.Fields.victim_team,
			Round_hit_events_extended_cacheDto.Fields.blindtime,
		});
		// @formatter:on
		this.dto = new Round_hit_events_extended_cacheDto();
	}

	@Override
	public Round_hit_events_extended_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Round_hit_events_extended_cacheDto(), row, rowMetaData);
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

	public Long getAttacker_team() {
		return dto.getAttacker_team();
	}

	public void setAttacker_team(Long attacker_team) {
		this.dto.setAttacker_team(attacker_team);
	}

	public Long getVictim_team() {
		return dto.getVictim_team();
	}

	public void setVictim_team(Long victim_team) {
		this.dto.setVictim_team(victim_team);
	}

	public BigDecimal getBlindtime() {
		return dto.getBlindtime();
	}

	public void setBlindtime(BigDecimal blindtime) {
		this.dto.setBlindtime(blindtime);
	}

	public Round_hit_events_extended_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Round_hit_events_extended_cacheDto dto) {
		this.dto = dto;
	}

}