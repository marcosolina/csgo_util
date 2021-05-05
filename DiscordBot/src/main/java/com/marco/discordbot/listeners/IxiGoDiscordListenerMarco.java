package com.marco.discordbot.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class IxiGoDiscordListenerMarco extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // @formatter:off
        //These are provided with every event in JDA
        JDA jda = event.getJDA();                       //JDA, the core of the api.
        long responseNumber = event.getResponseNumber();//The amount of discord events that JDA has received since the last reconnect.

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
            if (msg.equals("!ping")) {
                channel.sendMessage("pong!").queue(); 
            }
        }
        
        // @formatter:on
    }

}
