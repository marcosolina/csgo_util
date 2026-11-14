package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_weapon_rankingDao extends IxigoDao<Player_weapon_rankingDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_weapon_rankingDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_weapon_ranking";
	private Player_weapon_rankingDto dto = null;

	public Player_weapon_rankingDao() {
		_LOGGER.trace("Instanciating Player_weapon_rankingDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Player_weapon_rankingDto.Fields.steamid,
			Player_weapon_rankingDto.Fields.weapon,
			Player_weapon_rankingDto.Fields.weapon_rank,
		});
		// @formatter:on
		this.dto = new Player_weapon_rankingDto();
	}

	@Override
	public Player_weapon_rankingDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_weapon_rankingDto(), row, rowMetaData);
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

	public Long getWeapon_rank() {
		return dto.getWeapon_rank();
	}

	public void setWeapon_rank(Long weapon_rank) {
		this.dto.setWeapon_rank(weapon_rank);
	}

	public Player_weapon_rankingDto getDto() {
		return this.dto;
	}

	public void setDto(Player_weapon_rankingDto dto) {
		this.dto = dto;
	}

}
