package com.ixigo.demmanager.models.database;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Match_statsDao extends IxigoDao<Match_statsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Match_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "match_stats";
	private Match_statsDto dto = null;

	public Match_statsDao() {
		_LOGGER.trace("Instanciating Match_statsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {
			Match_statsDto.Fields.match_filename,
		});
		this.setSqlFields(new String[] {
			Match_statsDto.Fields.match_filename,
			Match_statsDto.Fields.mapname,
			Match_statsDto.Fields.match_date,
		});
		// @formatter:on
		this.dto = new Match_statsDto();
	}

	@Override
	public Match_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Match_statsDto(), row, rowMetaData);
	}

	public String getMatch_filename() {
		return dto.getMatch_filename();
	}

	public void setMatch_filename(String match_filename) {
		this.dto.setMatch_filename(match_filename);
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
