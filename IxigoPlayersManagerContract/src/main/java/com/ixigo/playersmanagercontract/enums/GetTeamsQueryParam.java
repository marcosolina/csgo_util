package com.ixigo.playersmanagercontract.enums;

public enum GetTeamsQueryParam {
	NUMBER_OF_MATCHES("numberOfMatches"),
	USERS_STEAM_IDS("steamIDs"),
	MINIMUM_PERC_MATCH_PLAYED("minPercPlayed"),
	PENALTY_WEIGHT("penaltyWeigth"),
	PARTITION_SCORE_TYPE("partitionScore"),
    ;
    // @formatter:on

    private final String queryParamKey;

    GetTeamsQueryParam(String queryParamKey) {
        this.queryParamKey = queryParamKey;
    }

    public String getQueryParamKey() {
        return this.queryParamKey;
    }

    public static GetTeamsQueryParam fromDesc(String desc) {
        for (GetTeamsQueryParam i : GetTeamsQueryParam.values()) {
            if (i.queryParamKey.equals(desc)) {
                return i;
            }
        }
        return null;
    }
}
