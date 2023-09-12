import { MRT_ColumnDef } from "material-react-table";
import { useMatchRoundsContent } from "./useMatchRoundsContent";
import { IMatchRoundContentRequest } from "./interfaces";
import IxigoTable from "../../../../common/material-table/IxigoTable";

const MatchRoundsContent: React.FC<IMatchRoundContentRequest> = ({ match_id }) => {
  const { columns, data, state, renderDetailPanel } = useMatchRoundsContent({ match_id });

  return (
    <IxigoTable
      state={state}
      columns={columns as MRT_ColumnDef<object>[]}
      data={data}
      enableColumnActions={false}
      enableColumnFilters={false}
      enableSorting={false}
      enableTableHead={true}
      enableTopToolbar={false}
      enableBottomToolbar={false}
      enableGlobalFilter={false}
      enableFullScreenToggle={false}
      enableHiding={false}
      enablePagination={false}
      renderDetailPanel={renderDetailPanel}
      sorting={[{ id: "round", desc: false }]}
    />
  );
};

export default MatchRoundsContent;
