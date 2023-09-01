import { Skeleton } from "@mui/material";
import Case from "../../../../../common/switch-case/Case";
import Switch from "../../../../../common/switch-case/Switch";
import { IPlayerWeaponSummaryTableProps } from "./interfaces";
import { useWaponSummary } from "./useWeaponSummary";
import { QueryStatus } from "../../../../../lib/http-requests";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import MaterialReactTable from "material-react-table";
import { useTranslation } from "react-i18next";

const BASE_TRANSLATION_KEY = "page.stats.player.content.overall.weapons-stats";

const PlayerWeaponSummaryTable: React.FC<IPlayerWeaponSummaryTableProps> = ({ steamid }) => {
  const { t } = useTranslation();
  const { state, columns, data } = useWaponSummary({ steamid });
  return (
    <Switch value={state}>
      <Case case={QueryStatus.success}>
        <MaterialReactTable
          columns={columns}
          data={data} //data is undefined on first render  ?.data ?? []
          initialState={{
            showColumnFilters: false,
            density: "compact",
            pagination: { pageIndex: 0, pageSize: 5 },
            sorting: [{ id: "kills", desc: true }], //sort by state by default
            columnPinning: { left: ["weapon", "weapon_img"] },
          }}
          enableColumnActions={false}
          enableColumnFilters={false}
          enablePagination={true}
          enableSorting={true}
          enableBottomToolbar={true}
          enableTopToolbar={false}
          enableGlobalFilter={false}
          enableFullScreenToggle={false}
          enableHiding={false}
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
      <Case case={QueryStatus.loading}>
        <Skeleton animation="wave" style={{ height: "100%" }} />
      </Case>
      <Case case={QueryStatus.error}>
        <ErrorOutlineIcon color="error" fontSize="large" />
      </Case>
    </Switch>
  );
};

export default PlayerWeaponSummaryTable;
