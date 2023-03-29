package com.ixigo.playersmanager.models.svc;

import java.math.BigDecimal;

import lombok.ToString;

@ToString
public class SvcUserAvgScore {
	private String steamID;
	private String userName;
	private BigDecimal teamSplitScore;
	private BigDecimal originalTeamSplitScore;
	private BigDecimal roundWinShare;
	private BigDecimal kills;
	private BigDecimal assists;
	private BigDecimal deaths;
	private BigDecimal killDeathRatio;
	private BigDecimal headShots;
	private BigDecimal headShotsPercentage;
	private BigDecimal teamKillFriendlyFire;
	private BigDecimal entryKill;
	private BigDecimal bombPLanted;
	private BigDecimal bombDefused;
	private BigDecimal mostValuablePlayer;
	private BigDecimal score;
	private BigDecimal halfLifeTelevisionRating;
	private BigDecimal fiveKills;
	private BigDecimal fourKills;
	private BigDecimal threeKills;
	private BigDecimal twoKills;
	private BigDecimal oneKill;
	private BigDecimal tradeKill;
	private BigDecimal tradeDeath;
	private BigDecimal killPerRound;
	private BigDecimal assistsPerRound;
	private BigDecimal deathPerRound;
	private BigDecimal averageDamagePerRound;
	private BigDecimal totalDamageHealth;
	private BigDecimal totalDamageArmor;
	private BigDecimal oneVersusOne;
	private BigDecimal oneVersusTwo;
	private BigDecimal oneVersusThree;
	private BigDecimal oneVersusFour;
	private BigDecimal oneVersusFive;
	private BigDecimal grenadesThrownCount;
	private BigDecimal flashesThrownCount;
	private BigDecimal smokesThrownCount;
	private BigDecimal fireThrownCount;
	private BigDecimal highExplosiveDamage;
	private BigDecimal fireDamage;
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

	public BigDecimal getMatchPlayed() {
		return matchPlayed;
	}

	public void setMatchPlayed(BigDecimal matchPlayed) {
		this.matchPlayed = matchPlayed;
	}

}
