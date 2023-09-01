package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_overall_match_stats_cacheDao extends IxigoDao<Player_overall_match_stats_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_overall_match_stats_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_overall_match_stats_cache";
	private Player_overall_match_stats_cacheDto dto = null;

	public Player_overall_match_stats_cacheDao() {
		_LOGGER.trace("Instanciating Player_overall_match_stats_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Player_overall_match_stats_cacheDto.Fields.steamid,
			Player_overall_match_stats_cacheDto.Fields.wins,
			Player_overall_match_stats_cacheDto.Fields.loss,
			Player_overall_match_stats_cacheDto.Fields.averagewinscore,
			Player_overall_match_stats_cacheDto.Fields.winlossratio,
		});
		// @formatter:on
		this.dto = new Player_overall_match_stats_cacheDto();
	}

	@Override
	public Player_overall_match_stats_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_overall_match_stats_cacheDto(), row, rowMetaData);
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

	public Player_overall_match_stats_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Player_overall_match_stats_cacheDto dto) {
		this.dto = dto;
	}

}
