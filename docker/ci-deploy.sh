#!/usr/bin/env bash
# Builds, pushes and deploys the IxiGo stack on this host (the mini PC) from one csgo_util commit.
# The Jenkins job IxiGoDeploy (docker/Jenkinsfile) copies this file to ~/ixigo and runs it over SSH
# on every change to main; it can also be run by hand on the mini PC.
#
#   bash ci-deploy.sh [--force] [--build-only] [<commit>]       (default commit: origin/main)
#
#   --build-only  build and push the images tagged with the commit, nothing else
#   --force       deploy even when only documentation changed or server-helper is running
#
# Steps: skip documentation-only changes; refuse while server-helper is up (game night); build the
# amd64 images and push them as :<commit>, then move :latest to them; copy compose.yml and deploy.sh
# into the deploy folder; remove every container with its anonymous volumes (the Postgres data; the
# named demfiles volume with the .dem files on the NAS is never touched); deploy.sh --reparse with
# TAG=<commit>; wait until every container is healthy and dem-manager has parsed every file; prune
# images and build cache older than a week.
set -euo pipefail

SRC=${IXIGO_SRC:-$HOME/ixigo-src}            # clone used for the build
DEPLOY_DIR=${IXIGO_DIR:-$HOME/ixigo}          # compose.yml, deploy.sh and .env of this host
REPO_URL=https://github.com/marcosolina/csgo_util.git
EUREKA_URL=${EUREKA_URL:-http://localhost:8765/discovery/eureka}
HEALTH_TIMEOUT=300                            # seconds for every container to become healthy
PARSE_TIMEOUT=3600                            # seconds for dem-manager to parse every .dem file
# Changes under these paths do not touch the images or the deploy files
SKIP_PATHS='^(docs/|legacy/|tools/|plugins/|docker/test/|docker/Jenkinsfile$|docker/ci-deploy\.sh$|docker/build-and-push\.ps1$)|\.md$'

force=false
build_only=false
commit=origin/main
for arg in "$@"; do
    case "$arg" in
        --force) force=true ;;
        --build-only) build_only=true ;;
        -*) echo "Unknown option: $arg" >&2; exit 2 ;;
        *) commit=$arg ;;
    esac
done

step() { echo; echo "=== $(date +%H:%M:%S) $*"; }
fail() { echo "ERROR: $*" >&2; exit 1; }

cd "$DEPLOY_DIR"
exec 9>"$DEPLOY_DIR/.deploy.lock"
flock -n 9 || fail "another deploy is running on this host"

step "Source: $commit"
[ -d "$SRC/.git" ] || git clone -q "$REPO_URL" "$SRC"
git -C "$SRC" fetch -q --prune origin
sha=$(git -C "$SRC" rev-parse --verify "$commit^{commit}")
short=$(git -C "$SRC" rev-parse --short=8 "$sha")
git -C "$SRC" checkout -q --detach "$sha"
git -C "$SRC" clean -q -fdx
git -C "$SRC" log -1 --format='%h %s (%an, %cd)' --date=short

deployed=$(cat "$DEPLOY_DIR/.deployed-commit" 2>/dev/null || true)
if ! $build_only && ! $force && [ -n "$deployed" ] && git -C "$SRC" cat-file -e "$deployed^{commit}" 2>/dev/null; then
    changed=$(git -C "$SRC" diff --name-only "$deployed" "$sha")
    relevant=$(printf '%s\n' "$changed" | grep -vE "$SKIP_PATHS" | grep -v '^$' || true)
    if [ -z "$relevant" ]; then
        echo "Nothing that changes the stack since the deployed commit ${deployed:0:8}:"
        printf '%s\n' "${changed:-  (no changes)}" | sed 's/^/  /'
        echo "Not deploying (use --force to deploy anyway)."
        exit 0
    fi
    echo "Changed since the deployed commit ${deployed:0:8}:"
    printf '%s\n' "$relevant" | sed 's/^/  /'
fi

