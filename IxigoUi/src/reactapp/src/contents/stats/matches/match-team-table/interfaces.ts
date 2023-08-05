export interface TeamMatchProps {
  match_id: number;
  team: string;
}

export interface TeamMatch {
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
