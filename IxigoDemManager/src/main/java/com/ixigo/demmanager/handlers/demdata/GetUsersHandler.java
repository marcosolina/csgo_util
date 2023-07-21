package com.ixigo.demmanager.handlers.demdata;

/**
 * Handler used to retrieve the list of users seen in the DEM files
 * 
 * @author marco
 *
@Component
public class GetUsersHandler implements WebCommandHandler<CmdGetUsers, RestUsersResp> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetUsersHandler.class);
	@Autowired
	private DemFileParser service;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<RestUsersResp>> handle(CmdGetUsers request) {
		_LOGGER.trace("Inside GetUsersHandler.handle");

		// @formatter:off
		return service.getListOfUsers()
				.collectList()
				.map(mapper::fromSvcToRestUserList)
				.map(list -> {
					RestUsersResp resp = new RestUsersResp();
					resp.setUsers(list);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				});
		// @formatter:on
	}
}

 */