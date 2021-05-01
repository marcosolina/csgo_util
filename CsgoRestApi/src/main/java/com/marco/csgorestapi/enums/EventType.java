package com.marco.csgorestapi.enums;

public enum EventType {
    // @formatter:off
    ROUND_END("Round End"),
    START_VOTE("Start Vote"),
    ROUND_START("Round Started"),
    CS_WIN_PANEL_MATCH("End Map"),
    // @formatter:on

    ;

    private final String desc;

    EventType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public static EventType fromString(String desc) {
        for (EventType i : EventType.values()) {
            if (i.desc.equals(desc)) {
                return i;
            }
        }
        return null;
    }
}
