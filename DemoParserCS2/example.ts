import { parseEvent, parseTicks, listGameEvents,parseGrenades,parseHeader,parsePlayerInfo } from "@laihoe/demoparser2";

const filePath = "C:\\temp\\auto-20231009-1930-de_overpass-IXI-GO__Monday_Nights.dem"

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

//let eventsPlayerHurt = parseEvent(filePath, "player_hurt")
//let eventsPlayerDeath = parseEvent(filePath, "player_death", ["X", "Y"], ["total_rounds_played"])
//let eventsPlayerWeaponFire = parseEvent(filePath, "weapon_fire")
//let eventsPlayerBombPlant = parseEvent(filePath, "bomb_planted")


//let grenades = parseGrenades(filePath)

//let heDmg = events.filter(e => e.weapon == "hegrenade")
//let molotovDmg = events.filter(e => e.weapon == "molotov" || e.weapon == "inferno")

//console.log(heDmg)
//console.log(molotovDmg)

let eventNames = [
    //'announce_phase_end',
    'begin_new_match',
    //'bomb_dropped',
    //'bomb_pickup',
    //'buytime_ended',
    //'cs_pre_restart',
    //'cs_round_final_beep',
    //'cs_round_start_beep',
    //'cs_win_panel_match',
    //'cs_win_panel_round',
    'flashbang_detonate',
    'hegrenade_detonate',
    //'hltv_chase',
    //'hltv_fixed',
    //'hltv_versioninfo',
    'inferno_expire',
    'inferno_startburn',
    //'item_equip',
    //'item_pickup',
    //'other_death',
    'player_blind',
    'player_connect',
    'player_connect_full',
    'player_death',
    'player_disconnect',
    //'player_footstep',
    'player_hurt',
    //'player_jump',
    'player_spawn',
    'player_team',
    //'round_announce_last_round_half',
    //'round_announce_match_point',
    //'round_announce_match_start',
    //'round_announce_warmup',
    'round_end',
    //'round_freeze_end',
    'round_mvp',
    'round_officially_ended',
    //'round_poststart',
    //'round_prestart',
    'round_start',
    //'round_time_warning',
    'smokegrenade_detonate',
    'weapon_fire',
    //'weapon_reload',
    //'weapon_zoom'
  ]
  

let header = parseHeader(filePath);
let playerInfo = parsePlayerInfo(filePath);
let gameEvents = listGameEvents(filePath);

// Commented out as it is not used in the provided code
// let grenades = parseGrenades(filePath);

const parsedEvents: { [key: string]: any } = {};

// Loop through each event name and parse the events
for (let eventName of eventNames) {
    parsedEvents[eventName] = parseEvent(filePath, eventName, ["is_alive","start_balance","cash_spent_this_round","total_cash_spent"], ["total_rounds_played","round_win_status","round_win_reason"]);
}

// Print only the first parsed event for each eventName
for (let eventName in parsedEvents) {
    if (parsedEvents[eventName].length > 0) {
        console.log(`Example event for ${eventName}:`, parsedEvents[eventName][0]);
    } else {
        console.log(`No events found for ${eventName}.`);
    }
}

//let events = parseEvent(filePath, "other_death");
//let chickenKills = events.filter(event => event.othertype == "chicken");

// Print only the first chicken kill
//if (chickenKills.length > 0) {
//    console.log('Example chicken kill:', chickenKills[0]);
//} else {
//    console.log('No chicken kills found.');
//}

//console.log(chickenKills)

// Empty arrays to hold our data
let mapStats: MapStats;
mapStats = new MapStats(filePath);
let allPlayerStats: IPlayerStats[] = [];
let allRoundStats: IRoundStats[] = [];
let allPlayerRoundStats: IPlayerRoundStats[] = [];
let allRoundKillEvents: IRoundKillEvents[] = [];
let allRoundShotEvents: IRoundShotEvents[] = [];
let allRoundHitEvents: IRoundHitEvents[] = [];
let allRoundEvents: IRoundEvents[] = [];

for (let eventName in parsedEvents) {
    const event = parsedEvents[eventName][0];

    switch (eventName) {
        case 'player_death':
          allRoundKillEvents.push({
                eventtime: event.tick,
                steamid: event.attacker_steamid,
                assister: event.assister_steamid,
                flashassister: event.assistedflash ? event.assister_steamid : undefined,
                killerflashed: event.attackerblind,
                round: event.total_rounds_played,
                weapon: event.weapon,
                headshot: event.headshot,
                victimsteamid: event.user_steamid,
                isfirstkill: false, // You'll need logic to determine this
                istradekill: false, // You'll need logic to determine this
                istradedeath: false, // You'll need logic to determine this
            });
            break;
        case 'round_mvp':
            const existingStats = allPlayerRoundStats.find(ps => ps.steamid === event.user_steamid && ps.round === event.total_rounds_played);
            if (existingStats) {
                existingStats.mvp = true;
            } else {
              allPlayerRoundStats.push({
                    username: event.user_name,
                    steamid: event.user_steamid,
                    round: event.total_rounds_played,
                    team: 0, // You'll need logic to determine the team
                    clutchchance: 0, // You'll need logic to determine this
                    clutchsuccess: false, // You'll need logic to determine this
                    survived: false, // You'll need logic to determine this
                    moneyspent: 0, // You'll need logic to determine this
                    equipmentvalue: 0, // You'll need logic to determine this
                    mvp: true
                });
            }
            break;
        case 'weapon_fire':
          allRoundShotEvents.push({
                eventtype: eventName,
                eventtime: event.tick,
                steamid: event.user_steamid,
                round: event.total_rounds_played,
                weapon: event.weapon
            });
            break;
        default:
          allRoundEvents.push({
                eventtype: eventName,
                eventtime: event.tick,
                steamid: event.user_steamid,
                round: event.total_rounds_played
            });
            break;
    }
}


// Add additional processing to determine other values like "isfirstkill", "istradekill", etc.

// Output our processed arrays:
//console.log(allPlayerRoundStats);
//console.log(allRoundEvents);
//console.log(allRoundKillEvents);
//console.log(allRoundShotEvents);

const mergedStats: IMergedStats = {
  mapStats,
  allPlayerStats,
  allRoundStats,
  allPlayerRoundStats,
  allRoundKillEvents,
  allRoundShotEvents,
  allRoundHitEvents,
  allRoundEvents
};

console.log(JSON.stringify(mergedStats, null, 2));
