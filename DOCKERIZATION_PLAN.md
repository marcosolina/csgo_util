# Dockerization plan (temporary file, delete when the work is done)

Goal: every service ships as a Docker image built from this repo, pushed to `docker-registry.marco.selfip.net`, and run on the hosts by one Docker Compose file that pulls those images, mounts host folders where needed and injects environment variables.

Chosen path: **Plan A, then Plan B, with the folder restructure.** Order of work: Phase 1 → Phase R → Phases 2 to 7 (Plan A) → B1 to B4 (Plan B).

## 1. Where we are today

- Only DemManager is containerized. Its image is assembled from helper images (`my-nodejs`, `my-maven`), and `BuildNode/Dockerfile` does `git clone --branch main` from GitHub, so the build ignores the local working tree.
- The other services are plain jars copied by `Scripts/Jenkins/deployJars.sh` to LAN hosts (`docker.lan`, `rasp30.lan`, `ixigoservices.lan`) and started with `nohup` through each module's `Scripts/startStop.sh`.
- Every deploy recreates Postgres with `docker system prune --all --volumes` and then calls `parse/all`: stats are rebuilt from the `.dem` files, players and listeners come back from the seed SQL.
- Maven has no aggregator POM: the build order lives in four copies (`MavenPackageAll.ps1`, `Scripts/Jenkins/buildProjects.sh`, `BuildJava/Dockerfile`, and `ixi_go/Scripts/jenkins/cs2.tokenised.sh`).
- Old per-service Dockerfiles under `Scripts/Docker/Build/*` use deprecated `openjdk:11`, refer to local image names (`my-maven-img`) that no longer match, and wait for dependencies with `sleep 20`.
- No service exposes a health endpoint (no actuator), so Compose cannot order startup properly.
- `Scripts/createDatabase.py` has a database user and password committed in clear text.

### The game server (repo `ixi_go`)

- CS2 runs on an **on-demand Azure VM** provisioned by `ixi_go/Scripts/jenkins/cs2.tokenised.sh` (profile `rasp,azure`). The VM clones `csgo_util` `main`, builds `IxigoServerHelper` with Maven on the VM, runs it as a jar (`startHelper.sh`), and starts CS2 inside `screen -S cs2` from `rc.local`; the VM reboots when CS2 exits.
- server-helper registers in Eureka through the **public** URL `https://marco.selfip.net/discovery/eureka` with `eureka.instance.ipAddress=<VM public IP>`, and reads config from the public `https://marco.selfip.net/config`. Zuul then routes `/ixigoproxy/ixigo-server-helper/...` to the VM. Its DynDNS updater keeps `ixigo.selfip.net` pointed at the VM.
- `ixi_go` depends on this repo's layout: `installMetaModAndPlugin.sh` and `donwloadLatestsModsFrameworks.sh/.ps1` build the plugin from `CsgoPlugins/IxigoPlugin`; `cs2.tokenised.sh` runs per-module `mvn -f` builds. The VM start script also calls the public proxy (`/ixigoproxy/ixigo-event-dispatcher/...` and `/ixigoproxy/ixigo-dem-manager/demmanager/parse/failed`), so those URLs must stay stable.

### Runtime inventory (ports from `csgo_props`)

| Service | Port | Database | Host mounts | External needs |
| --- | --- | --- | --- | --- |
| discovery (Eureka) | 8765 | | | reachable by the Azure VM (today through NPM at `/discovery`) |
| config-server | 8888 | | | GitHub access to `csgo_props`, encryption key, basic auth user; reachable by the Azure VM (`/config`) |
| proxy (Zuul) | 8763 | | | public entry point behind Nginx Proxy Manager |
| event-dispatcher | 8081 | `event_dispatcher` | | listener URLs stored in the DB |
| dem-manager | 8080 (8090 in the `docker` profile) | `demfiles` | dem files folder → `/var/www/html/cstrike/demfiles` | Node 24, ts-node, both parsers |
| players-manager | 8082 | | | |
| rcon-api | 8083 | | | reaches the game server on 27015 |
| server-helper | 8084 | | CS2 install folder | Azure VM only, `screen` session of the CS2 user |
| discord-bot | 8085 | `discordbot` | | Discord bot token |
| ui | 8086 | | | |
| notification | 8087 | | | Telegram token |
| postgres | 5432 | all three | none (rebuilt every deploy) | |

