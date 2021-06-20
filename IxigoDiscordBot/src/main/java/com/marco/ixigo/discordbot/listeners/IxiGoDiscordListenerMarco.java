package com.marco.ixigo.discordbot.listeners;

import com.marco.ixigo.discordbot.services.interfaces.IxiGoBot;
import com.marco.utils.MarcoException;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class IxiGoDiscordListenerMarco extends ListenerAdapter {

    private IxiGoBot ixiGoBot;

    public IxiGoDiscordListenerMarco(IxiGoBot ixiGoBot) {
        this.ixiGoBot = ixiGoBot;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // @formatter:off
        //These are provided with every event in JDA
        //JDA jda = event.getJDA();                       //JDA, the core of the api.
        //long responseNumber = event.getResponseNumber();//The amount of discord events that JDA has received since the last reconnect.

        //Event specific information
        User author = event.getAuthor();                //The user that sent the message
        Message message = event.getMessage();           //The message that was received.
        MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.
                                                        //  This could be a TextChannel, PrivateChannel, or Group!

        String msg = message.getContentDisplay();              //This returns a human readable version of the Message. Similar to
                                                        // what you would see in the client.

        boolean bot = author.isBot();                    //This boolean is useful to determine if the User that
                                                        // sent the Message is a BOT or not!
        
        if(!bot) {
            switch(msg.trim()) {
            case "!ping":
                channel.sendMessage("pong!").queue();
                break;
            case "!balance":
                runInSeparateThread(() -> {
                    try {
                        channel.sendMessage("Let me see...").queue();
                        if(this.ixiGoBot.balanceTheTeams()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Done\n");
                            
                            channel.sendMessage(sb.toString()).queue();
                        }else {
                            channel.sendMessage("Sorry, I was not able to do it. There are no players on the server").queue();
                        }
                    } catch (MarcoException e) {
                        channel.sendMessage(String.format("Mmmm... %s", e.getMessage())).queue();
                    }
                });
                break;
            case "!together":
                runInSeparateThread(() -> {
                    try {
                        this.ixiGoBot.moveAllMembersIntoGeneralChannel();
                    } catch (MarcoException e) {
                        channel.sendMessage(String.format("Mmmm... %s", e.getMessage())).queue();
                    }
                });
                break;
            case "!moveToChannel":
                runInSeparateThread(() -> {
                    try {
                        if(this.ixiGoBot.moveDiscordUsersInTheAppropirateChannel()) {
                            channel.sendMessage("Done").queue();
                        }else {
                            channel.sendMessage("Sorry, I cannot do that").queue();
                        }
                    } catch (MarcoException e) {
                        channel.sendMessage(String.format("Mmmm... %s", e.getMessage())).queue();
                    }
                });
                break;
            case "!help":
                channel.sendMessage(listOfCommandsMessage().toString()).queue();
                break;
            case "!autoBalanceOn":
                this.ixiGoBot.setAutoBalance(true);
                channel.sendMessage("Done").queue();
                break;
            case "!autoBalanceOff":
                this.ixiGoBot.setAutoBalance(false);
                channel.sendMessage("Done").queue();
                break;
            case "!autoBalanceStatus":
                String tmpMessage = this.ixiGoBot.isAutobalance() ? "Autobalance is On" : "Autobalance is Off";
                channel.sendMessage(tmpMessage).queue();
                break;
            default:
                break;
            }
        }
        
        // @formatter:on
    }

    private void runInSeparateThread(Runnable r) {
        new Thread(r).start();
    }

    /*
     * @Override public void onReady(ReadyEvent event) { super.onReady(event); JDA
     * jda = event.getJDA(); TextChannel txtc =
     * jda.getTextChannelById(this.dsProps.getTextChannels().getGeneral()); if(txtc
     * != null) { txtc.sendMessage(listOfCommandsMessage().toString()).queue(); } }
     * 
     */

    private StringBuilder listOfCommandsMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hi!\n");
        sb.append("Here a list of what you can send me:");
        sb.append("\n- !ping -> Check if I am responsive");
        sb.append("\n- !balance -> Generate the CSGO teams and move the players (in the game)");
        sb.append("\n- !together -> Move everybody in the general voice channel");
        sb.append("\n- !moveToChannel -> Move everybody in the appropirat voice channel");
        sb.append(
                "\n- !autoBalanceOn -> I will monitor the game, balance the teams and move the users in the appropriate voice channels");
        sb.append("\n- !autoBalanceOff -> Turn off the auto balance");
        sb.append("\n- !autoBalanceStatus -> It tells you if the auto balance is On or Off");
        sb.append("\n- !help -> Print this message again");

        return sb;
    }

}
