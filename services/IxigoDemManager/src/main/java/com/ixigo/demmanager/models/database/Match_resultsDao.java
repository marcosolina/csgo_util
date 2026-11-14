package com.ixigo.demmanager.models.database;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Match_resultsDao extends IxigoDao<Match_resultsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Match_resultsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "match_results";
	private Match_resultsDto dto = null;

	public Match_resultsDao() {
		_LOGGER.trace("Instanciating Match_resultsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Match_resultsDto.Fields.team1_wins_as_ct,
			Match_resultsDto.Fields.match_id,
			Match_resultsDto.Fields.team2_wins_as_ct,
			Match_resultsDto.Fields.team2_wins_as_t,
			Match_resultsDto.Fields.mapname,
			Match_resultsDto.Fields.total_t_wins,
			Match_resultsDto.Fields.total_ct_wins,
			Match_resultsDto.Fields.team1_total_wins,
			Match_resultsDto.Fields.match_date,
			Match_resultsDto.Fields.team1_wins_as_t,
			Match_resultsDto.Fields.team2_total_wins,
		});
		// @formatter:on
		this.dto = new Match_resultsDto();
	}

	@Override
	public Match_resultsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Match_resultsDto(), row, rowMetaData);
	}

	public Long getTeam1_wins_as_ct() {
		return dto.getTeam1_wins_as_ct();
	}

	public void setTeam1_wins_as_ct(Long team1_wins_as_ct) {
		this.dto.setTeam1_wins_as_ct(team1_wins_as_ct);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Long getTeam2_wins_as_ct() {
		return dto.getTeam2_wins_as_ct();
	}

	public void setTeam2_wins_as_ct(Long team2_wins_as_ct) {
		this.dto.setTeam2_wins_as_ct(team2_wins_as_ct);
	}

	public Long getTeam2_wins_as_t() {
		return dto.getTeam2_wins_as_t();
	}

	public void setTeam2_wins_as_t(Long team2_wins_as_t) {
		this.dto.setTeam2_wins_as_t(team2_wins_as_t);
	}

	public String getMapname() {
		return dto.getMapname();
	}

	public void setMapname(String mapname) {
		this.dto.setMapname(mapname);
	}

	public Long getTotal_t_wins() {
		return dto.getTotal_t_wins();
	}

	public void setTotal_t_wins(Long total_t_wins) {
		this.dto.setTotal_t_wins(total_t_wins);
	}

	public Long getTotal_ct_wins() {
		return dto.getTotal_ct_wins();
	}

	public void setTotal_ct_wins(Long total_ct_wins) {
		this.dto.setTotal_ct_wins(total_ct_wins);
	}

	public Long getTeam1_total_wins() {
		return dto.getTeam1_total_wins();
	}

	public void setTeam1_total_wins(Long team1_total_wins) {
		this.dto.setTeam1_total_wins(team1_total_wins);
	}

	public LocalDateTime getMatch_date() {
		return dto.getMatch_date();
	}

	public void setMatch_date(LocalDateTime match_date) {
		this.dto.setMatch_date(match_date);
	}

	public Long getTeam1_wins_as_t() {
		return dto.getTeam1_wins_as_t();
	}

	public void setTeam1_wins_as_t(Long team1_wins_as_t) {
		this.dto.setTeam1_wins_as_t(team1_wins_as_t);
	}

	public Long getTeam2_total_wins() {
		return dto.getTeam2_total_wins();
	}

	public void setTeam2_total_wins(Long team2_total_wins) {
		this.dto.setTeam2_total_wins(team2_total_wins);
	}

	public Match_resultsDto getDto() {
		return this.dto;
	}

	public void setDto(Match_resultsDto dto) {
		this.dto = dto;
	}

}
