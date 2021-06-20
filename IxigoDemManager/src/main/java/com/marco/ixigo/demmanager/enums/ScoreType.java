package com.marco.ixigo.demmanager.enums;

public enum ScoreType {
    // @formatter:off
    /**
     * Round Win Share
     */
    RWS("Round Win Share"),
    /**
     * Kills
     */
    KILLS("Kills"),
    /**
     * Assists
     */
    ASSISTS("Assists"),
    /**
     * Deaths
     */
    DEATHS("Deaths"),
    /**
     * Kill Death Ratio
     */
    KDR("Kill Death Ratio"),
    /**
     * Head Shots
     */
    HS("Head Shots"),
    /**
     * Head Shots Percentage
     */
    HSP("Head Shots Percentage"),
    /**
     * Team Kill Friendly Fire
     */
    FF("Team Kill Friendly Fire"),
    /**
     * Entry Kill
     */
    EK("Entry Kill"),
    /**
     * Bomb Planted
     */
    BP("Bomb Planted"),
    /**
     * Bomb Defused
     */
    BD("Bomb Defused"),
    /**
     * Most Valuable Player
     */
    MVP("Most Valuable Player"),
    /**
     * Score
     */
    SCORE("Score"),
    /**
     * Half Life Television Rating
     */
    HLTV("Half Life Television Rating"),
    /**
     * Five Kills
     */
    _5K("Five Kills"),
    /**
     * Four Kills
     */
    _4K("Four Kills"),
    /**
     * Three Kills
     */
    _3K("Three Kills"),
    /**
     * Two Kills
     */
    _2K("Two Kills"),
    /**
     * One Kill
     */
    _1K("One Kill"),
    /**
     * Trade Kill
     */
    TK("Trade Kill"),
    /**
     * Trade Death
     */
    TD("Trade Death"),
    /**
     * Kill Per Round
     */
    KPR("Kill Per Round"),
    /**
     * Assists Per Round
     */
    APR("Assists Per Round"),
    /**
     * Death Per Round
     */
    DPR("Death Per Round"),
    /**
     * Average Damage Per Round
     */
    ADR("Average Damage Per Round"),
    /**
     * Total Damage Health
     */
    TDH("Total Damage Health"),
    /**
     * Total Damage Armor
     */
    TDA("Total Damage Armor"),
    /**
     * One Versus One
     */
    _1V1("One Versus One"),
    /**
     * One Versus Two
     */
    _1V2("One Versus Two"),
    /**
     * One Versus Three
     */
    _1V3("One Versus Three"),
    /**
     * One Versu Four
     */
    _1V4("One Versus Four"),
    /**
     * One Versus Five
     */
    _1V5("One Versus Five"),
    /**
     * Grenades Thrown Count
     */
    GRENADES("Grenades Thrown Count"),
    /**
     * Flashes Thrown Count
     */
    FLASHES("Flashes Thrown Count"),
    /**
     * Smoke Thrown Count
     */
    SMOKES("Smokes Thrown Count"),
    /**
     * Fire Thrown Count
     */
    FIRE("Fire Thrown Count"),
    /**
     * High Explosive Damage
     */
    HED("High Explosive Damage"),
    /**
     * Fire Damage
     */
    FD("Fire Damage"),
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
