package com.ixigo.demmanager.models.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_round_assist_statsDao extends IxigoDao<Player_round_assist_statsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_round_assist_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_round_assist_stats";
	private Player_round_assist_statsDto dto = null;

	public Player_round_assist_statsDao() {
		_LOGGER.trace("Instanciating Player_round_assist_statsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Player_round_assist_statsDto.Fields.steamid,
			Player_round_assist_statsDto.Fields.match_filename,
			Player_round_assist_statsDto.Fields.round,
			Player_round_assist_statsDto.Fields.assists,
		});
		// @formatter:on
		this.dto = new Player_round_assist_statsDto();
	}

	@Override
	public Player_round_assist_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_round_assist_statsDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public String getMatch_filename() {
		return dto.getMatch_filename();
	}

	public void setMatch_filename(String match_filename) {
		this.dto.setMatch_filename(match_filename);
	}

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
	}

	public Long getAssists() {
		return dto.getAssists();
	}

	public void setAssists(Long assists) {
		this.dto.setAssists(assists);
	}

	public Player_round_assist_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_round_assist_statsDto dto) {
		this.dto = dto;
	}

}
