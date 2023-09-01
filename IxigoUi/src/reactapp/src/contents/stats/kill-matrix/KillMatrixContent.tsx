import React from "react";
import { IconButton, Tooltip, Box, Typography  } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import { MaterialReactTable } from "material-react-table";
import { QueryStatus } from "../../../lib/http-requests";
import Switch from "../../../common/switch-case/Switch";
import Case from "../../../common/switch-case/Case";
import TableLoading from "../../../common/loading/table-loading/TableLoading";
import { useTranslation } from "react-i18next";
import { useKillMatrixContent } from "./useKillMatrixContent";

const STRING_PREFIX = "page.stats.killmatrix";

const KillMatrixContent: React.FC = () => {
  const { t } = useTranslation();
  const { columns, flattenedData, state, refetch } = useKillMatrixContent();

  return (
    <Switch value={state}>
      <Case case={QueryStatus.loading}>
        <TableLoading />
      </Case>
      <Case case={QueryStatus.success}>
        <Box textAlign="center">
          <Typography variant="h5">{t(`${STRING_PREFIX}.title`)}</Typography>
        </Box>
        <MaterialReactTable
      columns={columns as any}
      data={flattenedData ?? []}
      initialState={{
        showColumnFilters: false,
        density: "compact",
        columnVisibility: {
          Team: false,
        },
        sorting: [{ id: "Team", desc: false }],
        pagination: { pageIndex: 0, pageSize: 10 },
        columnPinning: { left: ["Killer/Victim"] },
      }}
      enableColumnFilterModes
      enablePinning
      enableMultiSort
      enableDensityToggle={false}
      enablePagination
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
      rowCount={flattenedData?.length ?? 0}
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

export default KillMatrixContent;
