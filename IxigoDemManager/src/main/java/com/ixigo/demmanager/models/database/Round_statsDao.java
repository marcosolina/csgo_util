package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Round_statsDao extends IxigoDao<Round_statsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Round_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "round_stats";
	private Round_statsDto dto = null;

	public Round_statsDao() {
		_LOGGER.trace("Instanciating Round_statsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Round_statsDto.Fields.reasonendround,
			Round_statsDto.Fields.match_id,
			Round_statsDto.Fields.roundnumber,
			Round_statsDto.Fields.winnerside,
		});
		// @formatter:on
		this.dto = new Round_statsDto();
	}

	@Override
	public Round_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Round_statsDto(), row, rowMetaData);
	}

	public Long getReasonendround() {
		return dto.getReasonendround();
	}

	public void setReasonendround(Long reasonendround) {
		this.dto.setReasonendround(reasonendround);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Long getRoundnumber() {
		return dto.getRoundnumber();
	}

	public void setRoundnumber(Long roundnumber) {
		this.dto.setRoundnumber(roundnumber);
	}

	public Long getWinnerside() {
		return dto.getWinnerside();
	}

	public void setWinnerside(Long winnerside) {
		this.dto.setWinnerside(winnerside);
	}

	public Round_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Round_statsDto dto) {
		this.dto = dto;
	}

}
