import { MRT_ColumnDef } from "material-react-table";
import { IPlayerMatch } from "../../../../services/stats";
import { QueryStatus } from "react-query";

export interface IPlayersMapsContentProps {
  steamId: string;
}

export interface IPlayersMapsContentRequest {
  steamId: string;
}

export interface IPlayersMapsContentResponse {
  state: QueryStatus;
  columns: MRT_ColumnDef<IPlayerMatch>[];
  data: IPlayerMatch[];
  refetch: () => void;
}
