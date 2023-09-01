package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Map_shot_stats_extendedDao extends IxigoDao<Map_shot_stats_extendedDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Map_shot_stats_extendedDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "map_shot_stats_extended";
	private Map_shot_stats_extendedDto dto = null;

	public Map_shot_stats_extendedDao() {
		_LOGGER.trace("Instanciating Map_shot_stats_extendedDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Map_shot_stats_extendedDto.Fields.steamid,
			Map_shot_stats_extendedDto.Fields.weapon,
			Map_shot_stats_extendedDto.Fields.shots_fired,
			Map_shot_stats_extendedDto.Fields.mapname,
		});
		// @formatter:on
		this.dto = new Map_shot_stats_extendedDto();
	}

	@Override
	public Map_shot_stats_extendedDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Map_shot_stats_extendedDto(), row, rowMetaData);
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

	public String getMapname() {
		return dto.getMapname();
	}

	public void setMapname(String mapname) {
		this.dto.setMapname(mapname);
	}

	public Map_shot_stats_extendedDto getDto() {
		return this.dto;
	}

	public void setDto(Map_shot_stats_extendedDto dto) {
		this.dto = dto;
	}

}
