import { useTranslation } from "react-i18next";
import { usePlayersWeapons } from "./usePlayersWeapons";
import { PlayerWeaponsContentProps } from "./interfaces";
import IxigoTable from "../../../../common/material-table/IxigoTable";

const BASE_TRANSLATION_KEY = "page.stats.player.content.overall.weapons-stats";
const PlayerWeaponsContent: React.FC<PlayerWeaponsContentProps> = ({ steamId }) => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = usePlayersWeapons({ steamId });
  return (
    <IxigoTable
      columns={columns}
      data={data}
      state={state}
      pagination={{ pageIndex: 0, pageSize: 20 }}
      sorting={[{ id: "kills", desc: true }]}
      columnPinning={{ left: ["weapon", "weapon_img"] }}
      enableColumnActions={false}
      enableColumnFilters={true}
      enableSorting={true}
      enableBottomToolbar={true}
      enablePagination={true}
      enableTopToolbar={true}
      enableFilterMatchHighlighting={false}
      enableGlobalFilter={false}
      enableFullScreenToggle={true}
      enableHiding={true}
      muiTableBodyRowProps={true}
      refetch={refetch}
      errorMsg={t(`${BASE_TRANSLATION_KEY}.error-loading-data`) as string}
    />
  );
};

export default PlayerWeaponsContent;
