package com.ixigo.demmanager.models.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_round_utility_statsDao extends IxigoDao<Player_round_utility_statsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_round_utility_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_round_utility_stats";
	private Player_round_utility_statsDto dto = null;

	public Player_round_utility_statsDao() {
		_LOGGER.trace("Instanciating Player_round_utility_statsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Player_round_utility_statsDto.Fields.steamid,
			Player_round_utility_statsDto.Fields.grenades_thrown,
			Player_round_utility_statsDto.Fields.smokes_thrown,
			Player_round_utility_statsDto.Fields.match_filename,
			Player_round_utility_statsDto.Fields.round,
			Player_round_utility_statsDto.Fields.flashes_thrown,
			Player_round_utility_statsDto.Fields.inferno_thrown,
		});
		// @formatter:on
		this.dto = new Player_round_utility_statsDto();
	}

	@Override
	public Player_round_utility_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_round_utility_statsDto(), row, rowMetaData);
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

	public Long getFlashes_thrown() {
		return dto.getFlashes_thrown();
	}

	public void setFlashes_thrown(Long flashes_thrown) {
		this.dto.setFlashes_thrown(flashes_thrown);
	}

	public Long getInferno_thrown() {
		return dto.getInferno_thrown();
	}

	public void setInferno_thrown(Long inferno_thrown) {
		this.dto.setInferno_thrown(inferno_thrown);
	}

	public Player_round_utility_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_round_utility_statsDto dto) {
		this.dto = dto;
	}

}
