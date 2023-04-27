package com.ixigo.demmanagercontract.models.rest.demdata;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

@ToString
public class RestUserGotvScore implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("user_name")
	private String userName;
	@JsonProperty("steam_id")
	private String steamID;
	@JsonProperty("round_win_share")
	private Double roundWinShare;
	@JsonProperty("kills")
	private Long kills;
	@JsonProperty("assists")
	private Long assists;
	@JsonProperty("deaths")
	private Long deaths;
	@JsonProperty("kill_death_ration")
	private Double killDeathRation;
	@JsonProperty("head_shots")
	private Long headShots;
	@JsonProperty("head_shots_percentage")
	private Double headShotsPercentage;
	@JsonProperty("team_kill_friendly_fire")
	private Long teamKillFriendlyFire;
	@JsonProperty("entry_kill")
	private Long entryKill;
	@JsonProperty("bomb_planted")
	private Long bombPLanted;
	@JsonProperty("bomb_defused")
	private Long bombDefused;
	@JsonProperty("most_valuable_player")
	private Long mostValuablePlayer;
	@JsonProperty("score")
	private Long score;
	@JsonProperty("half_life_television_rating")
	private Double halfLifeTelevisionRating;
	@JsonProperty("five_kills")
	private Long fiveKills;
	@JsonProperty("four_kills")
	private Long fourKills;
	@JsonProperty("three_kills")
	private Long threeKills;
	@JsonProperty("two_kills")
	private Long twoKills;
	@JsonProperty("one_kill")
	private Long oneKill;
	@JsonProperty("trade_kill")
	private Long tradeKill;
	@JsonProperty("trade_death")
	private Long tradeDeath;
	@JsonProperty("kill_per_round")
	private Double killPerRound;
	@JsonProperty("assists_per_round")
	private Double assistsPerRound;
	@JsonProperty("death_per_round")
	private Double deathPerRound;
	@JsonProperty("average_damage_per_round")
	private Double averageDamagePerRound;
	@JsonProperty("total_damage_health")
	private Long totalDamageHealth;
	@JsonProperty("total_damage_armor")
	private Long totalDamageArmor;
	@JsonProperty("one_versus_one")
	private Long oneVersusOne;
	@JsonProperty("one_versus_two")
	private Long oneVersusTwo;
	@JsonProperty("one_versus_three")
	private Long oneVersusThree;
	@JsonProperty("one_versus_four")
	private Long oneVersusFour;
	@JsonProperty("one_versus_five")
	private Long oneVersusFive;
	@JsonProperty("grenades_thrown_count")
	private Long grenadesThrownCount;
	@JsonProperty("flashes_thrown_count")
	private Long flashesThrownCount;
	@JsonProperty("smokes_thrown_count")
	private Long smokesThrownCount;
	@JsonProperty("fire_thrown_count")
	private Long fireThrownCount;
	@JsonProperty("high_explosive_damage")
	private Long highExplosiveDamage;
	@JsonProperty("fire_damage")
	private Long fireDamage;
	@JsonProperty("match_played")
	private Double matchPlayed;
	@JsonProperty("side")
	private String side;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSteamID() {
		return steamID;
	}

	public void setSteamID(String steamID) {
		this.steamID = steamID;
	}

	public Double getRoundWinShare() {
		return roundWinShare;
	}

	public void setRoundWinShare(Double roundWinShare) {
		this.roundWinShare = roundWinShare;
	}

	public Long getKills() {
		return kills;
	}

	public void setKills(Long kills) {
		this.kills = kills;
	}

	public Long getAssists() {
		return assists;
	}

	public void setAssists(Long assists) {
		this.assists = assists;
	}

	public Long getDeaths() {
		return deaths;
	}

	public void setDeaths(Long deaths) {
		this.deaths = deaths;
	}

	public Double getKillDeathRation() {
		return killDeathRation;
	}

	public void setKillDeathRation(Double killDeathRation) {
		this.killDeathRation = killDeathRation;
	}

	public Long getHeadShots() {
		return headShots;
	}

	public void setHeadShots(Long headShots) {
		this.headShots = headShots;
	}

	public Double getHeadShotsPercentage() {
		return headShotsPercentage;
	}

	public void setHeadShotsPercentage(Double headShotsPercentage) {
		this.headShotsPercentage = headShotsPercentage;
	}

	public Long getTeamKillFriendlyFire() {
		return teamKillFriendlyFire;
	}

	public void setTeamKillFriendlyFire(Long teamKillFriendlyFire) {
		this.teamKillFriendlyFire = teamKillFriendlyFire;
	}

	public Long getEntryKill() {
		return entryKill;
	}

	public void setEntryKill(Long entryKill) {
		this.entryKill = entryKill;
	}

	public Long getBombPLanted() {
		return bombPLanted;
	}

	public void setBombPLanted(Long bombPLanted) {
		this.bombPLanted = bombPLanted;
	}

	public Long getBombDefused() {
		return bombDefused;
	}

	public void setBombDefused(Long bombDefused) {
		this.bombDefused = bombDefused;
	}

	public Long getMostValuablePlayer() {
		return mostValuablePlayer;
	}

	public void setMostValuablePlayer(Long mostValuablePlayer) {
		this.mostValuablePlayer = mostValuablePlayer;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Double getHalfLifeTelevisionRating() {
		return halfLifeTelevisionRating;
	}

	public void setHalfLifeTelevisionRating(Double halfLifeTelevisionRating) {
		this.halfLifeTelevisionRating = halfLifeTelevisionRating;
	}

	public Long getFiveKills() {
		return fiveKills;
	}

	public void setFiveKills(Long fiveKills) {
		this.fiveKills = fiveKills;
	}

	public Long getFourKills() {
		return fourKills;
	}

	public void setFourKills(Long fourKills) {
		this.fourKills = fourKills;
	}

	public Long getThreeKills() {
		return threeKills;
	}

	public void setThreeKills(Long threeKills) {
		this.threeKills = threeKills;
	}

	public Long getTwoKills() {
		return twoKills;
	}

	public void setTwoKills(Long twoKills) {
		this.twoKills = twoKills;
	}

	public Long getOneKill() {
		return oneKill;
	}

	public void setOneKill(Long oneKill) {
		this.oneKill = oneKill;
	}

	public Long getTradeKill() {
		return tradeKill;
	}

	public void setTradeKill(Long tradeKill) {
		this.tradeKill = tradeKill;
	}

	public Long getTradeDeath() {
		return tradeDeath;
	}

	public void setTradeDeath(Long tradeDeath) {
		this.tradeDeath = tradeDeath;
	}

	public Double getKillPerRound() {
		return killPerRound;
	}

	public void setKillPerRound(Double killPerRound) {
		this.killPerRound = killPerRound;
	}

	public Double getAssistsPerRound() {
		return assistsPerRound;
	}

	public void setAssistsPerRound(Double assistsPerRound) {
		this.assistsPerRound = assistsPerRound;
	}

	public Double getDeathPerRound() {
		return deathPerRound;
	}

	public void setDeathPerRound(Double deathPerRound) {
		this.deathPerRound = deathPerRound;
	}

	public Double getAverageDamagePerRound() {
		return averageDamagePerRound;
	}

	public void setAverageDamagePerRound(Double averageDamagePerRound) {
		this.averageDamagePerRound = averageDamagePerRound;
	}

	public Long getTotalDamageHealth() {
		return totalDamageHealth;
	}

	public void setTotalDamageHealth(Long totalDamageHealth) {
		this.totalDamageHealth = totalDamageHealth;
	}

	public Long getTotalDamageArmor() {
		return totalDamageArmor;
	}

	public void setTotalDamageArmor(Long totalDamageArmor) {
		this.totalDamageArmor = totalDamageArmor;
	}

	public Long getOneVersusOne() {
		return oneVersusOne;
	}

	public void setOneVersusOne(Long oneVersusOne) {
		this.oneVersusOne = oneVersusOne;
	}

	public Long getOneVersusTwo() {
		return oneVersusTwo;
	}

	public void setOneVersusTwo(Long oneVersusTwo) {
		this.oneVersusTwo = oneVersusTwo;
	}

	public Long getOneVersusThree() {
		return oneVersusThree;
	}

	public void setOneVersusThree(Long oneVersusThree) {
		this.oneVersusThree = oneVersusThree;
	}

	public Long getOneVersusFour() {
		return oneVersusFour;
	}

	public void setOneVersusFour(Long oneVersusFour) {
		this.oneVersusFour = oneVersusFour;
	}

	public Long getOneVersusFive() {
		return oneVersusFive;
	}

	public void setOneVersusFive(Long oneVersusFive) {
		this.oneVersusFive = oneVersusFive;
	}

	public Long getGrenadesThrownCount() {
		return grenadesThrownCount;
	}

	public void setGrenadesThrownCount(Long grenadesThrownCount) {
		this.grenadesThrownCount = grenadesThrownCount;
	}

	public Long getFlashesThrownCount() {
		return flashesThrownCount;
	}

	public void setFlashesThrownCount(Long flashesThrownCount) {
		this.flashesThrownCount = flashesThrownCount;
	}

	public Long getSmokesThrownCount() {
		return smokesThrownCount;
	}

	public void setSmokesThrownCount(Long smokesThrownCount) {
		this.smokesThrownCount = smokesThrownCount;
	}

	public Long getFireThrownCount() {
		return fireThrownCount;
	}

	public void setFireThrownCount(Long fireThrownCount) {
		this.fireThrownCount = fireThrownCount;
	}

	public Long getHighExplosiveDamage() {
		return highExplosiveDamage;
	}

	public void setHighExplosiveDamage(Long highExplosiveDamage) {
		this.highExplosiveDamage = highExplosiveDamage;
	}

	public Long getFireDamage() {
		return fireDamage;
	}

	public void setFireDamage(Long fireDamage) {
		this.fireDamage = fireDamage;
	}

	public Double getMatchPlayed() {
		return matchPlayed;
	}

	public void setMatchPlayed(Double matchPlayed) {
		this.matchPlayed = matchPlayed;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

}
