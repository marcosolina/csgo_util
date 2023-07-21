package com.ixigo.demmanager.handlers.demdata;

/**
 * Handler used to retrieve the scores of the players according to the input
 * filters
 * 
 * @author marco
 *
@Component
public class GetUsersLastScoresHandler implements WebCommandHandler<CmdGetUsersLastScores, RestUsersScores> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetUsersLastScoresHandler.class);
	@Autowired
	private DemFileParser service;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<RestUsersScores>> handle(CmdGetUsersLastScores request) {
		_LOGGER.trace("Inside GetUsersLastScoresHandler.handle");

		// @formatter:off
		return service.getUsersStatsForLastXGames(request.getNumberOfMatches(), request.getUsersIDs(), request.getMinPercPlayed())
				.collectList().map(list -> {
					Map<String, List<RestMapStats>> map = new HashMap<>();
					// Save the statistics into a map where the key is the user steam ID and the value the list of statistics
					list.forEach(stat -> {
						map.put(stat.getSteamId(), mapper.fromSvcToRestMapStats(stat.getStats()));
					});
		
					RestUsersScores resp = new RestUsersScores();
					resp.setUsersScores(map);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				});
		// @formatter:on
	}

}

 */