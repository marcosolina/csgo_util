#!/bin/sh
# Runs after 030-initEventDispatcherDb.sql (docker-entrypoint-initdb.d, in name order).
# The seed points every event listener at the public gateway. With IXIGO_LISTENERS_BASE_URL set
# (for example http://192.168.1.24:8763/ixigoproxy, or http://localhost:8763/ixigoproxy for local
# development) the listeners go through that gateway instead.
if [ -n "${IXIGO_LISTENERS_BASE_URL:-}" ]; then
    echo "Event listeners: base URL $IXIGO_LISTENERS_BASE_URL"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname event_dispatcher \
        -v base="$IXIGO_LISTENERS_BASE_URL" <<'EOSQL'
UPDATE event_listeners
   SET url_listener = replace(url_listener, 'https://marco.selfip.net/ixigoproxy', :'base');
EOSQL
fi
