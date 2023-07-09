/* eslint-disable no-console */

import * as ansiStyles from "ansi-styles";
import * as fs from "fs";

import {
  DemoFile,
  extractPublicEncryptionKey,
  Player,
  TeamNumber
} from "demofile";

type KillEvent = {
  killerSteamId: string;
  round: number;
  weapon: string;
  victimSteamId: string;
};

interface IPlayerStats {
  userName: string;
  steamID: string;
  roundWinShare: number;
  kills: number;
  assists: number;
  deaths: number;
  killDeathRatio: number;
  headShots: number;
  headShotsPercentage: number;
  teamKillFriendlyFire: number;
  entryKill: number;
  bombPlanted: number;
  bombDefused: number;
  mostValuablePlayer: number;
  score: number;
  halfLifeTelevisionRating: number;
  fiveKills: number;
  fourKills: number;
  threeKills: number;
  twoKills: number;
  oneKill: number;
  tradeKill: number;
  tradeDeath: number;
  killPerRound: number;
  assistsPerRound: number;
  deathPerRound: number;
  averageDamagePerRound: number;
  totalDamageHealth: number;
  totalDamageArmor: number;
  oneVersusOne: number;
  oneVersusTwo: number;
  oneVersusThree: number;
  oneVersusFour: number;
  oneVersusFive: number;
  grenadesThrownCount: number;
  flashesThrownCount: number;
  smokesThrownCount: number;
  fireThrownCount: number;
  highExplosiveDamage: number;
  fireDamage: number;
  side: string;
  // New info
  roundsPlayed: number;
  roundsWon: number;
  opponentsFlashed: number;
}

interface IWeaponStats {
  userName: string;
  steamID: string;
  weapon: string;
  totalKills: number;
  totalDamage: number;
  killsPerRound: number;
  damagePerRound: number;
  shotsFired: number;
  damagePerShot: number;
  hits: number;
  danagePerHit: number;
  accuracyPerc: number;
  headshotPerc: number;
  chestPerc: number;
  stomachPerc: number;
  armsPerc: number;
  legsPerc: number;
}

interface IMergedStats {
  allPlayerStats: IPlayerStats[];
  mapStats: MapStats;
  weaponStats: IWeaponStats[];
  killEvents: KillEvent[];
}

// Create the currentRound variable
let currentRound = 0;
let roundDamage = {};
let allRoundsDamage = [];
let firstKillInRound: Player | null = null;
let potentialClutchPlayers: { player: any; clutchSize: number }[] = [];

// Create a map to hold player statistics
const playerStats = new Map<string, PlayerStats>();

class MapStats {
  date: Date | null;
  mapName: string;
  fileName: string;
  terroristRoundWins: number;
  ctRoundWins: number;
  terroristRoundWinsFirstHalf: number;
  ctRoundWinsFirstHalf: number;
  terroristRoundWinsSecondHalf: number;
  ctRoundWinsSecondHalf: number;

  constructor(fileName: string) {
    this.date = null;
    this.mapName = "";
    this.fileName = fileName;
    this.terroristRoundWins = 0;
    this.ctRoundWins = 0;
    this.terroristRoundWinsFirstHalf = 0;
    this.ctRoundWinsFirstHalf = 0;
    this.terroristRoundWinsSecondHalf = 0;
    this.ctRoundWinsSecondHalf = 0;
  }
}

let steamIdToName: Map<string, string> = new Map();

class PlayerStats {
  userid: number;
  steamid: string;
  name: string;
  kills: number;
  deaths: number;
  nKills!: number[];
  roundKills: Map<number, number>;
  assists: number;
  roundsPlayed: number;
  teamRounds: Map<TeamNumber, number>;
  roundStart: Map<number, boolean>;
  lastTeam: TeamNumber | null;
  roundDamage: Map<number, number>;
  roundRWS: Map<number, number>;
  headshots: number;
  teamKills: number;
  entryKills: number;
  bombsPlanted: number;
  bombsDefused: number;
  mvps: number;
  score: number;
  tradeKills: number;
  tradeDeaths: number;
  totalDamageHealth: number;
  totalDamageArmor: number;
  lastManAttempts: number[];
  successfulClutches: number[];
  weaponDamage: Map<number, Map<string, number>>;
  weaponKills: Map<number, Map<string, number>>;
  weaponShotsFired: Map<string, number>;
  weaponHitGroups: Map<string, Map<number, number>>;
  roundVictimKills: Map<number, Map<string, string>>;
  roundWins: number;
  grenadeThrows: Map<string, number>;
  grenadeDamage: Map<string, number>;
  opponentsFlashed: number;

