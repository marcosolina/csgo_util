package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Round_hit_stats_extended_cacheDao extends IxigoDao<Round_hit_stats_extended_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Round_hit_stats_extended_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "round_hit_stats_extended_cache";
	private Round_hit_stats_extended_cacheDto dto = null;

	public Round_hit_stats_extended_cacheDao() {
		_LOGGER.trace("Instanciating Round_hit_stats_extended_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Round_hit_stats_extended_cacheDto.Fields.steamid,
			Round_hit_stats_extended_cacheDto.Fields.hits,
			Round_hit_stats_extended_cacheDto.Fields.stomach_hits,
			Round_hit_stats_extended_cacheDto.Fields.weapon,
			Round_hit_stats_extended_cacheDto.Fields.total_damage,
			Round_hit_stats_extended_cacheDto.Fields.leg_hits,
			Round_hit_stats_extended_cacheDto.Fields.headshots,
			Round_hit_stats_extended_cacheDto.Fields.round,
			Round_hit_stats_extended_cacheDto.Fields.arm_hits,
			Round_hit_stats_extended_cacheDto.Fields.match_id,
			Round_hit_stats_extended_cacheDto.Fields.chest_hits,
		});
		// @formatter:on
		this.dto = new Round_hit_stats_extended_cacheDto();
	}

	@Override
	public Round_hit_stats_extended_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Round_hit_stats_extended_cacheDto(), row, rowMetaData);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public Long getHits() {
		return dto.getHits();
	}

	public void setHits(Long hits) {
		this.dto.setHits(hits);
	}

	public Long getStomach_hits() {
		return dto.getStomach_hits();
	}

	public void setStomach_hits(Long stomach_hits) {
		this.dto.setStomach_hits(stomach_hits);
	}

	public String getWeapon() {
		return dto.getWeapon();
	}

	public void setWeapon(String weapon) {
		this.dto.setWeapon(weapon);
	}

	public Long getTotal_damage() {
		return dto.getTotal_damage();
	}

	public void setTotal_damage(Long total_damage) {
		this.dto.setTotal_damage(total_damage);
	}

	public Long getLeg_hits() {
		return dto.getLeg_hits();
	}

	public void setLeg_hits(Long leg_hits) {
		this.dto.setLeg_hits(leg_hits);
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

	public Long getArm_hits() {
		return dto.getArm_hits();
	}

	public void setArm_hits(Long arm_hits) {
		this.dto.setArm_hits(arm_hits);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public Long getChest_hits() {
		return dto.getChest_hits();
	}

	public void setChest_hits(Long chest_hits) {
		this.dto.setChest_hits(chest_hits);
	}

	public Round_hit_stats_extended_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Round_hit_stats_extended_cacheDto dto) {
		this.dto = dto;
	}

}