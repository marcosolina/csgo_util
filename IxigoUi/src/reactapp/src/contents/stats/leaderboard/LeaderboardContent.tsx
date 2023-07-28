import { IconButton, Tooltip, Box } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import { MaterialReactTable } from "material-react-table";
import { useLeaderboardContent } from "./useLeaderboardContent";
import { QueryStatus } from "../../../lib/http-requests";
import Switch from "../../../common/switch-case/Switch";
import Case from "../../../common/switch-case/Case";

const LeaderboardContent = () => {
  const { columns, data, state } = useLeaderboardContent();

  return (
    <Switch value={state}>
      <Case case={QueryStatus.loading}>loading</Case>
      <Case case={QueryStatus.success}>
        <MaterialReactTable
          columns={columns}
          data={data || []} //data is undefined on first render  ?.data ?? []
          initialState={{
            showColumnFilters: false,
            density: "compact",
            pagination: { pageIndex: 0, pageSize: 10 },
            sorting: [{ id: "hltv_rating", desc: true }], //sort by state by default
            columnPinning: { left: ["username", "matches", "hltv_rating"] },
          }}
          enableColumnFilterModes
          enableDensityToggle={false}
          enablePinning
          enableMultiSort
          enableFilterMatchHighlighting={false}
          enablePagination
          enableGlobalFilter={true}
          muiToolbarAlertBannerProps={
            state === QueryStatus.error
              ? {
                  color: "error",
                  children: "Error loading data",
                }
              : undefined
          }
          renderTopToolbarCustomActions={() => (
            <Tooltip arrow title="Refresh Data">
              <IconButton
                onClick={() => {
                  /* TODO call refetch */
                }}
              >
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
      </Case>
    </Switch>
  );
};

export default LeaderboardContent;
