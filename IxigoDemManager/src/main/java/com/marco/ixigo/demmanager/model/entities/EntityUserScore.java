package com.marco.ixigo.demmanager.model.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity which represents the Steam Users scores per every game (map played)
 * 
 * @author Marco
 *
 */
@Entity
@Table(name = "USERS_SCORES")
public class EntityUserScore {
    @EmbeddedId
    private EntityUserScorePk id;
    @Column(name = "RWS")
    private BigDecimal roundWinShare;
    @Column(name = "KILLS")
    private Long kills;
    @Column(name = "ASSISTS")
    private Long assists;
    @Column(name = "DEATHS")
    private Long deaths;
    @Column(name = "KDR")
    private BigDecimal killDeathRation;
    @Column(name = "HS")
    private Long headShots;
    @Column(name = "HSP")
    private BigDecimal headShotsPercentage;
    @Column(name = "FF")
    private Long teamKillFriendlyFire;
    @Column(name = "EK")
    private Long entryKill;
    @Column(name = "BP")
    private Long bombPLanted;
    @Column(name = "BD")
    private Long bombDefused;
    @Column(name = "MVP")
    private Long mostValuablePlayer;
    @Column(name = "SCORE")
    private Long score;
    @Column(name = "HLTV")
    private BigDecimal halfLifeTelevisionRating;
    @Column(name = "_5K")
    private Long fiveKills;
    @Column(name = "_4K")
    private Long fourKills;
    @Column(name = "_3K")
    private Long threeKills;
    @Column(name = "_2K")
    private Long twoKills;
    @Column(name = "_1K")
    private Long oneKill;
    @Column(name = "TK")
    private Long tradeKill;
    @Column(name = "TD")
    private Long tradeDeath;
    @Column(name = "KPR")
    private BigDecimal killPerRound;
    @Column(name = "APR")
    private BigDecimal assistsPerRound;
    @Column(name = "DPR")
    private BigDecimal deathPerRound;
    @Column(name = "ADR")
    private BigDecimal averageDamagePerRound;
    @Column(name = "TDH")
    private Long totalDamageHealth;
    @Column(name = "TDA")
    private Long totalDamageArmor;
    @Column(name = "_1V1")
    private Long oneVersusOne;
    @Column(name = "_1V2")
    private Long oneVersusTwo;
    @Column(name = "_1V3")
    private Long oneVersusThree;
    @Column(name = "_1V4")
    private Long oneVersusFour;
    @Column(name = "_1V5")
    private Long oneVersusFive;
    @Column(name = "GRENADES")
    private Long grenadesThrownCount;
    @Column(name = "FLASHES")
    private Long flashesThrownCount;
    @Column(name = "SMOKES")
    private Long smokesThrownCount;
    @Column(name = "FIRE")
    private Long fireThrownCount;
    @Column(name = "HED")
    private Long highExplosiveDamage;
    @Column(name = "FD")
    private Long fireDamage;
    @Column(name = "MP")
    private BigDecimal matchPlayed;

    public EntityUserScorePk getId() {
        return id;
    }

    public void setId(EntityUserScorePk id) {
        this.id = id;
    }

    public BigDecimal getRoundWinShare() {
        return roundWinShare;
    }

    public void setRoundWinShare(BigDecimal roundWinShare) {
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

    public BigDecimal getKillDeathRation() {
        return killDeathRation;
    }

    public void setKillDeathRation(BigDecimal killDeathRation) {
        this.killDeathRation = killDeathRation;
    }

    public Long getHeadShots() {
        return headShots;
    }

    public void setHeadShots(Long headShots) {
        this.headShots = headShots;
    }

    public BigDecimal getHeadShotsPercentage() {
        return headShotsPercentage;
    }

    public void setHeadShotsPercentage(BigDecimal headShotsPercentage) {
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

    public BigDecimal getHalfLifeTelevisionRating() {
        return halfLifeTelevisionRating;
    }

    public void setHalfLifeTelevisionRating(BigDecimal halfLifeTelevisionRating) {
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
    
    public BigDecimal getMatchPlayed() {
        return matchPlayed;
    }

    public void setMatchPlayed(BigDecimal matchPlayed) {
        this.matchPlayed = matchPlayed;
    }

}
