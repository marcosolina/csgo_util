package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Map_player_weapon_stats_cacheDao extends IxigoDao<Map_player_weapon_stats_cacheDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Map_player_weapon_stats_cacheDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "map_player_weapon_stats_cache";
	private Map_player_weapon_stats_cacheDto dto = null;

	public Map_player_weapon_stats_cacheDao() {
		_LOGGER.trace("Instanciating Map_player_weapon_stats_cacheDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Map_player_weapon_stats_cacheDto.Fields.kills,
			Map_player_weapon_stats_cacheDto.Fields.headshotkills,
			Map_player_weapon_stats_cacheDto.Fields.damage_per_shot,
			Map_player_weapon_stats_cacheDto.Fields.accuracy,
			Map_player_weapon_stats_cacheDto.Fields.mapname,
			Map_player_weapon_stats_cacheDto.Fields.steamid,
			Map_player_weapon_stats_cacheDto.Fields.hits,
			Map_player_weapon_stats_cacheDto.Fields.weapon,
			Map_player_weapon_stats_cacheDto.Fields.total_damage,
			Map_player_weapon_stats_cacheDto.Fields.damage_per_hit,
			Map_player_weapon_stats_cacheDto.Fields.headshotkills_percentage,
			Map_player_weapon_stats_cacheDto.Fields.shots_fired,
			Map_player_weapon_stats_cacheDto.Fields.chest_hit_percentage,
			Map_player_weapon_stats_cacheDto.Fields.leg_hit_percentage,
			Map_player_weapon_stats_cacheDto.Fields.headshot_percentage,
			Map_player_weapon_stats_cacheDto.Fields.stomach_hit_percentage,
			Map_player_weapon_stats_cacheDto.Fields.arm_hit_percentage,
		});
		// @formatter:on
		this.dto = new Map_player_weapon_stats_cacheDto();
	}

	@Override
	public Map_player_weapon_stats_cacheDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Map_player_weapon_stats_cacheDto(), row, rowMetaData);
	}

	public Long getKills() {
		return dto.getKills();
	}

	public void setKills(Long kills) {
		this.dto.setKills(kills);
	}

	public Long getHeadshotkills() {
		return dto.getHeadshotkills();
	}

	public void setHeadshotkills(Long headshotkills) {
		this.dto.setHeadshotkills(headshotkills);
	}

	public BigDecimal getDamage_per_shot() {
		return dto.getDamage_per_shot();
	}

	public void setDamage_per_shot(BigDecimal damage_per_shot) {
		this.dto.setDamage_per_shot(damage_per_shot);
	}

	public BigDecimal getAccuracy() {
		return dto.getAccuracy();
	}

	public void setAccuracy(BigDecimal accuracy) {
		this.dto.setAccuracy(accuracy);
	}

	public String getMapname() {
		return dto.getMapname();
	}

	public void setMapname(String mapname) {
		this.dto.setMapname(mapname);
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

	public BigDecimal getDamage_per_hit() {
		return dto.getDamage_per_hit();
	}

	public void setDamage_per_hit(BigDecimal damage_per_hit) {
		this.dto.setDamage_per_hit(damage_per_hit);
	}

	public BigDecimal getHeadshotkills_percentage() {
		return dto.getHeadshotkills_percentage();
	}

	public void setHeadshotkills_percentage(BigDecimal headshotkills_percentage) {
		this.dto.setHeadshotkills_percentage(headshotkills_percentage);
	}

	public Long getShots_fired() {
		return dto.getShots_fired();
	}

	public void setShots_fired(Long shots_fired) {
		this.dto.setShots_fired(shots_fired);
	}

	public BigDecimal getChest_hit_percentage() {
		return dto.getChest_hit_percentage();
	}

	public void setChest_hit_percentage(BigDecimal chest_hit_percentage) {
		this.dto.setChest_hit_percentage(chest_hit_percentage);
	}

	public BigDecimal getLeg_hit_percentage() {
		return dto.getLeg_hit_percentage();
	}

	public void setLeg_hit_percentage(BigDecimal leg_hit_percentage) {
		this.dto.setLeg_hit_percentage(leg_hit_percentage);
	}

	public BigDecimal getHeadshot_percentage() {
		return dto.getHeadshot_percentage();
	}

	public void setHeadshot_percentage(BigDecimal headshot_percentage) {
		this.dto.setHeadshot_percentage(headshot_percentage);
	}

	public BigDecimal getStomach_hit_percentage() {
		return dto.getStomach_hit_percentage();
	}

	public void setStomach_hit_percentage(BigDecimal stomach_hit_percentage) {
		this.dto.setStomach_hit_percentage(stomach_hit_percentage);
	}

	public BigDecimal getArm_hit_percentage() {
		return dto.getArm_hit_percentage();
	}

	public void setArm_hit_percentage(BigDecimal arm_hit_percentage) {
		this.dto.setArm_hit_percentage(arm_hit_percentage);
	}

	public Map_player_weapon_stats_cacheDto getDto() {
		return this.dto;
	}

	public void setDto(Map_player_weapon_stats_cacheDto dto) {
		this.dto = dto;
	}

}
