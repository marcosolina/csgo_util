import { Tooltip } from "@mui/material";
import { MRT_Column } from "material-react-table";

function customHeader<T extends Record<string, any>>(tooltipText: string) {
  return ({ column }: { column: MRT_Column<T> }) => (
    <Tooltip title={tooltipText}>
      <span>{column.columnDef.header}</span>
    </Tooltip>
  );
}

export default customHeader;
