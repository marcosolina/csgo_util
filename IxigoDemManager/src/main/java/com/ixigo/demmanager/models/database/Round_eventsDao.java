package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Round_eventsDao extends IxigoDao<Round_eventsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Round_eventsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "round_events";
	private Round_eventsDto dto = null;

	public Round_eventsDao() {
		_LOGGER.trace("Instanciating Round_eventsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Round_eventsDto.Fields.steamid,
			Round_eventsDto.Fields.match_filename,
			Round_eventsDto.Fields.round,
			Round_eventsDto.Fields.eventtype,
			Round_eventsDto.Fields.eventtime,
		});
		// @formatter:on
		this.dto = new Round_eventsDto();
	}

	@Override
	public Round_eventsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Round_eventsDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public String getMatch_filename() {
		return dto.getMatch_filename();
	}

	public void setMatch_filename(String match_filename) {
		this.dto.setMatch_filename(match_filename);
	}

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
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

	public Round_eventsDto getDto() {
		return this.dto;
	}

	public void setDto(Round_eventsDto dto) {
		this.dto = dto;
	}

}
