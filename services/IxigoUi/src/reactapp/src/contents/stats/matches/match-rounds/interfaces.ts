import { IMatchRound } from "../../../../services";
import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";


export interface IMatchRoundContentRequest {
  match_id: number;
}


export interface IFinanceCellProps {
  cell: any;
  row: { index: number };
  team: "team1" | "team2";
  maxValues:
    | { total_equipment_value: number; total_money_spent: number }
    | undefined;
}

export interface ITableRound {
  round: number;
  team1?: IMatchRound;
  team2?: IMatchRound;
  killEvents?: any[];
  roundEvents?: any[];
}

export interface IUseMatchRoundsContentResponse {
    columns: MRT_ColumnDef<ITableRound>[];
    state: QueryStatus;
    data: ITableRound[];
    refetch: () => void;
    renderDetailPanel: ({ row }: { row: any }) => JSX.Element | null;
  }
  