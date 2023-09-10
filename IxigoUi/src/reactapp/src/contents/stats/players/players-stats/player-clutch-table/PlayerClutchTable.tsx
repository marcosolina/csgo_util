import React from "react";
import { usePlayerClutch } from "./usePlayerClutch";
import { IPlayerClutchTableProps } from "./interfaces";
import { Box, LinearProgress, Tooltip, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";
import IxigoTable from "../../../../../common/material-table/IxigoTable";

const LANG_BASE_PATH = "page.stats.player.content.overall.clutch-stats";

const PlayerClutchTable: React.FC<IPlayerClutchTableProps> = ({ steamid }) => {
  const { t } = useTranslation();
  const { state, columns, data, playerClutchStats } = usePlayerClutch({ steamid });

  return (
    <IxigoTable
      columns={columns}
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
  );
};

export default PlayerClutchTable;
