package com.ixigo.discordbot.handlers.event;

import java.awt.Color;
import java.time.DayOfWeek;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.discordbot.commands.events.EventReceivedCmd;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.enums.BotConfigKey;
import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.library.utils.DateUtils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import reactor.core.publisher.Mono;

@Component
public class EventHandler implements WebCommandHandler<EventReceivedCmd, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(EventHandler.class);
	@Autowired
	private IxigoBot botService;

	@Override
	public Mono<ResponseEntity<Void>> handle(EventReceivedCmd cmd) {
		_LOGGER.trace("Inside EventHandler.handle");

		_LOGGER.info(String.format("Received event: %s", cmd.getEventReceived().getEventType().getDesc()));
		
		if(cmd.getEventReceived().getEventType() == EventType.AZ_START_CSGO && DateUtils.getCurrentUtcDate().getDayOfWeek() == DayOfWeek.MONDAY) {
			
			new Thread(() -> {
				StringBuilder msg = new StringBuilder();
				msg.append("Hi there!!!\n\n");
				//msg.append("[Click here to join the server](https://marco.selfip.net/ixigoui/joinus?joinIxigo=true)");
				msg.append("The server is ready: ixigo.selfip.net");
				
				MessageEmbed me = new EmbedBuilder()
						.setTitle("Automatic message")
						.setDescription(msg)
						.setThumbnail("https://marco.selfip.net/ixigoui/jointheserver/ixigo-logo.png")
						.setColor(new Color(42, 255, 137))
						.addField(new MessageEmbed.Field("", "Balance bot is back!!!", false))
						.build();
				botService.sendEmbedMessageToGeneralChat(me);
			}).start();
			return Mono.just(new ResponseEntity<Void>(HttpStatus.OK));
		}
		
		var mono = botService.getBotConfig(BotConfigKey.AUTOBALANCE).map(config -> {
			if (!Boolean.parseBoolean(config.getConfigVal())) {
				return new ResponseEntity<Void>(HttpStatus.OK);
			}

			Runnable r = null;
			switch (cmd.getEventReceived().getEventType()) {
			case CS_WIN_PANEL_MATCH:
				r = () -> botService.moveAllMembersIntoGeneralChannel();
				break;
			case DEM_FILES_PROCESSED:
				r = () -> botService.balanceMembersInVoiceChannel().thenReturn(true);
				break;
				/*
			case WARMUP_START:
				r = () -> botService.warmUpBalanceTeamApi().subscribe(status -> _LOGGER.debug("Warmup status " + status.toString()));
				break;
			case WARMUP_END:
				r = () -> {
					// @formatter:off
					botService.kickTheBots()
						.then(botService.balanceTheTeams())
						.flatMap(status -> {
							botService.moveDiscordUsersInTheAppropriateChannel();
							return botService.restartCsgoMatch();
						})
						.subscribe(status -> _LOGGER.debug("Teams balanced"))
					;
					// @formatter:on
				};
				break;
				*/
			case WARMUP_END:
				r = () -> {
					// @formatter:off
					botService.kickTheBotsCs2()
						.then(botService.balanceTheTeamsCs2())
						.flatMap(status -> botService.moveDiscordUsersInTheAppropriateChannelCs2())
						.flatMap(staus -> {
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								_LOGGER.error(e.getMessage());
							}
							return botService.restartCs2Match();
						})
						.subscribe(status -> _LOGGER.debug("CS2 Teams balanced"))
					;
					// @formatter:on
				};
				break;
			default:
				break;
			}

			if (r != null) {
				new Thread(r).start();
			}

			return new ResponseEntity<Void>(HttpStatus.OK);
		});

		return mono;
	}

}
