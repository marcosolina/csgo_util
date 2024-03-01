import { parseEvents, parseTicks } from "@laihoe/demoparser2";
//import fs from "fs";

const filePath = process.argv[2]!;

interface IPlayerRoundStats {
  userName: string;
  steamID: string;
  round: number;
  team: number;
  clutchChance: number;
  clutchSuccess: boolean;
  survived: boolean;
  moneySpent: number;
  equipmentValue: number;
  mvp: boolean;
}

interface IRoundEvents {
  eventtype: string;
  eventtime: number;
  steamid: string;
  round: number;
}

interface IRoundKillEvents {
  eventtime: number;
  steamID: string;
  assister: string | undefined;
  flashAssister: string | undefined;
  killerFlashed: boolean | undefined;
  round: number;
  weapon: string;
  headshot: boolean;
  victimSteamId: string | undefined;
  isFirstKill: boolean;
  isTradeKill: boolean;
  isTradeDeath: boolean;
}

interface IRoundShotEvents {
  eventType: string;
  eventtime: number;
  steamID: string;
  round: number;
  weapon: string;
}

interface IRoundHitEvents {
  eventtime: number;
  steamID: string;
  round: number;
  weapon: string;
  victimSteamId: string;
  hitGroup: number;
  damageHealth: number;
  damageArmour: number;
  blindTime: number | undefined;
}

interface IPlayerStats {
  userName: string;
  steamID: string;
  score: number;
}

interface IRoundStats {
  roundNumber: number;
  winnerSide: number;
  reasonEndRound: number;
}

class MapStats {
  match_date: Date | null;
  mapName: string;
  match_filename: string;

  constructor(match_filename: string) {
    this.match_date = null;
    this.mapName = "";
    this.match_filename = match_filename;
  }
}

interface IMergedStats {
  mapStats: MapStats;
  allPlayerStats: IPlayerStats[];
  allRoundStats: IRoundStats[];
  allPlayerRoundStats: IPlayerRoundStats[];
  allRoundKillEvents: IRoundKillEvents[];
  allRoundShotEvents: IRoundShotEvents[];
  allRoundHitEvents: IRoundHitEvents[];
  allRoundEvents: IRoundEvents[];
}

const CT_TEAM_NUM = 3;
const T_TEAM_NUM = 2;
const SPECTATOR_TEAM_NUM = 1;
const UNASSIGNED_TEAM_NUM = 0;

function fromWinnerSideStringToNumber(winnerSide: any): number {
  if (typeof winnerSide === "number") {
    return winnerSide;
  }

  switch (winnerSide) {
    case "CT":
      return CT_TEAM_NUM;
    case "T":
      return T_TEAM_NUM;
    case "SPECT":
      return SPECTATOR_TEAM_NUM;
    default:
      return UNASSIGNED_TEAM_NUM;
  }
}

function fromReasonEndStringToNumber(reasonEnd: any): number {
  if (typeof reasonEnd === "number") {
    return reasonEnd;
  }

  switch (reasonEnd) {
    case "bomb_exploded":
      return 1;
    case "bomb_defused":
      return 7;
    case "t_killed":
      return 8;
    case "ct_killed":
      return 9;
    case "hostage_rescued":
      return 11;
    case "TargetSaved":
      return 12;
    case "HostageNotRescued":
      return 13;
    default:
      return -1;
  }
}

// Define all event names we're interested in
const eventNames = [
  "begin_new_match",
  "round_start",
  "round_end",
  "round_mvp",
  "player_death",
  "bomb_planted",
  "bomb_defused",
  "hostage_rescued",
  "weapon_fire",
  "flashbang_detonate",
  "hegrenade_detonate",
  "molotov_detonate",
  "smokegrenade_detonate",
  "player_hurt",
  "player_blind",
];

let allEvents = parseEvents(filePath, eventNames, [
  "game_time",
  "team_num",
  "match_start_time",
  "game_start_time",
  "is_match_started",
  "total_rounds_played",
]); // Fetch all events at once

const roundStartEvents = allEvents.filter((event: any) => event.event_name === "round_start");
// Find the last 'round_start' event with where the match is not started to account for any restarts
const lastRestartRoundStartEvent = roundStartEvents
  .slice()
  .reverse()
  .find((event: any) => event.is_match_started == false);
// Use the tick of this event as the start of the match, or default to 0 if not found
const matchStartTick = lastRestartRoundStartEvent ? lastRestartRoundStartEvent.tick : 0;

const roundEndEvents = allEvents.filter(
  (event: any) => event.event_name === "round_end" && event.tick >= matchStartTick
);

