package com.ixigo.discordbot.enums;

public enum DiscordChatCommands {
	// @formatter:off
    PING("!ping"),
    BALANCE("!balance"),
    TOGETHER("!together"),
    MOVE_TO_CHANNEL("!moveToChannel"),
    HELP("!help"),
    AUTO_BALANCE_ON("!autoBalanceOn"),
    AUTO_BALANCE_OFF("!autoBalanceOff"),
    AUTO_BALANCE_SATUS("!autoBalanceStatus"),
    // @formatter:on
    ;
    private final String desc;

    DiscordChatCommands(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public static DiscordChatCommands fromString(String desc) {
        for (DiscordChatCommands i : DiscordChatCommands.values()) {
            if (i.desc.equals(desc)) {
                return i;
            }
        }
        return null;
    }
}
