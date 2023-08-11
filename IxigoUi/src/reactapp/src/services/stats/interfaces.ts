export interface IGetStatsRequest<T> {
  viewName: string;
  queryParams?: Partial<T>;
}

export interface IGetStatsResponse<T> {
  view_name: string;
  view_data: T[];
}

export interface ISteamUser {
  steam_id: string;
  user_name: string;
}

export interface IMatchRound {
  match_id: number;
  round: number;
  team: number;
  player_count: number;
  death_count: number;
  winner_team: string;
  total_money_spent: number;
  total_equipment_value: number;
  round_end_reason: number;
  round_type: string;
  t_score: number;
  ct_score: number;
  team1_score: number;
  team2_score: number;
}

export interface IRoundEvent {
  steamid: string;
  round: number;
  match_id: number;
  eventtype: string;
  eventtime: number;
}

export interface IRoundKillEvent {
  victimsteamid: string;
  isfirstkill: boolean;
  match_id: number;
  eventtime: number;
  istradekill: boolean;
  assister: string;
  steamid: string;
  weapon: string;
  flashassister: string;
  round: number;
  killerflashed: string;
  headshot: boolean;
  istradedeath: boolean;
}

export interface IKillCount {
  match_id: number;
  kill_count: number;
  victim: string;
  killer: string;
  killerusername: string;
  victimusername: string;
}

export interface IPlayerStats {
  username: string;
  steamid: string;
  mapname: string;
  matches: string;
  rounds: number;

  first_weapon: string;
  second_weapon: string;
  wins: number;
  loss: number;

  kills: number;
  deaths: number;
  assists: number;
  kpr: number;
  dpr: number;
  kdr: number;
  fkr: number;
  ek: number;
  tk: number;

  mvp: number;
  hltv_rating: number;
  rws: number;

  adr: number;
  kast: number;

  _1v1: number;
  _1v2: number;
  _1v3: number;
  _1v4: number;
  _1v5: number;
  _1vnp: number;

  _1k: number;
  _2k: number;
  _3k: number;
  _4k: number;
  _5k: number;

  headshots: number;
  headshot_percentage: number;
  hr: number;

  bp: number;
  ud: number;
  ffd: number;

  winlossratio: number;
  averagewinscore: number;

  td: number;
  tda: number;
  tdh: number;

  fbt: number;
  fa: number;
  ebt: number;

  ff: number;
  bd: number;
}

export interface IPlayerMatch {
  username: string;
  steamid: string;
  mapname: string;
  matches: string;

  first_weapon: string;
  second_weapon: string;
  wins: number;
  loss: number;

  kills: number;
  deaths: number;
  assists: number;
  kpr: number;
  dpr: number;
  kdr: number;
  ek: number;
  tk: number;

  mvp: number;
  hltv_rating: number;

  adr: number;
  kast: number;

  _1v1: number;
  _1v2: number;
  _1v3: number;
  _1v4: number;
  _1v5: number;

  _1k: number;
  _2k: number;
  _3k: number;
  _4k: number;
  _5k: number;

  headshots: number;
  headshot_percentage: number;
  hr: number;

  bp: number;
  ud: number;
  ffd: number;

  winlossratio: number;
  averagewinscore: number;

  td: number;
  tda: number;
  tdh: number;

  fbt: number;
  fa: number;
  ebt: number;

  ff: number;
  bd: number;
}

export interface IMatchResults {
  match_date: string;
  match_id: number;
  mapname: string;
  team1_total_wins: number;
  team2_total_wins: number;
  total_t_wins: number;
  total_ct_wins: number;
  score_differential: number;
}

export interface IMatchResult {
  match_date: string;
  match_id: number;
  mapname: string;
  team1_total_wins: number;
  team2_total_wins: number;
  total_t_wins: number;
  total_ct_wins: number;
  team1_wins_as_ct: number;
  team2_wins_as_ct: number;
  team1_wins_as_t: number;
  team2_wins_as_t: number;
}

export interface ITeamMatchResults {
  usernames: string;
  steamid: string;

  roundsplayed: number;
  rounds_on_team1: number;
  rounds_on_team2: number;
  last_round_team: string;

  kills: number;
  deaths: number;
  assists: number;
  kpr: number;
  dpr: number;
  kdr: number;
  ek: number;
  tk: number;

