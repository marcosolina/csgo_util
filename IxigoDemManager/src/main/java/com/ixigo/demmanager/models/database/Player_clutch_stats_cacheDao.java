package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_clutch_stats_cacheDao extends IxigoDao<Player_clutch_stats_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_clutch_stats_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_clutch_stats_cache";
	private Player_clutch_stats_cacheDto dto = null;

	public Player_clutch_stats_cacheDao() {
		_LOGGER.trace("Instanciating Player_clutch_stats_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Player_clutch_stats_cacheDto.Fields._1v5l,
			Player_clutch_stats_cacheDto.Fields._1v2l,
			Player_clutch_stats_cacheDto.Fields._1v1l,
			Player_clutch_stats_cacheDto.Fields._1v5p,
			Player_clutch_stats_cacheDto.Fields._1v4l,
			Player_clutch_stats_cacheDto.Fields._1v3l,
			Player_clutch_stats_cacheDto.Fields.steamid,
			Player_clutch_stats_cacheDto.Fields._1v2p,
			Player_clutch_stats_cacheDto.Fields._1vnl,
			Player_clutch_stats_cacheDto.Fields._1v1p,
			Player_clutch_stats_cacheDto.Fields._1v4p,
			Player_clutch_stats_cacheDto.Fields._1v3p,
			Player_clutch_stats_cacheDto.Fields._1v5w,
			Player_clutch_stats_cacheDto.Fields._1vnp,
			Player_clutch_stats_cacheDto.Fields._1v4w,
			Player_clutch_stats_cacheDto.Fields._1v1w,
			Player_clutch_stats_cacheDto.Fields._1v3w,
			Player_clutch_stats_cacheDto.Fields._1v2w,
			Player_clutch_stats_cacheDto.Fields._1vnw,
		});
		// @formatter:on
		this.dto = new Player_clutch_stats_cacheDto();
	}

	@Override
	public Player_clutch_stats_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_clutch_stats_cacheDto(), row, rowMetaData);
	}

	public Long get_1v5l() {
		return dto.get_1v5l();
	}

	public void set_1v5l(Long _1v5l) {
		this.dto.set_1v5l(_1v5l);
	}

	public Long get_1v2l() {
		return dto.get_1v2l();
	}

	public void set_1v2l(Long _1v2l) {
		this.dto.set_1v2l(_1v2l);
	}

	public Long get_1v1l() {
		return dto.get_1v1l();
	}

	public void set_1v1l(Long _1v1l) {
		this.dto.set_1v1l(_1v1l);
	}

	public BigDecimal get_1v5p() {
		return dto.get_1v5p();
	}

	public void set_1v5p(BigDecimal _1v5p) {
		this.dto.set_1v5p(_1v5p);
	}

	public Long get_1v4l() {
		return dto.get_1v4l();
	}

	public void set_1v4l(Long _1v4l) {
		this.dto.set_1v4l(_1v4l);
	}

	public Long get_1v3l() {
		return dto.get_1v3l();
	}

	public void set_1v3l(Long _1v3l) {
		this.dto.set_1v3l(_1v3l);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public BigDecimal get_1v2p() {
		return dto.get_1v2p();
	}

	public void set_1v2p(BigDecimal _1v2p) {
		this.dto.set_1v2p(_1v2p);
	}

	public Long get_1vnl() {
		return dto.get_1vnl();
	}

	public void set_1vnl(Long _1vnl) {
		this.dto.set_1vnl(_1vnl);
	}

	public BigDecimal get_1v1p() {
		return dto.get_1v1p();
	}

	public void set_1v1p(BigDecimal _1v1p) {
		this.dto.set_1v1p(_1v1p);
	}

	public BigDecimal get_1v4p() {
		return dto.get_1v4p();
	}

	public void set_1v4p(BigDecimal _1v4p) {
		this.dto.set_1v4p(_1v4p);
	}

	public BigDecimal get_1v3p() {
		return dto.get_1v3p();
	}

	public void set_1v3p(BigDecimal _1v3p) {
		this.dto.set_1v3p(_1v3p);
	}

	public Long get_1v5w() {
		return dto.get_1v5w();
	}

	public void set_1v5w(Long _1v5w) {
		this.dto.set_1v5w(_1v5w);
	}

	public BigDecimal get_1vnp() {
		return dto.get_1vnp();
	}

	public void set_1vnp(BigDecimal _1vnp) {
		this.dto.set_1vnp(_1vnp);
	}

	public Long get_1v4w() {
		return dto.get_1v4w();
	}

	public void set_1v4w(Long _1v4w) {
		this.dto.set_1v4w(_1v4w);
	}

	public Long get_1v1w() {
		return dto.get_1v1w();
	}

	public void set_1v1w(Long _1v1w) {
		this.dto.set_1v1w(_1v1w);
	}

	public Long get_1v3w() {
		return dto.get_1v3w();
	}

	public void set_1v3w(Long _1v3w) {
		this.dto.set_1v3w(_1v3w);
	}

	public Long get_1v2w() {
		return dto.get_1v2w();
	}

	public void set_1v2w(Long _1v2w) {
		this.dto.set_1v2w(_1v2w);
	}

	public Long get_1vnw() {
		return dto.get_1vnw();
	}

	public void set_1vnw(Long _1vnw) {
		this.dto.set_1vnw(_1vnw);
	}

	public Player_clutch_stats_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Player_clutch_stats_cacheDto dto) {
		this.dto = dto;
	}

}
