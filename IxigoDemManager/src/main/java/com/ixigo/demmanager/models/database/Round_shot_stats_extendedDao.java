package com.ixigo.demmanager.models.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Round_shot_stats_extendedDao extends IxigoDao<Round_shot_stats_extendedDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Round_shot_stats_extendedDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "round_shot_stats_extended";
	private Round_shot_stats_extendedDto dto = null;

	public Round_shot_stats_extendedDao() {
		this.setSqlViewName(tableName);
	// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Round_shot_stats_extendedDto.Fields.steamid,
			Round_shot_stats_extendedDto.Fields.weapon,
			Round_shot_stats_extendedDto.Fields.round,
			Round_shot_stats_extendedDto.Fields.shots_fired,
			Round_shot_stats_extendedDto.Fields.match_id,
		});
	// @formatter:on
		this.dto = new Round_shot_stats_extendedDto();
	}

	@Override
	public Round_shot_stats_extendedDto mappingFunction(Row row, RowMetadata rowMetaData) {
		return this.genericMappingFunction(new Round_shot_stats_extendedDto(), row, rowMetaData);
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

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
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

	public Round_shot_stats_extendedDto getDto() {
		return this.dto;
	}

	public void setDto(Round_shot_stats_extendedDto dto) {
		this.dto = dto;
	}

}
