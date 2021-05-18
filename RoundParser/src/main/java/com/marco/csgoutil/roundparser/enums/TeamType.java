package com.marco.csgoutil.roundparser.enums;

public enum TeamType {
    // @formatter:off
    TERRORISTS("TERRORISTS"),
    CT("CT")
    // @formatter:on
    ;
    private final String desc;

    TeamType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public static TeamType fromString(String desc) {
        for (TeamType i : TeamType.values()) {
            if (i.desc.equals(desc)) {
                return i;
            }
        }
        return null;
    }
}