function preprocessRoundTicks(): Array<{ start: number; end: number }> {
  let roundRanges: Array<{ start: number; end: number }> = [];
  let startIndex = 0;

  for (let i = 0; i < roundEndEvents.length; i++) {
    // Skip end events before the official match start
    if (roundEndEvents[i].tick < matchStartTick) continue;

    // Find the corresponding round_start event based on tick values
    while (startIndex < roundStartEvents.length && roundStartEvents[startIndex].tick < roundEndEvents[i].tick) {
      startIndex++;
    }

    let startTick = startIndex > 0 ? roundStartEvents[startIndex - 1].tick : 0;
    roundRanges.push({ start: startTick, end: roundEndEvents[i].tick });
  }

  return roundRanges;
}

const roundRanges = preprocessRoundTicks();

// Filter events ready for use later
const mvpEvents = allEvents.filter((event: any) => event.event_name === "round_mvp" && event.tick >= matchStartTick);
const playerDeaths = allEvents.filter(
  (event: any) => event.event_name === "player_death" && event.tick >= matchStartTick
);
allEvents = allEvents.filter((event: any) => event.tick >= matchStartTick);

let shotEvents = allEvents.filter((event: any) => event.event_name === "weapon_fire");
let hitEvents = allEvents.filter((event: any) => event.event_name === "player_hurt");
let flashEvents = allEvents.filter((event: any) => event.event_name === "player_blind");

// Fetch grenade detonation events
let flashbangDetonateEvents = allEvents.filter((event: any) => event.event_name === "flashbang_detonate");
let heGrenadeDetonateEvents = allEvents.filter((event: any) => event.event_name === "hegrenade_detonate");
let molotovDetonateEvents = allEvents.filter((event: any) => event.event_name === "molotov_detonate");
let smokeGrenadeDetonateEvents = allEvents.filter((event: any) => event.event_name === "smokegrenade_detonate");

const roundEndTicks = roundEndEvents.map((event: any) => event.tick);

// Get ticks with metadata
const allTicksArray = parseTicks(
  filePath,
  [
    "round_start_equip_value",
    "cash_spent_this_round",
    "is_alive",
    "team_num",
    "player_name",
    "score",
    "player_steamid",
  ],
  allEvents.map((event: any) => event.tick)
); // Fetch all tick data once

const roundEndTickData = allTicksArray.filter((tick: any) => roundEndTicks.includes(tick.tick));
const gameEndTick = Math.max(...roundEndEvents.map((event: any) => event.tick));

// Convert the array of tick data into a map for efficient access
const allTicksMap = new Map();
allTicksArray.forEach((tick: any) => {
  if (!allTicksMap.has(tick.tick)) {
    allTicksMap.set(tick.tick, []);
  }
  allTicksMap.get(tick.tick).push(tick);
});
const scoreboard = allTicksMap.get(gameEndTick) || [];

// Caching results for findRoundForTick
const roundForTickCache = new Map();
function findRoundForTick(tick: number) {
  if (roundForTickCache.has(tick)) {
    return roundForTickCache.get(tick);
  }

  for (let i = 0; i < roundRanges.length; i++) {
    const nextRoundStart = i + 1 < roundRanges.length ? roundRanges[i + 1].start : Number.MAX_SAFE_INTEGER;
    if (tick >= roundRanges[i].start && tick < nextRoundStart) {
      roundForTickCache.set(tick, i + 1); // Cache the result
      return i + 1;
    }
  }
  roundForTickCache.set(tick, -1);
  return -1;
}

let allPlayerRoundStats: IPlayerRoundStats[] = roundEndTickData
  .filter((tick: any) => tick.team_num === CT_TEAM_NUM || tick.team_num === T_TEAM_NUM)
  .map((tick: any): IPlayerRoundStats => {
    let isMVP = mvpEvents.some((mvp: any) => mvp.tick === tick.tick && mvp.user_steamid === tick.steamid);
    return {
      userName: tick.player_name,
      steamID: tick.steamid,
      round: findRoundForTick(tick.tick),
      team: tick.team_num,
      survived: tick.is_alive,
      moneySpent: tick.cash_spent_this_round,
      equipmentValue: tick.round_start_equip_value + tick.cash_spent_this_round,
      clutchChance: 0, //to be updated later
      clutchSuccess: false, //to be updated later
      mvp: isMVP,
    };
  });

let clutchChance = new Map<number, { steamID: string; clutchChance: number; clutchSuccess: boolean }[]>();
let alivePlayersPerRound = new Map<number, { ct: Set<string>; t: Set<string> }>();

