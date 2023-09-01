import React from "react";
import { IconButton, Tooltip } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import { MaterialReactTable } from "material-react-table";
import { IMapMatchRequest } from "./interfaces";
import { useTranslation } from "react-i18next";
import { useMapMatchesContent } from "./useMapMatchesContent";
import { QueryStatus } from "../../../../lib/http-requests";
import Switch from "../../../../common/switch-case/Switch";
import Case from "../../../../common/switch-case/Case";
import TableLoading from "../../../../common/loading/table-loading/TableLoading";

const STRING_PREFIX = "page.stats.maps.matches";
const MapMatchesContent: React.FC<IMapMatchRequest> = ({ mapName }) => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = useMapMatchesContent({ mapName });

  return (
    <Switch value={state}>
      <Case case={QueryStatus.loading}>
        <TableLoading />
      </Case>
      <Case case={QueryStatus.success}>
        <MaterialReactTable
          columns={columns}
          data={data ?? []} //data is undefined on first render  ?.data ?? []
          initialState={{
            showColumnFilters: false,
            density: "compact",
            pagination: { pageIndex: 0, pageSize: 10 },
            //grouping: ['mapname'],
            sorting: [{ id: "match_date", desc: true }], //sort by state by default
          }}
          enableGrouping
          enableColumnFilters={true}
          enableColumnFilterModes
          enableFilterMatchHighlighting={false}
          enablePagination
          enableDensityToggle={false}
          muiToolbarAlertBannerProps={
            state === QueryStatus.error
              ? {
                  color: "error",
                  children: t(`${STRING_PREFIX}.error-loading-data`),
                }
              : undefined
          }
          renderTopToolbarCustomActions={() => (
            <Tooltip arrow title={t(`${STRING_PREFIX}.refresh`)}>
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

export default MapMatchesContent;
