# Automatic deploy from Jenkins (temporary file, delete when the work is done)

Goal: every change that lands on `main` is built and deployed to the mini PC without manual steps. A Jenkins job connects to the mini PC over SSH, builds and pushes the images, restarts every container from the new images with an empty database, and waits for `parse/all` to rebuild the stats from the `.dem` files.

## Part 1: Jenkins on the mini PC (done 2026-11-17)

Jenkins ran on the `BuildServer` VM (192.168.1.199, a KVM guest on the mini PC: 1 CPU, 3.8 GB, 6.7 GB free, no Docker) as `java -jar Jenkins.war --prefix=/jenkins`, started from `rc.local`. It now runs in a container on the mini PC.

- [x] Disabled the old jobs that deploy jars on a push: `IxiGoUtils` (csgo_util; its runs for PRs #193 to #195 failed before deploying anything), `ConfigServer`, `DiscoveryService`, `ZuulService` (old separate repos, deploying to rasp26).
- [x] Container definition in Marco's `docker` repo, folder `jenkins/` (`c5231a7`): official `jenkins/jenkins:2.387.3-lts-jdk17` (the same version, so the plugins keep working) plus Maven 3.9.9; port 8080, `--prefix=/jenkins`, 2 GB limit. On the mini PC in `~/jenkins`, data in `~/jenkins/jenkins_home`.
- [x] Dry run on port 18080 against a copy with 0 executors: clean start, SSH with the migrated key to docker.lan and rasp26.
- [x] Cutover: old Jenkins stopped (start line commented in the VM's `~/startApps.sh`), final copy of `~/.jenkins`, `~/.ssh/id_rsa`, `known_hosts` and `.gitconfig`; only `IxiDeployVm`, `Java Utils` and `TapoCSharp` carried over (the other jobs stay in the `jobs` git history and on the VM). Nginx Proxy Manager `/jenkins` points at `192.168.1.24:8080`. Checked: login session kept, `github` credential decrypts, the GitHub webhook endpoint answers.
- [x] Azure test (VM `20.0.233.180`): the Azure pipeline started `IxiDeployVm` #203 remotely as `azurepipeline`, the migrated key logged into the VM, the build succeeded, server-helper registered in Eureka and updated DynDNS at startup (`good 20.0.233.180`, the PR #194 fix).
- [x] The Jenkins key (`marco@buildserver`) is in the mini PC `authorized_keys` with `from="172.16.0.0/12"` (only Docker containers on the mini PC; the same key from the LAN is refused), and the mini PC host key is pinned in the Jenkins `known_hosts`.
- [x] `BuildServer` VM shut down, autostart disabled. Delete it in a few weeks: `virsh -c qemu:///system undefine BuildServer --remove-all-storage`.
- [ ] Later: upgrade Jenkins and its plugins (2.387.3 is from May 2023 and public).
- [ ] `TapoCSharp`'s deploy script runs `docker system prune --all --volumes --force` on docker.lan, which also runs Immich (fix in that repo).

## Part 2: the deploy job

Decisions: build on the mini PC over SSH, logic in this repo; amd64 only; fail while server-helper is running; wait for the reparse and report; the job is created in the Jenkins UI.

- `docker/Jenkinsfile`: Pipeline from SCM (`*/main`), `githubPush()` trigger, one build at a time, 90-minute timeout. Parameters `COMMIT` (redeploy or roll back), `FORCE`, `BUILD_ONLY`. It copies `docker/ci-deploy.sh` to `~/ixigo` on the mini PC and runs it over SSH.
- `docker/ci-deploy.sh` (on the mini PC, also by hand): lock; check out the commit in `~/ixigo-src`; skip when only documentation changed since `~/ixigo/.deployed-commit`; refuse while server-helper answers its health check; build amd64 with `buildx bake` and push `:<commit>`, then move `:latest`; copy `compose.yml` and `deploy.sh`, fail if `.env` lacks a variable of `.env.example`; remove every container with its anonymous volumes (Postgres data only, never the `demfiles` volume); `TAG=<commit> sh deploy.sh --reparse`; wait for every healthcheck and for the parse queue to empty; report parsed, failed, matches and duplicates; prune images and build cache older than a week.

Tasks:

- [x] `docker/ci-deploy.sh` and `docker/Jenkinsfile` (branch `marco/jenkins-deploy`).
- [x] Test `ci-deploy.sh --build-only` by hand on the mini PC: all 12 images built there and pushed as `:7952745c` in 3 minutes from an empty cache. buildx 0.39 needs `--allow=fs.read=..` for the `..` context.
- [ ] Test a full `ci-deploy.sh` run by hand (no game on): started 2026-11-17 11:44; build from cache, `:latest` moved, containers replaced and all 11 healthy within about a minute, reparse running.
- [ ] Merge, then create the `IxiGoDeploy` job in the Jenkins UI and run it once by hand (that also registers the webhook trigger).
- [ ] Check a documentation-only merge is skipped and a code merge is deployed.
- [ ] CLAUDE.md and README: describe the automatic deploy and the rollback.
- [ ] Delete this file.
