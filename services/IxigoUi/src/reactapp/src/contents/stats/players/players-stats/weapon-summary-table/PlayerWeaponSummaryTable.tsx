import { IPlayerWeaponSummaryTableProps } from "./interfaces";
import { useWaponSummary } from "./useWeaponSummary";
import { useTranslation } from "react-i18next";
import IxigoTable from "../../../../../common/material-table/IxigoTable";

const BASE_TRANSLATION_KEY = "page.stats.player.content.overall.weapons-stats";

const PlayerWeaponSummaryTable: React.FC<IPlayerWeaponSummaryTableProps> = ({ steamid }) => {
  const { t } = useTranslation();
  const { state, columns, data } = useWaponSummary({ steamid });
  return (
    <IxigoTable
      columns={columns}
      data={data}
      state={state}
      pagination={{ pageIndex: 0, pageSize: 5 }}
      sorting={[{ id: "kills", desc: true }]}
      columnPinning={{ left: ["weapon", "weapon_img"] }}
      enableColumnActions={false}
      enableColumnFilters={false}
      enablePagination={true}
      enableSorting={true}
      enableBottomToolbar={true}
      enableTopToolbar={false}
      enableGlobalFilter={false}
      enableFullScreenToggle={false}
      enableHiding={false}
      muiTableBodyRowProps={true}
      errorMsg={t(`${BASE_TRANSLATION_KEY}.error-loading-data`) as string}
    />
  );
};

export default PlayerWeaponSummaryTable;
