export interface IMapPlayed {
  map_name: string;
  count: number;
}

export interface IMapsPlayed {
  maps: IMapPlayed[];
}

export interface IGetAvgScoresPerMapRequest {
  steamIds: string[];
  scoreType: string;
  maps: string[];
  matchesToConsider: string;
}

export interface ITeamScorePerMapRequest {
  map: string;
  matchesToConsider: string;
}

export interface ITeamScorePerMapResponse {
  matches: Record<string, IUserGotvScore[]>;
}

export interface IUserGotvScore {
  user_name: string;
  steam_id: string;
  round_win_share: number;
  kills: number;
  assists: number;
  deaths: number;
  kill_death_ration: number;
  head_shots: number;
  head_shots_percentage: number;
  team_kill_friendly_fire: number;
  entry_kill: number;
  bomb_planted: number;
  bomb_defused: number;
  most_valuable_player: number;
  score: number;
  half_life_television_rating: number;
  five_kills: number;
  four_kills: number;
  three_kills: number;
  two_kills: number;
  one_kill: number;
  trade_kill: number;
  trade_death: number;
  kill_per_round: number;
  assists_per_round: number;
  death_per_round: number;
  average_damage_per_round: number;
  total_damage_health: number;
  total_damage_armor: number;
  one_versus_one: number;
  one_versus_two: number;
  one_versus_three: number;
  one_versus_four: number;
  one_versus_five: number;
  grenades_thrown_count: number;
  flashes_thrown_count: number;
  smokes_thrown_count: number;
  fire_thrown_count: number;
  high_explosive_damage: number;
  fire_damage: number;
  match_played: number;
  side: string;
}

export interface IAvgScoresPerMap {
  scores: Record<string, IAvgScorePerMap[]>;
}

export interface IAvgScorePerMap {
  steam_id: string;
  map_name: string;
  avg_score: number;
}
