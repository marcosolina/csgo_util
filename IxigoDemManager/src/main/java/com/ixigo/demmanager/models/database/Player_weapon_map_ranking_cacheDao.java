package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_weapon_map_ranking_cacheDao extends IxigoDao<Player_weapon_map_ranking_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_weapon_map_ranking_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_weapon_map_ranking_cache";
	private Player_weapon_map_ranking_cacheDto dto = null;

	public Player_weapon_map_ranking_cacheDao() {
		_LOGGER.trace("Instanciating Player_weapon_map_ranking_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Player_weapon_map_ranking_cacheDto.Fields.steamid,
			Player_weapon_map_ranking_cacheDto.Fields.weapon,
			Player_weapon_map_ranking_cacheDto.Fields.mapname,
			Player_weapon_map_ranking_cacheDto.Fields.weapon_rank,
		});
		// @formatter:on
		this.dto = new Player_weapon_map_ranking_cacheDto();
	}

	@Override
	public Player_weapon_map_ranking_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_weapon_map_ranking_cacheDto(), row, rowMetaData);
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

	public String getMapname() {
		return dto.getMapname();
	}

	public void setMapname(String mapname) {
		this.dto.setMapname(mapname);
	}

	public Long getWeapon_rank() {
		return dto.getWeapon_rank();
	}

	public void setWeapon_rank(Long weapon_rank) {
		this.dto.setWeapon_rank(weapon_rank);
	}

	public Player_weapon_map_ranking_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Player_weapon_map_ranking_cacheDto dto) {
		this.dto = dto;
	}

}
