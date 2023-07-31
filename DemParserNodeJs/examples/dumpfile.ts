/* eslint-disable no-console */

import * as ansiStyles from "ansi-styles";
import * as fs from "fs";

import {
  DemoFile,
  extractPublicEncryptionKey,
  Player,
  TeamNumber
} from "demofile";

interface IPlayerRoundStats {
  username: string;
  steamid: string;
  round: number;
  team: number;
  clutchchance: number;
  clutchsuccess: boolean;
  survived: boolean;
  moneyspent: number;
  moneysaved: number;
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

interface IRoundStats {
  roundnumber: number;
  winnerside: TeamNumber;
  reasonendround: number;
}

// Create the currentRound variable
let currentRound = 0;
let roundDamage = {};
let allRoundsDamage = [];
let firstKillInRound: Player | null = null;
let potentialClutchPlayers: { player: any; clutchSize: number }[] = [];
let blindedPlayers = new Map();
let mvpPlayer: Player | null = null;

// Create a map to hold player statistics
const playerStats = new Map<string, PlayerStats>();

const roundStats = new Map<number, RoundStats>();

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

class RoundStats {
  roundNumber: number;
  winnerSide: TeamNumber;
  deathsTerrorists: number;
  deathsCT: number;
  reasonEndRound: number;
  terroristSteamIDs: string[];
  ctSteamIDs: string[];

  constructor(roundNumber: number) {
    this.winnerSide = TeamNumber.Unassigned;
    this.deathsTerrorists = 0;
    this.deathsCT = 0;
    this.reasonEndRound = 0;
    this.roundNumber = roundNumber;
    this.terroristSteamIDs = [];
    this.ctSteamIDs = [];
  }
}

class PlayerStats {
  userid: number;
  steamid: string;
  name: string;
  roundStart: Map<number, boolean>;
  score: number;
  totalspend: number;
  totalsaved: number;

