package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_round_event_statsDao extends IxigoDao<Player_round_event_statsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_round_event_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_round_event_stats";
	private Player_round_event_statsDto dto = null;

	public Player_round_event_statsDao() {
		_LOGGER.trace("Instanciating Player_round_event_statsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Player_round_event_statsDto.Fields.steamid,
			Player_round_event_statsDto.Fields.bombs_defused,
			Player_round_event_statsDto.Fields.round,
			Player_round_event_statsDto.Fields.bombs_planted,
			Player_round_event_statsDto.Fields.match_id,
			Player_round_event_statsDto.Fields.hostages_rescued,
		});
		// @formatter:on
		this.dto = new Player_round_event_statsDto();
	}

	@Override
	public Player_round_event_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_round_event_statsDto(), row, rowMetaData);
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

	public Player_round_event_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_round_event_statsDto dto) {
		this.dto = dto;
	}

}
