package com.ixigo.demmanager.models.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Match_shot_stats_extendedDao extends IxigoDao<Match_shot_stats_extendedDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Match_shot_stats_extendedDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "match_shot_stats_extended";
	private Match_shot_stats_extendedDto dto = null;

	public Match_shot_stats_extendedDao() {
		_LOGGER.trace("Instanciating Match_shot_stats_extendedDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Match_shot_stats_extendedDto.Fields.steamid,
			Match_shot_stats_extendedDto.Fields.weapon,
			Match_shot_stats_extendedDto.Fields.match_filename,
			Match_shot_stats_extendedDto.Fields.shots_fired,
		});
		// @formatter:on
		this.dto = new Match_shot_stats_extendedDto();
	}

	@Override
	public Match_shot_stats_extendedDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Match_shot_stats_extendedDto(), row, rowMetaData);
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

	public Long getShots_fired() {
		return dto.getShots_fired();
	}

	public void setShots_fired(Long shots_fired) {
		this.dto.setShots_fired(shots_fired);
	}

	public Match_shot_stats_extendedDto getDto() {
		return this.dto;
	}

	public void setDto(Match_shot_stats_extendedDto dto) {
		this.dto = dto;
	}

}
