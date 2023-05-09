package com.ixigo.discordbot.handlers.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.discordbot.commands.events.EventReceivedCmd;
import com.ixigo.discordbot.config.properties.CsgoServerProps;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.enums.BotConfigKey;
import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class EventHandler implements WebCommandHandler<EventReceivedCmd, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(EventHandler.class);
	@Autowired
	private IxigoBot botService;
	@Autowired
	private CsgoServerProps csgoServerProps;

	@Override
	public Mono<ResponseEntity<Void>> handle(EventReceivedCmd cmd) {
		_LOGGER.trace("Inside EventHandler.handle");

		_LOGGER.info(String.format("Received event: %s", cmd.getEventReceived().getEventType().getDesc()));
		
		if(cmd.getEventReceived().getEventType() == EventType.AZ_START_CSGO) {// && DateUtils.getCurrentUtcDate().getDayOfWeek() == DayOfWeek.MONDAY) {
			
			new Thread(() -> {
				StringBuilder sb = new StringBuilder();
				sb.append("Automatic message:\n\n");
				sb.append("The CSGO server is starting...\n");
				sb.append("Click on the following link to join the game, have fun!!!\n\n");
				sb.append("steam://connect/ixigo.selfip.net/" + csgoServerProps.getRconPassword());
				botService.sendMessageToGeneralChat(sb.toString());
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
