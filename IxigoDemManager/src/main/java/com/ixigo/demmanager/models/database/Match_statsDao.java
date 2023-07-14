package com.ixigo.demmanager.models.database;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Match_statsDao extends IxigoDao<Match_statsDto> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(Match_statsDao.class);
	public static final String tableName = "match_stats";

	private static final long serialVersionUID = 1L;
	private Match_statsDto dto = null;

	// @formatter:off
	public Match_statsDao() {
		this.setSqlViewName(tableName);
		this.setSqlKeys(new String[] { 
			Match_statsDto.Fields.match_id
		});
		this.setSqlFields(new String[] { 
			Match_statsDto.Fields.match_filename,
			Match_statsDto.Fields.mapname,
			Match_statsDto.Fields.match_id,
			Match_statsDto.Fields.match_date

		});
		this.dto = new Match_statsDto();
	}
	// @formatter:on
	
	@Override
	public Match_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		return this.genericMappingFunction(new Match_statsDto(), row, rowMetaData);
	}

	public String getMatch_filename() {
		return dto.getMatch_filename();
	}

	public void setMatch_filename(String match_filename) {
		this.dto.setMatch_filename(match_filename);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public String getMapname() {
		return dto.getMapname();
	}

	public void setMapname(String mapname) {
		this.dto.setMapname(mapname);
	}

	public LocalDateTime getMatch_date() {
		return dto.getMatch_date();
	}

	public void setMatch_date(LocalDateTime match_date) {
		this.dto.setMatch_date(match_date);
	}

	public Match_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Match_statsDto dto) {
		this.dto = dto;
	}

}