Services call each other only through the gateway URL (`ixigo.end-points.gateway.*`), never through Eureka load balancing: Eureka is used only by Zuul for routing. The `docker` profile in `csgo_props` already sets the parser paths `/demParserCs2/cs2DemoParser.ts` and `/demParserCsgo/examples/dumpfile.ts`.

## 2. Decisions

- [x] **D1 Hosts:** split across several hosts.
- [x] **D2 server-helper:** decide later; it stays a jar on the Azure VM for now (see Phase S).
- [x] **D3 Database:** rebuild on every deploy, as today.
- [x] **D4 Plan:** A, then B, with the folder restructure.
- [ ] **O1 Host mapping:** which services run on which host (`docker.lan`, `rasp30.lan`, `ixigoservices.lan`, other). Decide at Phase 4 (Compose profiles).
- [x] **O2 CPU architectures:** amd64 and arm64, so every image is built multi-arch (`linux/amd64,linux/arm64`).
- [x] **O3 Legacy code:** move the C# `DemParser` and the SourceMod `.sp` plugins to `legacy/`, outside the build. (`DemParserNodeJs` stays a live parser: old CSGO demos still need it on every rebuild.)

## 3. Plan A

### Phase 1: one Maven build

- [x] Add a root `pom.xml` (packaging `pom`) listing every Maven module, so `mvn -B package` builds everything in dependency order and `mvn -pl <module> -am package` builds one service with what it needs. `IxigoUtils` left out (depends on `com.marco:Utils`, not on Maven Central); `IxigoIntegrationTests` in the `integration-tests` profile.
- [x] In each service POM set `<relativePath>` to the `IxigoParent` POM: Maven 3 does not resolve a parent from the reactor, and the default `../pom.xml` would point at the aggregator.
- [x] Add `spring-boot-starter-actuator` to `IxigoParent`, `IxigoConfigServer` and `IxigoDiscovery`. Default web exposure is `health` only; the Config Server lets `/actuator/health` through without basic auth.
- [x] Springfox 2.9.2 crashes next to actuator on Boot 2.6+ (NPE in `documentationPluginsBootstrapper`): fixed with `SpringfoxActuatorCompatibility` in IxigoLibrary (hides the actuator mappings from springfox; delete it when springfox goes).
- [x] Enable Config Client `fail-fast` with retry (`spring-retry` + AOP): about 2.5 minutes of retries, then exit. `IXIGO_CONFIG_FAIL_FAST` and `IXIGO_CONFIG_RETRY_ATTEMPTS` override it.
- [x] Make the Eureka preferred network overridable (`IXIGO_PREFERRED_NETWORKS`, default `192.168.1`).
- [x] Delete `MavenPackageAll.ps1`.
- [ ] Commit Phase 1.

### Phase R: folder restructure

Target layout (module folder names unchanged, only grouped):

```
pom.xml                 aggregator
libs/                   IxigoParent, IxigoLibrary, Ixigo*Contract (7)
services/               IxigoConfigServer, IxigoDiscovery, IxigoProxy, IxigoDemManager, IxigoDiscordBot,
                        IxigoEventDispatcher, IxigoNotification, IxigoPlayersManager, IxigoRconApi,
                        IxigoServerHelper, IxigoUi
parsers/                DemoParserCS2, DemParserNodeJs
plugins/                IxigoPlugin (CS2) (+ legacy per O3)
tools/                  IxigoUtils, IxigoIntegrationTests
docker/                 Dockerfile, compose files, .env.example, postgres init SQL, sample .dem files
docs/                   diagrams and pictures (from misc/)
```

