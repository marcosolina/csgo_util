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
- [x] **O1 Host mapping:** keep today's split (read from the production Eureka registry on 2026-11-14), one Compose profile per host role:

  | Profile | Services | Host today |
  | --- | --- | --- |
  | `infra` | config-server, discovery | target mini PC; today rasp26 / raspweb.lan, 192.168.1.26 (Eureka at `/discovery`, not built from this repo: it has an actuator that IxigoDiscovery never had) |
  | `gateway` | proxy | target mini PC; today pi.hole, 192.168.1.30 |
  | `services` | event-dispatcher, players-manager, rcon-api, discord-bot, ui, notification | target mini PC; today ixigoservices.lan, 192.168.1.210 (jars, VM to delete) |
  | `db` | postgres | target mini PC; today docker.lan, 192.168.1.243 |
  | `dem` | dem-manager | ds1522 (Synology DS1522+, amd64), 192.168.1.237: already a container, port 8090, profile `rasp,docker`, registers `ds1522`, runs as root, dem files in `/volume1/ixigo-dem-files` |
  | none | server-helper | Azure VM (Phase S) |

  Host facts (over SSH, 2026-11-14):

  | Host | Arch | OS | CPU / RAM | Docker | Runs today |
  | --- | --- | --- | --- | --- | --- |
  | rasp26 (`pi@`) | aarch64, 64-bit | Debian 13 | 4 / **906 MB** | **none** | `DiscoveryService.jar`, `ConfigurationServer.jar` (about 370 MB RSS together) |
  | pi.hole, hostname rasp30 (`pi@`) | aarch64, 64-bit | Debian 11 | 4 / 3.7 GB | **none** | `IxigoProxy.jar`, and Pi-hole (the LAN DNS) |
  | ixigoservices.lan (`marco@`) | x86_64 | **Ubuntu 18.04 (end of life)** | 2 / 7.8 GB | **none** | the six business jars (300 to 400 MB RSS each) |
  | docker.lan (`marco@`) | x86_64 | Ubuntu 22.04 | 6 / 15 GB | 27.3.1, Compose v2.29.7 | ixigo Postgres (`postgresql-postgres-1`, image `postgres_ixigo`, port 5432), Immich (5433 and 2283), `tagimages-app` (**8085**, the discord-bot port), `tap-the-frog` (9080) |
  | ds1522 | x86_64 | DSM, Container Manager | | yes | dem-manager; dem files owned by `1026:100` (marco:users) with ACLs |

  No 32-bit host, so amd64 plus arm64 images cover everything. Only docker.lan and the NAS have Docker.

