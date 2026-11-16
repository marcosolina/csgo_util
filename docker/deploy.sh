#!/bin/sh
# Deploys this host: pulls the images and (re)starts the services that its .env selects.
# On the db host it first removes Postgres and its data, so the database starts again from the
# seed SQL in the image (it is rebuilt from the .dem files on every deploy).
#
#   sh deploy.sh             this host
#   sh deploy.sh --reparse   this host, then dem-manager re-parses every .dem file
#
# Full deploy order: db, infra, gateway, services, dem, then the reparse from any host.
set -eu
cd "$(dirname "$0")"

reparse=false
for arg in "$@"; do
    case "$arg" in
        --reparse) reparse=true ;;
        *) echo "Unknown option: $arg" >&2; exit 2 ;;
    esac
done

docker compose pull

if docker compose config --services | grep -qx postgres; then
    echo "Recreating Postgres from the seed SQL (the current data is dropped)"
    docker compose rm --stop --force --volumes postgres
fi

docker compose up -d

if [ "$reparse" = true ]; then
    docker compose run --rm reparse
fi

docker compose ps
