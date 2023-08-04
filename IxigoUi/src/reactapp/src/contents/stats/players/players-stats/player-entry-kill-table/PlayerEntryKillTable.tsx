import { Box, LinearProgress, Skeleton, Tooltip, Typography } from "@mui/material";
import Case from "../../../../../common/switch-case/Case";
import Switch from "../../../../../common/switch-case/Switch";
import { QueryStatus } from "../../../../../lib/http-requests";
import { IPlayerEntryKillTableProps } from "./interfaces";
import { usePlayerEntryKill } from "./usePlayerEntryKill";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import MaterialReactTable from "material-react-table";
import { useTranslation } from "react-i18next";

const LANG_BASE_PATH = "page.stats.player.content.overall.entry-kill-stats";

const PLayerEntryKillTable: React.FC<IPlayerEntryKillTableProps> = ({ steamid }) => {
  const { t } = useTranslation();
  const { state, columns, data, entryKillStats } = usePlayerEntryKill({ steamid });
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
              {entryKillStats && (
                <Box display="flex" width="100%" alignItems="center">
                  <Typography width="100%" component="span">
                    {`${t(`${LANG_BASE_PATH}.toolbar-label`)}: ${entryKillStats.ek_success_rate_overall}%`}
                  </Typography>
                  <Box width="100%" mx={2}>
                    <Tooltip title={`${entryKillStats.ek_success_rate_overall}%`} placement="top">
                      <LinearProgress
                        variant="determinate"
                        value={entryKillStats.ek_success_rate_overall}
                        color="primary"
                      />
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

export default PLayerEntryKillTable;
