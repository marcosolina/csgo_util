package com.ixigo.demmanager.models.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_statsDao extends IxigoDao<Player_statsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_stats";
	private Player_statsDto dto = null;

	public Player_statsDao() {
		_LOGGER.trace("Instanciating Player_statsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Player_statsDto.Fields.steamid,
			Player_statsDto.Fields.score,
			Player_statsDto.Fields.match_filename,
			Player_statsDto.Fields.username,
		});
		// @formatter:on
		this.dto = new Player_statsDto();
	}

	@Override
	public Player_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_statsDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public Long getScore() {
		return dto.getScore();
	}

	public void setScore(Long score) {
		this.dto.setScore(score);
	}

	public String getMatch_filename() {
		return dto.getMatch_filename();
	}

	public void setMatch_filename(String match_filename) {
		this.dto.setMatch_filename(match_filename);
	}

	public String getUsername() {
		return dto.getUsername();
	}

	public void setUsername(String username) {
		this.dto.setUsername(username);
	}

	public Player_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_statsDto dto) {
		this.dto = dto;
	}

}
