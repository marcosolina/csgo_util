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
	private String lastRoundTeam = "";
	private String steamId = "";
	private String userName = "";
	private LocalDateTime matchDate = null;
	/**
	 * Percentage of rounds where a player did something (get a Kill, Assist, Survived or Traded)
	 */
	private BigDecimal kast = BigDecimal.ZERO;
	private BigDecimal kills = BigDecimal.ZERO;
	private BigDecimal teamKillFriendlyFire = BigDecimal.ZERO;
	private BigDecimal halfLifeTelevisionRating = BigDecimal.ZERO;
	private BigDecimal bombDefused = BigDecimal.ZERO;
	private BigDecimal hostageRescued = BigDecimal.ZERO;
	private BigDecimal bombPlanted = BigDecimal.ZERO;
	private BigDecimal fireDamage = BigDecimal.ZERO;
	private BigDecimal roundWinShare = BigDecimal.ZERO;
	private BigDecimal headShots = BigDecimal.ZERO;
	private BigDecimal assists = BigDecimal.ZERO;
	private BigDecimal headShotsPercentage = BigDecimal.ZERO;
	private BigDecimal deaths = BigDecimal.ZERO;
	private BigDecimal teammateDamageHealth = BigDecimal.ZERO;
	private BigDecimal entryKill = BigDecimal.ZERO;
	private BigDecimal deathPerRound = BigDecimal.ZERO;
	private BigDecimal roundWinShareTotal = BigDecimal.ZERO;
	private BigDecimal killPerRound = BigDecimal.ZERO;
	private BigDecimal averageDamagePerRound = BigDecimal.ZERO;
	private BigDecimal tradeDeath = BigDecimal.ZERO;
	private BigDecimal totalDamageArmor = BigDecimal.ZERO;
	private BigDecimal opponentBlindTime = BigDecimal.ZERO;
	private BigDecimal tradeKill = BigDecimal.ZERO;
	private BigDecimal killDeathRatio = BigDecimal.ZERO;
	private BigDecimal totalDamageHealth = BigDecimal.ZERO;
	private BigDecimal teammateBlindTime = BigDecimal.ZERO;
	private BigDecimal flashAssists = BigDecimal.ZERO;
	private Long oneVersusFive = 0L;
	private Long oneVersusThree = 0L;
	private Long oneVersusTwo = 0L;
	private Long oneVersusOne = 0L;
	private Long kasttotal = 0L;
	private Long score = 0L;
	private Long fourKills = 0L;
	private Long twoKills = 0L;
	private Long oneVersusFour = 0L;
	private Long roundsOnTeam2 = 0L;
	private Long roundsOnTeam1 = 0L;
	private Long roundsplayed = 0L;
	private Long mostValuablePlayer = 0L;
	private Long matchId = 0L;
	private Long fiveKills = 0L;
	private Long threeKills = 0L;
	private Long oneKill = 0L;
}
