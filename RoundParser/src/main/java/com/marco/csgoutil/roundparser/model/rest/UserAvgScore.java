package com.marco.csgoutil.roundparser.model.rest;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Response model used to return the Average score calculated for the
 * specific user
 * 
 * @author Marco
 *
 */
public class UserAvgScore {
	@ApiModelProperty(notes = "Steam ID of the user")
	private String steamID;
	@ApiModelProperty(notes = "User Name")
	private String userName;
	@ApiModelProperty(notes = "Scored used to genarate the teams")
	private BigDecimal teamSplitScore;
	@ApiModelProperty(notes = "Back-up of the original value")
	private BigDecimal originalTeamSplitScore;
	@ApiModelProperty(notes = "RWS - Round Win Share")
	private BigDecimal roundWinShare;
	@ApiModelProperty(notes = "Kills")
	private BigDecimal kills;
	@ApiModelProperty(notes = "Assists")
	private BigDecimal assists;
	@ApiModelProperty(notes = "Deaths")
	private BigDecimal deaths;
	@ApiModelProperty(notes = "KDR - Kill Death Ratio")
	private BigDecimal killDeathRatio;
	@ApiModelProperty(notes = "HS - Head Shots")
	private BigDecimal headShots;
	@ApiModelProperty(notes = "HSP - Head Shots Percentage")
	private BigDecimal headShotsPercentage;
	@ApiModelProperty(notes = "FF - Team Kill Friendly Fire")
	private BigDecimal teamKillFriendlyFire;
	@ApiModelProperty(notes = "EK - Entry Kill")
	private BigDecimal entryKill;
	@ApiModelProperty(notes = "BP - Bomb Planted")
	private BigDecimal bombPLanted;
	@ApiModelProperty(notes = "BD - Bomb Defused")
	private BigDecimal bombDefused;
	@ApiModelProperty(notes = "MVP - Most Valuable Player")
	private BigDecimal mostValuablePlayer;
	@ApiModelProperty(notes = "Score")
	private BigDecimal score;
	@ApiModelProperty(notes = "HLTV - Half Life Television Rating")
	private BigDecimal halfLifeTelevisionRating;
	@ApiModelProperty(notes = "5k - Five Kills")
	private BigDecimal fiveKills;
	@ApiModelProperty(notes = "4K - Four Kills")
	private BigDecimal fourKills;
	@ApiModelProperty(notes = "3K - Three Kills")
	private BigDecimal threeKills;
	@ApiModelProperty(notes = "2K - Two Kills")
	private BigDecimal twoKills;
	@ApiModelProperty(notes = "1K - One Kill")
	private BigDecimal oneKill;
	@ApiModelProperty(notes = "TK - Trade Kill")
	private BigDecimal tradeKill;
	@ApiModelProperty(notes = "TD - Trade Death")
	private BigDecimal tradeDeath;
	@ApiModelProperty(notes = "KR - Kill per Round")
	private BigDecimal killPerRound;
	@ApiModelProperty(notes = "AR - Assists per Round")
	private BigDecimal assistsPerRound;
	@ApiModelProperty(notes = "DR - Death per round")
	private BigDecimal deathPerRound;
	@ApiModelProperty(notes = "AD - Average damge per round")
	private BigDecimal averageDamagePerRound;
	@ApiModelProperty(notes = "Total Damage health")
	private BigDecimal totalDamageHealth;
	@ApiModelProperty(notes = "Total Damage Armor")
	private BigDecimal totalDamageArmor;
	@ApiModelProperty(notes = "One Versus one")
	private BigDecimal oneVersusOne;
	@ApiModelProperty(notes = "One Versus two")
	private BigDecimal oneVersusTwo;
	@ApiModelProperty(notes = "One versus three")
	private BigDecimal oneVersusThree;
	@ApiModelProperty(notes = "One versus four")
	private BigDecimal oneVersusFour;
	@ApiModelProperty(notes = "One versus five")
	private BigDecimal oneVersusFive;
	@ApiModelProperty(notes = "Grenades thrown count")
	private BigDecimal grenadesThrownCount;
	@ApiModelProperty(notes = "Flashes trhown count")
	private BigDecimal flashesThrownCount;
	@ApiModelProperty(notes = "Smokes thrown count")
	private BigDecimal smokesThrownCount;
	@ApiModelProperty(notes = "Fire thrown count")
	private BigDecimal fireThrownCount;
	@ApiModelProperty(notes = "High Explosive damage")
	private BigDecimal highExplosiveDamage;
	@ApiModelProperty(notes = "Fire Damage")
	private BigDecimal fireDamage;
	@ApiModelProperty(name = "Match Played Percent")
	private BigDecimal matchPlayed;

