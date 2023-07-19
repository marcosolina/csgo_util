package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_round_utility_stats_cacheDao extends IxigoDao<Player_round_utility_stats_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_round_utility_stats_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_round_utility_stats_cache";
	private Player_round_utility_stats_cacheDto dto = null;

	public Player_round_utility_stats_cacheDao() {
		_LOGGER.trace("Instanciating Player_round_utility_stats_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Player_round_utility_stats_cacheDto.Fields.steamid,
			Player_round_utility_stats_cacheDto.Fields.grenades_thrown,
			Player_round_utility_stats_cacheDto.Fields.smokes_thrown,
			Player_round_utility_stats_cacheDto.Fields.round,
			Player_round_utility_stats_cacheDto.Fields.flashes_thrown,
			Player_round_utility_stats_cacheDto.Fields.match_id,
			Player_round_utility_stats_cacheDto.Fields.inferno_thrown,
		});
		// @formatter:on
		this.dto = new Player_round_utility_stats_cacheDto();
	}

	@Override
	public Player_round_utility_stats_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_round_utility_stats_cacheDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public Long getGrenades_thrown() {
		return dto.getGrenades_thrown();
	}

	public void setGrenades_thrown(Long grenades_thrown) {
		this.dto.setGrenades_thrown(grenades_thrown);
	}

	public Long getSmokes_thrown() {
		return dto.getSmokes_thrown();
	}

	public void setSmokes_thrown(Long smokes_thrown) {
		this.dto.setSmokes_thrown(smokes_thrown);
	}

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
	}

	public Long getFlashes_thrown() {
		return dto.getFlashes_thrown();
	}

	public void setFlashes_thrown(Long flashes_thrown) {
		this.dto.setFlashes_thrown(flashes_thrown);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Long getInferno_thrown() {
		return dto.getInferno_thrown();
	}

	public void setInferno_thrown(Long inferno_thrown) {
		this.dto.setInferno_thrown(inferno_thrown);
	}

	public Player_round_utility_stats_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Player_round_utility_stats_cacheDto dto) {
		this.dto = dto;
	}

}
