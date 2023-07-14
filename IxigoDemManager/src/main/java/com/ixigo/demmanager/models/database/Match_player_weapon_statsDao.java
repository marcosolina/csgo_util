package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Match_player_weapon_statsDao extends IxigoDao<Match_player_weapon_statsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Match_player_weapon_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "match_player_weapon_stats";
	private Match_player_weapon_statsDto dto = null;

	public Match_player_weapon_statsDao() {
		this.setSqlViewName(tableName);
	// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Match_player_weapon_statsDto.Fields.damage_per_shot,
			Match_player_weapon_statsDto.Fields.match_id,
			Match_player_weapon_statsDto.Fields.accuracy,
			Match_player_weapon_statsDto.Fields.steamid,
			Match_player_weapon_statsDto.Fields.hits,
			Match_player_weapon_statsDto.Fields.weapon,
			Match_player_weapon_statsDto.Fields.total_damage,
			Match_player_weapon_statsDto.Fields.damage_per_hit,
			Match_player_weapon_statsDto.Fields.shots_fired,
			Match_player_weapon_statsDto.Fields.chest_hit_percentage,
			Match_player_weapon_statsDto.Fields.leg_hit_percentage,
			Match_player_weapon_statsDto.Fields.headshot_percentage,
			Match_player_weapon_statsDto.Fields.stomach_hit_percentage,
			Match_player_weapon_statsDto.Fields.username,
			Match_player_weapon_statsDto.Fields.arm_hit_percentage,
		});
	// @formatter:on
		this.dto = new Match_player_weapon_statsDto();
	}

	@Override
	public Match_player_weapon_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		return this.genericMappingFunction(new Match_player_weapon_statsDto(), row, rowMetaData);
	}

	public BigDecimal getDamage_per_shot() {
		return dto.getDamage_per_shot();
	}

	public void setDamage_per_shot(BigDecimal damage_per_shot) {
		this.dto.setDamage_per_shot(damage_per_shot);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public BigDecimal getAccuracy() {
		return dto.getAccuracy();
	}

	public void setAccuracy(BigDecimal accuracy) {
		this.dto.setAccuracy(accuracy);
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

	public String getUsername() {
		return dto.getUsername();
	}

	public void setUsername(String username) {
		this.dto.setUsername(username);
	}

	public BigDecimal getArm_hit_percentage() {
		return dto.getArm_hit_percentage();
	}

	public void setArm_hit_percentage(BigDecimal arm_hit_percentage) {
		this.dto.setArm_hit_percentage(arm_hit_percentage);
	}

	public Match_player_weapon_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Match_player_weapon_statsDto dto) {
		this.dto = dto;
	}

}