  constructor(userid: number, steamid: string, name: string) {
    this.userid = userid;
    this.steamid = steamid;
    this.name = name;
    this.kills = 0;
    this.deaths = 0;
    this.assists = 0;
    this.roundsPlayed = 0;
    this.nKills = [0, 0, 0, 0, 0];
    this.roundKills = new Map();
    this.teamRounds = new Map();
    this.roundStart = new Map();
    this.lastTeam = null;
    this.roundDamage = new Map();
    this.roundRWS = new Map();
    this.headshots = 0;
    this.teamKills = 0;
    this.entryKills = 0;
    this.bombsPlanted = 0;
    this.bombsDefused = 0;
    this.mvps = 0;
    this.score = 0;
    this.tradeKills = 0;
    this.tradeDeaths = 0;
    this.totalDamageHealth = 0;
    this.totalDamageArmor = 0;
    this.lastManAttempts = [0, 0, 0, 0, 0];
    this.successfulClutches = [0, 0, 0, 0, 0];
    this.weaponDamage = new Map();
    this.weaponKills = new Map();
    this.weaponShotsFired = new Map();
    this.weaponHitGroups = new Map();
    this.roundVictimKills = new Map();
    this.roundWins = 0;
    this.grenadeThrows = new Map();
    this.grenadeDamage = new Map();
    this.opponentsFlashed = 0;
    steamIdToName.set(steamid, name);
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

function computeHltvOrgRating(
  roundCount: number,
  kills: number,
  deaths: number,
  nKills: number[]
): number {
  const AVERAGE_KPR = 0.679;
  const AVERAGE_SPR = 0.317;
  const AVERAGE_RMK = 1.277;
  const killRating = kills / roundCount / AVERAGE_KPR;
  const survivalRating = (roundCount - deaths) / roundCount / AVERAGE_SPR;
  const roundsWithMultipleKillsRating =
    ((nKills[0] || 0) +
      4 * (nKills[1] || 0) +
      9 * (nKills[2] || 0) +
      16 * (nKills[3] || 0) +
      25 * (nKills[4] || 0)) /
    roundCount /
    AVERAGE_RMK;
  return Number(
    (
      (killRating + 0.7 * survivalRating + roundsWithMultipleKillsRating) /
      2.7
    ).toFixed(3)
  );
}

function calculateTotalWeaponKills(stats: PlayerStats): Map<string, number> {
  const weaponKills = new Map<string, number>();
  for (const [round, weaponStats] of stats.weaponKills.entries()) {
    for (const [weapon, kills] of weaponStats.entries()) {
      const currentKills = weaponKills.get(weapon) || 0;
      weaponKills.set(weapon, currentKills + kills);
    }
  }

  return weaponKills;
}

function calculateTotalWeaponDamage(stats: PlayerStats): Map<string, number> {
  const weaponDamage = new Map<string, number>();
  for (const [round, weaponStats] of stats.weaponDamage.entries()) {
    for (const [weapon, damage] of weaponStats.entries()) {
      const currentDamage = weaponDamage.get(weapon) || 0;
      weaponDamage.set(weapon, currentDamage + damage);
    }
  }

  return weaponDamage;
}

function calculateTotalShotsFired(stats: PlayerStats): Map<string, number> {
  return stats.weaponShotsFired;
}

function calculateTotalShotsHit(stats: PlayerStats): Map<string, number> {
  const totalShotsHit = new Map<string, number>();

  for (const [weapon, hitGroups] of stats.weaponHitGroups.entries()) {
    let totalHits = 0;
    for (const count of hitGroups.values()) {
      totalHits += count;
    }
    totalShotsHit.set(weapon, totalHits);
  }

  return totalShotsHit;
}

function getWeaponPartHitPercentage(
  weaponName: string,
  stats: PlayerStats,
  hitGroup1: EHitGroup,
  hitGroup2?: EHitGroup
): number {
  const hitGroups = stats.weaponHitGroups.get(weaponName);
  if (!hitGroups) {
    return 0;
  }

  let partHits = hitGroups.get(hitGroup1) || 0;
  if (hitGroup2) {
    partHits += hitGroups.get(hitGroup2) || 0;
  }

  let totalHits = 0;
  for (const count of hitGroups.values()) {
    totalHits += count;
  }

  return (100 * partHits) / totalHits; // avoid division by zero by not calculating if totalHits is 0
}

enum EHitGroup {
  EHG_Generic = 0,
  EHG_Head = 1,
  EHG_Chest = 2,
  EHG_Stomach = 3,
  EHG_LeftArm = 4,
  EHG_RightArm = 5,
  EHG_LeftLeg = 6,
  EHG_RightLeg = 7,
  EHG_Gear = 8,
  EHG_Miss = 9
}

demoFile.on("start", () => {
  mapStats.mapName = demoFile.header.mapName;
  // Reset all stats if the game is restarted
  playerStats.clear();
});

let players = new Map();
let playerNames = new Map();
let playerSteamIds = new Map();
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

demoFile.gameEvents.on("round_start", e => {
  roundDamage = {};
  potentialClutchPlayers = [];
  firstKillInRound = null;
  lastKill = null;
  currentRound++;
  for (const player of demoFile.entities.players) {
    const playerStat = playerStats.get(player.steam64Id);
    if (playerStat) {
      playerStat.roundStart.set(currentRound, true);
      playerStat.roundKills.set(currentRound, 0);
    }
  }
});

demoFile.conVars.on("change", e => {
  // Reset player stats when the game is restarted
  if (
    (e.name === "bot_quota" && e.value === "0") ||
    e.name === "steamworks_sessionid_server"
  ) {
    playerStats.clear();
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
      playerStat.roundKills.set(currentRound, 0);
    }
  }
});

let lastWeaponFired: Map<string, { weapon: string; time: number }> = new Map();

demoFile.gameEvents.on("weapon_fire", e => {
  const shooter = demoFile.entities.getByUserId(e.userid);

  if (shooter) {
    const weaponName = e.weapon;
    const stats = playerStats.get(shooter.steam64Id);

    if (stats) {
      stats.weaponShotsFired.set(
        weaponName,
        (stats.weaponShotsFired.get(weaponName) || 0) + 1
      );
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
    const stats = playerStats.get(thrower.steam64Id);
    if (stats) {
      stats.grenadeThrows.set(
        "flashbang",
        (stats.grenadeThrows.get("flashbang") || 0) + 1
      );
    }
  }
});

demoFile.gameEvents.on("hegrenade_detonate", e => {
  const thrower = demoFile.entities.getByUserId(e.userid);
  if (thrower) {
    lastWeaponFired.set(thrower.steam64Id, {
      weapon: "hegrenade",
      time: demoFile.currentTime
    });
    const stats = playerStats.get(thrower.steam64Id);
    if (stats) {
      stats.grenadeThrows.set(
        "hegrenade",
        (stats.grenadeThrows.get("hegrenade") || 0) + 1
      );
    }
  }
});

demoFile.on("molotovDetonate", e => {
  const thrower = e.thrower;
  if (thrower) {
    lastWeaponFired.set(thrower.steam64Id, {
      weapon: "inferno",
      time: demoFile.currentTime
    });
    const stats = playerStats.get(thrower.steam64Id);
    if (stats) {
      stats.grenadeThrows.set(
        "inferno",
        (stats.grenadeThrows.get("inferno") || 0) + 1
      );
    }
  }
});

demoFile.gameEvents.on("smokegrenade_detonate", e => {
  const thrower = demoFile.entities.getByUserId(e.userid);
  if (thrower) {
    const stats = playerStats.get(thrower.steam64Id);
    if (stats) {
      stats.grenadeThrows.set(
        "smokegrenade",
        (stats.grenadeThrows.get("smokegrenade") || 0) + 1
      );
    }
  }
});

demoFile.gameEvents.on("player_blind", e => {
  const attacker = demoFile.entities.getByUserId(e.attacker);
  const victim = demoFile.entities.getByUserId(e.userid);
  if (attacker && victim) {
    if (attacker.teamNumber !== victim.teamNumber) {
      const stats = playerStats.get(attacker.steam64Id);
      if (stats) {
        stats.opponentsFlashed += 1;
      }
    }
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

        // Handle grenade and flame damage
        if (weaponName === "hegrenade" || weaponName === "inferno") {
          const grenadeDamage = stats.grenadeDamage.get(weaponName) || 0;
          stats.grenadeDamage.set(weaponName, grenadeDamage + e.dmg_health);
        }

        // record hit group stats
        let hitGroups = stats.weaponHitGroups.get(weaponName);
        if (!hitGroups) {
          hitGroups = new Map();
          stats.weaponHitGroups.set(weaponName, hitGroups);
        }
        hitGroups.set(e.hitgroup, (hitGroups.get(e.hitgroup) || 0) + 1);

        const roundDamage = stats.weaponDamage.get(currentRound) || new Map();
        roundDamage.set(
          weaponName,
          (roundDamage.get(weaponName) || 0) + e.dmg_health
        );
        stats.weaponDamage.set(currentRound, roundDamage);

        stats.roundDamage.set(
          currentRound,
          (stats.roundDamage.get(currentRound) || 0) + e.dmg_health
        );

        stats.totalDamageHealth += e.dmg_health;
        stats.totalDamageArmor += e.dmg_armor;
      }
    }
  }
});

