import {
  parseEvent,
  parseTicks
} from "@laihoe/demoparser2";

const filePath =
  "C:\\temp\\auto-20231009-1930-de_overpass-IXI-GO__Monday_Nights.dem";

const CT_TEAM_NUM=3;
const T_TEAM_NUM=2;

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
  fileName: string;

  constructor(fileName: string) {
    this.date = null;
    this.mapName = "";
    this.fileName = fileName;
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

function preprocessRoundTicks(filePath: string): Array<{ start: number, end: number }> {
  const roundStartEvents = parseEvent(filePath, "round_start");
  const roundEndEvents = parseEvent(filePath, "round_officially_ended");

  let roundRanges: Array<{ start: number, end: number }> = []; // Explicit type definition

  for (let i = 0; i < roundStartEvents.length; i++) {
      roundRanges.push({
          start: roundStartEvents[i].tick,
          end: roundEndEvents[i] ? roundEndEvents[i].tick : Number.MAX_SAFE_INTEGER
      });
  }

  return roundRanges;
}


const roundRanges = preprocessRoundTicks(filePath);

// Find round for a given tick
function findRoundForTick(tick: number): number | undefined {
  return (
    roundRanges.findIndex((range) => tick >= range.start && tick <= range.end) +
      1 || undefined
  );
}

let mvpEvents = parseEvent(filePath, "round_mvp");
let roundEndTicks = parseEvent(filePath, "round_end").map((event: any) => event.tick);
let tickFields = ["equipment_value_this_round", "cash_spent_this_round", "is_alive", "team_num", "player_name"];
let tickData = parseTicks(filePath, tickFields).filter((tick: any) => roundEndTicks.includes(tick.tick));


let allPlayerRoundStats: IPlayerRoundStats[] = tickData.map((tick: any) => {
  let isMVP = mvpEvents.some((mvp: any) => mvp.tick === tick.tick && mvp.user_steamid === tick.steamid);

  return {
    userName: tick.player_name,
    steamID: tick.steamid,
    round: findRoundForTick(tick.tick),
    team: tick.team_num,
    //clutchChance: calculateClutchChance(tick), // Requires additional logic
    //clutchSuccess: calculateClutchSuccess(tick), // Requires additional logic
    survived: tick.is_alive,
    moneySpent: tick.cash_spent_this_round,
    equipmentValue: tick.equipment_value_this_round,
    mvp: isMVP
  };
});

// Placeholder for clutch information
interface ClutchInfo {
  steamid: string;
  round: number;
  opponentsAlive: number;
  success: boolean;
}

let clutchChances: ClutchInfo[] = [];
let playerDeaths = parseEvent(filePath, "player_death",["num_player_alive_ct","num_player_alive_t","team_num"]);

playerDeaths.forEach((deathEvent: any) => {
  let ctAlive = deathEvent.num_player_alive_ct;
  let tAlive = deathEvent.num_player_alive_t;
  let teamOfDeadPlayer = deathEvent.team_num; // Assuming this is available
  let isClutchChance = false;

  if (teamOfDeadPlayer === CT_TEAM_NUM && ctAlive === 1) { // CT_TEAM_NUM is a constant for CT team number
    isClutchChance = true;
  } else if (teamOfDeadPlayer === T_TEAM_NUM && tAlive === 1) { // T_TEAM_NUM is a constant for T team number
    isClutchChance = true;
  }

  if (isClutchChance) {
    let survivingPlayerSteamId = "3252532";
    let opponentsAlive = teamOfDeadPlayer === CT_TEAM_NUM ? tAlive : ctAlive;
    let roundNumber = findRoundForTick(deathEvent.tick);

    clutchChances.push({
      steamid: survivingPlayerSteamId,
      round: roundNumber ?? -1,
      opponentsAlive: opponentsAlive,
      success: false // Default to false, to be updated later
    });
  }
});

let roundEndEvents = parseEvent(filePath, "round_end");

// Determine clutch success using roundEndEvents
roundEndEvents.forEach((endEvent: any) => {
  clutchChances.forEach(clutch => {
    if (clutch.round === findRoundForTick(endEvent.tick)) {
      clutch.success = false;
    }
  });
});

// Update allPlayerRoundStats with clutch information
allPlayerRoundStats.forEach(stat => {
  let clutch = clutchChances.find(c => c.steamid === stat.steamID && c.round === stat.round);
  if (clutch) {
    stat.clutchChance = clutch.opponentsAlive;
    stat.clutchSuccess = clutch.success;
  }
});

let allRoundStats: IRoundStats[] = roundEndEvents.map((event: any, index: number) => ({
  roundNumber: index + 1, // Assuming rounds are in sequential order
  winnerSide: event.winner, // Mapping might be needed based on how winners are represented
  reasonEndRound: event.reason // Assuming reason is a numerical value
}));


let bombPlantEvents = parseEvent(filePath, "bomb_planted", ["game_time"]);
let bombDefuseEvents = parseEvent(filePath, "bomb_defused", ["game_time"]);
let hostageRescueEvents = parseEvent(filePath, "hostage_rescued", ["game_time"]);

let allEvents = [...bombPlantEvents, ...bombDefuseEvents, ...hostageRescueEvents]; // Combine all event arrays

let allRoundEvents: IRoundEvents[] = allEvents.map((event: any) => ({
    eventtype: event.event_name, // This will be "bomb_planted" or "bomb_defused", etc.
    eventtime: event.game_time,
    steamid: event.user_steamid,
    round: findRoundForTick(event.tick) ?? -1
}));

let killEvents = parseEvent(filePath, "player_death", ["game_time", "team_num"]);

let roundKills = new Map<number, { time: number, killer: string, victim: string, victimTeam: number }[]>();

// First Pass - Record All Kills
killEvents.forEach((event: any) => {
  let round = findRoundForTick(event.tick) ?? -1;
  let kills = roundKills.get(round) || [];
  kills.push({ time: event.game_time, killer: event.attacker_steamid, victim: event.user_steamid, victimTeam: event.team_num });
  roundKills.set(round, kills);
});

// Second Pass - Determine Trade Kills and Deaths
let allRoundKillEvents: IRoundKillEvents[] = killEvents.map((event: any) => {
  let round = findRoundForTick(event.tick) ?? -1;
  let kills = roundKills.get(round) || [];
  let isEntryKill = kills.length > 0 && kills[0].time === event.game_time;
  let isTradeKill = kills.some(kill => 
    kill.killer !== event.attacker_steamid && 
    Math.abs(kill.time - event.game_time) <= 5 && 
    kill.victimTeam === event.team_num && // Check if a teammate of the current attacker was killed
    kill.victim !== event.attacker_steamid); // Ensure the current attacker wasn't the one killed
  
  let isTradeDeath = kills.some(kill => kill.killer !== event.attacker_steamid && Math.abs(kill.time - event.game_time) <= 5 && kill.victimTeam === event.team_num);

  return {
    time: event.game_time,
    steamID: event.attacker_steamid,
    flashAssister: event.assistedflash,
    killerFlashed: event.attackerblind,
    round: round,
    victimSteamId: event.user_steamid,
    assister: event.assister_steamid,
    weapon: event.weapon,
    headshot: event.headshot,
    isfirstkill: isEntryKill,
    istradekill: isTradeKill,
    istradedeath: isTradeDeath
  };
});


let shotEvents = parseEvent(filePath, "weapon_fire", ["game_time"]);

let allRoundShotEvents: IRoundShotEvents[] = shotEvents.map((event: any) => ({
  eventType: "weapon_fire",
  time: event.game_time,
  steamID: event.user_steamid,
  weapon: event.weapon,
  round: findRoundForTick(event.tick),
}));

let hitEvents = parseEvent(filePath, "player_hurt", ["game_time"]);

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

let flashEvents = parseEvent(filePath, "player_blind", ["game_time"]);

// Mapping flash events
flashEvents.forEach((event: any) => {
  allRoundHitEvents.push({
    time: event.game_time,
    steamID: event.attacker_steamid,
    round: findRoundForTick(event.tick)?? -1,
    weapon: "flashbang",
    victimSteamId: event.user_steamid,
    hitGroup: 0,
    damageHealth: 0,
    damageArmour: 0,
    blindTime: event.blind_duration
  });
});

let gameEndTick = Math.max(...parseEvent(filePath,"round_end").map((x: any) => x.tick))

let fields = ["score"]
let scoreboard = parseTicks(filePath, fields, [gameEndTick])

let allPlayerStats: IPlayerStats[] = scoreboard.map((player: any) => ({
  userName: player.name,
  steamID: player.steamid,
  score: player.score
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

const shortenedMergedStats = {
  mapStats,
  allPlayerStats: allPlayerStats.slice(0, 10),
  allRoundStats: allRoundStats.slice(0, 10),
  allPlayerRoundStats: allPlayerRoundStats.slice(0, 10),
  allRoundKillEvents: allRoundKillEvents.slice(0, 10),
  allRoundShotEvents: allRoundShotEvents.slice(0, 10),
  allRoundHitEvents: allRoundHitEvents.slice(0, 10),
  allRoundEvents: allRoundEvents.slice(0, 10),
};

console.log(JSON.stringify(shortenedMergedStats, null, 2));