playerDeaths.forEach((event: any) => {
  let round = findRoundForTick(event.tick) ?? -1;
  if (!alivePlayersPerRound.has(round)) {
    alivePlayersPerRound.set(round, { ct: new Set(), t: new Set() });
  }

  let alivePlayers = alivePlayersPerRound.get(round);
  if (alivePlayers) {
    const tickDataAtEvent = allTicksMap.get(event.tick) || [];
    tickDataAtEvent.forEach((player: { is_alive: boolean; team_num: number; player_steamid: string }) => {
      if (player.is_alive && event.user_steamid !== player.player_steamid) {
        if (player.team_num === CT_TEAM_NUM) alivePlayers?.ct.add(player.player_steamid);
        if (player.team_num === T_TEAM_NUM) alivePlayers?.t.add(player.player_steamid);
      } else if (event.user_steamid === player.player_steamid) {
        if (player.team_num === CT_TEAM_NUM) alivePlayers?.ct.delete(event.user_steamid);
        if (player.team_num === T_TEAM_NUM) alivePlayers?.t.delete(event.user_steamid);
      }
    });
    // Check for clutch chances separately for each team
    if (alivePlayers.ct.size === 1 && alivePlayers.t.size > 0) {
      let survivingCTPlayer = [...alivePlayers.ct][0];
      updateClutchChance(round, survivingCTPlayer, alivePlayers.t.size);
    }
    if (alivePlayers.t.size === 1 && alivePlayers.ct.size > 0) {
      let survivingTPlayer = [...alivePlayers.t][0];
      updateClutchChance(round, survivingTPlayer, alivePlayers.ct.size);
    }
  }
});

function updateClutchChance(round: number, survivingPlayer: string, opponentsAlive: number) {
  let roundClutchChance = clutchChance.get(round) || [];
  if (!roundClutchChance.some((clutch) => clutch.steamID === survivingPlayer)) {
    roundClutchChance.push({ steamID: survivingPlayer, clutchChance: opponentsAlive, clutchSuccess: false });
    clutchChance.set(round, roundClutchChance);
  }
}

// Logic for round_end to determine clutch success
roundEndEvents.forEach((endEvent: any) => {
  let round = findRoundForTick(endEvent.tick) ?? -1;
  let roundclutchChance = clutchChance.get(round);

  if (roundclutchChance) {
    roundclutchChance.forEach((clutch) => {
      let endTickData = allTicksMap.get(endEvent.tick) || [];
      let clutchPlayerData = endTickData.find((player: any) => player.steamid === clutch.steamID);

      if (clutchPlayerData && clutchPlayerData.team_num === endEvent.winner) {
        clutch.clutchSuccess = true;
      }
    });
  }
});

// Updating allPlayerRoundStats with clutch information
allPlayerRoundStats.forEach((stat) => {
  let roundclutchChance = clutchChance.get(stat.round);
  if (roundclutchChance) {
    let clutch = roundclutchChance.find((clutch) => clutch.steamID === stat.steamID);
    if (clutch) {
      stat.clutchChance = clutch.clutchChance;
      stat.clutchSuccess = clutch.clutchSuccess;
    }
  }
});

let allRoundStats: IRoundStats[] = roundEndEvents.map(
  (event: any, index: number): IRoundStats => ({
    roundNumber: index + 1, // Assuming rounds are in sequential order
    winnerSide: fromWinnerSideStringToNumber(event.winner), // Mapping might be needed based on how winners are represented
    reasonEndRound: fromReasonEndStringToNumber(event.reason), // Assuming reason is a numerical value
  })
);

let allRoundEvents: IRoundEvents[] = allEvents
  .filter(
    (event: any) =>
      event.event_name === "bomb_planted" ||
      event.event_name === "bomb_defused" ||
      event.event_name === "hostage_rescued"
  )
  .map(
    (event: any): IRoundEvents => ({
      eventtype: event.event_name, // Will be one of the filtered event types
      eventtime: event.game_time,
      steamid: event.user_steamid,
      round: findRoundForTick(event.tick) ?? -1,
    })
  );

let roundKills = new Map<number, { time: number; killer: string; victim: string; victimTeam: number }[]>();

// First Pass - Record All Kills
playerDeaths.forEach((event: any) => {
  let round = findRoundForTick(event.tick) ?? -1;
  let kills = roundKills.get(round) || [];
  kills.push({
    time: event.game_time,
    killer: event.attacker_steamid,
    victim: event.user_steamid,
    victimTeam: event.team_num,
  });
  roundKills.set(round, kills);
});