let lastKill: { killer: Player; victim: Player; time: number } | null = null;

demoFile.gameEvents.on("player_death", e => {
  const victim = demoFile.entities.getByUserId(e.userid);
  const victimColour = teamNumberToAnsi(
    victim ? victim.teamNumber : TeamNumber.Spectator
  );
  const victimName = victim ? victim.name : "unnamed";
  const attacker = demoFile.entities.getByUserId(e.attacker);
  const attackerColour = teamNumberToAnsi(
    attacker ? attacker.teamNumber : TeamNumber.Spectator
  );
  const attackerName = attacker ? attacker.name : "unnamed";
  const headshotText = e.headshot ? " HS" : "";

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
  const assister = e.assister
    ? demoFile.entities.getByUserId(e.assister)
    : null;

  if (attacker == victim) {
    return;
  }

  if (attacker) {
    const stats = playerStats.get(attacker.steam64Id)!;
    if (stats) {
      stats.kills++;
      const roundKills = stats.weaponKills.get(currentRound) || new Map();
      roundKills.set(weapon, (roundKills.get(weapon) || 0) + 1);
      stats.weaponKills.set(currentRound, roundKills);
      if (e.headshot) {
        stats.headshots! += 1;
      }

      if (victim && victim.teamNumber === attacker.teamNumber) {
        stats.teamKills += 1;
      }

      if (victim) {
        const roundVictimKills =
          stats.roundVictimKills.get(currentRound) || new Map();
        roundVictimKills.set(victim.steam64Id, weapon);
        stats.roundVictimKills.set(currentRound, roundVictimKills);
      }

      if (!firstKillInRound) {
        firstKillInRound = attacker;
        stats.entryKills += 1;
      }

      if (
        lastKill &&
        demoFile.currentTime - lastKill.time <= 5 &&
        lastKill.killer === victim
      ) {
        stats.tradeKills += 1;
        const lastKillerStats = playerStats.get(lastKill.killer.steam64Id)!;
        if (lastKillerStats) {
          lastKillerStats.tradeDeaths += 1;
        }
      }
      stats.roundKills.set(
        currentRound,
        (stats.roundKills.get(currentRound) || 0) + 1
      );
    }

    if (attacker && victim) {
      lastKill = {
        killer: attacker,
        victim: victim,
        time: demoFile.currentTime
      };
    }
  }

  if (victim && playerStats.get(victim.steam64Id)) {
    playerStats.get(victim.steam64Id)!.deaths++;
  }

  if (assister && playerStats.get(assister.steam64Id)) {
    playerStats.get(assister.steam64Id)!.assists++;
  }
});

