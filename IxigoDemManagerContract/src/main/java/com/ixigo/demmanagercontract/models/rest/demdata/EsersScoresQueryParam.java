package com.ixigo.demmanagercontract.models.rest.demdata;

public enum EsersScoresQueryParam {
	NUMBER_OF_MATCHES("numberOfMatches"),
	USERS_STEAM_IDS("usersIDs"),
	MINIMUM_PERCE_MATCH_PLAYED("minPercPlayed"),
    ;
    // @formatter:on

    private final String queryParamKey;

    EsersScoresQueryParam(String queryParamKey) {
        this.queryParamKey = queryParamKey;
    }

    public String getQueryParamKey() {
        return this.queryParamKey;
    }

    public static EsersScoresQueryParam fromDesc(String desc) {
        for (EsersScoresQueryParam i : EsersScoresQueryParam.values()) {
            if (i.queryParamKey.equals(desc)) {
                return i;
            }
        }
        return null;
    }
}
