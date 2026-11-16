# Automatic deploy from Jenkins (temporary file, delete when the work is done)

Goal: every change that lands on `main` is built and deployed to the mini PC without manual steps. A Jenkins job connects to the mini PC over SSH, builds and pushes the images, restarts every container from the new images with an empty database, and triggers `parse/all` once the services are up, since the stats are rebuilt from the `.dem` files.

Planned for 2026-11-17. First step: Marco shows how Jenkins is configured (see "To look at in Jenkins"), then the open decisions below are settled and the boxes ticked as the work goes.

## Where we start from

- The mini PC (`marco@192.168.1.24`, `~/ixigo`) runs all 11 containers from `docker/compose.yml` with `TAG=latest`; its `.env` holds the secrets and is never in git. `sh deploy.sh --reparse` already does pull, drop Postgres and its data, `up -d`, then `parse/all` once dem-manager is healthy.
- Images are built today by hand on Marco's Windows PC with `docker/build-and-push.ps1` (amd64 and arm64, tags `latest` and the short commit).
- Mini PC facts (checked 2026-11-16): buildx 0.39 with only the `default` builder, containerd image store (can hold multi-arch images), **no QEMU for arm64**, **no PowerShell**, git 2.43, 430 GB free, already logged in to `docker-registry.marco.selfip.net`.
- The old csgo_util Jenkins job (jars to the Pis) must not run any more; it is replaced by this one (PR #195 deletes its scripts).

## Proposed flow

1. **Trigger:** a push to `main` (merged PR).
2. **Skip what does not change the stack:** if the commits since the last successful deploy only touch `*.md`, `docs/`, `legacy/`, `tools/` or `plugins/` (the plugin is built on the Azure VM, not here), stop with success and do not deploy.
3. **Game night guard:** if `IXIGO-SERVER-HELPER` is registered in Eureka (`http://192.168.1.24:8765/discovery/eureka/apps`), the Azure VM is up and a game may be running: fail with a clear message instead of wiping the database and restarting the Discord bot mid game. Rerun the job after the game.
4. **Build and push first, while the current stack keeps running.** On the mini PC, in a dedicated clone (for example `~/ixigo-src`), check out exactly the commit Jenkins is building, build all images and push them as `:<short commit>`, then point `:latest` at them with `docker buildx imagetools create` (no rebuild). If the build fails, nothing is stopped and production stays as it is. This is a change to the order Marco described (stop and wipe, then build): building first avoids an outage when a build breaks.
5. **Update the deploy files:** copy `docker/compose.yml` and `docker/deploy.sh` from the checkout into `~/ixigo` (never `.env`). Fail if `.env` lacks a variable that `docker/.env.example` has, so a new setting is added by hand before the deploy.
6. **Stop, wipe, start:** stop every container, drop Postgres with its data, start every container again from the new images (`up -d --force-recreate`, so all of them restart even when an image did not change), and wait until every healthcheck is healthy.
7. **Reparse:** `parse/all` (the `reparse` service). Optionally wait for the queue to empty and report the parsed, failed and duplicate counts, as in the manual reparse (about 15 minutes for 1,420 demos).
8. **Clean up:** remove ixigo images that no container uses any more (`docker image prune`, limited to old images), so the disk does not fill up with one set per merge.
9. **Report:** the job result (and later maybe a Discord or Telegram message through the notification service).

What "wipe all the data" means here: **only the Postgres data**. The `.dem` files on the NAS (`demfiles` volume, about 50 GB, the only source for the stats) must never be touched: no `docker compose down -v`, no `docker volume prune -a`, no `docker system prune --volumes` (the old `deployJars.sh` did that). Logs inside the containers go with the containers.

## Decisions to make

- [ ] **Where the logic lives.** Recommended: in this repo, so it is versioned with the code: a `Jenkinsfile` (pipeline from SCM, `disableConcurrentBuilds()`) that only connects over SSH and calls one script, for example `docker/ci-deploy.sh`, which does steps 4 to 8 on the mini PC. The same script can then also be run by hand.
- [ ] **Where to build.** Recommended: on the mini PC (it already has buildx, git, the registry login and the BuildKit cache), so Jenkins needs nothing but SSH. Alternative: on the Jenkins machine, if it has Docker.
- [ ] **arm64.** No arm64 host runs anything since the cutover, and the mini PC has no QEMU. Recommended: build amd64 only in this job (faster, nothing to install); keep arm64 as an option in `compose.build.yml` for manual builds. Otherwise install `tonistiigi/binfmt` and a `docker-container` builder on the mini PC.
- [ ] **Build script.** `build-and-push.ps1` needs PowerShell, which the mini PC does not have. Recommended: the bash script calls `docker buildx bake -f compose.build.yml` directly (from `docker/`, see the bake gotcha in CLAUDE.md) and checks the pushed platforms; keep the `.ps1` for builds from Windows.
- [ ] **Trigger.** A GitHub webhook needs Jenkins reachable from the internet (a path in Nginx Proxy Manager plus a webhook secret); otherwise poll the repository every few minutes (`pollSCM('H/5 * * * *')`), which needs nothing public.
- [ ] **Game night guard:** fail (recommended) or wait until the VM is gone.
- [ ] **Wait for the reparse to finish** in the job, or report success once `parse/all` is accepted.

## To look at in Jenkins

- Where Jenkins runs (host, Docker or not), its version, and its agents.
- How the old csgo_util job and the Azure VM job are defined (freestyle or pipeline, inline script or `Jenkinsfile`), and how they connect to other hosts.
- Installed plugins: Git, Pipeline, SSH Agent, GitHub, Credentials Binding.
- The credentials it already holds (an SSH key that the mini PC could accept, GitHub access).
- Whether Jenkins is reachable from GitHub (for a webhook).

## Tasks

- [ ] Jenkins: an SSH key credential for `marco@192.168.1.24` (a dedicated key in the mini PC's `authorized_keys`), with the mini PC host key pinned.
- [ ] Mini PC: the build clone `~/ixigo-src`.
- [ ] `docker/ci-deploy.sh` (steps 4 to 8), with a lock so a manual deploy and a Jenkins deploy cannot overlap.
- [ ] `Jenkinsfile` (trigger, path filter, game night guard, SSH call, result).
- [ ] The Jenkins job pointing at the `Jenkinsfile` on `main`; delete the old csgo_util job.
- [ ] Test: a run by hand on a quiet evening, then a real merge (a README change should be skipped, a code change deployed).
- [ ] CLAUDE.md "Deployment" section and README: describe the automatic deploy and the rollback (`TAG=<commit>` in the mini PC `.env`, then `sh deploy.sh --reparse`).
- [ ] Delete this file.
