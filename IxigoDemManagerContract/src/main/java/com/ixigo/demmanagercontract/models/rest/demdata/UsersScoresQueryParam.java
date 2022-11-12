package com.ixigo.demmanagercontract.models.rest.demdata;

public enum UsersScoresQueryParam {
	NUMBER_OF_MATCHES("numberOfMatches"),
	USERS_STEAM_IDS("usersIDs"),
	MINIMUM_PERCE_MATCH_PLAYED("minPercPlayed"),
    ;
    // @formatter:on

    private final String queryParamKey;

    UsersScoresQueryParam(String queryParamKey) {
        this.queryParamKey = queryParamKey;
    }

    public String getQueryParamKey() {
        return this.queryParamKey;
    }

    public static UsersScoresQueryParam fromDesc(String desc) {
        for (UsersScoresQueryParam i : UsersScoresQueryParam.values()) {
            if (i.queryParamKey.equals(desc)) {
                return i;
            }
        }
        return null;
    }
}
