package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_kill_count_cacheDao extends IxigoDao<Player_kill_count_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_kill_count_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_kill_count_cache";
	private Player_kill_count_cacheDto dto = null;

	public Player_kill_count_cacheDao() {
		_LOGGER.trace("Instanciating Player_kill_count_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Player_kill_count_cacheDto.Fields.kill_count,
			Player_kill_count_cacheDto.Fields.victim,
			Player_kill_count_cacheDto.Fields.killer,
		});
		// @formatter:on
		this.dto = new Player_kill_count_cacheDto();
	}

	@Override
	public Player_kill_count_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_kill_count_cacheDto(), row, rowMetaData);
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

	public Player_kill_count_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Player_kill_count_cacheDto dto) {
		this.dto = dto;
	}

}
