import { parseEvent, parseTicks } from "@laihoe/demoparser2";
//import fs from "fs";

const filePath = process.argv[2]!;

const CT_TEAM_NUM = 3;
const T_TEAM_NUM = 2;

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
  time: number;
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
  time: number;
  steamID: string;
  round: number;
  weapon: string;
}

interface IRoundHitEvents {
  time: number;
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
  date: Date | null;
  mapName: string;
  match_filename: string;

  constructor(match_filename: string) {
    this.date = null;
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

const matchStartTick = parseEvent(filePath, "begin_new_match").pop()?.tick || 0;

function preprocessRoundTicks(filePath: string): Array<{ start: number; end: number }> {
  const roundStartEvents = parseEvent(filePath, "round_start");
  const roundEndEvents = parseEvent(filePath, "round_end");

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

const roundRanges = preprocessRoundTicks(filePath);

function findRoundForTick(tick: number): number {
  for (let i = 0; i < roundRanges.length; i++) {
    const nextRoundStart = i + 1 < roundRanges.length ? roundRanges[i + 1].start : Number.MAX_SAFE_INTEGER;

    if (tick >= roundRanges[i].start && tick < nextRoundStart) {
      return i + 1; // Return the current round index as a 1-based index
    }
  }
  return -1; // Return undefined if no matching round is found
}

let mvpEvents = parseEvent(filePath, "round_mvp").filter((event: any) => event.tick >= matchStartTick);
let roundEndTicks = parseEvent(filePath, "round_end")
  .filter((event: any) => event.tick >= matchStartTick)
  .map((event: any) => event.tick);
let tickFields = ["equipment_value_this_round", "cash_spent_this_round", "is_alive", "team_num", "player_name"];
let tickData = parseTicks(filePath, tickFields).filter((tick: any) => roundEndTicks.includes(tick.tick));

let allPlayerRoundStats: IPlayerRoundStats[] = tickData.map((tick: any) => {
  let isMVP = mvpEvents.some((mvp: any) => mvp.tick === tick.tick && mvp.user_steamid === tick.steamid);

  return {
    userName: tick.player_name,
    steamID: tick.steamid,
    round: findRoundForTick(tick.tick),
    team: tick.team_num,
    survived: tick.is_alive,
    moneySpent: tick.cash_spent_this_round,
    equipmentValue: tick.equipment_value_this_round,
    clutchChance: 0, //to be updated later
    clutchSuccess: false, //to be updated later
    mvp: isMVP,
  };
});

let playerDeaths = parseEvent(filePath, "player_death").filter((event: any) => event.tick >= matchStartTick);
let clutchChance = new Map<number, { steamID: string; clutchChance: number; clutchSuccess: boolean }[]>();
let alivePlayersPerRound = new Map<number, { ct: Set<string>; t: Set<string> }>();

playerDeaths.forEach((event: any) => {
  let round = findRoundForTick(event.tick) ?? -1;
  if (!alivePlayersPerRound.has(round)) {
    alivePlayersPerRound.set(round, { ct: new Set(), t: new Set() });
  }

  let alivePlayers = alivePlayersPerRound.get(round);
  if (alivePlayers) {
    let tickData = parseTicks(filePath, ["is_alive", "team_num", "player_steamid"], [event.tick]);
    tickData.forEach((player: { is_alive: boolean; team_num: number; player_steamid: string }) => {
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

// Logic for round_end to determine clutch success would follow

let roundEndEvents = parseEvent(filePath, "round_end").filter((event: any) => event.tick >= matchStartTick);

// Logic for round_end to determine clutch success
roundEndEvents.forEach((endEvent: any) => {
  let round = findRoundForTick(endEvent.tick) ?? -1;
  let roundclutchChance = clutchChance.get(round);

  if (roundclutchChance) {
    roundclutchChance.forEach((clutch) => {
      let endTickData = parseTicks(filePath, ["team_num"], [endEvent.tick]);
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

let allRoundStats: IRoundStats[] = roundEndEvents.map((event: any, index: number) => ({
  roundNumber: index + 1, // Assuming rounds are in sequential order
  winnerSide: event.winner, // Mapping might be needed based on how winners are represented
  reasonEndRound: event.reason, // Assuming reason is a numerical value
}));

let bombPlantEvents = parseEvent(filePath, "bomb_planted", ["game_time"]).filter(
  (event: any) => event.tick >= matchStartTick
);
let bombDefuseEvents = parseEvent(filePath, "bomb_defused", ["game_time"]).filter(
  (event: any) => event.tick >= matchStartTick
);
let hostageRescueEvents = parseEvent(filePath, "hostage_rescued", ["game_time"]).filter(
  (event: any) => event.tick >= matchStartTick
);

let allEvents = [...bombPlantEvents, ...bombDefuseEvents, ...hostageRescueEvents]; // Combine all event arrays

let allRoundEvents: IRoundEvents[] = allEvents.map((event: any) => ({
  eventtype: event.event_name, // This will be "bomb_planted" or "bomb_defused", etc.
  eventtime: event.game_time,
  steamid: event.user_steamid,
  round: findRoundForTick(event.tick) ?? -1,
}));

let killEvents = parseEvent(filePath, "player_death", ["game_time", "team_num"]).filter(
  (event: any) => event.tick >= matchStartTick
);

let roundKills = new Map<number, { time: number; killer: string; victim: string; victimTeam: number }[]>();

// First Pass - Record All Kills
killEvents.forEach((event: any) => {
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
let allRoundKillEvents: IRoundKillEvents[] = killEvents.map((event: any) => {
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
    time: event.game_time,
    steamID: event.attacker_steamid,
    flashAssister: event.assistedflash ? event.assister_steamid : undefined,
    killerFlashed: event.attackerblind,
    round: round,
    victimSteamId: event.user_steamid,
    assister: event.assister_steamid,
    weapon: event.weapon,
    headshot: event.headshot,
    isfirstkill: isEntryKill,
    istradekill: isTradeKill,
    istradedeath: isTradeDeath,
  };
});

let shotEvents = parseEvent(filePath, "weapon_fire", ["game_time"]).filter(
  (event: any) => event.tick >= matchStartTick
);

// Fetch grenade detonation events
let flashbangDetonateEvents = parseEvent(filePath, "flashbang_detonate", ["game_time"]);
let heGrenadeDetonateEvents = parseEvent(filePath, "hegrenade_detonate", ["game_time"]);
let molotovDetonateEvents = parseEvent(filePath, "molotov_detonate", ["game_time"]);
let smokeGrenadeDetonateEvents = parseEvent(filePath, "smokegrenade_detonate", ["game_time"]);

// Combine all shot and grenade detonation events
let combinedEvents = [
  ...shotEvents,
  ...flashbangDetonateEvents,
  ...heGrenadeDetonateEvents,
  ...molotovDetonateEvents,
  ...smokeGrenadeDetonateEvents,
].filter((event: any) => event.tick >= matchStartTick);

let allRoundShotEvents: IRoundShotEvents[] = combinedEvents.map((event: any) => {
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
    time: event.game_time,
    steamID: event.user_steamid,
    weapon: weapon,
    round: findRoundForTick(event.tick),
  };
});

let hitEvents = parseEvent(filePath, "player_hurt", ["game_time"]).filter((event: any) => event.tick >= matchStartTick);

let allRoundHitEvents: IRoundHitEvents[] = hitEvents.map((event: any) => ({
  time: event.game_time,
  steamID: event.attacker_steamid,
  round: findRoundForTick(event.tick),
  weapon: event.weapon,
  victimSteamId: event.user_steamid,
  hitGroup: event.hitgroup,
  damageHealth: event.dmg_health,
  damageArmour: event.dmg_armor,
}));

let flashEvents = parseEvent(filePath, "player_blind", ["game_time"]).filter(
  (event: any) => event.tick >= matchStartTick
);

// Mapping flash events
flashEvents.forEach((event: any) => {
  allRoundHitEvents.push({
    time: event.game_time,
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

let gameEndTick = Math.max(...parseEvent(filePath, "round_end").map((x: any) => x.tick));

let fields = ["score"];
let scoreboard = parseTicks(filePath, fields, [gameEndTick]);

let allPlayerStats: IPlayerStats[] = scoreboard.map((player: any) => ({
  userName: player.name,
  steamID: player.steamid,
  score: player.score,
}));

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
