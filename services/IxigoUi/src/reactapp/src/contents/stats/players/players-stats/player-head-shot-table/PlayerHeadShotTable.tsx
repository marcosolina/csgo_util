import { useTranslation } from "react-i18next";
import { Box, LinearProgress, Tooltip, Typography } from "@mui/material";
import { MRT_ColumnDef } from "material-react-table";
import { IPlayerHeadShotTableProps } from "./interfaces";
import { usePlayerHeadShotTable } from "./usePlayerHeadShotTable";
import { useMemo } from "react";
import IxigoTable from "../../../../../common/material-table/IxigoTable";

const LANG_BASE_PATH = "page.stats.player.content.overall.head-shots";

const PlayerHeadShotTable: React.FC<IPlayerHeadShotTableProps> = ({ steamid }) => {
  const { t } = useTranslation();
  const { state, columns, data, playerOverall } = usePlayerHeadShotTable({ steamid });

  const percentage = useMemo(() => {
    if (!playerOverall) return 0;
    return (playerOverall.headshots * 100.0) / playerOverall.kills;
  }, [playerOverall]);

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
                {`${t(`${LANG_BASE_PATH}.toolbar-label`)}: ${percentage.toFixed(0)}%`}
              </Typography>
              <Box width="100%" mx={2}>
                <Tooltip title={`${percentage.toFixed(0)}%`} placement="top">
                  <LinearProgress variant="determinate" value={percentage} color="primary" />
                </Tooltip>
              </Box>
            </Box>
          )}
        </Box>
      )}
    />
  );
};

export default PlayerHeadShotTable;
