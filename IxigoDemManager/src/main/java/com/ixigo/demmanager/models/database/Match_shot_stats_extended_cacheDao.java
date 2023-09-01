package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Match_shot_stats_extended_cacheDao extends IxigoDao<Match_shot_stats_extended_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Match_shot_stats_extended_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "match_shot_stats_extended_cache";
	private Match_shot_stats_extended_cacheDto dto = null;

	public Match_shot_stats_extended_cacheDao() {
		_LOGGER.trace("Instanciating Match_shot_stats_extended_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Match_shot_stats_extended_cacheDto.Fields.steamid,
			Match_shot_stats_extended_cacheDto.Fields.weapon,
			Match_shot_stats_extended_cacheDto.Fields.shots_fired,
			Match_shot_stats_extended_cacheDto.Fields.match_id,
		});
		// @formatter:on
		this.dto = new Match_shot_stats_extended_cacheDto();
	}

	@Override
	public Match_shot_stats_extended_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Match_shot_stats_extended_cacheDto(), row, rowMetaData);
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

	public Long getShots_fired() {
		return dto.getShots_fired();
	}

	public void setShots_fired(Long shots_fired) {
		this.dto.setShots_fired(shots_fired);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Match_shot_stats_extended_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Match_shot_stats_extended_cacheDto dto) {
		this.dto = dto;
	}

}
