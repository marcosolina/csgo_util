import { IconButton, Tooltip } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import { MaterialReactTable } from "material-react-table";
import { useMatchRoundsContent } from "./useMatchRoundsContent";
import { IMatchRoundContentRequest } from "./interfaces";
import { QueryStatus } from "../../../../lib/http-requests";

const MatchRoundsContent: React.FC<IMatchRoundContentRequest> = ({ match_id }) => {

  const { columns, data, state, refetch, renderDetailPanel } = useMatchRoundsContent({ match_id});

  return (
    <MaterialReactTable
      columns={columns}
      data={data}
      initialState={{
        showColumnFilters: false,
        density: "compact",
        sorting: [{ id: "round", desc: false }],
      }}
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
      renderTopToolbarCustomActions={() => (
        <Tooltip arrow title="Refresh Data">
          <IconButton onClick={() => refetch()}>
            <RefreshIcon />
          </IconButton>
        </Tooltip>
      )}
      rowCount={data?.length ?? 0}
      state={{
        isLoading: state === QueryStatus.loading,
        showAlertBanner: state === QueryStatus.error,
        showProgressBars: state === QueryStatus.loading,
      }}
    />
  );
};

export default MatchRoundsContent;
