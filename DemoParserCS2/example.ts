import {
  parseEvent,
  parseTicks,
  listGameEvents,
  parseGrenades,
  parseHeader,
  parsePlayerInfo,
} from "@laihoe/demoparser2";

const filePath =
  "C:\\temp\\auto-20231009-1930-de_overpass-IXI-GO__Monday_Nights.dem";

interface IPlayerRoundStats {
  username: string;
  steamid: string;
  round: number;
  team: number;
  clutchchance: number;
  clutchsuccess: boolean;
  survived: boolean;
  moneyspent: number;
  //moneysaved: number;
  equipmentvalue: number;
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
  steamid: string;
  assister: string | undefined;
  flashassister: string | undefined;
  killerflashed: boolean | undefined;
  round: number;
  weapon: string;
  headshot: boolean;
  victimsteamid: string | undefined;
  isfirstkill: boolean;
  istradekill: boolean;
  istradedeath: boolean;
}

interface IRoundShotEvents {
  eventtype: string;
  eventtime: number;
  steamid: string;
  round: number;
  weapon: string;
}

interface IRoundHitEvents {
  eventtime: number;
  steamid: string;
  round: number;
  weapon: string;
  victimsteamid: string;
  hitgroup: number;
  damagehealth: number;
  damagearmour: number;
  blindtime: number | undefined;
}

interface IPlayerStats {
  username: string;
  steamid: string;
  score: number;
}

interface IRoundStats {
  roundnumber: number;
  winnerside: number;
  reasonendround: number;
}

class MapStats {
  match_date: Date | null;
  mapname: string;
  match_filename: string;

  constructor(fileName: string) {
    this.match_date = null;
    this.mapname = "";
    this.match_filename = fileName;
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

// Example: Parsing player events for each round
let playerEvents = parseEvent(filePath, "round_end");

let allPlayerRoundStats: IPlayerRoundStats[] = playerEvents.map(
  (event: any) => ({
    username: event.player_name,
    steamid: event.player_steamid,
    round: event.round,
    team: event.team,
    // Other fields like clutchchance, survived, moneyspent, equipmentvalue, and mvp need to be calculated or extracted from relevant events
  })
);

// Using parseTicks to get additional data
let tickFields = ["current_equip_value"]; // Example fields, adjust as needed
let tickData = parseTicks(filePath, tickFields);

allPlayerRoundStats.forEach((playerStat) => {
  let playerTickData = tickData.filter(
    (tick: any) =>
      tick.steamid === playerStat.steamid &&
      isTickInRound(tick.tick, playerStat.round) // isTickInRound is a hypothetical function to check if the tick is within the round
  );

  // Aggregate or calculate the necessary data
  playerStat.moneyspent = calculateMoneySpent(playerTickData);
  playerStat.equipmentvalue = calculateEquipmentValue(playerTickData);
  // Continue with other fields
  // ...
});

function isTickInRound(tick: number, round: number): boolean {
  // Implement logic to determine if a tick is within a round
  // ...
  return false;
}

function calculateMoneySpent(tickData: any[]): number {
  // Implement logic to calculate money spent
  // ...
  return 0;
}

function calculateEquipmentValue(tickData: any[]): number {
  // Implement logic to calculate equipment value
  // ...
  return 0;
}

let bombPlantEvents = parseEvent(filePath, "bomb_planted", ["game_time"]);
let bombDefuseEvents = parseEvent(filePath, "bomb_defused", ["game_time"]);
let hostageRescueEvents = parseEvent(filePath, "hostage_rescued", ["game_time"]);

// Add more event types as needed

let allEvents = [...bombPlantEvents, ...bombDefuseEvents, ...hostageRescueEvents]; // Combine all event arrays

let allRoundEvents: IRoundEvents[] = allEvents.map((event: any) => ({
    eventtype: event.event_name, // This will be "bomb_planted" or "bomb_defused", etc.
    eventtime: event.game_time,
    steamid: event.user_steamid,
    round: findRoundForTick(event.tick) ?? -1
}));

let killEvents = parseEvent(filePath, "player_death", ["game_time"]);

let allRoundKillEvents: IRoundKillEvents[] = killEvents.map((event: any) => ({
  eventtime: event.game_time,
  steamid: event.attacker_steamid,
  // Fields like assister, flashassister, killerflashed, headshot, victimsteamid need to be extracted from event data
  round: findRoundForTick(event.tick),
  victimsteamid: event.user_steamid,
  assister: event.assister_steamid,
  weapon: event.weapon,
  headshot: event.headshot,
  // isfirstkill, istradekill, istradedeath need logic to determine
}));

let shotEvents = parseEvent(filePath, "weapon_fire", ["game_time"]);

let allRoundShotEvents: IRoundShotEvents[] = shotEvents.map((event: any) => ({
  eventtype: "weapon_fire",
  eventtime: event.game_time,
  steamid: event.user_steamid,
  weapon: event.weapon,
  round: findRoundForTick(event.tick),
}));

let hitEvents = parseEvent(filePath, "player_hurt", ["game_time"]);

let allRoundHitEvents: IRoundHitEvents[] = hitEvents.map((event: any) => ({
  eventtime: event.game_time,
  steamid: event.attacker_steamid,
  round: findRoundForTick(event.tick),
  weapon: event.weapon,
  victimsteamid: event.user_steamid,
  hitgroup: event.hitgroup,
  damagehealth: event.dmg_health,
  damagearmour: event.dmg_armor,
  //blindtime: event.blind_duration // Or any other relevant field
}));

// Empty arrays to hold our data
let mapStats: MapStats;
mapStats = new MapStats(filePath);
let allPlayerStats: IPlayerStats[] = [];
let allRoundStats: IRoundStats[] = [];

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

console.log(JSON.stringify(mergedStats, null, 2));
