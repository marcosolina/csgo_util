package com.marco.ixigo.demmanager.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundParserUtils {
    /*
     * DEM PARSER CSV COLUMNS
     */
    public static final int DEM_COL_USER_NAME = 0;
    public static final int DEM_COL_STEAM_ID = 1;
    public static final int DEM_COL_ROUND_WIN_SHARE = 2;
    public static final int DEM_COL_KILLS = 3;
    public static final int DEM_COL_ASSISTS = 4;
    public static final int DEM_COL_DEATHS = 5;
    public static final int DEM_COL_KILL_DEATH_RATION = 6;
    public static final int DEM_COL_HEAD_SHOTS = 7;
    public static final int DEM_COL_HEAD_SHOTS_PERC = 8;
    public static final int DEM_COL_TEAM_KILL_FRIENDLY_FIRE = 9;
    public static final int DEM_COL_ENTRY_KILL = 10;
    public static final int DEM_COL_BOMB_PLANTED = 11;
    public static final int DEM_COL_BOMB_DEFUSED = 12;
    public static final int DEM_COL_MOST_VALUABLE_PLAYER = 13;
    public static final int DEM_COL_SCORE = 14;
    public static final int DEM_COL_HALF_LIVE_TELEVISION_RATING = 15;
    public static final int DEM_COL_FIVE_KILLS = 16;
    public static final int DEM_COL_FOUR_KILLS = 17;
    public static final int DEM_COL_THREE_KILLS = 18;
    public static final int DEM_COL_TWO_KILLS = 19;
    public static final int DEM_COL_ONE_KILL = 20;
    public static final int DEM_COL_TRADE_KILL = 21;
    public static final int DEM_COL_TRADE_DEATH = 22;
    public static final int DEM_COL_KILL_PER_ROUND = 23;
    public static final int DEM_COL_ASSIST_PER_ROUND = 24;
    public static final int DEM_COL_DEATH_PER_ROUND = 25;
    public static final int DEM_COL_AVERAGE_DAMAGE_PER_ROUND = 26;
    public static final int DEM_COL_TOTAL_DAMAGE_HEALTH = 27;
    public static final int DEM_COL_TOTAL_DAMAGE_ARMOR = 28;
    public static final int DEM_COL_ONE_VERSUS_ONE = 29;
    public static final int DEM_COL_ONE_VERSUS_TWO = 30;
    public static final int DEM_COL_ONE_VERSUS_THREE = 31;
    public static final int DEM_COL_ONE_VERSUS_FOUR = 32;
    public static final int DEM_COL_ONE_VERSUS_FIVE = 33;
    public static final int DEM_COL_GRENADES_THROWN_COUNT = 34;
    public static final int DEM_COL_FLASHES_THROWN_COUNT = 35;
    public static final int DEM_COL_SMOKES_THROWN_COUNT = 36;
    public static final int DEM_COL_FIRE_THROWN_COUNT = 37;
    public static final int DEM_COL_HIGH_EXPLOSIVE_DAMAGE = 38;
    public static final int DEM_COL_FIRE_DAMAGE = 39;
    public static final int DEM_COL_MATCH_PLAYED = 40;

    public static BigDecimal doubleToBigDecimal(Double d, int decimals) {
        return BigDecimal.valueOf(d).setScale(decimals, RoundingMode.DOWN);
    }

    public static Double bigDecimalToDouble(BigDecimal d, int decimals) {
        return d.setScale(decimals, RoundingMode.DOWN).doubleValue();
    }
}
