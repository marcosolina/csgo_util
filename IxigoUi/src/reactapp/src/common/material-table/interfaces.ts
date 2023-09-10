import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";

export interface ColumnSort {
  id: string;
  desc: boolean;
}

export interface ColumnPinningState {
  left?: string[];
  right?: string[];
}

export interface IxigoTableProps<T extends Record<string, any> = {}> {
  state: QueryStatus;
  columns: MRT_ColumnDef<T>[];
  data: T[];
  sorting: ColumnSort[];
  columnPinning?: ColumnPinningState;

  refetch?: () => void;
  title?: string;
  errorMsg?: string;
  refreshMsg?: string;

  enableGrouping?: boolean;
  enableColumnFilters?: boolean;
  enableFilterMatchHighlighting?: boolean;

  columnVisibility?: Record<string, boolean>;
}
