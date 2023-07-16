package com.ixigo.demmanager.models.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_weapon_overall_killsDao extends IxigoDao<Player_weapon_overall_killsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_weapon_overall_killsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_weapon_overall_kills";
	private Player_weapon_overall_killsDto dto = null;

	public Player_weapon_overall_killsDao() {
		_LOGGER.trace("Instanciating Player_weapon_overall_killsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Player_weapon_overall_killsDto.Fields.steamid,
			Player_weapon_overall_killsDto.Fields.kills,
			Player_weapon_overall_killsDto.Fields.weapon,
			Player_weapon_overall_killsDto.Fields.headshots,
		});
		// @formatter:on
		this.dto = new Player_weapon_overall_killsDto();
	}

	@Override
	public Player_weapon_overall_killsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_weapon_overall_killsDto(), row, rowMetaData);
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

	public Player_weapon_overall_killsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_weapon_overall_killsDto dto) {
		this.dto = dto;
	}

}
