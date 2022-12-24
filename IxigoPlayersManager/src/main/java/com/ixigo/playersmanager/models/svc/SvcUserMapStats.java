package com.ixigo.playersmanager.models.svc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SvcUserMapStats {
	private String userName;
    private String steamID;
    private Double roundWinShare;
    private Long kills;
    private Long assists;
    private Long deaths;
    private Double killDeathRation;
    private Long headShots;
    private Double headShotsPercentage;
    private Long teamKillFriendlyFire;
    private Long entryKill;
    private Long bombPLanted;
    private Long bombDefused;
    private Long mostValuablePlayer;
    private Long score;
    private Double halfLifeTelevisionRating;
    private Long fiveKills;
    private Long fourKills;
    private Long threeKills;
    private Long twoKills;
    private Long oneKill;
    private Long tradeKill;
    private Long tradeDeath;
    private Double killPerRound;
    private Double assistsPerRound;
    private Double deathPerRound;
    private Double averageDamagePerRound;
    private Long totalDamageHealth;
    private Long totalDamageArmor;
    private Long oneVersusOne;
    private Long oneVersusTwo;
    private Long oneVersusThree;
    private Long oneVersusFour;
    private Long oneVersusFive;
    private Long grenadesThrownCount;
    private Long flashesThrownCount;
    private Long smokesThrownCount;
    private Long fireThrownCount;
    private Long highExplosiveDamage;
    private Long fireDamage;
    private Double matchPlayed;
}
