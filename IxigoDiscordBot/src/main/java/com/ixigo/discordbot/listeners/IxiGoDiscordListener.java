package com.ixigo.discordbot.listeners;

import com.ixigo.discordbot.enums.DiscordChatCommands;
import com.ixigo.discordbot.models.svc.discord.SvcBotConfig;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.enums.BotConfigKey;
import com.ixigo.enums.BotConfigValueType;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class IxiGoDiscordListener extends ListenerAdapter {
	private IxigoBot ixigoBot;

	public IxiGoDiscordListener(IxigoBot ixigoBot) {
		this.ixigoBot = ixigoBot;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		// @formatter:off
		//Event specific information
        User author = event.getAuthor(); //The user that sent the message
        boolean bot = author.isBot();    //If the message was sent by a human or not
        
        if(bot) {
        	return;
        }
        
        Message message = event.getMessage();           //The message that was received.
        MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.
    													//This could be a TextChannel, PrivateChannel, or Group!
        
        String msg = message.getContentDisplay().trim();//This returns a human readable version of the Message. Similar to
        												//what you would see in the client.
        // @formatter:on
		try {
			manageTheMessage(channel, msg);
		} catch (Exception e) {
			channel.sendMessage(String.format("Mmmm... %s", e.getMessage())).queue();
		}

	}

	private void manageTheMessage(MessageChannel channel, String msg) {
		switch (DiscordChatCommands.fromString(msg)) {
		case PING:
			channel.sendMessage("pong!").queue();
			break;
		case BALANCE:
			// @formatter:off
			channel.sendMessage("On it").queue();
			ixigoBot.balanceTheTeams()
				.onErrorContinue((e, o) -> {
					channel.sendMessage(e.getMessage()).queue();
				}).subscribe(status -> {
					channel.sendMessage("Done").queue();
				});
			// @formatter:on
			break;
		case TOGETHER:
			ixigoBot.moveAllMembersIntoGeneralChannel();
			channel.sendMessage("On it").queue();
			break;
		case MOVE_TO_CHANNEL:
			ixigoBot.moveDiscordUsersInTheAppropriateChannel();
			channel.sendMessage("On it").queue();
			break;
		case HELP:
			channel.sendMessage(listOfCommandsMessage().toString()).queue();
			break;
		case AUTO_BALANCE_ON:
			ixigoBot.updateBotConfig(new SvcBotConfig(BotConfigKey.AUTOBALANCE, Boolean.TRUE.toString(), BotConfigValueType.BOOLEAN))
				.subscribe(b -> channel.sendMessage(b ? "Done" : "Sorry, I was not able to do that").queue())
				;
			break;
		case AUTO_BALANCE_OFF:
			ixigoBot.updateBotConfig(new SvcBotConfig(BotConfigKey.AUTOBALANCE, Boolean.FALSE.toString(), BotConfigValueType.BOOLEAN))
				.subscribe(b -> channel.sendMessage(b ? "Done" : "Sorry, I was not able to do that").queue())
				;
			break;
		case AUTO_BALANCE_SATUS:
			ixigoBot.getBotConfig(BotConfigKey.AUTOBALANCE)
				.subscribe(config -> {
					var status = Boolean.parseBoolean(config.getConfigVal());
					channel.sendMessage(status ? "On" : "Off").queue();
				});
			break;
		default:
			break;
		}
	}

	private StringBuilder listOfCommandsMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Hi!\n");
		sb.append("Here a list of what you can send me:");
		sb.append(String.format("%s- %s -> Check if I am responsive", System.lineSeparator(), DiscordChatCommands.PING.getDesc()));
		sb.append(String.format("%s- %s -> Generate the CSGO teams and move the players (in the game)", System.lineSeparator(), DiscordChatCommands.BALANCE.getDesc()));
		sb.append(String.format("%s- %s -> Move everybody in the general voice channel", System.lineSeparator(), DiscordChatCommands.TOGETHER.getDesc()));
		sb.append(String.format("%s- %s -> Move everybody in the appropirat voice channel", System.lineSeparator(), DiscordChatCommands.MOVE_TO_CHANNEL.getDesc()));
		sb.append(String.format("%s- %s -> I will monitor the game, balance the teams and move the users in the appropriate voice channels", System.lineSeparator(), DiscordChatCommands.AUTO_BALANCE_ON.getDesc()));
		sb.append(String.format("%s- %s -> Turn off the auto balance", System.lineSeparator(), DiscordChatCommands.AUTO_BALANCE_OFF.getDesc()));
		sb.append(String.format("%s- %s -> It tells you if the auto balance is On or Off", System.lineSeparator(), DiscordChatCommands.AUTO_BALANCE_SATUS.getDesc()));
		sb.append(String.format("%s- %s -> Print this message again", System.lineSeparator(), DiscordChatCommands.HELP.getDesc()));
		return sb;
	}
}
