import { useTranslation } from "react-i18next";
import { usePlayersWeapons } from "./usePlayersWeapons";
import { MaterialReactTable } from "material-react-table";
import { QueryStatus } from "../../../../lib/http-requests";
import { PlayerWeaponsContentProps } from "./interfaces";
import Case from "../../../../common/switch-case/Case";
import Switch from "../../../../common/switch-case/Switch";
import TableLoading from "../../../../common/loading/table-loading/TableLoading";

const BASE_TRANSLATION_KEY = "page.stats.player.content.overall.weapons-stats";
const PlayerWeaponsContent: React.FC<PlayerWeaponsContentProps> = ({ steamId }) => {
  const { t } = useTranslation();
  const { columns, data, state } = usePlayersWeapons({ steamId });
  return (
    <Switch value={state}>
      <Case case={QueryStatus.loading}>
        <TableLoading />
      </Case>
      <Case case={QueryStatus.success}>
        <MaterialReactTable
          columns={columns}
          data={data} //data is undefined on first render  ?.data ?? []
          initialState={{
            showColumnFilters: false,
            density: "compact",
            sorting: [{ id: "kills", desc: true }], //sort by state by default
            pagination: { pageIndex: 0, pageSize: 20 },
            columnPinning: { left: ["weapon", "weapon_img"] },
          }}
          enableColumnActions={false}
          enableColumnFilters={true}
          enableColumnFilterModes
          enableSorting={true}
          enableBottomToolbar={true}
          enablePagination={true}
          enableTopToolbar={true}
          enableFilterMatchHighlighting={false}
          enableDensityToggle={false}
          enableGlobalFilter={false}
          enableFullScreenToggle={true}
          enableHiding={true}
          muiTableBodyRowProps={{ hover: false }}
          muiToolbarAlertBannerProps={
            state === QueryStatus.error
              ? {
                  color: "error",
                  children: t(`${BASE_TRANSLATION_KEY}.error-loading-data`),
                }
              : undefined
          }
          rowCount={data.length}
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

export default PlayerWeaponsContent;
