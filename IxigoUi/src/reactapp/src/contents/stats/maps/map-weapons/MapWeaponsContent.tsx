import React from "react";
import { useTranslation } from "react-i18next";
import { useMapWeaponsContent } from "./useMapWeaponsContent";
import { IMapWeaponContentRequest } from "./interfaces";
import IxigoTable from "../../../../common/material-table/IxigoTable";

const STRING_PREFIX = "page.stats.maps.weapons";
const MapWeaponsContent: React.FC<IMapWeaponContentRequest> = ({ mapName }) => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = useMapWeaponsContent({ mapName });

  return (
    <IxigoTable
      state={state}
      columns={columns}
      data={data}
      sorting={[
        { id: "kills", desc: true },
        { id: "total_damage", desc: true },
      ]}
      columnPinning={{ left: ["weapon_img", "weapon", "username"] }}
      expanded={true}
      grouping={["weapon_img"]}
      pagination={{ pageIndex: 0, pageSize: 20 }}
      enableGrouping={true}
      enableSorting={true}
      enableColumnFilters={true}
      enableBottomToolbar={true}
      enablePagination={true}
      enableFilterMatchHighlighting={false}
      enableTopToolbar={true}
      enableGlobalFilter={false}
      enableFullScreenToggle={true}
      enableColumnOrdering={false}
      enableColumnActions={false}
      enableHiding={true}
      muiTableBodyRowProps={true}
      errorMsg={t(`${STRING_PREFIX}.error-loading-data`) as string}
      refreshMsg={t(`${STRING_PREFIX}.refresh`) as string}
      refetch={refetch}
    />
  );
};

export default MapWeaponsContent;
