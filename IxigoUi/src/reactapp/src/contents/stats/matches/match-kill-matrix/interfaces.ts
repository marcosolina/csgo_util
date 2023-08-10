import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IPlayerStats } from "../../../../services/stats";

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