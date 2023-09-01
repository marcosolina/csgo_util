import React from "react";
import { IconButton, Tooltip } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import { MaterialReactTable} from "material-react-table";
import { IMapLeaderboardRequest } from "./interfaces";
import { useTranslation } from "react-i18next";
import { useLeaderboardContent } from "./useMapLeaderboardContent";
import { QueryStatus } from "../../../../lib/http-requests";
import Switch from "../../../../common/switch-case/Switch";
import Case from "../../../../common/switch-case/Case";
import TableLoading from "../../../../common/loading/table-loading/TableLoading";


const STRING_PREFIX = "page.stats.maps.leaderboard";
const MapLeaderboardsContent: React.FC<IMapLeaderboardRequest> = ({ mapName }) => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = useLeaderboardContent({ mapName});

  

  return (
    <Switch value={state}>
    <Case case={QueryStatus.loading}>
      <TableLoading />
    </Case>
    <Case case={QueryStatus.success}>
      <MaterialReactTable
      columns={columns}
      data={data ?? []}
      initialState={{
        showColumnFilters: false,
        columnVisibility: {
          steamid: false,
        },
        density: "compact",
        sorting: [{ id: "hltv_rating", desc: true }], //sort by state by default
        pagination: { pageIndex: 0, pageSize: 20 },
        columnPinning: { left: ["username"] },
      }}
      enableColumnActions={false}
      enableColumnFilters={true}
      enableColumnFilterModes
      enableFilterMatchHighlighting={false}
      enableDensityToggle={false}
      enableSorting={true}
      enableTopToolbar={true}
      enableBottomToolbar={true}
      enableGlobalFilter={false}
      enableFullScreenToggle={false}
      enableHiding={true}
      muiTableBodyRowProps={{ hover: false }}
      muiToolbarAlertBannerProps={
        state === QueryStatus.error
          ? {
              color: "error",
              children: t(`${STRING_PREFIX}.error-loading-data`),
            }
          : undefined
      }
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
           </Case>
    </Switch>
  );
};

export default MapLeaderboardsContent;
