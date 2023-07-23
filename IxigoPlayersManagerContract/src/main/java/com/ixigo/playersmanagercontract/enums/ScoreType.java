package com.ixigo.playersmanagercontract.enums;

public enum ScoreType {
	// @formatter:off
    /**
     * Kills
     */
    KILLS("Kills"),
    /**
     * Team kill friendly fire
     */
    FF("Team kill friendly fire"),
    /**
     * Half life television rating
     */
    HLTV("Half life television rating"),
    /**
     * Bomb defuse
     */
    BD("Bomb defuse"),
    /**
     * One versus three
     */
    _1V3("One versus three"),
    /**
     * One versus two
     */
    _1V2("One versus two"),
    /**
     * Kill, Assist, Survived or Traded
     */
    KAST("Kill, Assist, Survived or Traded"),
    /**
     * One versus one
     */
    _1V1("One versus one"),
    /**
     * Hostage rescued
     */
    HR("Hostage rescued"),
    /**
     * Bomb planted
     */
    BP("Bomb planted"),
    /**
     * Fire damage
     */
    UD("Fire damage"),
    /**
     * Kast total
     */
    KAST_TOTAL("Kast total"),
    /**
     * Round win share
     */
    RWS("Round win share"),
    /**
     * Score
     */
    SCORE("Score"),
    /**
     * Head shots
     */
    HEAD_SHOTS("Head shots"),
    /**
     * Assists
     */
    ASSISTS("Assists"),
    /**
     * Four kills
     */
    _4K("Four kills"),
    /**
     * Two kills
     */
    _2K("Two kills"),
    /**
     * One versus five
     */
    _1V5("One versus five"),
    /**
     * One versus four
     */
    _1V4("One versus four"),
    /**
     * Headshot percentage
     */
    HEADSHOT_PERCENTAGE("Headshot percentage"),
    /**
     * Deaths
     */
    DEATHS("Deaths"),
    
    /**
     * Teammate damage health
     */
    FFD("Teammate damage health"),
    /**
     * Entry kill
     */
    EK("Entry kill"),
    /**
     * Most valuable player
     */
    MVP("Most valuable player"),
    /**
     * Death per round
     */
    DPR("Death per round"),
    /**
     * Round win share total
     */
    RWS_TOTAL("Round win share total"),
    /**
     * Kill per round
     */
    KPR("Kill per round"),
    /**
     * Average damage per round
     */
    ADR("Average damage per round"),
    /**
     * Trade death
     */
    TD("Trade death"),
    /**
     * Total damage armor
     */
    TDA("Total damage armor"),
    /**
     * Five kills
     */
    _5K("Five kills"),
    /**
     * Three kills
     */
    _3K("Three kills"),
    /**
     * Opponent blind time
     */
    EBT("Opponent blind time"),
    /**
     * Trade kill
     */
    TK("Trade kill"),
    /**
     * Kill death ratio
     */
    _KDR("Kill death ratio"),
    /**
     * One kill
     */
    _1K("One kill"),
    /**
     * Total damage health
     */
    TDH("Total damage health"),
    /**
     * Teammate blind time
     */
    FBT("Teammate blind time"),
    /**
     * Flash assists
     */
    FA("Flash assists"),
    ;
    // @formatter:on

    private final String desc;

    ScoreType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public static ScoreType fromDesc(String desc) {
        for (ScoreType i : ScoreType.values()) {
            if (i.desc.equals(desc)) {
                return i;
            }
        }
        return null;
    }
}
