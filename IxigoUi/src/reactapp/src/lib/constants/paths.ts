export const PROXY_BASE_PATH =
  (window as { [key: string]: any })["srvContextPath"] || "https://marco.selfip.net/ixigoproxy"; //"http://localhost:8763/ixigoproxy";
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
    "get-dem-data-users": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/demdata/users",
    "get-dem-data-users-scores": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/demdata/usersscores",
    "get-stats": PROXY_BASE_PATH + "/ixigo-dem-manager/demmanager/charts/view",
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
    "get-scores-type": PROXY_BASE_PATH + "/ixigo-players-manager/playersmanager/teams/scorestype",
  },
  "server-helper": {
    "get-maps": PROXY_BASE_PATH + "/ixigo-server-helper/ixigohelper/maps",
  },
  "rcon-api": {
    "gateway-path": "ixigo-rcon-api",
    "base-url": PROXY_BASE_PATH + "/ixigo-rcon-api/rcon",
    "post-command": PROXY_BASE_PATH + "/ixigo-rcon-api/rcon/cmd",
  },
  "discord-bot": {
    "gateway-path": "ixigo-discord-bot",
    "get-config-all": PROXY_BASE_PATH + "/ixigo-discord-bot/discordbot/config",
    "get-config": PROXY_BASE_PATH + "/ixigo-discord-bot/discordbot/config",
    "put-config": PROXY_BASE_PATH + "/ixigo-discord-bot/discordbot/config",
    "get-mapped-players": PROXY_BASE_PATH + "/ixigo-discord-bot/discordbot/users/mapping",
    "put-mapped-players": PROXY_BASE_PATH + "/ixigo-discord-bot/discordbot/users/mapping",
    "get-discord-channel-members": PROXY_BASE_PATH + "/ixigo-discord-bot/discordbot/users/discord",
  },
  "ixigo-server": {
    "gateway-path": "ixigo-server-helper",
    "get-csgo-config": PROXY_BASE_PATH + "/ixigo-server-helper/ixigohelper/config/csgo",
  },
};
