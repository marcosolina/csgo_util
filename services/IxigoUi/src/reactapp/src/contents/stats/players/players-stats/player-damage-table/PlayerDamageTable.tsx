import { useTranslation } from "react-i18next";
import { Box, LinearProgress, Tooltip, Typography } from "@mui/material";
import { MRT_ColumnDef } from "material-react-table";
import { IPlayerDamageTableProps } from "./interfaces";
import { usePlayerDamageTable } from "./usePlayerDamageTable";
import IxigoTable from "../../../../../common/material-table/IxigoTable";

const LANG_BASE_PATH = "page.stats.player.content.overall.damage";

const PlayerDamageTable: React.FC<IPlayerDamageTableProps> = ({ steamid }) => {
  const { t } = useTranslation();
  const { state, columns, data, playerOverall, maxAdr } = usePlayerDamageTable({ steamid });

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
          {playerOverall && (
            <Box display="flex" width="100%" alignItems="center">
              <Typography width="100%" component="span">
                {`${t(`${LANG_BASE_PATH}.toolbar-label`)}: ${playerOverall.adr.toFixed(0)}`}
              </Typography>
              <Box width="100%" mx={2}>
                <Tooltip title={`${playerOverall.adr.toFixed(0)}`} placement="top">
                  <LinearProgress
                    variant="determinate"
                    value={playerOverall && maxAdr ? (playerOverall.adr / maxAdr) * 100 : 0}
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

export default PlayerDamageTable;
