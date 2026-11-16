import React from "react";
import { IMapLeaderboardRequest } from "./interfaces";
import { useTranslation } from "react-i18next";
import { useLeaderboardContent } from "./useMapLeaderboardContent";
import IxigoTable from "../../../../common/material-table/IxigoTable";

const STRING_PREFIX = "page.stats.maps.leaderboard";
const MapLeaderboardsContent: React.FC<IMapLeaderboardRequest> = ({ mapName }) => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = useLeaderboardContent({ mapName });

  return (
    <IxigoTable
      state={state}
      columns={columns}
      data={data}
      columnVisibility={{
        steamid: false,
      }}
      sorting={[{ id: "hltv_rating", desc: true }]}
      pagination={{ pageIndex: 0, pageSize: 20 }}
      columnPinning={{ left: ["username"] }}
      enableColumnActions={false}
      enableColumnFilters={true}
      enableFilterMatchHighlighting={false}
      enableSorting={true}
      enableTopToolbar={true}
      enableBottomToolbar={true}
      enableGlobalFilter={false}
      enableFullScreenToggle={false}
      enableHiding={true}
      muiTableBodyRowProps={true}
      errorMsg={t(`${STRING_PREFIX}.error-loading-data`) as string}
      refetch={refetch}
    />
  );
};

export default MapLeaderboardsContent;
