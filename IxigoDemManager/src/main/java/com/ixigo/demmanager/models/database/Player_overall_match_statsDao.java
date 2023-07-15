package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_overall_match_statsDao extends IxigoDao<Player_overall_match_statsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_overall_match_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_overall_match_stats";
	private Player_overall_match_statsDto dto = null;

	public Player_overall_match_statsDao() {
		_LOGGER.trace("Instanciating Player_overall_match_statsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Player_overall_match_statsDto.Fields.steamid,
			Player_overall_match_statsDto.Fields.wins,
			Player_overall_match_statsDto.Fields.loss,
			Player_overall_match_statsDto.Fields.averagewinscore,
			Player_overall_match_statsDto.Fields.winlossratio,
		});
		// @formatter:on
		this.dto = new Player_overall_match_statsDto();
	}

	@Override
	public Player_overall_match_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_overall_match_statsDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public Long getWins() {
		return dto.getWins();
	}

	public void setWins(Long wins) {
		this.dto.setWins(wins);
	}

	public Long getLoss() {
		return dto.getLoss();
	}

	public void setLoss(Long loss) {
		this.dto.setLoss(loss);
	}

	public BigDecimal getAveragewinscore() {
		return dto.getAveragewinscore();
	}

	public void setAveragewinscore(BigDecimal averagewinscore) {
		this.dto.setAveragewinscore(averagewinscore);
	}

	public BigDecimal getWinlossratio() {
		return dto.getWinlossratio();
	}

	public void setWinlossratio(BigDecimal winlossratio) {
		this.dto.setWinlossratio(winlossratio);
	}

	public Player_overall_match_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_overall_match_statsDto dto) {
		this.dto = dto;
	}

}
