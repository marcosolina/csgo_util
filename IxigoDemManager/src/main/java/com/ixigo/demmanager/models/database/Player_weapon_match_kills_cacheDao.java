package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_weapon_match_kills_cacheDao extends IxigoDao<Player_weapon_match_kills_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_weapon_match_kills_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_weapon_match_kills_cache";
	private Player_weapon_match_kills_cacheDto dto = null;

	public Player_weapon_match_kills_cacheDao() {
		_LOGGER.trace("Instanciating Player_weapon_match_kills_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Player_weapon_match_kills_cacheDto.Fields.steamid,
			Player_weapon_match_kills_cacheDto.Fields.kills,
			Player_weapon_match_kills_cacheDto.Fields.weapon,
			Player_weapon_match_kills_cacheDto.Fields.headshots,
			Player_weapon_match_kills_cacheDto.Fields.match_id,
		});
		// @formatter:on
		this.dto = new Player_weapon_match_kills_cacheDto();
	}

	@Override
	public Player_weapon_match_kills_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_weapon_match_kills_cacheDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public Long getKills() {
		return dto.getKills();
	}

	public void setKills(Long kills) {
		this.dto.setKills(kills);
	}

	public String getWeapon() {
		return dto.getWeapon();
	}

	public void setWeapon(String weapon) {
		this.dto.setWeapon(weapon);
	}

	public Long getHeadshots() {
		return dto.getHeadshots();
	}

	public void setHeadshots(Long headshots) {
		this.dto.setHeadshots(headshots);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Player_weapon_match_kills_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Player_weapon_match_kills_cacheDto dto) {
		this.dto = dto;
	}

}
