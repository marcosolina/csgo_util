package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_round_kill_statsDao extends IxigoDao<Player_round_kill_statsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_round_kill_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_round_kill_stats";
	private Player_round_kill_statsDto dto = null;

	public Player_round_kill_statsDao() {
		this.setSqlViewName(tableName);
	// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Player_round_kill_statsDto.Fields.steamid,
			Player_round_kill_statsDto.Fields.kills,
			Player_round_kill_statsDto.Fields.team_kills,
			Player_round_kill_statsDto.Fields.headshots,
			Player_round_kill_statsDto.Fields.round,
			Player_round_kill_statsDto.Fields.trade_kills,
			Player_round_kill_statsDto.Fields.match_id,
			Player_round_kill_statsDto.Fields.headshot_percentage,
			Player_round_kill_statsDto.Fields.entry_kills,
		});
	// @formatter:on
		this.dto = new Player_round_kill_statsDto();
	}

	@Override
	public Player_round_kill_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		return this.genericMappingFunction(new Player_round_kill_statsDto(), row, rowMetaData);
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

	public Long getTeam_kills() {
		return dto.getTeam_kills();
	}

	public void setTeam_kills(Long team_kills) {
		this.dto.setTeam_kills(team_kills);
	}

	public Long getHeadshots() {
		return dto.getHeadshots();
	}

	public void setHeadshots(Long headshots) {
		this.dto.setHeadshots(headshots);
	}

	public Long getRound() {
		return dto.getRound();
	}

	public void setRound(Long round) {
		this.dto.setRound(round);
	}

	public Long getTrade_kills() {
		return dto.getTrade_kills();
	}

	public void setTrade_kills(Long trade_kills) {
		this.dto.setTrade_kills(trade_kills);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public BigDecimal getHeadshot_percentage() {
		return dto.getHeadshot_percentage();
	}

	public void setHeadshot_percentage(BigDecimal headshot_percentage) {
		this.dto.setHeadshot_percentage(headshot_percentage);
	}

	public Long getEntry_kills() {
		return dto.getEntry_kills();
	}

	public void setEntry_kills(Long entry_kills) {
		this.dto.setEntry_kills(entry_kills);
	}

	public Player_round_kill_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_round_kill_statsDto dto) {
		this.dto = dto;
	}

}
