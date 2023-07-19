package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_round_event_stats_cacheDao extends IxigoDao<Player_round_event_stats_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_round_event_stats_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_round_event_stats_cache";
	private Player_round_event_stats_cacheDto dto = null;

	public Player_round_event_stats_cacheDao() {
		_LOGGER.trace("Instanciating Player_round_event_stats_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Player_round_event_stats_cacheDto.Fields.steamid,
			Player_round_event_stats_cacheDto.Fields.bombs_defused,
			Player_round_event_stats_cacheDto.Fields.round,
			Player_round_event_stats_cacheDto.Fields.bombs_planted,
			Player_round_event_stats_cacheDto.Fields.match_id,
			Player_round_event_stats_cacheDto.Fields.hostages_rescued,
		});
		// @formatter:on
		this.dto = new Player_round_event_stats_cacheDto();
	}

	@Override
	public Player_round_event_stats_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_round_event_stats_cacheDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public Long getBombs_defused() {
		return dto.getBombs_defused();
	}

	public void setBombs_defused(Long bombs_defused) {
		this.dto.setBombs_defused(bombs_defused);
	}

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
	}

	public Long getBombs_planted() {
		return dto.getBombs_planted();
	}

	public void setBombs_planted(Long bombs_planted) {
		this.dto.setBombs_planted(bombs_planted);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Long getHostages_rescued() {
		return dto.getHostages_rescued();
	}

	public void setHostages_rescued(Long hostages_rescued) {
		this.dto.setHostages_rescued(hostages_rescued);
	}

	public Player_round_event_stats_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Player_round_event_stats_cacheDto dto) {
		this.dto = dto;
	}

}
