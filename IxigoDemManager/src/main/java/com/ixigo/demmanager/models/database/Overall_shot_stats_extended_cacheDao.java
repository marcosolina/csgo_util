package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Overall_shot_stats_extended_cacheDao extends IxigoDao<Overall_shot_stats_extended_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Overall_shot_stats_extended_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "overall_shot_stats_extended_cache";
	private Overall_shot_stats_extended_cacheDto dto = null;

	public Overall_shot_stats_extended_cacheDao() {
		_LOGGER.trace("Instanciating Overall_shot_stats_extended_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Overall_shot_stats_extended_cacheDto.Fields.steamid,
			Overall_shot_stats_extended_cacheDto.Fields.weapon,
			Overall_shot_stats_extended_cacheDto.Fields.shots_fired,
		});
		// @formatter:on
		this.dto = new Overall_shot_stats_extended_cacheDto();
	}

	@Override
	public Overall_shot_stats_extended_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Overall_shot_stats_extended_cacheDto(), row, rowMetaData);
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

	public Overall_shot_stats_extended_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Overall_shot_stats_extended_cacheDto dto) {
		this.dto = dto;
	}

}
