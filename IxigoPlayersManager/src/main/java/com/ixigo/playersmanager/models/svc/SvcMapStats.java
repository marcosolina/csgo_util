package com.ixigo.playersmanager.models.svc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcMapStats implements Serializable{
	private static final long serialVersionUID = 1L;
	private BigDecimal kills = BigDecimal.ZERO;
	private BigDecimal teamKillFriendlyFire = BigDecimal.ZERO;
	private BigDecimal halfLifeTelevisionRating = BigDecimal.ZERO;
	private BigDecimal bombDefused = BigDecimal.ZERO;
	private Long oneVersusThree;
	private Long oneVersusTwo;
	/**
	 * Percentage of rounds where a player did something (get a Kill, Assist, Survived or Traded)
	 */
	private BigDecimal kast = BigDecimal.ZERO;
	private Long oneVersusOne;
	private BigDecimal hostageRescued = BigDecimal.ZERO;
	private LocalDateTime matchDate = null;
	private BigDecimal bombPlanted = BigDecimal.ZERO;
	private BigDecimal fireDamage = BigDecimal.ZERO;
	private Long kasttotal = 0L;
	private BigDecimal roundWinShare = BigDecimal.ZERO;
	private Long score;
	private BigDecimal headShots = BigDecimal.ZERO;
	private BigDecimal assists = BigDecimal.ZERO;
	private Long fourKills;
	private String lastRoundTeam = "";
	private Long twoKills;
	private String userName = "";
	private Long oneVersusFive;
	private Long oneVersusFour;
	private BigDecimal headShotsPercentage = BigDecimal.ZERO;
	private BigDecimal deaths = BigDecimal.ZERO;
	private Long roundsOnTeam2 = 0L;
	private Long roundsOnTeam1 = 0L;
	private BigDecimal teammateDamageHealth = BigDecimal.ZERO;
	private Long roundsplayed = 0L;
	private BigDecimal entryKill = BigDecimal.ZERO;
	private Long mostValuablePlayer;
	private Long matchId = 0L;
	private BigDecimal deathPerRound = BigDecimal.ZERO;
	private BigDecimal roundWindShareTotal = BigDecimal.ZERO;
	private BigDecimal killPerRound = BigDecimal.ZERO;
	private BigDecimal averageDamagePerRound = BigDecimal.ZERO;
	private String steamId = "";
	private BigDecimal tradeDeath = BigDecimal.ZERO;
	private BigDecimal totalDamageArmor = BigDecimal.ZERO;
	private Long fiveKills;
	private Long threeKills;
	private BigDecimal opponentBlindTime = BigDecimal.ZERO;
	private BigDecimal tradeKill = BigDecimal.ZERO;
	private BigDecimal killDeathRatio = BigDecimal.ZERO;
	private Long oneKill;
	private BigDecimal totalDamageHealth = BigDecimal.ZERO;
	private BigDecimal teammateBlindTime = BigDecimal.ZERO;
	private BigDecimal flashAssists = BigDecimal.ZERO;
}
