import { useTranslation } from "react-i18next";
import { IPlayersMapsContentProps } from "./interfaces";
import { usePlayerMaps } from "./usePlayerMaps";
import IxigoTable from "../../../../common/material-table/IxigoTable";

const BASE_TRANSLATION_KEY = "page.stats.player.content.maps";
const PlayerMapsContent: React.FC<IPlayersMapsContentProps> = ({ steamId }) => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = usePlayerMaps({ steamId });
  return (
    <IxigoTable
      columns={columns}
      data={data}
      state={state}
      errorMsg={t(`${BASE_TRANSLATION_KEY}.error-loading-data`) as string}
      refetch={refetch}
      refreshMsg={t(`${BASE_TRANSLATION_KEY}.refresh-data`) as string}
      sorting={[{ id: "matches", desc: true }]}
      columnPinning={{ left: ["mapname", "matches", "hltv_rating"] }}
      enableFilterMatchHighlighting={false}
    />
  );
};

export default PlayerMapsContent;