demoFile.gameEvents.on("bomb_planted", e => {
  const planter = demoFile.entities.getByUserId(e.userid);

  if (planter) {
    const stats = playerStats.get(planter.steam64Id);
    if (stats) {
      stats.bombsPlanted += 1;
    }
  }
});

demoFile.gameEvents.on("bomb_defused", e => {
  const defuser = demoFile.entities.getByUserId(e.userid);

  if (defuser) {
    const stats = playerStats.get(defuser.steam64Id)!;
    stats.bombsDefused += 1;
  }
});

demoFile.gameEvents.on("round_mvp", e => {
  const mvp = demoFile.entities.getByUserId(e.userid);

  if (mvp) {
    const stats = playerStats.get(mvp.steam64Id)!;
    if (stats) {
      stats.mvps++;
    }
  }
});

demoFile.gameEvents.on("round_end", e => {
  const winnerSide = e.winner;
  if (currentRound <= 7) {
    // First half
    if (winnerSide === TeamNumber.Terrorists) {
      mapStats.terroristRoundWinsFirstHalf++;
    } else if (winnerSide === TeamNumber.CounterTerrorists) {
      mapStats.ctRoundWinsFirstHalf++;
    }
  } else {
    // Second half
    if (winnerSide === TeamNumber.Terrorists) {
      mapStats.terroristRoundWinsSecondHalf++;
    } else if (winnerSide === TeamNumber.CounterTerrorists) {
      mapStats.ctRoundWinsSecondHalf++;
    }
  }

  if (winnerSide === TeamNumber.Terrorists) {
    mapStats.terroristRoundWins++;
  } else if (winnerSide === TeamNumber.CounterTerrorists) {
    mapStats.ctRoundWins++;
  }

  let totalDamageByWinningTeam = 0;

  for (const [playerid, stats] of playerStats.entries()) {
    const player = players.get(playerid);
    if (player) {
      if (player.teamNumber === winnerSide) {
        stats.roundWins++;
        totalDamageByWinningTeam += stats.roundDamage.get(currentRound) || 0;
      }
    }
  }

  for (const [playerid, stats] of playerStats.entries()) {
    const player = players.get(playerid);
    if (player) {
      if (player.teamNumber === winnerSide) {
        const damage = stats.roundDamage.get(currentRound) || 0;
        const rws =
          totalDamageByWinningTeam > 0
            ? (damage / totalDamageByWinningTeam) * 100
            : 0;
        stats.roundRWS.set(currentRound, rws);
      }
    }
  }

  for (const clutch of potentialClutchPlayers) {
    // If the clutch player is still alive, increment the successful clutches
    if (clutch.player.isAlive) {
      const stats = playerStats.get(clutch.player.steam64Id)!;
      stats.successfulClutches[clutch.clutchSize - 1]++;
    }
  }

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
    if (playerStats.get(player.steam64Id)!.roundStart.get(currentRound)) {
      playerStats.get(player.steam64Id)!.roundsPlayed++;
    }

    const teamNum = player.teamNumber;
    const stats = playerStats.get(player.steam64Id)!;
    const teamRounds = stats.teamRounds.get(teamNum) || 0;
    stats.teamRounds.set(teamNum, teamRounds + 1);
    stats.lastTeam = player.teamNumber;
    stats.score = player.score;
  }
});

