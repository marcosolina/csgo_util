package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Round_kill_events_extended_cacheDao extends IxigoDao<Round_kill_events_extended_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Round_kill_events_extended_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "round_kill_events_extended_cache";
	private Round_kill_events_extended_cacheDto dto = null;

	public Round_kill_events_extended_cacheDao() {
		_LOGGER.trace("Instanciating Round_kill_events_extended_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Round_kill_events_extended_cacheDto.Fields.victimsteamid,
			Round_kill_events_extended_cacheDto.Fields.isfirstkill,
			Round_kill_events_extended_cacheDto.Fields.match_id,
			Round_kill_events_extended_cacheDto.Fields.eventtime,
			Round_kill_events_extended_cacheDto.Fields.istradekill,
			Round_kill_events_extended_cacheDto.Fields.assister,
			Round_kill_events_extended_cacheDto.Fields.steamid,
			Round_kill_events_extended_cacheDto.Fields.weapon,
			Round_kill_events_extended_cacheDto.Fields.killer_team,
			Round_kill_events_extended_cacheDto.Fields.flashassister,
			Round_kill_events_extended_cacheDto.Fields.round,
			Round_kill_events_extended_cacheDto.Fields.killerflashed,
			Round_kill_events_extended_cacheDto.Fields.headshot,
			Round_kill_events_extended_cacheDto.Fields.victim_team,
			Round_kill_events_extended_cacheDto.Fields.istradedeath,
		});
		// @formatter:on
		this.dto = new Round_kill_events_extended_cacheDto();
	}

	@Override
	public Round_kill_events_extended_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Round_kill_events_extended_cacheDto(), row, rowMetaData);
	}

	public String getVictimsteamid() {
		return dto.getVictimsteamid();
	}

	public void setVictimsteamid(String victimsteamid) {
		this.dto.setVictimsteamid(victimsteamid);
	}

	public Boolean getIsfirstkill() {
		return dto.getIsfirstkill();
	}

	public void setIsfirstkill(Boolean isfirstkill) {
		this.dto.setIsfirstkill(isfirstkill);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public BigDecimal getEventtime() {
		return dto.getEventtime();
	}

	public void setEventtime(BigDecimal eventtime) {
		this.dto.setEventtime(eventtime);
	}

	public Boolean getIstradekill() {
		return dto.getIstradekill();
	}

	public void setIstradekill(Boolean istradekill) {
		this.dto.setIstradekill(istradekill);
	}

	public String getAssister() {
		return dto.getAssister();
	}

	public void setAssister(String assister) {
		this.dto.setAssister(assister);
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

	public Long getKiller_team() {
		return dto.getKiller_team();
	}

	public void setKiller_team(Long killer_team) {
		this.dto.setKiller_team(killer_team);
	}

	public String getFlashassister() {
		return dto.getFlashassister();
	}

	public void setFlashassister(String flashassister) {
		this.dto.setFlashassister(flashassister);
	}

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
	}

	public String getKillerflashed() {
		return dto.getKillerflashed();
	}

	public void setKillerflashed(String killerflashed) {
		this.dto.setKillerflashed(killerflashed);
	}

	public Boolean getHeadshot() {
		return dto.getHeadshot();
	}

	public void setHeadshot(Boolean headshot) {
		this.dto.setHeadshot(headshot);
	}

	public Long getVictim_team() {
		return dto.getVictim_team();
	}

	public void setVictim_team(Long victim_team) {
		this.dto.setVictim_team(victim_team);
	}

	public Boolean getIstradedeath() {
		return dto.getIstradedeath();
	}

	public void setIstradedeath(Boolean istradedeath) {
		this.dto.setIstradedeath(istradedeath);
	}

	public Round_kill_events_extended_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Round_kill_events_extended_cacheDto dto) {
		this.dto = dto;
	}

}
