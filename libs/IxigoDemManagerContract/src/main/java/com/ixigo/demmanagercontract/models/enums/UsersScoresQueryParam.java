package com.ixigo.demmanagercontract.models.enums;

public enum UsersScoresQueryParam {
	NUMBER_OF_MATCHES("numberOfMatches"),
	USERS_STEAM_IDS("usersIDs"),
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
