package com.ixigo.discordbot.listeners;

import com.ixigo.discordbot.enums.DiscordChatCommands;
import com.ixigo.discordbot.services.interfaces.IxigoBot;

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
			ixigoBot.setAutoBalance(true);
			channel.sendMessage("Done").queue();
			break;
		case AUTO_BALANCE_OFF:
			ixigoBot.setAutoBalance(false);
			channel.sendMessage("Done").queue();
			break;
		case AUTO_BALANCE_SATUS:
			ixigoBot.isAutobalance().subscribe(status -> channel.sendMessage(status ? "On" : "Off").queue());
			break;
		default:
			break;
		}
	}

	private StringBuilder listOfCommandsMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Hi!\n");
		sb.append("Here a list of what you can send me:");
		sb.append("\n- !ping -> Check if I am responsive");
		sb.append("\n- !balance -> Generate the CSGO teams and move the players (in the game)");
		sb.append("\n- !together -> Move everybody in the general voice channel");
		sb.append("\n- !moveToChannel -> Move everybody in the appropirat voice channel");
		sb.append("\n- !autoBalanceOn -> I will monitor the game, balance the teams and move the users in the appropriate voice channels");
		sb.append("\n- !autoBalanceOff -> Turn off the auto balance");
		sb.append("\n- !autoBalanceStatus -> It tells you if the auto balance is On or Off");
		sb.append("\n- !help -> Print this message again");

		return sb;
	}
}
