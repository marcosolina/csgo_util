package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Round_shot_eventsDao extends IxigoDao<Round_shot_eventsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Round_shot_eventsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "round_shot_events";
	private Round_shot_eventsDto dto = null;

	public Round_shot_eventsDao() {
		_LOGGER.trace("Instanciating Round_shot_eventsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Round_shot_eventsDto.Fields.steamid,
			Round_shot_eventsDto.Fields.weapon,
			Round_shot_eventsDto.Fields.round,
			Round_shot_eventsDto.Fields.match_id,
			Round_shot_eventsDto.Fields.eventtype,
			Round_shot_eventsDto.Fields.eventtime,
		});
		// @formatter:on
		this.dto = new Round_shot_eventsDto();
	}

	@Override
	public Round_shot_eventsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Round_shot_eventsDto(), row, rowMetaData);
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

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public String getEventtype() {
		return dto.getEventtype();
	}

	public void setEventtype(String eventtype) {
		this.dto.setEventtype(eventtype);
	}

	public BigDecimal getEventtime() {
		return dto.getEventtime();
	}

	public void setEventtime(BigDecimal eventtime) {
		this.dto.setEventtime(eventtime);
	}

	public Round_shot_eventsDto getDto() {
		return this.dto;
	}

	public void setDto(Round_shot_eventsDto dto) {
		this.dto = dto;
	}

}
