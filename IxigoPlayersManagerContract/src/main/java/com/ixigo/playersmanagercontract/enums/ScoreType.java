package com.ixigo.playersmanagercontract.enums;

public enum ScoreType {
	// @formatter:off
	kills("Total Kills"),
	deaths("Total Deaths"),
	assists("Total Assists"),
	score("Score"),
	rws("Round Win Share"),
	rws_total("Round Win Share total"),
	headshots("Headshots"),
	headshot_percentage("Headshot Percentage"),
	mvp("Most Valuable Player"),
	hltv_rating("HLTV Rating"),
	adr("Average Damage per Round"),
	kpr("Kills Per Round"),
	dpr("Deaths Per Round"),
	kast("KAST"),
	kast_total("KAST Total"),
	kdr("Kill/Death Ratio"),
	hr("Hostages Rescued"),
	bp("Bomb Planted"),
	ud("Utility Damage"),
	ffd("Friendly Fire Damage"),
	ffk("Friendly Fire kills"),
	td("Trade Deaths"),
	bd("Bomb defused"),
	tda("Total Damage Armour"),
	tdh("Total Damage Health"),
	fa("Flash Assists"),
	ebt("Enemy Blind Time"),
	fbt("Friendly Blind Time"),
	ek("Entry Kills"),
	tk("Trade Kills"),
	_1k("One Kill"),
	_2k("Two Kills"),
	_3k("Three Kills"),
	_4k("Four Kills"),
	_5k("Five Kills"),
	_1v1("1v1 Clutches"),
	_1v2("1v2 Clutches"),
	_1v3("1v3 Clutches"),
	_1v4("1v4 Clutches"),
	_1v5("1v5 Clutches"),
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
