import { MRT_ColumnDef, MRT_TableInstance } from "material-react-table";
import { ReactNode } from "react";
import { QueryStatus } from "react-query";

export interface ColumnSort {
  id: string;
  desc: boolean;
}

export interface ColumnPinningState {
  left?: string[];
  right?: string[];
}

export interface PaginationState {
  pageIndex: number;
  pageSize: number;
}

export interface IxigoTableProps<T extends Record<string, any>> {
  state: QueryStatus;
  columns: MRT_ColumnDef<T>[];
  data: T[];
  sorting: ColumnSort[];
  columnPinning?: ColumnPinningState;

  pagination?: PaginationState;

  enableColumnActions?: boolean;
  enableColumnFilters?: boolean;
  enablePagination?: boolean;
  enableSorting?: boolean;
  enableBottomToolbar?: boolean;
  enableTopToolbar?: boolean;
  enableGlobalFilter?: boolean;
  enableFullScreenToggle?: boolean;
  enableHiding?: boolean;
  muiTableBodyRowProps?: boolean;

  renderTopToolbarCustomActions?: (props: { table: MRT_TableInstance<T> }) => ReactNode;
  refetch?: () => void;
  title?: string;
  errorMsg?: string;
  refreshMsg?: string;

  enableGrouping?: boolean;
  enableFilterMatchHighlighting?: boolean;

  columnVisibility?: Record<string, boolean>;
}
