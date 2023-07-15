package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_round_extended_statsDao extends IxigoDao<Player_round_extended_statsDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_round_extended_statsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_round_extended_stats";
	private Player_round_extended_statsDto dto = null;

	public Player_round_extended_statsDao() {
		_LOGGER.trace("Instanciating Player_round_extended_statsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Player_round_extended_statsDto.Fields.kills,
			Player_round_extended_statsDto.Fields.grenades_thrown,
			Player_round_extended_statsDto.Fields.headshot_deaths,
			Player_round_extended_statsDto.Fields.bombs_defused,
			Player_round_extended_statsDto.Fields.trade_kills,
			Player_round_extended_statsDto.Fields.kast,
			Player_round_extended_statsDto.Fields.opponent_blindtime,
			Player_round_extended_statsDto.Fields.hostages_rescued,
			Player_round_extended_statsDto.Fields.rws,
			Player_round_extended_statsDto.Fields.smokes_thrown,
			Player_round_extended_statsDto.Fields.headshots,
			Player_round_extended_statsDto.Fields.assists,
			Player_round_extended_statsDto.Fields.clutchchance,
			Player_round_extended_statsDto.Fields.bombs_planted,
			Player_round_extended_statsDto.Fields.total_damage_armour,
			Player_round_extended_statsDto.Fields.equipmentvalue,
			Player_round_extended_statsDto.Fields.team_deaths,
			Player_round_extended_statsDto.Fields.entry_kills,
			Player_round_extended_statsDto.Fields.headshot_percentage,
			Player_round_extended_statsDto.Fields.deaths,
			Player_round_extended_statsDto.Fields.team_kills,
			Player_round_extended_statsDto.Fields.moneyspent,
			Player_round_extended_statsDto.Fields.clutchsuccess,
			Player_round_extended_statsDto.Fields.mvp,
			Player_round_extended_statsDto.Fields.survived,
			Player_round_extended_statsDto.Fields.team,
			Player_round_extended_statsDto.Fields.inferno_thrown,
			Player_round_extended_statsDto.Fields.teammate_blindtime,
			Player_round_extended_statsDto.Fields.steamid,
			Player_round_extended_statsDto.Fields.match_filename,
			Player_round_extended_statsDto.Fields.he_damage,
			Player_round_extended_statsDto.Fields.round,
			Player_round_extended_statsDto.Fields.trade_deaths,
			Player_round_extended_statsDto.Fields.fire_damage,
			Player_round_extended_statsDto.Fields.flashassists,
			Player_round_extended_statsDto.Fields.flashes_thrown,
			Player_round_extended_statsDto.Fields.opponents_flashed,
			Player_round_extended_statsDto.Fields.teammate_damage_health,
			Player_round_extended_statsDto.Fields.total_damage_health,
		});
		// @formatter:on
		this.dto = new Player_round_extended_statsDto();
	}

	@Override
	public Player_round_extended_statsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_round_extended_statsDto(), row, rowMetaData);
	}

	public Long getKills() {
		return dto.getKills();
	}

	public void setKills(Long kills) {
		this.dto.setKills(kills);
	}

	public Long getGrenades_thrown() {
		return dto.getGrenades_thrown();
	}

	public void setGrenades_thrown(Long grenades_thrown) {
		this.dto.setGrenades_thrown(grenades_thrown);
	}

	public Long getHeadshot_deaths() {
		return dto.getHeadshot_deaths();
	}

	public void setHeadshot_deaths(Long headshot_deaths) {
		this.dto.setHeadshot_deaths(headshot_deaths);
	}

	public Long getBombs_defused() {
		return dto.getBombs_defused();
	}

	public void setBombs_defused(Long bombs_defused) {
		this.dto.setBombs_defused(bombs_defused);
	}

	public Long getTrade_kills() {
		return dto.getTrade_kills();
	}

	public void setTrade_kills(Long trade_kills) {
		this.dto.setTrade_kills(trade_kills);
	}

	public Long getKast() {
		return dto.getKast();
	}

	public void setKast(Long kast) {
		this.dto.setKast(kast);
	}

	public BigDecimal getOpponent_blindtime() {
		return dto.getOpponent_blindtime();
	}

	public void setOpponent_blindtime(BigDecimal opponent_blindtime) {
		this.dto.setOpponent_blindtime(opponent_blindtime);
	}

	public Long getHostages_rescued() {
		return dto.getHostages_rescued();
	}

	public void setHostages_rescued(Long hostages_rescued) {
		this.dto.setHostages_rescued(hostages_rescued);
	}

	public BigDecimal getRws() {
		return dto.getRws();
	}

	public void setRws(BigDecimal rws) {
		this.dto.setRws(rws);
	}

	public Long getSmokes_thrown() {
		return dto.getSmokes_thrown();
	}

	public void setSmokes_thrown(Long smokes_thrown) {
		this.dto.setSmokes_thrown(smokes_thrown);
	}

	public Long getHeadshots() {
		return dto.getHeadshots();
	}

	public void setHeadshots(Long headshots) {
		this.dto.setHeadshots(headshots);
	}

	public Long getAssists() {
		return dto.getAssists();
	}

	public void setAssists(Long assists) {
		this.dto.setAssists(assists);
	}

	public BigDecimal getClutchchance() {
		return dto.getClutchchance();
	}

	public void setClutchchance(BigDecimal clutchchance) {
		this.dto.setClutchchance(clutchchance);
	}

	public Long getBombs_planted() {
		return dto.getBombs_planted();
	}

	public void setBombs_planted(Long bombs_planted) {
		this.dto.setBombs_planted(bombs_planted);
	}

	public Long getTotal_damage_armour() {
		return dto.getTotal_damage_armour();
	}

	public void setTotal_damage_armour(Long total_damage_armour) {
		this.dto.setTotal_damage_armour(total_damage_armour);
	}

	public Long getEquipmentvalue() {
		return dto.getEquipmentvalue();
	}

	public void setEquipmentvalue(Long equipmentvalue) {
		this.dto.setEquipmentvalue(equipmentvalue);
	}

	public Long getTeam_deaths() {
		return dto.getTeam_deaths();
	}

	public void setTeam_deaths(Long team_deaths) {
		this.dto.setTeam_deaths(team_deaths);
	}

	public Long getEntry_kills() {
		return dto.getEntry_kills();
	}

	public void setEntry_kills(Long entry_kills) {
		this.dto.setEntry_kills(entry_kills);
	}

	public BigDecimal getHeadshot_percentage() {
		return dto.getHeadshot_percentage();
	}

	public void setHeadshot_percentage(BigDecimal headshot_percentage) {
		this.dto.setHeadshot_percentage(headshot_percentage);
	}

	public Long getDeaths() {
		return dto.getDeaths();
	}

	public void setDeaths(Long deaths) {
		this.dto.setDeaths(deaths);
	}

	public Long getTeam_kills() {
		return dto.getTeam_kills();
	}

	public void setTeam_kills(Long team_kills) {
		this.dto.setTeam_kills(team_kills);
	}

	public Long getMoneyspent() {
		return dto.getMoneyspent();
	}

	public void setMoneyspent(Long moneyspent) {
		this.dto.setMoneyspent(moneyspent);
	}

	public Boolean getClutchsuccess() {
		return dto.getClutchsuccess();
	}

	public void setClutchsuccess(Boolean clutchsuccess) {
		this.dto.setClutchsuccess(clutchsuccess);
	}

	public Boolean getMvp() {
		return dto.getMvp();
	}

	public void setMvp(Boolean mvp) {
		this.dto.setMvp(mvp);
	}

	public Boolean getSurvived() {
		return dto.getSurvived();
	}

	public void setSurvived(Boolean survived) {
		this.dto.setSurvived(survived);
	}

	public Long getTeam() {
		return dto.getTeam();
	}

	public void setTeam(Long team) {
		this.dto.setTeam(team);
	}

	public Long getInferno_thrown() {
		return dto.getInferno_thrown();
	}

	public void setInferno_thrown(Long inferno_thrown) {
		this.dto.setInferno_thrown(inferno_thrown);
	}

	public BigDecimal getTeammate_blindtime() {
		return dto.getTeammate_blindtime();
	}

	public void setTeammate_blindtime(BigDecimal teammate_blindtime) {
		this.dto.setTeammate_blindtime(teammate_blindtime);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public String getMatch_filename() {
		return dto.getMatch_filename();
	}

	public void setMatch_filename(String match_filename) {
		this.dto.setMatch_filename(match_filename);
	}

	public Long getHe_damage() {
		return dto.getHe_damage();
	}

	public void setHe_damage(Long he_damage) {
		this.dto.setHe_damage(he_damage);
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

	public Long getFire_damage() {
		return dto.getFire_damage();
	}

	public void setFire_damage(Long fire_damage) {
		this.dto.setFire_damage(fire_damage);
	}

	public Long getFlashassists() {
		return dto.getFlashassists();
	}

	public void setFlashassists(Long flashassists) {
		this.dto.setFlashassists(flashassists);
	}

	public Long getFlashes_thrown() {
		return dto.getFlashes_thrown();
	}

	public void setFlashes_thrown(Long flashes_thrown) {
		this.dto.setFlashes_thrown(flashes_thrown);
	}

	public Long getOpponents_flashed() {
		return dto.getOpponents_flashed();
	}

	public void setOpponents_flashed(Long opponents_flashed) {
		this.dto.setOpponents_flashed(opponents_flashed);
	}

	public Long getTeammate_damage_health() {
		return dto.getTeammate_damage_health();
	}

	public void setTeammate_damage_health(Long teammate_damage_health) {
		this.dto.setTeammate_damage_health(teammate_damage_health);
	}

	public Long getTotal_damage_health() {
		return dto.getTotal_damage_health();
	}

	public void setTotal_damage_health(Long total_damage_health) {
		this.dto.setTotal_damage_health(total_damage_health);
	}

	public Player_round_extended_statsDto getDto() {
		return this.dto;
	}

	public void setDto(Player_round_extended_statsDto dto) {
		this.dto = dto;
	}

}
