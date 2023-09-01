import { QueryStatus } from "react-query";
import { IWeaponData } from "../../../../services";
import { MRT_ColumnDef } from "material-react-table";

export interface PlayerWeaponsContentProps {
  steamId: string;
}

export interface IWeaponDataDetails extends IWeaponData {
  weapon_img: string;
}

export interface IPlayerWeaponContentRequest {
  steamId: string;
}

export interface IPlayerWeaponContentResponse {
  state: QueryStatus;
  columns: MRT_ColumnDef<IWeaponDataDetails>[];
  data: IWeaponDataDetails[];
}
