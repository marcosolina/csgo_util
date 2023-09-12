import React from "react";
import { MRT_ColumnDef } from "material-react-table";
import { useTranslation } from "react-i18next";
import { useMatchWeaponsContent } from "./useMatchWeaponsContent";
import { IMatchWeaponContentRequest } from "./interfaces";
import IxigoTable from "../../../../common/material-table/IxigoTable";

const STRING_PREFIX = "page.stats.match.weapons";

const MatchWeaponsContent: React.FC<IMatchWeaponContentRequest> = ({ match_id }) => {
  const { t } = useTranslation();
  const { columns, data, state } = useMatchWeaponsContent({ match_id });

  return (
    <IxigoTable
      state={state}
      columns={columns as MRT_ColumnDef<object>[]}
      data={data}
      enableGrouping={true}
      enableColumnFilters={true}
      enableSorting={true}
      enableBottomToolbar={true}
      enablePagination={true}
      enableTopToolbar={true}
      enableFilterMatchHighlighting={false}
      enableGlobalFilter={false}
      enableFullScreenToggle={true}
      enableColumnOrdering={false}
      enableColumnActions={false}
      enableHiding={true}
      muiTableBodyRowProps={true}
      sorting={[
        { id: "kills", desc: true },
        { id: "total_damage", desc: true },
      ]}
      expanded={true}
      columnPinning={{ left: ["weapon_img", "weapon", "username"] }}
      grouping={["weapon_img"]}
      pagination={{ pageIndex: 0, pageSize: 20 }}
      errorMsg={t(`${STRING_PREFIX}.error-loading-data`) as string}
    />
  );
};

export default MatchWeaponsContent;