demoFile.gameEvents.on("round_officially_ended", () => {
  for (const player of demoFile.entities.players) {
    if (!player || player.name === "GOTV") {
      continue;
    }
    const stats = playerStats.get(player.steam64Id);
    if (stats) {
      const roundKills = stats.roundKills.get(currentRound) || 0;

      if (roundKills > 0 && roundKills <= 5) {
        stats.nKills[roundKills - 1]++;
      }
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

function finalTeam(playerStats: PlayerStats): string {
  return playerStats.lastTeam === TeamNumber.Terrorists ? "T" : "CT";
}

demoFile.on("end", e => {
  // Print map stats first
  let mapStatsTable = {
    Date: mapStats.date ? mapStats.date.toISOString() : "N/A",
    "Map Name": mapStats.mapName,
    "File Name": mapStats.fileName,
    "Terrorist Round Wins": mapStats.terroristRoundWins,
    "CT Round Wins": mapStats.ctRoundWins,
    "Terrorist Round Wins (First Half)": mapStats.terroristRoundWinsFirstHalf,
    "CT Round Wins (First Half)": mapStats.ctRoundWinsFirstHalf,
    "Terrorist Round Wins (Second Half)": mapStats.terroristRoundWinsSecondHalf,
    "CT Round Wins (Second Half)": mapStats.ctRoundWinsSecondHalf
  };

  let allPlayerStats: IPlayerStats[] = [];
  let allPlayerGrenStats = [];

  for (const [playerid, stats] of playerStats.entries()) {
    const player = players.get(playerid);
    // Ignore bots in the final output
    if (!player || player.name === "GOTV" || stats.roundsPlayed == 0) {
      continue;
    }
    const HLTVRating = computeHltvOrgRating(
      stats.roundsPlayed,
      stats.kills,
      stats.deaths,
      stats.nKills
    );

    // Calculate average RWS
    let totalRWS = 0;
    for (const rws of stats.roundRWS.values()) {
      totalRWS += rws;
    }
    const averageRWS = totalRWS / stats.roundsPlayed;

    const team = finalTeam(stats);

    const KDR = stats.deaths > 0 ? stats.kills / stats.deaths : stats.kills;
    const HSP = stats.kills > 0 ? (stats.headshots / stats.kills) * 100 : 0;

    const KPR = stats.roundsPlayed > 0 ? stats.kills / stats.roundsPlayed : 0;
    const APR = stats.roundsPlayed > 0 ? stats.assists / stats.roundsPlayed : 0;
    const DPR = stats.roundsPlayed > 0 ? stats.deaths / stats.roundsPlayed : 0;
    const ADR =
      stats.roundsPlayed > 0
        ? (stats.totalDamageHealth + stats.totalDamageArmor) /
          stats.roundsPlayed
        : 0;

    // Format clutch data
    let clutchData = "";
    for (let i = 0; i < stats.successfulClutches.length; i++) {
      if (stats.successfulClutches[i]! > 0) {
        clutchData += `1v${i + 1}: ${stats.successfulClutches[i]}, `;
      }
    }
    // Remove trailing comma and space
    if (clutchData.length > 0) {
      clutchData = clutchData.slice(0, -2);
    }

    const playerStatsTable: IPlayerStats = {
      userName: stats.name,
      steamID: stats.steamid,
      side: team,
      roundsPlayed: stats.roundsPlayed,
      roundsWon: stats.roundWins,
      kills: stats.kills,
      deaths: stats.deaths,
      assists: stats.assists,
      killDeathRatio: parseFloat(KDR.toFixed(2)),
      headShots: stats.headshots,
      headShotsPercentage: parseFloat(HSP.toFixed(2)),
      killPerRound: parseFloat(KPR.toFixed(2)),
      assistsPerRound: parseFloat(APR.toFixed(2)),
      deathPerRound: parseFloat(DPR.toFixed(2)),
      averageDamagePerRound: parseFloat(ADR.toFixed(2)),
      teamKillFriendlyFire: stats.teamKills,
      entryKill: stats.entryKills,
      tradeKill: stats.tradeKills,
      tradeDeath: stats.tradeDeaths,
      oneKill: stats.nKills[0] || 0,
      twoKills: stats.nKills[1] || 0,
      threeKills: stats.nKills[2] || 0,
      fourKills: stats.nKills[3] || 0,
      fiveKills: stats.nKills[4] || 0,
      oneVersusOne: stats.successfulClutches[0] || 0,
      oneVersusTwo: stats.successfulClutches[1] || 0,
      oneVersusThree: stats.successfulClutches[2] || 0,
      oneVersusFour: stats.successfulClutches[3] || 0,
      oneVersusFive: stats.successfulClutches[4] || 0,
      totalDamageHealth: stats.totalDamageHealth,
      totalDamageArmor: stats.totalDamageArmor,
      bombPlanted: stats.bombsPlanted,
      bombDefused: stats.bombsDefused,
      flashesThrownCount: stats.grenadeThrows.get("flashbang") || 0,
      grenadesThrownCount: stats.grenadeThrows.get("hegrenade") || 0,
      fireThrownCount: stats.grenadeThrows.get("inferno") || 0,
      smokesThrownCount: stats.grenadeThrows.get("smokegrenade") || 0,
      halfLifeTelevisionRating: HLTVRating,
      mostValuablePlayer: stats.mvps,
      score: stats.score,
      roundWinShare: parseFloat(averageRWS.toFixed(2)),
      opponentsFlashed: stats.opponentsFlashed,
      highExplosiveDamage: stats.grenadeDamage.get("hegrenade") || 0,
      fireDamage: stats.grenadeDamage.get("inferno") || 0
    };

    allPlayerStats.push(playerStatsTable);
  }

  const weaponStats: IWeaponStats[] = [];

  for (const [playerName, stats] of playerStats) {
    const weaponKills = calculateTotalWeaponKills(stats);
    const weaponDamage = calculateTotalWeaponDamage(stats);
    const totalShotsFired = calculateTotalShotsFired(stats);
    const totalShotsHit = calculateTotalShotsHit(stats);

    for (const [weapon, totalKills] of weaponKills) {
      const totalDamage = weaponDamage.get(`weapon_${weapon}`) || 0;
      const killsPerRound = totalKills / stats.roundsPlayed;
      const damagePerRound = totalDamage / stats.roundsPlayed;

      const shotsFired = totalShotsFired.get(`weapon_${weapon}`) || 0;
      const shotsHit = totalShotsHit.get(`weapon_${weapon}`) || 0;
      const accuracy = ((shotsHit / (shotsFired || 1)) * 100).toFixed(2);
      const headshotPercentage = getWeaponPartHitPercentage(
        `weapon_${weapon}`,
        stats,
        EHitGroup.EHG_Head
      );

      const chestPercentage = getWeaponPartHitPercentage(
        `weapon_${weapon}`,
        stats,
        EHitGroup.EHG_Chest
      );
      const stomachPercentage = getWeaponPartHitPercentage(
        `weapon_${weapon}`,
        stats,
        EHitGroup.EHG_Stomach
      );
      const armsPercentage = getWeaponPartHitPercentage(
        `weapon_${weapon}`,
        stats,
        EHitGroup.EHG_LeftArm,
        EHitGroup.EHG_RightArm
      );
      const legsPercentage = getWeaponPartHitPercentage(
        `weapon_${weapon}`,
        stats,
        EHitGroup.EHG_LeftLeg,
        EHitGroup.EHG_RightLeg
      );

      weaponStats.push({
        userName: stats.name,
        steamID: stats.steamid,
        weapon: weapon,
        totalKills: totalKills,
        totalDamage: totalDamage,
        killsPerRound: parseFloat(killsPerRound.toFixed(2)),
        damagePerRound: parseFloat(damagePerRound.toFixed(2)),
        shotsFired: shotsFired,
        damagePerShot: parseFloat((totalDamage / shotsFired).toFixed(2)),
        hits: shotsHit,
        danagePerHit: parseFloat((totalDamage / shotsHit).toFixed(2)),
        accuracyPerc: parseFloat(accuracy),
        headshotPerc: parseFloat(headshotPercentage.toFixed(2)),
        chestPerc: parseFloat(chestPercentage.toFixed(2)),
        stomachPerc: parseFloat(stomachPercentage.toFixed(2)),
        armsPerc: parseFloat(armsPercentage.toFixed(2)),
        legsPerc: parseFloat(legsPercentage.toFixed(2))
      });
    }
  }

  let killEvents: KillEvent[] = [];

  playerStats.forEach((playerStat, killerSteamId) => {
    const killer = steamIdToName.get(killerSteamId) || "Unknown Player"; // Default to 'Unknown Player' if the name is not found

    playerStat.roundVictimKills.forEach((victimWeaponMap, round) => {
      victimWeaponMap.forEach((weapon, victimSteamId) => {
        const victim = steamIdToName.get(victimSteamId) || "Unknown Player";
        killEvents.push({ killerSteamId, round, weapon, victimSteamId });
      });
    });

    // Sort the kill events by round
    killEvents.sort((a, b) => a.round - b.round);
  });

  const mergedStats: IMergedStats = {
    allPlayerStats,
    mapStats,
    weaponStats,
    killEvents
  };
  console.log(JSON.stringify(mergedStats, null, 2));
});

demoFile.parseStream(stream);
