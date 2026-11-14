import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IWeaponMatchData } from "../../../../services/stats";

  export interface IMatchWeaponContent {
    state: QueryStatus;
    columns: MRT_ColumnDef<IWeaponMatchData>[];
    data: IWeaponMatchData[];
    refetch: () => void;
  }

  export interface IMatchWeaponContentRequest {
    match_id: number;
  }