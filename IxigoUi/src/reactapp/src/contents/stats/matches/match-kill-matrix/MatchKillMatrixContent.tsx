import React from "react";
import { IconButton, Tooltip } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import { MaterialReactTable } from "material-react-table";
import { IMatchKillMatrixRequest } from "./interfaces";
import { useMatchKillMatrixContent } from "./useMatchKillMatrixContent";
import { QueryStatus } from "../../../../lib/http-requests";
import { useTranslation } from "react-i18next";

const STRING_PREFIX = "page.stats.match.killmatrix";


const MatchKillMatrixContent: React.FC<IMatchKillMatrixRequest> = ({ match_id }) => {
  const { t } = useTranslation();
  const { columns, flattenedData, state, refetch } = useMatchKillMatrixContent({ match_id});

  return (
    <MaterialReactTable
      columns={columns as any} // cast to 'any' for now
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
  );
};

export default MatchKillMatrixContent;
