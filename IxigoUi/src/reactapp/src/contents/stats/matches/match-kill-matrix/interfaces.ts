import { QueryStatus } from "react-query";

export interface IRowData {
    "Killer/Victim": string;
    [player: string]: string | number;
  }

  export interface IMatchKillMatrixResponse {
    state: QueryStatus;
    columns: any;
    flattenedData: any;
    refetch: () => void;
  }

  export interface IMatchKillMatrixRequest {
    match_id: number;
  }