// Second Pass - Determine Trade Kills and Deaths
let allRoundKillEvents: IRoundKillEvents[] = playerDeaths.map((event: any): IRoundKillEvents => {
  let round = findRoundForTick(event.tick) ?? -1;
  let kills = roundKills.get(round) || [];
  let isEntryKill = kills.length > 0 && kills[0].time === event.game_time;
  let isTradeKill = kills.some(
    (kill) =>
      kill.killer !== event.attacker_steamid &&
      Math.abs(kill.time - event.game_time) <= 5 &&
      kill.victimTeam === event.team_num && // Check if a teammate of the current attacker was killed
      kill.victim !== event.attacker_steamid
  ); // Ensure the current attacker wasn't the one killed

  let isTradeDeath = kills.some(
    (kill) =>
      kill.killer !== event.attacker_steamid &&
      Math.abs(kill.time - event.game_time) <= 5 &&
      kill.victimTeam === event.team_num
  );

  return {
    eventtime: event.game_time,
    steamID: event.attacker_steamid,
    flashAssister: event.assistedflash ? event.assister_steamid : undefined,
    killerFlashed: event.attackerblind,
    round: round,
    victimSteamId: event.user_steamid,
    assister: event.assister_steamid,
    weapon: event.weapon,
    headshot: event.headshot,
    isFirstKill: isEntryKill,
    isTradeKill: isTradeKill,
    isTradeDeath: isTradeDeath,
  };
});

// Combine all shot and grenade detonation events
let combinedEvents = [
  ...shotEvents,
  ...flashbangDetonateEvents,
  ...heGrenadeDetonateEvents,
  ...molotovDetonateEvents,
  ...smokeGrenadeDetonateEvents,
].filter((event: any) => event.tick >= matchStartTick);

let allRoundShotEvents: IRoundShotEvents[] = combinedEvents.map((event: any): IRoundShotEvents => {
  let weaponType;
  let weapon;
  switch (event.event_name) {
    case "flashbang_detonate":
      weaponType = "flashbang";
      weapon = "flashbang";
      break;
    case "hegrenade_detonate":
      weaponType = "hegrenade";
      weapon = "hegrenade";
      break;
    case "molotovDetonate":
      weaponType = "inferno";
      weapon = "inferno";
      break;
    case "smokegrenade_detonate":
      weaponType = "smokegrenade";
      weapon = "smokegrenade";
      break;
    default:
      weaponType = "weapon_fire";
      weapon = event.weapon;
  }
  return {
    eventType: weaponType,
    eventtime: event.game_time,
    steamID: event.user_steamid,
    weapon: weapon,
    round: findRoundForTick(event.tick),
  };
});

let allRoundHitEvents: IRoundHitEvents[] = hitEvents.map(
  (event: any): IRoundHitEvents => ({
    eventtime: event.game_time,
    steamID: event.attacker_steamid,
    round: findRoundForTick(event.tick),
    weapon: "weapon_" + event.weapon,
    victimSteamId: event.user_steamid,
    hitGroup: event.hitgroup,
    damageHealth: event.dmg_health,
    damageArmour: event.dmg_armor,
    blindTime: undefined,
  })
);

// Mapping flash events
flashEvents.forEach((event: any) => {
  allRoundHitEvents.push({
    eventtime: event.game_time,
    steamID: event.attacker_steamid,
    round: findRoundForTick(event.tick) ?? -1,
    weapon: "flashbang",
    victimSteamId: event.user_steamid,
    hitGroup: 0,
    damageHealth: 0,
    damageArmour: 0,
    blindTime: event.blind_duration,
  });
});

let allPlayerStats: IPlayerStats[] = scoreboard
  .filter((player: any) => {
    // Find the last tick data for the player
    const playerLastTickData = allTicksMap.get(gameEndTick).find((tick: any) => tick.steamid === player.steamid);
    // Check if the player's team_num is either 2 (T_TEAM_NUM) or 3 (CT_TEAM_NUM)
    return (
      playerLastTickData && (playerLastTickData.team_num === CT_TEAM_NUM || playerLastTickData.team_num === T_TEAM_NUM)
    );
  })
  .map(
    (player: any): IPlayerStats => ({
      userName: player.name,
      steamID: player.steamid,
      score: player.score,
    })
  );

// Empty arrays to hold our data
let mapStats: MapStats;
mapStats = new MapStats(filePath);

const mergedStats: IMergedStats = {
  mapStats,
  allPlayerStats,
  allRoundStats,
  allPlayerRoundStats,
  allRoundKillEvents,
  allRoundShotEvents,
  allRoundHitEvents,
  allRoundEvents,
};

//fs.writeFileSync("C:\\Temp\\test.json", JSON.stringify(mergedStats, null, 2));
console.log(JSON.stringify(mergedStats, null, 2));
