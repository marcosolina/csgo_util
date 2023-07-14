package com.ixigo.demmanager.models.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Overall_shot_stats_extendedDao extends IxigoDao<Overall_shot_stats_extendedDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Overall_shot_stats_extendedDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "overall_shot_stats_extended";
	private Overall_shot_stats_extendedDto dto = null;

	public Overall_shot_stats_extendedDao() {
		this.setSqlViewName(tableName);
	// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Overall_shot_stats_extendedDto.Fields.steamid,
			Overall_shot_stats_extendedDto.Fields.weapon,
			Overall_shot_stats_extendedDto.Fields.shots_fired,
		});
	// @formatter:on
		this.dto = new Overall_shot_stats_extendedDto();
	}

	@Override
	public Overall_shot_stats_extendedDto mappingFunction(Row row, RowMetadata rowMetaData) {
		return this.genericMappingFunction(new Overall_shot_stats_extendedDto(), row, rowMetaData);
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

	public Overall_shot_stats_extendedDto getDto() {
		return this.dto;
	}

	public void setDto(Overall_shot_stats_extendedDto dto) {
		this.dto = dto;
	}

}
