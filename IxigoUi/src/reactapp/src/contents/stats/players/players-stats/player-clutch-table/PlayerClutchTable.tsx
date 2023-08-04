import React from "react";
import { usePlayerClutch } from "./usePlayerClutch";
import { IPlayerClutchTableProps } from "./interfaces";
import Switch from "../../../../../common/switch-case/Switch";
import Case from "../../../../../common/switch-case/Case";
import { QueryStatus } from "../../../../../lib/http-requests";
import { Box, LinearProgress, Skeleton, Tooltip, Typography } from "@mui/material";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import MaterialReactTable from "material-react-table";
import { useTranslation } from "react-i18next";

const LANG_BASE_PATH = "page.stats.player.content.overall.clutch-stats";

const PlayerClutchTable: React.FC<IPlayerClutchTableProps> = ({ steamid }) => {
  const { t } = useTranslation();
  const { state, columns, data, playerClutchStats } = usePlayerClutch({ steamid });

  return (
    <Switch value={state}>
      <Case case={QueryStatus.success}>
        <MaterialReactTable
          columns={columns}
          data={data}
          enableColumnActions={false}
          enableColumnFilters={false}
          enablePagination={false}
          enableSorting={false}
          enableBottomToolbar={false}
          enableTopToolbar={true}
          enableDensityToggle={false}
          enableGlobalFilter={false}
          enableFullScreenToggle={false}
          enableHiding={false}
          muiTableBodyRowProps={{ hover: false }}
          initialState={{ density: "compact" }}
          state={{
            isLoading: state === QueryStatus.loading,
            showAlertBanner: state === QueryStatus.error,
            showProgressBars: state === QueryStatus.loading,
          }}
          renderTopToolbarCustomActions={() => (
            <Box width="100%">
              <Typography variant="h5" component="h2" align="center" gutterBottom>
                {t(`${LANG_BASE_PATH}.title`)}
              </Typography>
              {playerClutchStats && (
                <Box display="flex" width="100%" alignItems="center">
                  <Typography width="100%" component="span">
                    {`${t(`${LANG_BASE_PATH}.toolbar-label`)}: ${playerClutchStats._1vnp}%`}
                  </Typography>
                  <Box width="100%" mx={2}>
                    <Tooltip title={`${playerClutchStats._1vnp}%`} placement="top">
                      <LinearProgress variant="determinate" value={playerClutchStats._1vnp} color="primary" />
                    </Tooltip>
                  </Box>
                </Box>
              )}
            </Box>
          )}
        />
      </Case>
      <Case case={QueryStatus.loading}>
        <Skeleton animation="wave" style={{ height: "100%" }} />
      </Case>
      <Case case={QueryStatus.error}>
        <ErrorOutlineIcon color="error" fontSize="large" />
      </Case>
    </Switch>
  );
};

export default PlayerClutchTable;
