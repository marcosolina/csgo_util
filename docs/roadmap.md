# Roadmap

Open work after the move to Docker (cutover on 2026-11-16). The plan of that move and its progress log are in git history: `DOCKERIZATION_PLAN.md`, deleted in the commit that added this file.

## Plan B

- [ ] **B1 Replace Zuul with an nginx container** keeping the same URLs: `/ixigoproxy/<service-id>/...` to `http://<host>:<port>/...` from an env-templated config, with the 500 MB upload limit, 120 s timeouts and CORS headers. No change for the UI, the listener rows, the VM scripts or `csgo_props`. Removes `IxigoProxy` (Spring Boot 2.3 / Hoxton, end of life). It also fixes a Zuul quirk: with `zuul.servlet-path: /`, any path without a route gets an empty 200, so the proxy healthcheck only proves that Tomcat answers, and the event dispatcher counts a post to a service that is not registered (server-helper while the VM is off) as a success.
- [ ] **B2 Drop Eureka**, only if server-helper can be routed statically: nginx would proxy `ixigo-server-helper` to the DynDNS name `ixigo.selfip.net:8084` (re-resolved with a `resolver` and a variable `proxy_pass`). This needs DynDNS to be reliable on the Azure VM (server-helper updates it at startup since PR #194); otherwise keep Eureka for server-helper only. Then disable the client, remove the starter from `IxigoParent`, delete `IxigoDiscovery`, and stop exposing `/discovery` publicly.
- [ ] **B3 Postgres 11 to a supported major** (16 or 17). Low risk, since the database is rebuilt on every deploy.
- [ ] **B4 (optional) CI build and push** from GitHub Actions, with the registry credentials as repository secrets.

Keep the Config Server: it centralizes the profiles and holds the encrypted values in `csgo_props`.

## server-helper as a Docker image

Today server-helper is a jar built with Maven on every new Azure VM (`ixi_go/Scripts/jenkins/cs2.tokenised.sh`). The `server-helper` image already exists: the VM could `docker run` it instead, with no JDK, Maven or git build on the VM (faster provisioning). It needs Docker on the VM, mounts for the CS2 folder and `/run/screen/S-<user>`, the CS2 user's uid so `screen -S cs2 -X stuff` works, and `EUREKA_INSTANCE_IP_ADDRESS` set to the VM's public IP. The screen socket from inside the container is the part to test first.

## Known issues

- [ ] `tools/IxigoIntegrationTests` has not compiled since September 2023: its step classes import DemManager contract classes that were removed or moved (for example `rest.demdata.RestMapStats` and `rest.charts.RestMapsPlayed`).
- [ ] Two CS2 demos from 2026-05-04 (`de_cache` and `de_safehouse`) are real matches, but the parser finds their rounds and no players, so they have no player stats.
- [ ] Remove `ixigoservices.lan` from Pi-hole if it was added by hand (the VM is deleted).

## Not planned

Spring Boot 3 / Java 17+ (springfox and `javax` to `jakarta`), schema migrations, unit tests.
