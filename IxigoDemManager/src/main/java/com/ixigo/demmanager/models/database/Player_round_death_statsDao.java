package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_round_death_statsDao extends IxigoDao<Player_round_death_statsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_round_death_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_round_death_stats";
	private Player_round_death_statsDto dto = null;

	public Player_round_death_statsDao() {
		_LOGGER.trace("Instanciating Player_round_death_statsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Player_round_death_statsDto.Fields.steamid,
			Player_round_death_statsDto.Fields.headshot_deaths,
			Player_round_death_statsDto.Fields.round,
			Player_round_death_statsDto.Fields.trade_deaths,
			Player_round_death_statsDto.Fields.match_id,
			Player_round_death_statsDto.Fields.team_deaths,
			Player_round_death_statsDto.Fields.deaths,
		});
		// @formatter:on
		this.dto = new Player_round_death_statsDto();
	}

	@Override
	public Player_round_death_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_round_death_statsDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public Long getHeadshot_deaths() {
		return dto.getHeadshot_deaths();
	}

	public void setHeadshot_deaths(Long headshot_deaths) {
		this.dto.setHeadshot_deaths(headshot_deaths);
	}

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
	}

	public Long getTrade_deaths() {
		return dto.getTrade_deaths();
	}

	public void setTrade_deaths(Long trade_deaths) {
		this.dto.setTrade_deaths(trade_deaths);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Long getTeam_deaths() {
		return dto.getTeam_deaths();
	}

	public void setTeam_deaths(Long team_deaths) {
		this.dto.setTeam_deaths(team_deaths);
	}

	public Long getDeaths() {
		return dto.getDeaths();
	}

	public void setDeaths(Long deaths) {
		this.dto.setDeaths(deaths);
	}

	public Player_round_death_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_round_death_statsDto dto) {
		this.dto = dto;
	}

}
