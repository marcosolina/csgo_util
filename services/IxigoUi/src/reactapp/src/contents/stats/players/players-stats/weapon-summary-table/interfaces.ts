import { QueryStatus } from "react-query";
import { IWeaponData } from "../../../../../services/stats";
import { MRT_ColumnDef } from "material-react-table";

export interface IPlayerWeaponSummaryTableProps {
  steamid: string;
}

export interface IWeaponDataSummary extends IWeaponData {
  weapon_img: string;
}

export interface IPlayerWeaponSummaryRequest {
  steamid: string;
}
export interface IPlayerWeaponSummaryResponse {
  state: QueryStatus;
  columns: MRT_ColumnDef<IWeaponDataSummary>[];
  data: IWeaponDataSummary[];
}
