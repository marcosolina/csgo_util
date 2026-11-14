import { Box, LinearProgress, Tooltip, Typography } from "@mui/material";
import { IPlayerEntryKillTableProps } from "./interfaces";
import { usePlayerEntryKill } from "./usePlayerEntryKill";
import { MRT_ColumnDef } from "material-react-table";
import { useTranslation } from "react-i18next";
import IxigoTable from "../../../../../common/material-table/IxigoTable";

const LANG_BASE_PATH = "page.stats.player.content.overall.entry-kill-stats";

const PLayerEntryKillTable: React.FC<IPlayerEntryKillTableProps> = ({ steamid }) => {
  const { t } = useTranslation();
  const { state, columns, data, entryKillStats } = usePlayerEntryKill({ steamid });
  return (
    <IxigoTable
      columns={columns as MRT_ColumnDef<object>[]}
      data={data}
      state={state}
      sorting={[]}
      enableColumnActions={false}
      enableColumnFilters={false}
      enablePagination={false}
      enableSorting={false}
      enableBottomToolbar={false}
      enableTopToolbar={true}
      enableGlobalFilter={false}
      enableFullScreenToggle={false}
      enableHiding={false}
      muiTableBodyRowProps={true}
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
  );
};

export default PLayerEntryKillTable;
