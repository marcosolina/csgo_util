package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Entry_kill_stats_extendedDao extends IxigoDao<Entry_kill_stats_extendedDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Entry_kill_stats_extendedDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "entry_kill_stats_extended";
	private Entry_kill_stats_extendedDto dto = null;

	public Entry_kill_stats_extendedDao() {
		_LOGGER.trace("Instanciating Entry_kill_stats_extendedDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Entry_kill_stats_extendedDto.Fields.total_rounds_t,
			Entry_kill_stats_extendedDto.Fields.ekct_success,
			Entry_kill_stats_extendedDto.Fields.ekt_attempts,
			Entry_kill_stats_extendedDto.Fields.ekt_success,
			Entry_kill_stats_extendedDto.Fields.ekct_attempts,
			Entry_kill_stats_extendedDto.Fields.total_rounds,
			Entry_kill_stats_extendedDto.Fields.ekct_success_rate_overall,
			Entry_kill_stats_extendedDto.Fields.ekt_success_rate,
			Entry_kill_stats_extendedDto.Fields.steamid,
			Entry_kill_stats_extendedDto.Fields.ek_success_rate_overall,
			Entry_kill_stats_extendedDto.Fields.ek_success_rate,
			Entry_kill_stats_extendedDto.Fields.total_rounds_ct,
			Entry_kill_stats_extendedDto.Fields.ek_success,
			Entry_kill_stats_extendedDto.Fields.ekct_success_rate,
			Entry_kill_stats_extendedDto.Fields.ek_attempts,
			Entry_kill_stats_extendedDto.Fields.ekt_success_rate_overall,
		});
		// @formatter:on
		this.dto = new Entry_kill_stats_extendedDto();
	}

	@Override
	public Entry_kill_stats_extendedDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Entry_kill_stats_extendedDto(), row, rowMetaData);
	}

	public Long getTotal_rounds_t() {
		return dto.getTotal_rounds_t();
	}

	public void setTotal_rounds_t(Long total_rounds_t) {
		this.dto.setTotal_rounds_t(total_rounds_t);
	}

	public Long getEkct_success() {
		return dto.getEkct_success();
	}

	public void setEkct_success(Long ekct_success) {
		this.dto.setEkct_success(ekct_success);
	}

	public Long getEkt_attempts() {
		return dto.getEkt_attempts();
	}

	public void setEkt_attempts(Long ekt_attempts) {
		this.dto.setEkt_attempts(ekt_attempts);
	}

	public Long getEkt_success() {
		return dto.getEkt_success();
	}

	public void setEkt_success(Long ekt_success) {
		this.dto.setEkt_success(ekt_success);
	}

	public Long getEkct_attempts() {
		return dto.getEkct_attempts();
	}

	public void setEkct_attempts(Long ekct_attempts) {
		this.dto.setEkct_attempts(ekct_attempts);
	}

	public Long getTotal_rounds() {
		return dto.getTotal_rounds();
	}

	public void setTotal_rounds(Long total_rounds) {
		this.dto.setTotal_rounds(total_rounds);
	}

	public BigDecimal getEkct_success_rate_overall() {
		return dto.getEkct_success_rate_overall();
	}

	public void setEkct_success_rate_overall(BigDecimal ekct_success_rate_overall) {
		this.dto.setEkct_success_rate_overall(ekct_success_rate_overall);
	}

	public BigDecimal getEkt_success_rate() {
		return dto.getEkt_success_rate();
	}

	public void setEkt_success_rate(BigDecimal ekt_success_rate) {
		this.dto.setEkt_success_rate(ekt_success_rate);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public BigDecimal getEk_success_rate_overall() {
		return dto.getEk_success_rate_overall();
	}

	public void setEk_success_rate_overall(BigDecimal ek_success_rate_overall) {
		this.dto.setEk_success_rate_overall(ek_success_rate_overall);
	}

	public BigDecimal getEk_success_rate() {
		return dto.getEk_success_rate();
	}

	public void setEk_success_rate(BigDecimal ek_success_rate) {
		this.dto.setEk_success_rate(ek_success_rate);
	}

	public Long getTotal_rounds_ct() {
		return dto.getTotal_rounds_ct();
	}

	public void setTotal_rounds_ct(Long total_rounds_ct) {
		this.dto.setTotal_rounds_ct(total_rounds_ct);
	}

	public Long getEk_success() {
		return dto.getEk_success();
	}

	public void setEk_success(Long ek_success) {
		this.dto.setEk_success(ek_success);
	}

	public BigDecimal getEkct_success_rate() {
		return dto.getEkct_success_rate();
	}

	public void setEkct_success_rate(BigDecimal ekct_success_rate) {
		this.dto.setEkct_success_rate(ekct_success_rate);
	}

	public Long getEk_attempts() {
		return dto.getEk_attempts();
	}

	public void setEk_attempts(Long ek_attempts) {
		this.dto.setEk_attempts(ek_attempts);
	}

	public BigDecimal getEkt_success_rate_overall() {
		return dto.getEkt_success_rate_overall();
	}

	public void setEkt_success_rate_overall(BigDecimal ekt_success_rate_overall) {
		this.dto.setEkt_success_rate_overall(ekt_success_rate_overall);
	}

	public Entry_kill_stats_extendedDto getDto() {
		return this.dto;
	}

	public void setDto(Entry_kill_stats_extendedDto dto) {
		this.dto = dto;
	}

}
