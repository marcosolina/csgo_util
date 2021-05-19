package com.marco.ixigoserverhelper.enums;

/**
 * Types of events that the CSGO server can dispatch
 * 
 * @author Marco
 *
 */
public enum EventType {
    // @formatter:off
    ROUND_END,
    ROUND_START,
    CS_WIN_PANEL_MATCH,
    WARMUP_START,
    WARMUP_END,
    SHUT_DOWN(),
    // @formatter:on
    ;

}
