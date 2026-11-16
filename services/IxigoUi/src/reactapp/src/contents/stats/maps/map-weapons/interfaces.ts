import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IWeaponMapData } from "../../../../services/stats";

  export interface IMapWeaponContent {
    state: QueryStatus;
    columns: MRT_ColumnDef<IWeaponMapData>[];
    data: IWeaponMapData[];
    refetch: () => void;
  }

  export interface IMapWeaponContentRequest {
    mapName: string;
  }