	public String getSteamID() {
		return steamID;
	}

	public void setSteamID(String steamID) {
		this.steamID = steamID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getRoundWinShare() {
		return roundWinShare;
	}

	public void setRoundWinShare(BigDecimal roundWinShare) {
		this.roundWinShare = roundWinShare;
	}

	public BigDecimal getKills() {
		return kills;
	}

	public void setKills(BigDecimal kills) {
		this.kills = kills;
	}

	public BigDecimal getAssists() {
		return assists;
	}

	public void setAssists(BigDecimal assists) {
		this.assists = assists;
	}

	public BigDecimal getDeaths() {
		return deaths;
	}

	public void setDeaths(BigDecimal deaths) {
		this.deaths = deaths;
	}

	public BigDecimal getKillDeathRatio() {
		return killDeathRatio;
	}

	public void setKillDeathRatio(BigDecimal killDeathRatio) {
		this.killDeathRatio = killDeathRatio;
	}

	public BigDecimal getHeadShots() {
		return headShots;
	}

	public void setHeadShots(BigDecimal headShots) {
		this.headShots = headShots;
	}

	public BigDecimal getHeadShotsPercentage() {
		return headShotsPercentage;
	}

	public void setHeadShotsPercentage(BigDecimal headShotsPercentage) {
		this.headShotsPercentage = headShotsPercentage;
	}

	public BigDecimal getTeamKillFriendlyFire() {
		return teamKillFriendlyFire;
	}

	public void setTeamKillFriendlyFire(BigDecimal teamKillFriendlyFire) {
		this.teamKillFriendlyFire = teamKillFriendlyFire;
	}

	public BigDecimal getEntryKill() {
		return entryKill;
	}

	public void setEntryKill(BigDecimal entryKill) {
		this.entryKill = entryKill;
	}

	public BigDecimal getBombPLanted() {
		return bombPLanted;
	}

	public void setBombPLanted(BigDecimal bombPLanted) {
		this.bombPLanted = bombPLanted;
	}

	public BigDecimal getBombDefused() {
		return bombDefused;
	}

	public void setBombDefused(BigDecimal bombDefused) {
		this.bombDefused = bombDefused;
	}

	public BigDecimal getMostValuablePlayer() {
		return mostValuablePlayer;
	}

	public void setMostValuablePlayer(BigDecimal mostValuablePlayer) {
		this.mostValuablePlayer = mostValuablePlayer;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public BigDecimal getHalfLifeTelevisionRating() {
		return halfLifeTelevisionRating;
	}

	public void setHalfLifeTelevisionRating(BigDecimal halfLifeTelevisionRating) {
		this.halfLifeTelevisionRating = halfLifeTelevisionRating;
	}

	public BigDecimal getFiveKills() {
		return fiveKills;
	}

	public void setFiveKills(BigDecimal fiveKills) {
		this.fiveKills = fiveKills;
	}

	public BigDecimal getFourKills() {
		return fourKills;
	}

	public void setFourKills(BigDecimal fourKills) {
		this.fourKills = fourKills;
	}

	public BigDecimal getThreeKills() {
		return threeKills;
	}

	public void setThreeKills(BigDecimal threeKills) {
		this.threeKills = threeKills;
	}

	public BigDecimal getTwoKills() {
		return twoKills;
	}

	public void setTwoKills(BigDecimal twoKills) {
		this.twoKills = twoKills;
	}

	public BigDecimal getOneKill() {
		return oneKill;
	}

	public void setOneKill(BigDecimal oneKill) {
		this.oneKill = oneKill;
	}

	public BigDecimal getTradeKill() {
		return tradeKill;
	}

	public void setTradeKill(BigDecimal tradeKill) {
		this.tradeKill = tradeKill;
	}

	public BigDecimal getTradeDeath() {
		return tradeDeath;
	}

	public void setTradeDeath(BigDecimal tradeDeath) {
		this.tradeDeath = tradeDeath;
	}

	public BigDecimal getKillPerRound() {
		return killPerRound;
	}

	public void setKillPerRound(BigDecimal killPerRound) {
		this.killPerRound = killPerRound;
	}

	public BigDecimal getAssistsPerRound() {
		return assistsPerRound;
	}

	public void setAssistsPerRound(BigDecimal assistsPerRound) {
		this.assistsPerRound = assistsPerRound;
	}

	public BigDecimal getDeathPerRound() {
		return deathPerRound;
	}

	public void setDeathPerRound(BigDecimal deathPerRound) {
		this.deathPerRound = deathPerRound;
	}

	public BigDecimal getAverageDamagePerRound() {
		return averageDamagePerRound;
	}

	public void setAverageDamagePerRound(BigDecimal averageDamagePerRound) {
		this.averageDamagePerRound = averageDamagePerRound;
	}

	public BigDecimal getTotalDamageHealth() {
		return totalDamageHealth;
	}

	public void setTotalDamageHealth(BigDecimal totalDamageHealth) {
		this.totalDamageHealth = totalDamageHealth;
	}

	public BigDecimal getTotalDamageArmor() {
		return totalDamageArmor;
	}

	public void setTotalDamageArmor(BigDecimal totalDamageArmor) {
		this.totalDamageArmor = totalDamageArmor;
	}

	public BigDecimal getOneVersusOne() {
		return oneVersusOne;
	}

	public void setOneVersusOne(BigDecimal oneVersusOne) {
		this.oneVersusOne = oneVersusOne;
	}

	public BigDecimal getOneVersusTwo() {
		return oneVersusTwo;
	}

	public void setOneVersusTwo(BigDecimal oneVersusTwo) {
		this.oneVersusTwo = oneVersusTwo;
	}

	public BigDecimal getOneVersusThree() {
		return oneVersusThree;
	}

	public void setOneVersusThree(BigDecimal oneVersusThree) {
		this.oneVersusThree = oneVersusThree;
	}

	public BigDecimal getOneVersusFour() {
		return oneVersusFour;
	}

	public void setOneVersusFour(BigDecimal oneVersusFour) {
		this.oneVersusFour = oneVersusFour;
	}

	public BigDecimal getOneVersusFive() {
		return oneVersusFive;
	}

	public void setOneVersusFive(BigDecimal oneVersusFive) {
		this.oneVersusFive = oneVersusFive;
	}

	public BigDecimal getGrenadesThrownCount() {
		return grenadesThrownCount;
	}

	public void setGrenadesThrownCount(BigDecimal grenadesThrownCount) {
		this.grenadesThrownCount = grenadesThrownCount;
	}

	public BigDecimal getFlashesThrownCount() {
		return flashesThrownCount;
	}

	public void setFlashesThrownCount(BigDecimal flashesThrownCount) {
		this.flashesThrownCount = flashesThrownCount;
	}

	public BigDecimal getSmokesThrownCount() {
		return smokesThrownCount;
	}

	public void setSmokesThrownCount(BigDecimal smokesThrownCount) {
		this.smokesThrownCount = smokesThrownCount;
	}

	public BigDecimal getFireThrownCount() {
		return fireThrownCount;
	}

	public void setFireThrownCount(BigDecimal fireThrownCount) {
		this.fireThrownCount = fireThrownCount;
	}

	public BigDecimal getHighExplosiveDamage() {
		return highExplosiveDamage;
	}

	public void setHighExplosiveDamage(BigDecimal highExplosiveDamage) {
		this.highExplosiveDamage = highExplosiveDamage;
	}

	public BigDecimal getFireDamage() {
		return fireDamage;
	}

	public void setFireDamage(BigDecimal fireDamage) {
		this.fireDamage = fireDamage;
	}

	public BigDecimal getTeamSplitScore() {
		return teamSplitScore;
	}

	public void setTeamSplitScore(BigDecimal teamSplitScore) {
		this.teamSplitScore = teamSplitScore;
	}

	public BigDecimal getOriginalTeamSplitScore() {
		return originalTeamSplitScore;
	}

	public void setOriginalTeamSplitScore(BigDecimal originalTeamSplitScore) {
		this.originalTeamSplitScore = originalTeamSplitScore;
	}
	
	public BigDecimal getMatchPlayed() {
		return matchPlayed;
	}

	public void setMatchPlayed(BigDecimal matchPlayed) {
		this.matchPlayed = matchPlayed;
	}

}
