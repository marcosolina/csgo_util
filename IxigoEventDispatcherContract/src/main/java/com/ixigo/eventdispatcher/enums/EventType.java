package com.ixigo.eventdispatcher.enums;

/**
 * These are the events that the CSGO server can send me
 * 
 * @author Marco
 *
 */
public enum EventType {
    // @formatter:off
    ROUND_END("round_end"),
    ROUND_START("round_start"),
    CS_WIN_PANEL_MATCH("cs_win_panel_match"),
    WARMUP_START("round_announce_warmup"),
    WARMUP_END("xxx"),
    AZ_START_DEPLOY_VM("start_deploy_vm"),
    AZ_START_CONFIGURING_VM("start_configuring_vm"),
    AZ_START_INSTALLING_CSGO("start_installing_csgo"),
    AZ_START_CSGO("start_csgo"),
    AZ_START_DELETE_RESOURCE("start_delete_resources"),
    DEM_FILES_PROCESSED("dem_files_processed"),
    SHUT_DOWN("shutdown"),
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