  _1v1: number;
  _1v2: number;
  _1v3: number;
  _1v4: number;
  _1v5: number;

  _1k: number;
  _2k: number;
  _3k: number;
  _4k: number;
  _5k: number;

  headshots: number;
  headshot_percentage: number;
  hr: number;

  mvp: number;
  hltv_rating: number;

  adr: number;
  kast: number;

  bp: number;
  ud: number;
  ffd: number;

  score: number;
  rws: number;

  td: number;
  tda: number;
  tdh: number;

  fbt: number;
  fa: number;
  ebt: number;

  match_date: string;
  match_id: number;
  team: number;
  ff: number;
  bd: number;
}

export interface IWeaponMatchData {
  steamid: string;
  username: string;
  match_id: number;
  kills: number;
  headshotkills: number;
  damage_per_shot: number;
  accuracy: number;
  hits: number;
  weapon: string;
  weapon_img: string;
  total_damage: number;
  damage_per_hit: number;
  headshotkills_percentage: number;
  shots_fired: number;
  headshot_percentage: number;
  chest_hit_percentage: number;
  leg_hit_percentage: number;
  stomach_hit_percentage: number;
  arm_hit_percentage: number;
}

export interface IWeaponMapData {
  steamid: string;
  username: string;
  mapname: string;
  kills: number;
  headshotkills: number;
  damage_per_shot: number;
  accuracy: number;
  hits: number;
  weapon: string;
  weapon_img: string;
  total_damage: number;
  damage_per_hit: number;
  headshotkills_percentage: number;
  shots_fired: number;
  headshot_percentage: number;
  chest_hit_percentage: number;
  leg_hit_percentage: number;
  stomach_hit_percentage: number;
  arm_hit_percentage: number;
}

export interface IPlayerOverallStats {
  kills: number;
  ff: number;
  hltv_rating: number;
  bd: number;
  _1v3: number;
  _1v2: number;
  kast: number;
  _1v1: number;
  first_weapon: string;
  hr: number;
  fkr: number;
  bp: number;
  ud: number;
  rws: number;
  loss: number;
  second_weapon: string;
  headshots: number;
  _1vnp: number;
  assists: number;
  _4k: number;
  _2k: number;
  _1v5: number;
  _1v4: number;
  headshot_percentage: number;
  deaths: number;
  wins: number;
  averagewinscore: number;
  winlossratio: number;
  ffd: number;
  ek: number;
  mvp: number;
  dpr: number;
  kpr: number;
  matches: number;
  adr: number;
  steamid: string;
  td: number;
  tda: number;
  _5k: number;
  _3k: number;
  ebt: number;
  tk: number;
  kdr: number;
  _1k: number;
  tdh: number;
  fbt: number;
  fa: number;
  rounds: number;
}

export interface IPlayerClutchStats {
  _1v1p: number;
  _1v2p: number;
  _1v3p: number;
  _1v4p: number;
  _1v5p: number;
  _1v1w: number;
  _1v2w: number;
  _1v3w: number;
  _1v4w: number;
  _1v5w: number;
  _1v1l: number;
  _1v2l: number;
  _1v3l: number;
  _1v4l: number;
  _1v5l: number;
  _1vnp: number;
  steamid: string;
}

export interface IPlayerEntryKillStats {
  steamid: string;
  total_rounds: number;
  total_rounds_t: number;
  total_rounds_ct: number;
  ek_attempts: number;
  ek_success: number;
  ekt_attempts: number;
  ekt_success: number;
  ekct_attempts: number;
  ekct_success: number;
  ek_success_rate: number;
  ek_success_rate_overall: number;
  ekt_success_rate: number;
  ekt_success_rate_overall: number;
  ekct_success_rate: number;
  ekct_success_rate_overall: number;
}

export interface IWeaponData {
  weapon: string;
  kills: number;
  headshotkills: number;
  damage_per_shot: number;
  accuracy: number;
  hits: number;
  damage_per_hit: number;
  headshotkills_percentage: number;
  shots_fired: number;
  chest_hit_percentage: number;
  stomach_hit_percentage: number;
  arm_hit_percentage: number;
  leg_hit_percentage: number;
  steamid: string;
  total_damage: number;
}