- [x] `git mv` the modules (keeps history with `--follow`); update aggregator module paths and every `<relativePath>` (now `../../libs/IxigoParent/pom.xml`). `misc/` became `docs/`; `CsgoPlugins/IxigoPlugin` became `plugins/IxigoPlugin`; `DemParser` and the `.sp` plugins went to `legacy/`.
- [x] Update README links and CLAUDE.md (the legacy `.sp` plugin URLs too).
- [x] Eclipse metadata (`.project`, `.settings`) moved with each module and stays tracked; re-import the projects in the IDE from their new folders.
- [x] `.github/java-upgrade` and `.github/modernize`: nothing to do, they are not tracked by git.
- [ ] Delete the leftover untracked `CsgoPlugins/IxigoPlugin/obj` and `DemParser/obj` folders after closing VS Code (its C# language server keeps them locked; the tracked files were moved one by one around the lock).
- [x] **Prepare `ixi_go`**: changes ready on branch `marco/docker-refactoring` in `ixi_go`, not committed:
  - `Scripts/jenkins/installMetaModAndPlugin.sh`, `Scripts/CssSharp/donwloadLatestsModsFrameworks.sh` and `.ps1`: `CsgoPlugins/IxigoPlugin` → `plugins/IxigoPlugin`.
  - `Scripts/jenkins/cs2.tokenised.sh`: the nine `mvn -f` calls became `mvn -B -DskipTests clean package -f $WORKSPACE_FOLDER/pom.xml -pl services/IxigoServerHelper -am`, and the jar path is `services/IxigoServerHelper/target/`.
- [x] Commit Phase R (csgo_util) and the `ixi_go` changes (local only, nothing pushed).
- [ ] **Merge gate:** both repos are cloned from `main` when an Azure VM is created, and the old Jenkins and Docker scripts in `Scripts/` still use the flat layout. Merge this branch and the `ixi_go` branch to `main` together, between game nights, and only once the new Docker deployment (Phases 2 to 6) replaces the old scripts.

### Phase 2: images

- [ ] Add a root `.dockerignore` (`**/target`, `**/bin`, `**/node_modules`, `.git`, `**/*.dem`, IDE files).
- [ ] Create one multi-stage `docker/Dockerfile` built from the local working tree (no more `git clone`):
  - `ui-build` (node): `npm ci` + `npm run build` of the React app. Mandatory: an IxigoUi jar without the React build does not start (`MainController` injects `classpath:static/static/**`).
  - `java-build` (`maven:3.9-eclipse-temurin-11`): copy sources and the UI build into IxigoUi's `static` folder, run the root build with a BuildKit cache mount on `~/.m2`.
  - `parsers` (node 24): `npm ci` for `DemoParserCS2` and `DemParserNodeJs`.
  - `jre-base` (`eclipse-temurin:11-jre`): non-root user, `JAVA_TOOL_OPTIONS=-XX:MaxRAMPercentage=75`, `ENTRYPOINT java -jar /app/app.jar`, curl or wget available for healthchecks.
  - One final target per Java service (`discovery`, `config-server`, `proxy`, `event-dispatcher`, `players-manager`, `rcon-api`, `discord-bot`, `notification`, `ui`, `server-helper`) that copies only its jar onto `jre-base`.
  - `dem-manager` target: Node 24 image plus the JRE copied from `eclipse-temurin:11-jre`, global `ts-node`, parsers at the paths the `docker` profile expects, the jar.
  - `postgres` target: Postgres 11 (upgrade in B3) with the init SQL baked in, so the hosts need only images.
- [ ] Multi-arch per O2: `java-build` and `ui-build` run with `--platform=$BUILDPLATFORM` (jars are architecture independent, so no emulated Maven); only runtime stages and the parsers `npm ci` (native `@laihoe/demoparser2` addon) run per target platform. Requires a buildx builder with QEMU on the build machine.

### Phase 3: `docker/compose.build.yml` (build and publish)

- [ ] One entry per image: `image: ${REGISTRY:-docker-registry.marco.selfip.net}/ixigo/<service>:${TAG:-latest}`, `build: {context: .., dockerfile: docker/Dockerfile, target: <service>}`, shared settings and `platforms` through a YAML anchor.
- [ ] Usage: `docker login docker-registry.marco.selfip.net`, then `docker compose -f docker/compose.build.yml build` and `push` (or `docker buildx bake -f docker/compose.build.yml --push`, required for multi-arch). Optional `TAG` = git short sha next to `latest` for rollbacks.
- [ ] Delete `build-and-push-dem-manager.ps1` (or keep a two-line wrapper).

### Phase 4: `docker/compose.yml` (run from the registry, split hosts)

One file for every host; each host's `.env` picks its services with `COMPOSE_PROFILES`.

- [ ] Every service gets a profile named after its host role (from O1). Deploy on a host: `docker compose pull && docker compose up -d`.
- [ ] Image references only, no `build:`. The host needs `compose.yml` and its `.env`, not the repo.
- [ ] Shared YAML anchor for Spring services: `restart: unless-stopped`, `IXIGO_PROFILE`, `IXIGO_CONFIG_SERVER_URI`, `IXIGO_CONFIG_SERVER_USER/PASSW`, `IXIGO_EUREKA_SERVER` (host addresses from `.env`, since the targets may be on another host), json-file log rotation.
- [ ] Cross-host Eureka registration: publish each Spring service port 1:1 and set `EUREKA_INSTANCE_IP_ADDRESS=${HOST_IP}`, so Zuul on another host reaches the published port, not a container IP. Verified in Phase 1: without it the service registers its container IP (`172.x`); with it both `ipAddr` and `hostName` become the given address. (Fallback: `network_mode: host`, which keeps today's behaviour but loses network isolation.)
- [ ] **Security:** the Zuul proxy sets `management.endpoints.web.exposure.include: '*'`, so `/ixigoproxy/actuator/env`, `/heapdump` and friends are open, and publicly so if Nginx Proxy Manager forwards all of `/ixigoproxy`. Restrict it to `health,info,routes` (or remove the proxy in B1).
- [ ] Healthchecks on every service. `depends_on: {condition: service_healthy, required: false}` covers ordering on the same host; across hosts the config-client retry from Phase 1 and the restart policy take over.
- [ ] Postgres rebuilt every deploy (D3): no named volume; the deploy recreates it with `docker compose up -d --force-recreate --renew-anon-volumes postgres`, so a plain host reboot keeps the data but a deploy starts fresh from the seed SQL.
- [ ] One-shot `reparse` service (curl image, `tools` profile) that waits for dem-manager to be healthy and calls `parse/all`: `docker compose run --rm reparse` replaces the last line of `deployJars.sh`.
- [ ] dem-manager volume: `${DEM_FOLDER}:/var/www/html/cstrike/demfiles`.
- [ ] `docker/.env.example` committed with every variable and a comment; real `.env` files live only on the hosts.
- [ ] A short deploy script (or README section) per host: pull, recreate Postgres, up, reparse.

### Phase 5: configuration (`csgo_props`)

- [ ] `docker` profile: `ixigo.postgres.host` and gateway endpoints pointing at reachable host addresses, one agreed dem-manager port (8080 in the old deploy compose vs 8090 in the profile).
- [ ] Event listener seed rows (`030-initEventDispatcherDb.sql`) use `http://localhost:8763/...`, which is wrong inside a container; point them at the proxy host address (they are re-seeded on every deploy, so this is the place to fix them).
- [ ] Agree the active profile list per host (for example `rasp,docker`); the Azure VM keeps `rasp,azure`.
- [ ] Default profile (`application.yml`, local Windows development): parser paths still point at `E:\Workspaces\Git\csgo_util\DemParserNodeJs\...` and `...\DemoParserCS2\...`; add the `parsers\` folder.

### Phase 6: cutover

- [ ] Fix `IxigoIntegrationTests` first or plan manual checks: it has not compiled since September 2023 (imports DemManager contract classes that were removed or moved, for example `rest.demdata.RestMapStats` and `rest.charts.RestMapsPlayed`).
- [ ] Start the new stack next to the old jars on different published ports; run the integration tests (or the manual checks) against it.
- [ ] Rebuild the data (`reparse`), switch the Nginx Proxy Manager hosts (`/ixigoproxy`, `/ixigoui`, `/discovery`, `/config`) to the new containers, check the UI, Discord commands and a full game night event round trip with an Azure VM.
- [ ] Stop the jars on the LAN hosts.

### Phase 7: cleanup

- [ ] Delete `Scripts/Docker/Build`, `Scripts/Docker/Deploy`, `Scripts/Jenkins`, every `*/Scripts/startStop.sh`, the legacy `CsGoServer` image and `setup.sh`.
- [ ] Delete `Scripts/createDatabase.py` (clear-text credentials) and rotate that password if it is still used anywhere.
- [ ] Apply O3 (legacy parser and plugins).
- [ ] Update README (Docker section, OpenAPI links) and CLAUDE.md; delete this file.

### Phase S: server-helper (deferred, D2)

Today it is a jar built with Maven on every new Azure VM. When we pick this up, the strongest option is to ship it as the `server-helper` image from Phase 2 and have the VM `docker run` it: no JDK, Maven or git build on the VM (faster provisioning), mounts for the CS2 folder and `/run/screen/S-<user>`, running with the CS2 user's uid so `screen -S cs2 -X stuff` works, `EUREKA_INSTANCE_IP_ADDRESS` set to the VM public IP. Needs Docker on the VM and a test of the screen socket from the container.

## 4. Plan B (after Plan A is live)

- [ ] **B1 Replace Zuul with an nginx container** keeping the same URLs, `/ixigoproxy/<service-id>/...` → `http://<host>:<port>/...` from an env-templated config, with the 500 MB upload limit, 120 s timeouts and CORS headers. No change for the UI, the listener rows, the VM scripts or `csgo_props`. Removes `IxigoProxy` (Spring Boot 2.3 / Hoxton, end of life).
- [ ] **B2 Drop Eureka**, only if server-helper can be routed statically: nginx would proxy `ixigo-server-helper` to the DynDNS name `ixigo.selfip.net:8084` (re-resolved with a `resolver` and a variable `proxy_pass`). Requires DynDNS to be reliable on the Azure VM; otherwise keep Eureka for server-helper only. Then disable the client, remove the starter from `IxigoParent` and delete `IxigoDiscovery`, and stop exposing `/discovery` publicly.
- [ ] **B3 Postgres 11 → a supported major** (16 or 17). Low risk, since the database is rebuilt on every deploy.
- [ ] **B4 (optional) CI build and push** from GitHub Actions with registry credentials as repo secrets.

Keep the Config Server in both plans: it centralizes profiles and holds encrypted values in `csgo_props`.

Out of scope: Spring Boot 3 / Java 17+ upgrade (springfox, `javax` → `jakarta`), schema migrations, unit tests.

## 5. Progress log

- **2026-11-14, Phase 1 done (branch `marco/docker-refactoring`, not committed yet).** Verified with `maven:3.9-eclipse-temurin-11` (the local JDK is 25, which cannot compile this code): the full reactor builds from an empty `~/.m2` in about 5.5 minutes (Docker volume `ixigo-m2` keeps the cache). Runtime checks in a throwaway Docker network: Discovery and Config Server health `UP`; Config Server still answers 401 without credentials and 200 with them; a service started before the Config Server retried for about 45 s and then loaded its config from `csgo_props`; with an unreachable Config Server the service exits with code 1 ("fail fast property is set"); PlayersManager starts with Swagger plus actuator, health `UP`, `/actuator/env` 404, Swagger lists only `/teams` and `/teams/scorestype`; `EUREKA_INSTANCE_IP_ADDRESS` overrides the registered address. Not verified at runtime: services that need Postgres, Discord or Telegram credentials (they compile and share the same changes).
- **2026-11-14, Phase 1 committed** as `ebfaff69`.
- **2026-11-14, Phase R done and committed** (local, not pushed). 1,431 files moved with `git mv` (history kept). Verified in `maven:3.9-eclipse-temurin-11`: `mvn clean package` of the whole reactor passes from the new layout, and the Azure VM command (`-pl services/IxigoServerHelper -am`) builds the server-helper jar with no `com.ixigo` artifacts in the local repository, as on a fresh VM. Matching `ixi_go` changes are prepared on its `marco/docker-refactoring` branch.
