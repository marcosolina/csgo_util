export const PROXY_BASE_PATH =
  (window as { [key: string]: any })["srvContextPath"] || "http://localhost:8763/ixigoproxy";
export const UI_CONTEXT_PATH = (window as { [key: string]: any })["uiContextPath"] || "";

export const SERVICES_URLS = {
  "dem-manager": {
    "gateway-path": "ixigo-dem-manager",
    "base-url": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager",
    "post-dem-file": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/files",
    "get-dem-file": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/files", // Append the file name as path param
    "get-all-dem-files": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/files",
    "delete-dem-file-from-queue": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/files", // Append the file name as path param
    "post-parse-queued-files": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/parse/queued",
    "post-parse-all-files": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/parse/all",
    "get-dem-data-scores-type": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/demdata/scorestype",
    "get-dem-data-maps-played": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/demdata/mapsplayed",
    "get-dem-data-users": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/demdata/users",
    "get-dem-data-users-scores": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/demdata/usersscores",
  },
  "event-dispatcher": {
    "gateway-path": "ixigo-event-dispatcher",
    "base-url": PROXY_BASE_PATH + "/ixigo-event-dispatcher/eventsdispatcher",
    "post-event": PROXY_BASE_PATH + "/ixigo-event-dispatcher/eventsdispatcher/event",
    "post-listener": PROXY_BASE_PATH + "/ixigo-event-dispatcher/eventsdispatcher/listener",
    "delete-listener": PROXY_BASE_PATH + "/ixigo-event-dispatcher/eventsdispatcher/listener",
  },
  "players-manager": {
    "gateway-path": "ixigo-players-manager",
    "base-url": PROXY_BASE_PATH + "/ixigo-players-manager/playersmanager",
    "get-teams": PROXY_BASE_PATH + "/ixigo-players-manager/playersmanager/teams",
  },
  "server-helper": {
    "get-maps": PROXY_BASE_PATH + "/ixigo-server-helper/ixigohelper/maps",
  },
  "rcon-api": {
    "gateway-path": "ixigo-rcon-api",
    "base-url": PROXY_BASE_PATH + "/ixigo-rcon-api/rcon",
    "post-command": PROXY_BASE_PATH + "/ixigo-rcon-api/rcon/cmd",
  },
};
