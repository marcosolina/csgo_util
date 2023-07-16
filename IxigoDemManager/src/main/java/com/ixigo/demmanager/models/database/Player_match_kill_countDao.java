package com.ixigo.demmanager.models.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_match_kill_countDao extends IxigoDao<Player_match_kill_countDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_match_kill_countDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_match_kill_count";
	private Player_match_kill_countDto dto = null;

	public Player_match_kill_countDao() {
		_LOGGER.trace("Instanciating Player_match_kill_countDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Player_match_kill_countDto.Fields.match_filename,
			Player_match_kill_countDto.Fields.kill_count,
			Player_match_kill_countDto.Fields.victim,
			Player_match_kill_countDto.Fields.killer,
		});
		// @formatter:on
		this.dto = new Player_match_kill_countDto();
	}

	@Override
	public Player_match_kill_countDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_match_kill_countDto(), row, rowMetaData);
	}

	public String getMatch_filename() {
		return dto.getMatch_filename();
	}

	public void setMatch_filename(String match_filename) {
		this.dto.setMatch_filename(match_filename);
	}

	public Long getKill_count() {
		return dto.getKill_count();
	}

	public void setKill_count(Long kill_count) {
		this.dto.setKill_count(kill_count);
	}

	public String getVictim() {
		return dto.getVictim();
	}

	public void setVictim(String victim) {
		this.dto.setVictim(victim);
	}

	public String getKiller() {
		return dto.getKiller();
	}

	public void setKiller(String killer) {
		this.dto.setKiller(killer);
	}

	public Player_match_kill_countDto getDto() {
		return this.dto;
	}

	public void setDto(Player_match_kill_countDto dto) {
		this.dto = dto;
	}

}
