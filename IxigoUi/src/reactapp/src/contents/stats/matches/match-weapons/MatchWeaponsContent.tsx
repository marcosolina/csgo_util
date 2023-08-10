import React from "react";
import { MaterialReactTable } from "material-react-table";
import { QueryStatus } from "../../../../lib/http-requests";
import Switch from "../../../../common/switch-case/Switch";
import Case from "../../../../common/switch-case/Case";
import TableLoading from "../../../../common/loading/table-loading/TableLoading";
import { useTranslation } from "react-i18next";
import { useMatchWeaponsContent } from "./useMatchWeaponsContent";
import { IMatchWeaponContentRequest } from "./interfaces";

const STRING_PREFIX = "page.stats.match.weapons";

const MatchWeaponsContent: React.FC<IMatchWeaponContentRequest> = ({ match_id }) => {
  const { t } = useTranslation();
  const { columns, data, state } = useMatchWeaponsContent({ match_id});

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
          sorting: [
            { id: "kills", desc: true },
            { id: "total_damage", desc: true },
          ], //sort by state by default
          columnPinning: { left: ["weapon_img", "weapon", "username"] },
          expanded: true,
          grouping: ["weapon_img"],
          pagination: { pageIndex: 0, pageSize: 20 },
        }}
        enableGrouping
        enableColumnFilters={true}
        enableColumnFilterModes
        enableSorting={true}
        enableMultiSort
        enableBottomToolbar={true}
        enablePagination={true}
        enableTopToolbar={true}
        enableDensityToggle={false}
        enableFilterMatchHighlighting={false}
        enableGlobalFilter={false}
        enableFullScreenToggle={true}
        enableColumnOrdering={false}
        enableColumnActions={false}
        enableColumnDragging={false}
        enableHiding={true}
        muiTableBodyRowProps={{ hover: false }}
        muiToolbarAlertBannerProps={
          state === QueryStatus.error
            ? {
                color: "error",
                children: t(`${STRING_PREFIX}.error-loading-data`),
              }
            : undefined
        }
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

export default MatchWeaponsContent;
