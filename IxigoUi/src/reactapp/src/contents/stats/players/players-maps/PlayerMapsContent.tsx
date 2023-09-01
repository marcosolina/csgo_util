import { useTranslation } from "react-i18next";
import { IPlayersMapsContentProps } from "./interfaces";
import { usePlayerMaps } from "./usePlayerMaps";
import Switch from "../../../../common/switch-case/Switch";
import Case from "../../../../common/switch-case/Case";
import { QueryStatus } from "../../../../lib/http-requests";
import TableLoading from "../../../../common/loading/table-loading/TableLoading";
import MaterialReactTable from "material-react-table";
import { IconButton, Tooltip } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";

const BASE_TRANSLATION_KEY = "page.stats.player.content.maps";
const PlayerMapsContent: React.FC<IPlayersMapsContentProps> = ({ steamId }) => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = usePlayerMaps({ steamId });
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
            sorting: [{ id: "matches", desc: true }], //sort by state by default
            columnPinning: { left: ["mapname", "matches", "hltv_rating"] },
          }}
          enableColumnFilterModes
          enableDensityToggle={false}
          enableFilterMatchHighlighting={false}
          enablePinning
          enableMultiSort
          enablePagination
          muiToolbarAlertBannerProps={
            state === QueryStatus.error
              ? {
                  color: "error",
                  children: t(`${BASE_TRANSLATION_KEY}.error-loading-data`),
                }
              : undefined
          }
          renderTopToolbarCustomActions={() => (
            <Tooltip arrow title={t(`${BASE_TRANSLATION_KEY}.refresh-data`)}>
              <IconButton onClick={refetch}>
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

export default PlayerMapsContent;
