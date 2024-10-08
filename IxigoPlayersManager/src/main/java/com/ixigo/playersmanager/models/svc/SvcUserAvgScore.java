package com.ixigo.playersmanager.models.svc;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
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
	private BigDecimal kast;
	private BigDecimal hostageRescued;
	private BigDecimal bombPlanted;
	private BigDecimal roundWinShareTotal;
	private BigDecimal teammateDamageHealth;
	private BigDecimal opponentBlindTime;
	private BigDecimal teammateBlindTime;
	private BigDecimal flashAssists;
	private BigDecimal kasttotal;
	private BigDecimal roundsOnTeam2;
	private BigDecimal roundsOnTeam1;
	private BigDecimal roundsplayed;
	
}
