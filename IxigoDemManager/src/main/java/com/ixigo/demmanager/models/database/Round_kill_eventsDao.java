package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Round_kill_eventsDao extends IxigoDao<Round_kill_eventsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Round_kill_eventsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "round_kill_events";
	private Round_kill_eventsDto dto = null;

	public Round_kill_eventsDao() {
		_LOGGER.trace("Instanciating Round_kill_eventsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Round_kill_eventsDto.Fields.victimsteamid,
			Round_kill_eventsDto.Fields.isfirstkill,
			Round_kill_eventsDto.Fields.eventtime,
			Round_kill_eventsDto.Fields.istradekill,
			Round_kill_eventsDto.Fields.assister,
			Round_kill_eventsDto.Fields.steamid,
			Round_kill_eventsDto.Fields.weapon,
			Round_kill_eventsDto.Fields.match_filename,
			Round_kill_eventsDto.Fields.flashassister,
			Round_kill_eventsDto.Fields.round,
			Round_kill_eventsDto.Fields.killerflashed,
			Round_kill_eventsDto.Fields.headshot,
			Round_kill_eventsDto.Fields.istradedeath,
		});
		// @formatter:on
		this.dto = new Round_kill_eventsDto();
	}

	@Override
	public Round_kill_eventsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Round_kill_eventsDto(), row, rowMetaData);
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

	public String getMatch_filename() {
		return dto.getMatch_filename();
	}

	public void setMatch_filename(String match_filename) {
		this.dto.setMatch_filename(match_filename);
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

	public Boolean getIstradedeath() {
		return dto.getIstradedeath();
	}

	public void setIstradedeath(Boolean istradedeath) {
		this.dto.setIstradedeath(istradedeath);
	}

	public Round_kill_eventsDto getDto() {
		return this.dto;
	}

	public void setDto(Round_kill_eventsDto dto) {
		this.dto = dto;
	}

}