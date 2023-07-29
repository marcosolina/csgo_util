import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";

export interface ISteamUser {
  steam_id: string;
  user_name: string;
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

export interface ILeaderboardContent {
  state: QueryStatus;
  columns: MRT_ColumnDef<IPlayerStats>[];
  data: IPlayerStats[];
  refetch: () => void;
}
