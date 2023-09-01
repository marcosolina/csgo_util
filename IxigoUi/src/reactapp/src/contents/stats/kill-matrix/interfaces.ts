import { QueryStatus } from "react-query";


export interface IRowData {
  "Killer/Victim": string;
  [player: string]: string | number;
}

export interface IKillMatrixResponse {
  state: QueryStatus;
  columns: any;
  flattenedData: any;
  refetch: () => void;
}

export interface IKillMatrixRequest {
  match_id: number;
}