  constructor(userid: number, steamid: string, name: string) {
    this.userid = userid;
    this.steamid = steamid;
    this.name = name;
    this.roundStart = new Map();
    this.score = 0;
    this.totalspend = 0;
    this.totalsaved = 0;
  }
}

const standardMessages: { [message: string]: string | undefined } = {
  Cstrike_Chat_All: "\x03%s\x01 : %s",
  Cstrike_Chat_AllDead: "*DEAD* \x03%s\x01 : %s",
  Game_connected: "%s connected."
};

function teamNumberToAnsi(teamNum: TeamNumber) {
  if (teamNum === TeamNumber.Terrorists) {
    return ansiStyles.redBright.open;
  }
  if (teamNum === TeamNumber.CounterTerrorists) {
    return ansiStyles.blueBright.open;
  }
  return ansiStyles.gray.open;
}

const filePath = process.argv[2]!;
let mapStats = new MapStats(filePath);
const stream = fs.createReadStream(filePath);
const demoFile = new DemoFile();

demoFile.on("start", () => {
  mapStats.mapname = demoFile.header.mapName;
  // Reset all stats if the game is restarted
  playerStats.clear();
  roundStats.clear();
  allPlayerRoundStats = [];
  allRoundEvents = [];
  allRoundKillEvents = [];
  allRoundHitEvents = [];
  allRoundShotEvents = [];
});

let players = new Map();
let playerNames = new Map();
let playerSteamIds = new Map();

let allPlayerRoundStats: IPlayerRoundStats[] = [];
let allRoundEvents: IRoundEvents[] = [];
let allRoundKillEvents: IRoundKillEvents[] = [];
let allRoundShotEvents: IRoundShotEvents[] = [];
let allRoundHitEvents: IRoundHitEvents[] = [];

demoFile.entities.on("create", e => {
  if (!(e.entity instanceof Player)) {
    return;
  }

  const player = e.entity;
  if (player.isFakePlayer) {
    return;
  }
  playerNames.set(player.userId, player.name);
  playerSteamIds.set(player.userId, player.steam64Id);
  if (!playerStats.has(player.steam64Id)) {
    playerStats.set(
      player.steam64Id,
      new PlayerStats(player.userId, player.steam64Id, player.name)
    );
  }
  playerStats.get(player.steam64Id)!.roundStart.set(currentRound, false); // Players that join mid-round did not start the round
  players.set(player.steam64Id, player);
});
let roundEndTriggered = false;
demoFile.gameEvents.on("round_start", e => {
  roundDamage = {};
  potentialClutchPlayers = [];
  firstKillInRound = null;
  mvpPlayer = null;
  lastKill = null;
  blindedPlayers = new Map();
  currentRound++;
  roundEndTriggered = false;
  for (const player of demoFile.entities.players) {
    const playerStat = playerStats.get(player.steam64Id);
    if (playerStat) {
      playerStat.roundStart.set(currentRound, true);
    }
  }
});

demoFile.conVars.on("change", e => {
  // Reset player stats when the game is restarted
  if (
    //(e.name === "bot_quota" && e.value === "0") || - seems this is only when bot quota is changed - could be mid-game
    e.name === "steamworks_sessionid_server"
  ) {
    playerStats.clear();
    roundStats.clear();
    allPlayerRoundStats = [];
    allRoundEvents = [];
    allRoundKillEvents = [];
    allRoundShotEvents = [];
    allRoundHitEvents = [];
    mapStats = new MapStats(filePath);
    potentialClutchPlayers = [];
    currentRound = 1;
    roundDamage = {};
    allRoundsDamage = [];
    lastKill = null;
    firstKillInRound = null;
    for (const player of demoFile.entities.players) {
      if (player.isFakePlayer) {
        continue;
      }
      playerNames.set(player.userId, player.name);
      playerSteamIds.set(player.userId, player.steam64Id);
      if (!playerStats.has(player.steam64Id)) {
        playerStats.set(
          player.steam64Id,
          new PlayerStats(player.userId, player.steam64Id, player.name)
        );
      }
      const playerStat = playerStats.get(player.steam64Id)!;
      playerStat.roundStart.set(currentRound, true);
    }
  }
});

let lastWeaponFired: Map<string, { weapon: string; time: number }> = new Map();

demoFile.gameEvents.on("weapon_fire", e => {
  const shooter = demoFile.entities.getByUserId(e.userid);

  if (shooter) {
    const weaponName = e.weapon;
    allRoundShotEvents.push({
      eventtype: "weapon_fire",
      eventtime: demoFile.currentTime,
      steamid: shooter.steam64Id,
      round: currentRound,
      weapon: weaponName
    });
    const stats = playerStats.get(shooter.steam64Id);

    if (stats) {
      lastWeaponFired.set(shooter.steam64Id, {
        weapon: weaponName,
        time: demoFile.currentTime
      });
    }
  }
});

demoFile.gameEvents.on("flashbang_detonate", e => {
  const thrower = demoFile.entities.getByUserId(e.userid);
  if (thrower) {
    allRoundShotEvents.push({
      eventtype: "flashbang_detonate",
      eventtime: demoFile.currentTime,
      steamid: thrower.steam64Id,
      round: currentRound,
      weapon: "flashbang"
    });
    const stats = playerStats.get(thrower.steam64Id);
  }
});

demoFile.gameEvents.on("hegrenade_detonate", e => {
  const thrower = demoFile.entities.getByUserId(e.userid);
  if (thrower) {
    allRoundShotEvents.push({
      eventtype: "hegrenade_detonate",
      eventtime: demoFile.currentTime,
      steamid: thrower.steam64Id,
      round: currentRound,
      weapon: "hegrenade"
    });
    lastWeaponFired.set(thrower.steam64Id, {
      weapon: "hegrenade",
      time: demoFile.currentTime
    });
    const stats = playerStats.get(thrower.steam64Id);
  }
});

demoFile.on("molotovDetonate", e => {
  const thrower = e.thrower;
  if (thrower) {
    allRoundShotEvents.push({
      eventtype: "molotovDetonate",
      eventtime: demoFile.currentTime,
      steamid: thrower.steam64Id,
      round: currentRound,
      weapon: "inferno"
    });
    lastWeaponFired.set(thrower.steam64Id, {
      weapon: "inferno",
      time: demoFile.currentTime
    });
    const stats = playerStats.get(thrower.steam64Id);
  }
});

demoFile.gameEvents.on("smokegrenade_detonate", e => {
  const thrower = demoFile.entities.getByUserId(e.userid);
  if (thrower) {
    allRoundShotEvents.push({
      eventtype: "smokegrenade_detonate",
      eventtime: demoFile.currentTime,
      steamid: thrower.steam64Id,
      round: currentRound,
      weapon: "smokegrenade"
    });
  }
});

demoFile.gameEvents.on("player_blind", e => {
  const attacker = demoFile.entities.getByUserId(e.attacker);
  const victim = demoFile.entities.getByUserId(e.userid);
  blindedPlayers.set(victim, {
    blindEndTime: demoFile.currentTime + e.blind_duration,
    blinder: attacker
  });
  if (attacker && victim) {
    allRoundHitEvents.push({
      eventtime: demoFile.currentTime,
      steamid: attacker.steam64Id,
      round: currentRound,
      weapon: "flashbang",
      victimsteamid: victim.steam64Id,
      hitgroup: 0,
      damagehealth: 0,
      damagearmour: 0,
      blindtime: e.blind_duration
    });
  }
});

demoFile.gameEvents.on("player_hurt", e => {
  const attacker = demoFile.entities.getByUserId(e.attacker);
  if (attacker) {
    const lastWeaponFiredByAttacker = lastWeaponFired.get(attacker.steam64Id);
    if (lastWeaponFiredByAttacker) {
      const stats = playerStats.get(attacker.steam64Id)!;
      if (stats) {
        const weaponName = lastWeaponFiredByAttacker.weapon;

        allRoundHitEvents.push({
          eventtime: demoFile.currentTime,
          steamid: attacker.steam64Id,
          round: currentRound,
          weapon: weaponName,
          victimsteamid: e.player.steam64Id,
          hitgroup: e.hitgroup,
          damagehealth: e.dmg_health,
          damagearmour: e.dmg_armor,
          blindtime: undefined
        });
      }
    }
  }
});

let lastKill: { killer: Player; victim: Player; time: number } | null = null;

demoFile.gameEvents.on("player_death", e => {
  const teams = demoFile.teams;
  for (const team of teams) {
    const alivePlayers = team.members.filter(player => player.isAlive);
    if (alivePlayers.length === 1) {
      const lastMan = alivePlayers[0]!;

      const opposingTeam = teams.find(
        t =>
          t.teamNumber !== team.teamNumber &&
          (t.teamNumber === TeamNumber.CounterTerrorists ||
            t.teamNumber === TeamNumber.Terrorists)
      );
      const aliveOpponents = opposingTeam
        ? opposingTeam.members.filter(player => player.isAlive).length
        : 0;
      if (
        aliveOpponents > 0 &&
        !potentialClutchPlayers.some(clutch => clutch.player === lastMan)
      ) {
        potentialClutchPlayers.push({
          player: lastMan,
          clutchSize: aliveOpponents
        });
      }
    }
  }
});

demoFile.gameEvents.on("player_death", e => {
  const victim = demoFile.entities.getByUserId(e.userid);
  const attacker = demoFile.entities.getByUserId(e.attacker);
  const weapon = e.weapon;
  let isFirstKill = false;
  let isTradeKill = false;
  let isTradeDeath = false;
  const assister = e.assister
    ? demoFile.entities.getByUserId(e.assister)
    : null;

  if (attacker == victim) {
    return;
  }

  let blinder = undefined;
  if (blindedPlayers.has(victim)) {
    let blindData = blindedPlayers.get(victim);
    if (demoFile.currentTime <= blindData.blindEndTime) {
      blinder = blindData.blinder;
    }
    blindedPlayers.delete(victim);
  }

  if (attacker) {
    const stats = playerStats.get(attacker.steam64Id)!;
    if (stats) {
      if (!firstKillInRound) {
        firstKillInRound = attacker;
        isFirstKill = true;
      }
      if (
        lastKill &&
        demoFile.currentTime - lastKill.time <= 5 &&
        lastKill.killer === victim
      ) {
        isTradeKill = true;
        const lastKillerStats = playerStats.get(lastKill.killer.steam64Id)!;
        if (lastKillerStats) {
          isTradeDeath = true;
        }
      }
    }

    if (attacker && victim) {
      lastKill = {
        killer: attacker,
        victim: victim,
        time: demoFile.currentTime
      };
    }

    let blinded = false;
    if (blindedPlayers.has(attacker)) {
      let blindData = blindedPlayers.get(attacker);
      if (demoFile.currentTime <= blindData.blindEndTime) {
        blinded = true;
      }
    }

    allRoundKillEvents.push({
      eventtime: demoFile.currentTime,
      steamid: attacker.steam64Id,
      assister: assister?.steam64Id,
      flashassister: blinder?.steam64Id,
      killerflashed: blinded,
      round: currentRound,
      weapon: weapon,
      headshot: e.headshot,
      victimsteamid: victim?.steam64Id,
      isfirstkill: isFirstKill,
      istradekill: isTradeKill,
      istradedeath: isTradeDeath
    });
  }
});

demoFile.gameEvents.on("bomb_planted", e => {
  const planter = demoFile.entities.getByUserId(e.userid);

  if (planter) {
    allRoundEvents.push({
      eventtype: "bomb_planted",
      eventtime: demoFile.currentTime,
      steamid: planter.steam64Id,
      round: currentRound
    });
  }
});

demoFile.gameEvents.on("bomb_defused", e => {
  const defuser = demoFile.entities.getByUserId(e.userid);

  if (defuser) {
    allRoundEvents.push({
      eventtype: "bomb_defused",
      eventtime: demoFile.currentTime,
      steamid: defuser.steam64Id,
      round: currentRound
    });
  }
});

demoFile.gameEvents.on("hostage_rescued", e => {
  const rescuer = demoFile.entities.getByUserId(e.userid);

  if (rescuer) {
    allRoundEvents.push({
      eventtype: "hostage_rescued",
      eventtime: demoFile.currentTime,
      steamid: rescuer.steam64Id,
      round: currentRound
    });
  }
});

demoFile.gameEvents.on("round_mvp", e => {
  const mvp = demoFile.entities.getByUserId(e.userid);
  mvpPlayer = mvp;
});

demoFile.gameEvents.on("round_end", e => {
  // If the round end event was already triggered, ignore this event
  if (roundEndTriggered) {
    return;
  }
  roundEndTriggered = true;
  let roundStat = new RoundStats(currentRound);
  const winnerSide = e.winner;

  roundStat.winnerSide = winnerSide;
  roundStat.reasonEndRound = e.reason;

  for (const [playerid, stats] of playerStats.entries()) {
    const player = players.get(playerid);
    if (player) {
      if (!player.isAlive) {
        if (player.teamNumber === TeamNumber.Terrorists) {
          roundStat.deathsTerrorists++;
        } else if (player.teamNumber === TeamNumber.CounterTerrorists) {
          roundStat.deathsCT++;
        }
      }
    }
  }

  roundStats.set(currentRound, roundStat);

  allRoundsDamage.push(roundDamage);
  roundDamage = {};

  for (const player of demoFile.entities.players) {
    if (
      !player ||
      player.name === "GOTV" ||
      !playerStats.get(player.steam64Id)
    ) {
      continue;
    }

    let roundStat = roundStats.get(currentRound);
    if (roundStat) {
      if (player.teamNumber === TeamNumber.Terrorists) {
        roundStat.terroristSteamIDs.push(player.steam64Id);
      } else if (player.teamNumber === TeamNumber.CounterTerrorists) {
        roundStat.ctSteamIDs.push(player.steam64Id);
      }
    }
    const stats = playerStats.get(player.steam64Id)!;
    stats.score = player.score;
  }
  for (const [playerid, stats] of playerStats.entries()) {
    const player = players.get(playerid);
    if (player) {
      let clutchChance = 0;
      let clutchSuccess = false;
      for (const clutch of potentialClutchPlayers) {
        // If the clutch player is still alive, increment the successful clutches
        if (player === clutch.player) {
          clutchChance = clutch.clutchSize;
          if (clutch.player.isAlive) {
            clutchSuccess = true;
          }
        }
      }
      const spend = player.resourceProp("m_iTotalCashSpent") - stats.totalspend;
      const cumulativeSaved = player.getIndexedProps("m_iMatchStats_MoneySaved").reduce((a: number, b: number) => a + b, 0)
      const saved = cumulativeSaved - stats.totalsaved;
      stats.totalsaved = cumulativeSaved;
      stats.totalspend = player.resourceProp("m_iTotalCashSpent");
      let prs = {
        username: stats.name,
        steamid: player.steam64Id,
        round: currentRound,
        team: player.teamNumber,
        clutchchance: clutchChance,
        clutchsuccess: clutchSuccess,
        survived: player.isAlive,
        moneyspent: spend,
        moneysaved: saved,
        equipmentvalue: player.roundStartEquipmentValue,
        mvp: player == mvpPlayer
      }
      allPlayerRoundStats.push(prs);
    }
  }
});

demoFile.entities.on("beforeremove", e => {
  if (!(e.entity instanceof Player)) {
    return;
  }
  const player = e.entity;
  if (player.isFakePlayer) {
    return;
  }
});

demoFile.on("end", e => {
  let allRoundStats: IRoundStats[] = [];

  for (const [round, roundStat] of roundStats.entries()) {
    const roundStatTable: IRoundStats = {
      roundnumber: roundStat.roundNumber,
      winnerside: roundStat.winnerSide,
      reasonendround: roundStat.reasonEndRound
    };
    allRoundStats.push(roundStatTable);
  }

  let allPlayerStats: IPlayerStats[] = [];

  for (const [playerid, stats] of playerStats.entries()) {
    const player = players.get(playerid);
    // Ignore bots in the final output
    if (!player || player.name === "GOTV") {
      continue;
    }
    const playerStatsTable: IPlayerStats = {
      username: stats.name,
      steamid: stats.steamid,
      score: stats.score
    };
    allPlayerStats.push(playerStatsTable);
  }

  const mergedStats: IMergedStats = {
    allPlayerStats,
    mapStats,
    allRoundStats,
    allPlayerRoundStats,
    allRoundKillEvents,
    allRoundShotEvents,
    allRoundHitEvents,
    allRoundEvents
  };
  console.log(JSON.stringify(mergedStats, null, 2));
});

demoFile.parseStream(stream);
