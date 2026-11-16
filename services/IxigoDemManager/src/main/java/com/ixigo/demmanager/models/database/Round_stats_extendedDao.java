package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Round_stats_extendedDao extends IxigoDao<Round_stats_extendedDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Round_stats_extendedDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "round_stats_extended";
	private Round_stats_extendedDto dto = null;

	public Round_stats_extendedDao() {
		_LOGGER.trace("Instanciating Round_stats_extendedDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Round_stats_extendedDto.Fields.reasonendround,
			Round_stats_extendedDto.Fields.match_id,
			Round_stats_extendedDto.Fields.roundnumber,
			Round_stats_extendedDto.Fields.total_damage_winners,
			Round_stats_extendedDto.Fields.winnerside,
		});
		// @formatter:on
		this.dto = new Round_stats_extendedDto();
	}

	@Override
	public Round_stats_extendedDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Round_stats_extendedDto(), row, rowMetaData);
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

	public BigDecimal getTotal_damage_winners() {
		return dto.getTotal_damage_winners();
	}

	public void setTotal_damage_winners(BigDecimal total_damage_winners) {
		this.dto.setTotal_damage_winners(total_damage_winners);
	}

	public Long getWinnerside() {
		return dto.getWinnerside();
	}

	public void setWinnerside(Long winnerside) {
		this.dto.setWinnerside(winnerside);
	}

	public Round_stats_extendedDto getDto() {
		return this.dto;
	}

	public void setDto(Round_stats_extendedDto dto) {
		this.dto = dto;
	}

}
