import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";

export interface IPlayersMatchesContentProps {
  steamId: string;
}

export interface IPlayersMatchesData {
  match_id: number;
  match_date: string;
  mapName: string;
  roundsplayed: number;
  last_round_team: string;
  rounds_on_team1: number;
  rounds_on_team2: number;
  kills: number;
  deaths: number;
  assists: number;
  score: number;
  rws: number;
  headshots: number;
  headshot_percentage: number;
  mvp: number;
  hltv_rating: number;
  adr: number;
  kpr: number;
  dpr: number;
  kdr: number;
  hr: number;
  bp: number;
  ud: number;
  ffd: number;
  td: number;
  tda: number;
  tdh: number;
  fa: number;
  ebt: number;
  fbt: number;
  ek: number;
  tk: number;
  _1k: number;
  _2k: number;
  _3k: number;
  _4k: number;
  _5k: number;
  _1v1: number;
  _1v2: number;
  _1v3: number;
  _1v4: number;
  _1v5: number;
}

export interface IPlayersMatchesContentRequest {
  steamId: string;
}

export interface IPlayersMatchesContentResponse {
  state: QueryStatus;
  columns: MRT_ColumnDef<IPlayersMatchesData>[];
  data: IPlayersMatchesData[];
}
