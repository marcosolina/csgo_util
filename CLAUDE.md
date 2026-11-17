# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

IxiGo Util is a personal project that automates a weekly Counter-Strike game night among friends: change maps and bots over RCON, parse the server's `.dem` recordings into player stats, generate balanced teams from those stats, and have a Discord bot move players into team voice channels. It started for CSGO and is being migrated to CS2, so several modules exist in a legacy CSGO version and a current CS2 version.

Open work (nginx instead of Zuul, dropping Eureka, a newer Postgres, server-helper as an image, known issues) is listed in `docs/roadmap.md`; update it when an item is done or a new one comes up.

Two related repos live outside this one: the dedicated server setup (`github.com/marcosolina/ixi_go`) and, more importantly, the runtime configuration of every service (`github.com/marcosolina/csgo_props`, branch `main`, served by the Config Server). DB credentials, ports, inter-service endpoints, parser paths and tokens are in `csgo_props`, not here.

## Modules

Modules are grouped by folder. Each one is still an independent project; only the root aggregator `pom.xml` ties the Maven ones together.

| Folder | Modules |
| --- | --- |
| `services/` (Spring Cloud infra) | `IxigoDiscovery` (Eureka, port 8765, `/ixigodiscovery`), `IxigoConfigServer` (port 8888, `/config`, backed by `csgo_props`), `IxigoProxy` (Zuul gateway, port 8763, `/ixigoproxy`) |
| `services/` (business) | `IxigoDemManager` (`/demmanager`), `IxigoPlayersManager` (`/playersmanager`), `IxigoRconApi` (`/rcon`), `IxigoDiscordBot` (`/discordbot`), `IxigoEventDispatcher` (`/eventsdispatcher`), `IxigoServerHelper` (`/ixigohelper`), `IxigoNotification` (`/notification`, Telegram), `IxigoUi` (`/ixigoui`) |
| `libs/` | `IxigoParent` (parent POM for business services), `IxigoLibrary` (artifactId `Library`), `Ixigo*Contract` (REST request/response models, version 2.0.0) |
| `parsers/` | `DemoParserCS2` (TypeScript on `@laihoe/demoparser2`, CS2 demos), `DemParserNodeJs` (vendored copy of `saul/demofile`, CSGO demos; still needed because every deploy re-parses the old games) |
| `plugins/` | `IxigoPlugin` (C# CounterStrikeSharp plugin for CS2) |
| `tools/` | `IxigoIntegrationTests` (Cucumber), `IxigoUtils` (one-off DAO/DTO code generator) |
| `legacy/` | Not built, kept for reference: `DemParser` (C#), `SourceModPlugins` (CSGO `.sp` plugins) |
| Other | `docker/` (Dockerfile, Compose files, Postgres seed SQL, sample demos in `docker/test/`), `docs/` (diagrams, logo, font, roadmap) |

The `ixi_go` VM scripts clone this repo from `main` and depend on two paths: they build the plugin from `plugins/IxigoPlugin` and server-helper with `mvn -f pom.xml -pl services/IxigoServerHelper -am package`. Moving either folder needs a matching change in `ixi_go`, merged at the same time.

Business services use Java 11, Spring Boot 2.7.4, Spring Cloud 2021.0.4. Exceptions: `IxigoConfigServer` and `IxigoDiscovery` extend `spring-boot-starter-parent` directly, and `IxigoProxy` is pinned to Boot 2.3.12 / Hoxton.SR11 because it still uses Zuul.

## Build and run

The root `pom.xml` is an aggregator, not a parent: it lists every Maven module so one command builds them in dependency order. Modules keep their own parents; the business services point at `IxigoParent` through an explicit `<relativePath>`. `IxigoUtils` is not listed (it depends on `com.marco:Utils`, which is not on Maven Central) and `IxigoIntegrationTests` is only in the `integration-tests` profile.

The code targets Java 11 and does not compile on a much newer JDK (Lombok from Boot 2.7). Use a JDK 11, or build in the same Maven image the Dockerfile uses:

```bash
mvn -B package                                     # everything
mvn -B -pl services/IxigoDemManager -am package    # one service plus the modules it needs
docker run --rm -v "E:/Workspaces/Git/csgo_util:/src" -v ixigo-m2:/root/.m2 -w /src maven:3.9-eclipse-temurin-11 mvn -B -ntp package
```

The `IxigoUi` POM copies `src/reactapp/build` into the static resources during `validate`, so build the React app first when packaging the UI: `npm install && npm run build` in `services/IxigoUi/src/reactapp`.

React dev server: `npm start` in `services/IxigoUi/src/reactapp`. When the page is not served by the Spring host, `window.srvContextPath` is undefined and `src/lib/constants/paths.ts` falls back to the **production** proxy `https://marco.selfip.net/ixigoproxy`, so a local `npm start` talks to live services unless that fallback is changed.

Docker images come from one multi-stage `docker/Dockerfile`, one `--target` per image (`config-server`, `discovery`, `proxy`, `event-dispatcher`, `players-manager`, `rcon-api`, `server-helper`, `discord-bot`, `ui`, `notification`, `dem-manager`, `postgres`). The build context is the repository root, filtered by `.dockerignore`, and it uses the local working tree. React and Maven run once on the build machine's platform and are shared by every target; add `--platform linux/arm64` for the Raspberry Pi hosts:

```bash
docker build -f docker/Dockerfile --target dem-manager -t ixigo/dem-manager .
```

To build every image for amd64 and arm64 and push it to `docker-registry.marco.selfip.net/ixigo/<image>`, use `docker/build-and-push.ps1` (`-NoPush` for a build check, `-Images dem-manager,ui` for a subset, `-Tag` for the tag; it also pushes the short git commit as a tag and refuses to push uncommitted changes). It wraps `docker buildx bake -f compose.build.yml`, which must run from the `docker/` folder: buildx bake resolves the compose `context: ..` against the current directory.

Every image runs as uid 1000. `dem-manager` is Node 24 plus the Java 11 JRE, with the parsers at `/demParserCs2` and `/demParserCsgo` (the paths the `docker` profile in `csgo_props` expects).

Run the CS2 parser by hand (prints the stats JSON to stdout; sample demos are in `docker/test/`; its `DownloadDemFiles.ps1` uploads every `.dem` in the current folder to the **production** dem-manager and starts `parse/all`):

```bash
cd parsers/DemoParserCS2 && npm install
npx ts-node cs2DemoParser.ts <path-to-file.dem>
```

A service started locally reads `IXIGO_PROFILE` (default `local`), `IXIGO_CONFIG_SERVER_URI` (default `http://localhost:8888/config`), `IXIGO_CONFIG_SERVER_USER`/`IXIGO_CONFIG_SERVER_PASSW` and `IXIGO_EUREKA_SERVER` (default `http://localhost:8765/ixigodiscovery/eureka`), so Config Server and Discovery must be running first. The config client is fail-fast: it retries for about 2.5 minutes, then exits instead of starting with local defaults (`IXIGO_CONFIG_FAIL_FAST=false` restores the old behaviour, `IXIGO_CONFIG_RETRY_ATTEMPTS` tunes it). `IXIGO_PREFERRED_NETWORKS` (default `192.168.1`) picks the IP that Eureka registers. Health is at `/<context-path>/actuator/health` (open without auth, also on the Config Server). Each service logs to `./logs/<service>.log`; Swagger (springfox 2.9.2) is at `/<context-path>/swagger-ui.html`.

## Tests

There are no unit tests in any service or in the React app. The only suite is `IxigoIntegrationTests`, and it currently **does not compile**: its step classes still import DemManager contract classes (`rest.demdata.RestMapStats`, `RestScoreTypes`, `RestUsersResp`, `RestUsersScores`, `rest.charts.RestMapsPlayed`) that were removed or moved in September 2023. It consists of Cucumber scenarios that call the real running services through their configured endpoints, so the stack (Config Server included) must be up. Features are in `src/test/resources/features`, tagged per area (`@demfiles`, `@demFilesParser`, `@eventDispatcher`, `@playersManager`, `@discordBot`, ...).

```bash
mvn -B -P integration-tests -pl tools/IxigoIntegrationTests -am test
mvn -B -P integration-tests -pl tools/IxigoIntegrationTests -am test -Dcucumber.filter.tags="@demFilesParser"
```

A new step package must be added to the `GLUE_PROPERTY_NAME` list in `CucumberConfigurationTest`, and that class name must keep the `Test` suffix for Surefire to pick it up.

## Architecture

### Configuration, discovery, routing

Each service's `bootstrap.yml` sets `spring.application.name` (this is the Eureka service id, marked "Do not change") and points to the Config Server; `application.yml` holds only static bits such as the context path, upload limits and log levels. Business services do not set `server.port` locally: it comes from `csgo_props`.

Everything is called through the Zuul proxy, which routes by Eureka id: `<host>/ixigoproxy/<eureka-id>/<context-path>/<endpoint>`, for example `/ixigoproxy/ixigo-dem-manager/demmanager/parse/all`. Renaming an application name or a context path therefore breaks the React `SERVICES_URLS` map (`services/IxigoUi/src/reactapp/src/lib/constants/paths.ts`), the listener URLs stored in the `event_listeners` table, and endpoint properties in `csgo_props`.

`IxigoUi` serves the React build through a Thymeleaf template (`src/main/resources/static/ui.html`). `MainController` injects the hashed `main.*.js`/`main.*.css` names and the context paths into `window`, and it lists the SPA routes explicitly (`/demfiles`, `/teams`, `/stats`, `/rcon`, `/discordbot`, `/joinus`, `/cs2server`): a new top-level React route must be added there too or deep links return 404.

### Service code pattern

All business services follow the same layering, built on `IxigoLibrary`:

- Controllers only build a command and call `WebMediator.send(cmd)`. Commands live in `commands/` and implement `WebCommandRequest<R>`; handlers live in `handlers/`, are `@Component`, and implement `WebCommandHandler<Cmd, R>`. `WebMediatorImpl` finds the handler by reflection on the generic type, so each command class needs exactly one handler bean. Handlers return `Mono<ResponseEntity<R>>` (Reactor types on top of the servlet stack).
- Service and repository implementations are wired explicitly as `@Bean`s in `config/beans/*.java` against interfaces in `services/interfaces` and `repositories/interfaces`.
- Every application class has `@ComponentScan("com.ixigo")` so the library beans (`IxigoStandardServiceBeans`: mediator, message source, `IxigoWebClientUtils`, `IxigoEventSender`) are picked up.
- Models move through three layers: `Rest*` (in the `*Contract` module) ↔ `Svc*` (service layer) ↔ `*Dto`/`*Dao` (database), converted by MapStruct mappers (`RestMapper`, `SvcMapper`, `RepoMapper`).
- Errors: throw `IxigoException` with a code from `constants/ErrorCodes.java`; messages are in `resources/messages/errorCodes.properties`. Validation uses fluentvalidator classes under `validators/`.

### Data access

No JPA repositories: data access is reactive R2DBC through the custom `IxigoDao<T extends IxigoDto>` in `libs/IxigoLibrary/.../dao`. Each table or view has a `<name>Dao` (table/view name, key fields, field list) and a `<name>Dto` whose field names are the snake_case column names (`Dto.Fields.x` constants); rows are mapped by reflection on setter names, so a DTO field must match its column exactly. These pairs were generated by `tools/IxigoUtils/CreateDaoAndDto` from the live schema. Most player statistics are PostgreSQL views (`012-initViewsDemFilesDb.sql`), and the DemManager `/charts/view` endpoint returns view data by name.

PostgreSQL 11 hosts three databases: `demfiles` (DemManager), `discordbot` (DiscordBot: `users_map`, `bot_config`), `event_dispatcher` (EventDispatcher: `event_listeners`). Schemas and seed data are `docker/postgres/init/*.sql`, baked into the `postgres` image target and run in filename order by `docker-entrypoint-initdb.d` on an empty data directory. **These scripts start with `DROP DATABASE`**: never run them against the live database. `docker/postgres/not-applied/018-optimizedNewDemFilesDb_views.sql` has never been applied.

### Game night event flow

1. The CS2 plugin (`plugins/IxigoPlugin/IxigoPlugin.cs`) writes the latest game event name (`round_start`, `round_end`, `round_announce_warmup`, `cs_win_panel_match`) to `event.txt` on the game server.
2. `IxigoServerHelper` runs on the game server host, polls that file every second (`IxiGoEventMonitorImp`) and POSTs each change to the EventDispatcher. Raw names are mapped to the `EventType` enum in `IxigoEventDispatcherContract`, so a new game event needs the plugin, that enum and listener rows.
3. `IxigoEventDispatcher` forwards each event to the listeners registered in `event_listeners` (defaults in `030-initEventDispatcherDb.sql`, all on the public gateway; `IXIGO_LISTENERS_BASE_URL` on the postgres container rewrites that base at init through `031-setEventListenersBaseUrl.sh`); a listener is deactivated after repeated failures. Zuul answers an empty 200 to any path without a route, so a post to a service that is not registered (server-helper while the VM is off) counts as a success.
4. Listeners: DiscordBot (`/discordbot/events`: team balancing and voice channel moves on warmup and match end), ServerHelper (`/ixigohelper/event`: on `WARMUP_*`/`SHUT_DOWN` it uploads the newest `.dem` files to DemManager), Notification (Telegram).
5. `IxigoDemManager` stores uploads, queues them (`dem_process_queue`) and parses each one by shelling out to `npx ts-node <parser> <file>`. `DemFileFormatChecker` picks the CS2 parser (`parserCs2ExecPath`) or the legacy CSGO one (`parserExecPath`); `parserEnvironment` (`WINDOWS` or `RASP`) selects `DemProcessorNodeJsWindows` or `DemProcessorNodeJsLinux`. The parser's stdout JSON is read into `SvcNodeJsParseOutput` and written to the `demfiles` tables, then `DEM_FILES_PROCESSED` is fired. Only one pass over the queue runs at a time (`startParseRun`): a trigger during a pass (`parse/all`, `parse/queued`, an upload) asks for one more pass after it, because two passes over the same queued files saved matches twice. Within a pass `parallel-parsers` (in `csgo_props`; 6 on the mini PC) bounds how many parsers run at once; without it, `process-files-in-parallel: true` means one per CPU. The JSON shape in `cs2DemoParser.ts`, `SvcNodeJsParseOutput` and the `demfiles` schema must change together.
6. `IxigoPlayersManager` reads player scores from DemManager and partitions players into balanced teams (`PartitionTeamsIxigo` plus `libs/IxigoLibrary/.../partitioning`).
7. `IxigoDiscordBot` maps Discord users to Steam IDs (`users_map`) and reacts to chat commands (`!balance`, `!together`, `!moveToChannel`, `!autoBalanceOn`/`Off`/`Status`, `!restart`, `!help`, `!ping`; see `DiscordChatCommands`). `IxigoRconApi` sends RCON commands to the server.

## Deployment

There is no GitHub Actions CI. Every merge into `main` is deployed by the Jenkins job `IxiGoDeploy` (Jenkins runs in a container on the mini PC): `docker/Jenkinsfile` copies `docker/ci-deploy.sh` to the mini PC and runs it over SSH, and that script builds the amd64 images on the host, pushes them as `:<commit>` and `:latest`, replaces every container with an empty Postgres and waits for the reparse (about 30 minutes in all). Commits that only touch `*.md`, `docs/`, `legacy/`, `tools/`, `plugins/`, `docker/test/` or the CI files are not deployed, and a deploy fails while server-helper answers (game night). A new variable in `docker/.env.example` must be added by hand to `~/ixigo/.env` on the mini PC before the merge, or the deploy stops before touching the containers. How it works, rollback (the `COMMIT` parameter) and manual runs: `docs/jenkins-pipeline.md`. `docker/build-and-push.ps1` still builds amd64 and arm64 by hand from Windows.

`docker/compose.yml` is the same file on every host. Each host keeps it with `deploy.sh` and its own `.env` (from `docker/.env.example`, never committed); `COMPOSE_PROFILES` there picks the host role: `infra` (config-server, discovery), `gateway` (proxy), `services` (the six business services), `db` (postgres), `dem` (dem-manager), plus each service's own name as a profile. Services publish their ports 1:1 (they must match `server.port` in `csgo_props`) and register `HOST_ADDRESS` in Eureka, because Zuul and the callers are on other hosts. Discovery runs at `/discovery` there (production path), unlike the code default `/ixigodiscovery`. `sh deploy.sh` pulls and restarts the host; on the db host it first deletes Postgres and its data so the seed SQL runs again, and `--reparse` then calls dem-manager's `parse/all`. Every service runs on the mini PC (`marco@192.168.1.24`, `~/ixigo`, `COMPOSE_PROFILES=infra,gateway,services,db,dem`, `IXIGO_PROFILE=rasp,minipc`; the `minipc` profile in `csgo_props` points Postgres and the gateway at it). The .dem files stay on the Synology NAS: the `demfiles` volume in `compose.yml` mounts `/volume1/ixigo-dem-files` over NFS (`DEM_FOLDER_TYPE`, `DEM_FOLDER_OPTIONS`, `DEM_FOLDER` in `.env`; a local folder is the default) and dem-manager runs as `1026:100`, the owner of those files. Docker mounts that share at every start of dem-manager, so when the mini PC boots faster than the NAS (after a power cut) the start fails and Docker does not retry it: the `nas-gate` container (profile `dem`, Docker CLI image with the Docker socket) tries to start dem-manager every 30 seconds until it runs, and only then reports healthy. Postgres starts with `synchronous_commit=off`: the database is rebuilt anyway, and dem-manager saves every shot event as its own insert, which otherwise stalls a reparse on WAL flushes. Full deploy order: db, infra, gateway, services, dem, reparse.

Before the cutover of 2026-11-16 a Jenkins job built the jars and copied them to LAN hosts (Raspberry Pis and VMs), and dem-manager ran as an older container on the NAS. Their scripts lived in `Scripts/`, now deleted (still in git history). The old hosts no longer start anything: the IxigoServices VM and the old Postgres on docker.lan are removed, and on the two Pis the start lines in `/home/pi/startApps.sh` are only commented out, with the jars still in `/opt`. The old Jenkins deploy job (`IxiGoUtils`) was not carried over when Jenkins moved to the mini PC on 2026-11-17; it only exists in the stopped Jenkins on the BuildServer VM. Never start it again: it would start jars next to the containers and prune Docker volumes on docker.lan, which also runs Immich.

## Recurring changes

- **Add a CS2 map to the UI picker:** add an entry to `CS2_MAPS` in `services/IxigoUi/src/reactapp/src/contents/cs2-server/useCs2ServerContent.ts` and add the image to `services/IxigoUi/src/reactapp/public/maps/cs2/`, named `<workshop_id>.jpeg` for workshop maps or `<map_name>.jpeg` for official ones (`Cs2ServerContent.tsx` builds the path, so the file name must match exactly).
- **Add a player:** insert the Steam user into `docker/postgres/init/016-seedBaseData.sql` (`demfiles.users`) and the Discord/Steam mapping into `020-initDiscordDb.sql` (`users_map`). The database is recreated from these files on every deploy, so the change applies at the next deploy; insert the same rows into the running database to apply it sooner.
- **Bump the CS2 demo parser:** change the `@laihoe/demoparser2` version in `parsers/DemoParserCS2/package.json` **and** refresh `package-lock.json` (`npm install --package-lock-only` in that folder, for example inside `node:24-bookworm-slim`), then rebuild and push the dem-manager image. Do not switch that parser to `npm ci`: some demoparser2 releases declare optional platform packages that were never published, which `npm ci` rejects.