if ! $build_only && ! $force; then
    step "Game night check"
    instances=$(curl -fs -m 10 -H 'Accept: application/json' "$EUREKA_URL/apps/IXIGO-SERVER-HELPER" 2>/dev/null \
        | python3 -c 'import sys, json
for i in json.load(sys.stdin)["application"]["instance"]:
    print("%s:%s" % (i["ipAddr"], i["port"]["$"]))' 2>/dev/null || true)
    for instance in $instances; do
        health=$(curl -fs -m 5 "http://$instance/ixigohelper/actuator/health" 2>/dev/null || true)
        if [[ "$health" == *'"UP"'* ]]; then
            fail "server-helper is running on $instance, so a game may be on. Deploy after the game (rerun the job) or use --force."
        fi
        echo "server-helper $instance is registered in Eureka but does not answer: not running"
    done
    [ -n "$instances" ] || echo "server-helper is not registered: no game running"
fi

step "Build and push the images as :$short (amd64)"
cd "$SRC/docker"     # buildx bake resolves the compose "context: .." against the current directory
export TAG=$short
docker buildx bake --allow=fs.read=.. -f compose.build.yml --set '*.platform=linux/amd64' --push   # the context is ..
images=$(docker compose -f compose.build.yml config --images)
for ref in $images; do
    pushed=$(docker buildx imagetools inspect "$ref" --format '{{json .Image}}')
    [[ "$pushed" == *'"amd64"'* ]] || fail "$ref is not in the registry for amd64"
done
echo "Pushed: $(printf '%s\n' "$images" | sed 's|.*/||' | tr '\n' ' ')"
if $build_only; then
    echo "Build only: :latest and the running containers are unchanged."
    exit 0
fi
for ref in $images; do
    docker buildx imagetools create -t "${ref%:*}:latest" "$ref" > /dev/null
done
echo ":latest now points at :$short"

step "Update the deploy files in $DEPLOY_DIR"
cd "$DEPLOY_DIR"
cp "$SRC/docker/compose.yml" "$SRC/docker/deploy.sh" "$DEPLOY_DIR/"
missing=$(comm -23 <(grep -oE '^[A-Z_]+=' "$SRC/docker/.env.example" | sort -u) <(grep -oE '^[A-Z_]+=' .env | sort -u) | tr -d '=' | tr '\n' ' ')
[ -z "$missing" ] || fail ".env lacks variables that docker/.env.example has: $missing(add them by hand, then rerun)"

step "Stop every container and drop the Postgres data"
docker compose rm --stop --force --volumes

step "Start :$short and re-parse the .dem files"
TAG=$short sh deploy.sh --reparse

step "Wait until every container is healthy"
waited=0
while true; do
    states=$(docker compose ps --format '{{.Service}} {{.Health}}')
    pending=$(printf '%s\n' "$states" | awk '$2 != "healthy"' || true)
    [ -z "$pending" ] && break
    [ "$waited" -ge "$HEALTH_TIMEOUT" ] && fail "not healthy after ${HEALTH_TIMEOUT}s: $(printf '%s\n' "$pending" | tr '\n' ' ')"
    sleep 10; waited=$((waited + 10))
done
echo "All $(printf '%s\n' "$states" | wc -l) containers are healthy"

step "Wait for dem-manager to parse every .dem file"
pg_user=$(grep -E '^POSTGRES_USER=' .env | cut -d= -f2-)
q() { docker compose exec -T postgres psql -U "$pg_user" -d demfiles -tAc "$1" < /dev/null; }
waited=0
while true; do
    todo=$(q "select count(*) from dem_process_queue where process_status = 'NOT_PROCESSED'")
    done_count=$(q "select count(*) from dem_process_queue where process_status = 'PROCESSED'")
    failed=$(q "select count(*) from dem_process_queue where process_status = 'PROCESS_FAILED'")
    if [ "$todo" = 0 ] && [ $((done_count + failed)) -gt 0 ]; then break; fi
    [ "$waited" -ge "$PARSE_TIMEOUT" ] && fail "still $todo files to parse after ${PARSE_TIMEOUT}s"
    [ $((waited % 120)) = 0 ] && echo "$(date +%H:%M:%S) parsed $done_count, failed $failed, left $todo"
    sleep 30; waited=$((waited + 30))
done
matches=$(q "select count(*) from match_stats")
duplicates=$(q "select count(*) from (select match_filename from match_stats group by 1 having count(*) > 1) x")
echo "Parsed $done_count files ($failed failed), $matches matches, $duplicates files saved more than once"
echo "$sha" > "$DEPLOY_DIR/.deployed-commit"
[ "$duplicates" = 0 ] || fail "$duplicates .dem files were saved as more than one match"

step "Prune images and build cache older than a week"
docker image prune -af --filter until=168h | tail -1
docker builder prune -f --filter until=168h | tail -1

step "Deployed $short"
