import { useLeaderboardContent } from "./useLeaderboardContent";
import { useTranslation } from "react-i18next";
import IxigoTable from "../../../common/material-table/IxigoTable";

const STRING_PREFIX = "page.stats.leaderboard";

const LeaderboardContent = () => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = useLeaderboardContent();

  return (
    <IxigoTable
      columns={columns}
      data={data}
      state={state}
      title={t(`${STRING_PREFIX}.title`) as string}
      columnPinning={{ left: ["username", "matches", "hltv_rating"] }}
      sorting={[{ id: "hltv_rating", desc: true }]}
      refetch={refetch}
      errorMsg={t(`${STRING_PREFIX}.error-loading-data`) as string}
      refreshMsg={t(`${STRING_PREFIX}.refresh-data`) as string}
    />
  );
};

export default LeaderboardContent;