- [x] **D5 Target hosts:** everything except dem-manager runs as containers directly on the **mini PC** (`marco@192.168.1.24`, DNS `mini-pc`, hostname `minipc`: Ryzen 5 5600U, 12 threads, 62 GB, Ubuntu 24.04, Docker 29.9.3, Compose v5.7.1, no containers yet, none of the IxiGo ports in use). It is the KVM host of docker.lan and ixigoservices.lan. dem-manager stays on the NAS. After the cutover: delete the ixigoservices.lan VM, stop the jars on rasp26 and rasp30, remove the old Postgres container from docker.lan. `.env` on the mini PC: `COMPOSE_PROFILES=infra,gateway,services,db`, `HOST_ADDRESS=192.168.1.24`.
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
- [x] Eclipse metadata (`.project`, `.settings`) moved with each module; re-import the projects in the IDE from their new folders. (2026-11-16: untracked and ignored, see below.)
- [x] `.github/java-upgrade` and `.github/modernize`: nothing to do, they are not tracked by git.
- [ ] Delete the leftover untracked `CsgoPlugins/IxigoPlugin/obj` and `DemParser/obj` folders after closing VS Code (its C# language server keeps them locked; the tracked files were moved one by one around the lock).
- [x] **Prepare `ixi_go`**: changes ready on branch `marco/docker-refactoring` in `ixi_go`, not committed:
  - `Scripts/jenkins/installMetaModAndPlugin.sh`, `Scripts/CssSharp/donwloadLatestsModsFrameworks.sh` and `.ps1`: `CsgoPlugins/IxigoPlugin` → `plugins/IxigoPlugin`.
  - `Scripts/jenkins/cs2.tokenised.sh`: the nine `mvn -f` calls became `mvn -B -DskipTests clean package -f $WORKSPACE_FOLDER/pom.xml -pl services/IxigoServerHelper -am`, and the jar path is `services/IxigoServerHelper/target/`.
- [x] Commit Phase R (csgo_util) and the `ixi_go` changes (local only, nothing pushed).
- [ ] **Merge gate:** both repos are cloned from `main` when an Azure VM is created, and the old Jenkins and Docker scripts in `Scripts/` still use the flat layout. Merge this branch and the `ixi_go` branch to `main` together, between game nights, and only once the new Docker deployment (Phases 2 to 6) replaces the old scripts.

### Phase 2: images

- [x] Add a root `.dockerignore` (build outputs, `node_modules`, `.dem` files, IDE metadata, and the folders no image needs: `docs`, `legacy`, `plugins`, `tools`, `Scripts`).
- [x] Create one multi-stage `docker/Dockerfile` built from the local working tree (no more `git clone`):
  - `ui-build` (node 24): `npm ci` + `npm run build` of the React app. Mandatory: an IxigoUi jar without the React build does not start (`MainController` injects `classpath:static/static/**`).
  - `java-build` (`maven:3.9-eclipse-temurin-11`): copy sources and the UI build, run the root build with a BuildKit cache mount on `~/.m2`, collect the jars as `/out/Ixigo<Name>.jar`.
  - `parsers` (node 24 bookworm-slim): CS2 parser with `npm install` (see below) and a `tsc --noEmit` check; CSGO parser with `npm ci`, dev dependencies included because `examples/dumpfile.ts` needs them. No `tsc` build for the CSGO parser: the script imports the published `demofile` package, not the vendored `dist/` (verified: same output without `dist/`), and skipping it saves about 4 minutes per arm64 build.
  - `java-base` (`eclipse-temurin:11-jre`, Ubuntu 26.04, curl included): runs as uid 1000, `/app/logs` writable by any uid, `JAVA_TOOL_OPTIONS=-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError`, `ENTRYPOINT java -jar /app/app.jar`.
  - One final target per Java service (`config-server`, `discovery`, `proxy`, `event-dispatcher`, `players-manager`, `rcon-api`, `server-helper`, `discord-bot`, `ui`, `notification`) that copies only its jar onto `java-base`.
  - `dem-manager` target: `node:24-bookworm-slim` plus the JRE copied from `eclipse-temurin:11-jre`, curl, global `ts-node@10.9.2`, parsers at `/demParserCs2` and `/demParserCsgo` (the `docker` profile paths), uid 1000, dem files folder owned by uid 1000.
  - `postgres` target: Postgres 11 (upgrade in B3) with `docker/postgres/init/*.sql` baked in. The SQL moved there from `Scripts/Docker/Build/PostgreSql` (old Dockerfile and compose removed); the never-applied `018` script is in `docker/postgres/not-applied/`.
- [x] `parsers/DemoParserCS2/package-lock.json` was stale (0.38.3 while `package.json` asks for `^0.41.3`; the old image hid it with `npm install`). Regenerated: it pins 0.41.3. `npm ci` still cannot be used for that parser: demoparser2 0.41.3 declares optional platform packages that were never published (darwin-x64, darwin-universal, freebsd-x64, win32-ia32), so the Dockerfile uses `npm install`, which keeps the locked versions.
- [x] Multi-arch per O2: `java-build` and `ui-build` run with `--platform=$BUILDPLATFORM` (jars are architecture independent, so no emulated Maven); only runtime stages and the parsers' npm install (native `@laihoe/demoparser2` addon, published for linux x64 and arm64 gnu) run per target platform. Docker Desktop's builder already has QEMU for arm64.

### Phase 3: `docker/compose.build.yml` (build and publish)

- [x] One entry per image: `image: ${REGISTRY:-docker-registry.marco.selfip.net}/ixigo/<service>:${TAG:-latest}`, `build: {context: .., dockerfile: docker/Dockerfile, target: <service>}`, shared settings and `platforms: [linux/amd64, linux/arm64]` through a YAML anchor.
- [x] `docker/build-and-push.ps1`: creates the `ixigo` buildx builder (docker-container driver; this Docker Desktop uses the classic image store, which cannot hold multi-arch images), runs `docker buildx bake --push`, and pushes each image as `-Tag` (default `latest`) and as the short git commit for rollbacks. Refuses to push from a dirty working tree unless `-AllowDirty` (then no commit tag). `-NoPush` builds only, `-Images` limits the targets.
- [x] Gotchas found in buildx bake 0.16: it resolves the compose `context: ..` against the current directory, not the compose file, so bake must run from `docker/` (the script does; from the repository root the build went to `E:\Workspaces\Git`). And it ignores `build.platforms` in compose files: platforms must also be given as `x-bake.platforms`.
- [x] Deleted `Scripts/Docker/Build/build-and-push-dem-manager.ps1`.
- [x] First push, as `next` (plus the commit tag). **Tag clash:** production pulls `ixigo/dem-manager:latest`, the same name and tag this pipeline produces, so pushing `latest` before cutover would replace the running dem-manager at its next pull. Until Phase 6, push with a different tag (for example `-Tag next`) or accept the replacement.

### Phase 4: `docker/compose.yml` (run from the registry, split hosts)

One file for every host; each host's `.env` picks its services with `COMPOSE_PROFILES`.

- [x] Every service has two profiles: its host role (from O1) and its own name, so one service can move host (`COMPOSE_PROFILES=services,dem-manager`). Deploy on a host: `sh deploy.sh`.
- [x] Image references only, no `build:`. The host needs `compose.yml`, `deploy.sh` and its `.env`, not the repo.
- [x] Shared YAML anchors: `restart: unless-stopped`, json-file log rotation (3 × 10 MB), `mem_limit` (`JAVA_MEM_LIMIT`, default 768m since the jars use 300 to 400 MB RSS today; the JVM heap takes 75% of it) and the Spring environment (`IXIGO_PROFILE`, Config Server URI and credentials, `IXIGO_EUREKA_SERVER`, all from `.env`). Secrets go only to the services that read them.
- [x] Cross-host Eureka registration: each service publishes its port 1:1 and sets `EUREKA_INSTANCE_IP_ADDRESS=${HOST_ADDRESS}`.
- [x] Discovery runs at `/discovery` (`SERVER_SERVLET_CONTEXT_PATH`), the path every host and Nginx Proxy Manager use today; the code default `/ixigodiscovery` is left for local development.
- [x] **Security:** the Zuul actuator exposure is now `health,info,routes` instead of `'*'` (in `IxigoProxy/application.yml`, in the images since `f1d159e2`). **Correction (found in the Phase 6 test):** the actuator was never reachable. With `zuul.servlet-path: /` the Zuul servlet takes every path, and any path without a route (its own `/actuator/...` included) gets an empty 200, so `env` and `heapdump` were not exposed. The change stays as defence in depth. Side effects of that behaviour: the proxy healthcheck only proves that Tomcat answers, and the event dispatcher records a success when it posts to a service that is not registered (for example server-helper while the VM is off). B1 fixes both.
- [x] Healthchecks on every service (curl to `/actuator/health`, `pg_isready`). No `depends_on`: every dependency is on another host or covered by the config-client retry and the restart policy, so the file needs no recent Compose feature.
- [x] Postgres rebuilt every deploy (D3): no named volume. `deploy.sh` runs `docker compose rm --stop --force --volumes postgres` before `up`, which also deletes the old anonymous volume (`--renew-anon-volumes` would leave it dangling). A restart or reboot keeps the data.
- [x] One-shot `reparse` service (`curlimages/curl`, `tools` profile): waits up to 10 minutes for dem-manager's health, then POSTs `parse/all` to `DEM_MANAGER_URL`. `sh deploy.sh --reparse` or `docker compose run --rm reparse`.
- [x] dem-manager: `${DEM_FOLDER}:/var/www/html/cstrike/demfiles`, `user: ${DEM_MANAGER_USER}` (the old image ran as root, so the folder may be root-owned), `IXIGO_PROFILE` plus `,docker` (parser paths and port 8090), 3 GB limit with the heap at 40% so the Node parsers keep the rest.
- [x] `docker/.env.example` with every variable and a comment; `docker/.env` is in `.gitignore`.
- [x] `docker/deploy.sh` per host: pull, recreate Postgres when this host runs it, `up -d`, optional reparse, `ps`. Full deploy order: db, infra, gateway, services, dem, reparse.
- [x] `.gitattributes` with `*.sh text eol=lf`; `deploy.sh` is committed executable.
- [x] Commit Phase 4 (`f1d159e2`), then push the proxy image again (actuator change) with the next `next` push.
- [x] NAS: Container Manager does **not** apply `COMPOSE_PROFILES` from `.env` (the first Build started nothing new) and its Build reuses a cached image. The NAS therefore runs `docker/compose.nas.yml` (dem-manager only, no profiles, `pull_policy: always`) as `/volume2/docker/ixigo-dem-manager/compose.yaml`, with `DEM_MANAGER_USER=1026:100`. The old project files are in `old-2026-11-16/` there. `marco` is in a `docker` group (DSM boot task `chown root:docker /var/run/docker.sock`), so `docker ps` and `docker logs` work over SSH without sudo; pulls still need root, which holds the registry login.
- [ ] The old `deployJars.sh` runs `docker system prune --all --volumes --force` on docker.lan, which now also runs Immich: a deploy while Immich is stopped would delete its unused volumes. Retire that script before any further Jenkins deploy, or remove that line.

### Phase 5: configuration (`csgo_props`)

- [x] New profile `minipc` in `csgo_props` (`a3446af`, pushed): Postgres host and gateway are the mini PC (`192.168.1.24`, proxy on 8763), so the containers call each other on the LAN and a test stack there never reaches production. `rasp` is unchanged, so the running jars are not affected.
- [x] Profiles: mini PC `IXIGO_PROFILE=rasp,minipc`; the NAS dem-manager the same (Compose appends `docker`); the Azure VM keeps `rasp,azure` and the public URLs.
- [x] Event listener seed (`030-initEventDispatcherDb.sql`): one set of 13 rows on the public gateway instead of three sets (localhost, the old `ixigo-proxy` Docker name, public). `031-setEventListenersBaseUrl.sh` rewrites the base when the postgres container has `IXIGO_LISTENERS_BASE_URL` (mini PC: `http://192.168.1.24:8763/ixigoproxy`; local development: `http://localhost:8763/ixigoproxy`).
- [ ] Default profile (`application.yml`, local Windows development): parser paths still point at `E:\Workspaces\Git\csgo_util\DemParserNodeJs\...` and `...\DemoParserCS2\...`; add the `parsers\` folder **when the branch is merged** (`main` still has the old layout).

### Phase 6: cutover

- [ ] Fix `IxigoIntegrationTests` first or plan manual checks: it has not compiled since September 2023 (imports DemManager contract classes that were removed or moved, for example `rest.demdata.RestMapStats` and `rest.charts.RestMapsPlayed`).
- [x] Test stack on the mini PC next to production (`~/ixigo`, `TAG=next`, throwaway secrets, own Config Server, Eureka and Postgres; without discord-bot and notification, which would double the Discord and Telegram traffic; a test dem-manager with 7 demos copied from the NAS in `~/ixigo/demfiles-test`). Results in the progress log.
- [x] Cutover (2026-11-16, done in this order):
  1. Mini PC `/home/marco/ixigo/.env` (prepared, mode 600, every non-secret value set): Marco replaces the six `CHANGE_ME` values (Config Server password and `IXIGO_CONFIG_ENC_KEY` as today, a new Postgres password, Discord token, RCON password). The test stack and the demo copy are already removed.
  2. `latest` = the tested `03bf3365` images (`docker buildx imagetools create`, no rebuild); the UI fix below moved `ui:latest` on.
  3. Stop the jars (rasp26: Discovery and Config Server; rasp30: proxy; ixigoservices.lan: the six services) and the old Postgres project on docker.lan, then `sh deploy.sh` on the mini PC.
  4. Nginx Proxy Manager: `/ixigoproxy`, `/ixigoui`, `/discovery`, `/config` to `192.168.1.24`.
  5. NAS: `compose.nas.yml` and `.env` (`HOST_ADDRESS=192.168.1.237`, `IXIGO_PROFILE=rasp,minipc`, `DEM_MANAGER_USER=1026:100`, the same passwords as the mini PC), Stop and Build in Container Manager, then the reparse from the mini PC (`docker compose run --rm reparse`).
  6. Check: UI and public URLs done; still to do: Discord commands and a full game night event round trip with an Azure VM.
- [ ] After a good game night: delete the ixigoservices.lan VM, disable the jars' autostart on rasp26 and rasp30, remove the old Postgres container and image from docker.lan.

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
- **2026-11-14, Phase R committed**: csgo_util `840fecf5`, ixi_go `55b5d0b` (both local).
- **2026-11-14, Phase 2 done and committed** (local). All 12 targets build for amd64 (shared React and Maven stages about 3 minutes once, then 2 to 15 s per target; Java images about 340 to 440 MB, `dem-manager` about 800 MB). `dem-manager`, `discovery` and `postgres` also build for arm64, reusing the React and Maven layers from the amd64 build (only the parsers' npm install and the runtime layers run under QEMU). Runtime checks: inside `dem-manager` (uid 1000, cwd `/app`, as the Java code calls them) the CS2 parser turns the sample `gd_rialto` CS2 demo into 73,180 bytes of JSON and the CSGO parser turns the sample `de_mirage` demo into 488,260 bytes, both with the keys `SvcNodeJsParseOutput` reads; the arm64 image gives the same CS2 output with the native `demoparser2-linux-arm64-gnu` addon and runs Temurin 11.0.33 aarch64. The `postgres` image initializes the three databases with no errors (29 users and 44 views in `demfiles`, 31 `users_map` rows, 31 `event_listeners` rows). `config-server`, `discovery` and `ui` images started together: the UI loaded its config, started in 18 s and served the page plus its 1.6 MB React bundle; all three report health `UP` through curl inside the container.
- **2026-11-14, Phase 3 build check (corrected).** `docker/build-and-push.ps1 -NoPush` created the `ixigo` builder and built all 12 images from an empty cache with one Maven run (1:39 min) and one React build. That run was amd64 only, which I did not notice at first: buildx bake 0.16 ignores `build.platforms` in compose files.
- **2026-11-14, first push** as `next` and `7da731f5` (Phase 3 commit): amd64 only for the same reason. Fixed by adding `x-bake.platforms` next to `platforms` in `compose.build.yml` (bake `--print` now shows both platforms for all 12 targets), and the script now checks after each push that every tag holds every expected platform. The `7da731f5` tags stay amd64 only; `next` is replaced by the next push. Production `dem-manager:latest` was not touched.
- **2026-11-14, Phase 3 done.** Second push from `0d3a7f2f`: all 12 images in the registry as `next` and `0d3a7f2f`, each with linux/amd64 and linux/arm64 (checked by the script after each push, spot-checked with `imagetools inspect`). Production `dem-manager:latest` is still the old single-architecture image.
- **2026-11-14, Phase 4 written** (not committed). Host map read from the production Eureka (`/discovery/eureka/apps`) and the NAS compose and environment Marco shared. Validated `docker compose config --services` for every host profile set. Local smoke test with the `next` images (project `ixigo-smoke`, local Config Server and Eureka, Postgres moved to 15432 by a scratch override): config-server, discovery, ui and postgres all healthy within 25 s; the UI registered in Eureka at `/discovery` with `HOST_ADDRESS` as both `ipAddr` and `hostName`; the Config Server still answers 401 without credentials. `deploy.sh --reparse` against it: a marker table disappeared, the seed came back (29 users), the old Postgres volume was deleted, the other containers were left alone, and the reparse waited for health, POSTed, and passed the failure exit code through (404, as it pointed at the UI on purpose). Not run locally: the business services that need the production Postgres, Discord or Telegram, and dem-manager (its image was checked in Phase 2). `csgo_props` has no `dev` branch any more (only `main` and `original`), although the old env files still set `IXIGO_GIT_REPO_BRANCH=dev`.
- **2026-11-14, Phase 4 committed** as `f1d159e2` and all 12 images pushed as `next` and `f1d159e2` (amd64 and arm64, checked by the script). The pushed proxy jar has the `health,info,routes` exposure. Production `dem-manager:latest` is still the old image. Host facts gathered over SSH (see O1); D5 open.
- **2026-11-14, D5 decided:** everything except dem-manager moves to containers on the mini PC (192.168.1.24, KVM host of docker.lan and ixigoservices.lan, checked over SSH); the ixigoservices.lan VM will be deleted after the cutover. `JAVA_MEM_LIMIT` default raised to 768m from the measured RSS of the jars.
- **2026-11-14, Phase 5 and the test stack.** `csgo_props` `a3446af` adds the `minipc` profile. Listener seed reduced to one set plus `031-setEventListenersBaseUrl.sh` (checked: 13 rows rewritten to the mini PC proxy); `postgres:next` pushed with it (from a dirty tree, `-AllowDirty`). On the mini PC (`~/ixigo`, logged in to the registry): the Config Server (serving `csgo_props` `a3446af`) gives `rasp,minipc` services Postgres `192.168.1.24` and the mini PC gateway, checked before any business service started. `sh deploy.sh --reparse`: pulled, recreated Postgres, all 9 containers healthy within about 25 s, every service registered in the mini PC Eureka as `192.168.1.24`, all 7 demos parsed (4 CSGO from 2020-08-24, 3 CS2 from 2026-11-09: 7 matches, 88 rounds, 52 player rows), `DEM_FILES_PROCESSED` dispatched to the mini PC proxy. Through the proxy the dem-manager and UI health answer; in Chrome the UI at `http://192.168.1.24:8086/ixigoui` lists the two test nights and shows the leaderboard and recent matches from them. Found: Zuul answers every unrouted path with an empty 200 (see the corrected security item). Not tested: discord-bot and notification (left out on purpose), RCON against a running game server.
- **2026-11-14, test stack removed and production `.env` prepared** on the mini PC (`~/ixigo`: `compose.yml`, `deploy.sh`, `.env` with `CHANGE_ME` placeholders for the secrets). The `next` images stay in its image cache.
- **2026-11-16, cutover.** Marco filled the secrets; the new Config Server accepted them and decrypted every `{cipher}` value before anything was stopped. `latest` moved to `03bf3365` for all 12 images. Stopped the 9 jars (rasp26, rasp30, ixigoservices.lan) and the old Postgres container on docker.lan (kept, `restart=no`), then `sh deploy.sh` on the mini PC: 10 containers healthy, Discord bot logged in, 13 listeners on the mini PC proxy. Marco switched Nginx Proxy Manager; server-helper on the running Azure VM re-registered by itself through the public URL. NAS: the first Build kept the old container (profiles ignored), the second ran the cached old `latest` (Build does not pull), the third with `compose.nas.yml` and `pull_policy: always` runs the new image as `1026:100` and can write the dem folder. A stale dem-manager entry in Eureka (the old image's `/start.sh` does not pass SIGTERM to Java, so it never deregistered) was removed with a DELETE on the Eureka REST API. Full reparse started: 1,443 demos queued, a few seconds each on the NAS.
- **2026-11-16, UI 502 through Nginx Proxy Manager.** `/ixigoui/services/...` answered 502 publicly but 200 on the LAN: `ForwardHttpReqCmdHandler` copied the proxy's response headers, so the response had `Transfer-Encoding: chunked` twice, which nginx rejects. Before the cutover the UI forwarded through the public HTTPS gateway, which hid it. Fixed by dropping hop-by-hop headers and `Content-Length` when copying; checked publicly (dem files list, users, score types return 200, one `Transfer-Encoding` header).
- **2026-11-16, IDE files out of git.** The 34 tracked Eclipse files (21 `.project`, 13 under `.settings/`) are untracked (`git rm --cached`, still on disk) and `.gitignore` now ignores Eclipse, Spring Tools, IntelliJ, VS Code and NetBeans metadata. The two `.sln` files stay: `dotnet build plugins/IxigoPlugin` on the Azure VM picks the solution when it sits next to the `.csproj`.
