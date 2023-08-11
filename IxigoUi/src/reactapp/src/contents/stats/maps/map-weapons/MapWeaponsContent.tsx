import React from "react";
import { IconButton, Tooltip } from "@mui/material";
import { MaterialReactTable } from "material-react-table";
import RefreshIcon from "@mui/icons-material/Refresh";
import { useTranslation } from "react-i18next";
import { useMapWeaponsContent } from "./useMapWeaponsContent";
import { QueryStatus } from "../../../../lib/http-requests";
import Switch from "../../../../common/switch-case/Switch";
import Case from "../../../../common/switch-case/Case";
import TableLoading from "../../../../common/loading/table-loading/TableLoading";
import { IMapWeaponContentRequest } from "./interfaces";

const MapWeaponsContent: React.FC<IMapWeaponContentRequest> = ({ mapName }) => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = useMapWeaponsContent({ mapName});

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
      enableSorting={true}
      enableMultiSort
      enableColumnFilters={true}
      enableColumnFilterModes
      enableDensityToggle={false}
      enableBottomToolbar={true}
      enablePagination={true}
      enableFilterMatchHighlighting={false}
      enableTopToolbar={true}
      enableGlobalFilter={false}
      enableFullScreenToggle={true}
      enableColumnOrdering={false}
      enableColumnActions={false}
      enableColumnDragging={false}
      enableHiding={true}
      muiTableBodyRowProps={{ hover: false }}
      renderTopToolbarCustomActions={() => (
        <Tooltip arrow title="Refresh Data">
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

export default MapWeaponsContent;
