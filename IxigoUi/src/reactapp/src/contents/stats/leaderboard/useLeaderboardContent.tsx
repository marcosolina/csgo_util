import { useTranslation } from "react-i18next";
import { ILeaderboardContent, IPlayerStats, ISteamUser } from "./interfaces";
import { useCallback, useEffect, useMemo, useState } from "react";
import { MRT_ColumnDef } from "material-react-table";
import { IGetStatsRequest, useGetStats } from "../../../services";
import { combineQueryStatuses } from "../../../lib/queries/queriesFunctions";
import { QueryStatus } from "../../../lib/http-requests";
import { Tooltip } from "@mui/material";

const COL_HEADERS_BASE_TRANSLATION_KEY = "page.stats.leaderboard.column-headers";
const SMALL_COL_SIZE = 5;

const USERS_REQUEST: IGetStatsRequest<ISteamUser> = {
  viewName: "users",
};

const PLAYERS_STATS__REQUEST: IGetStatsRequest<IPlayerStats> = {
  viewName: "PLAYER_OVERALL_STATS_EXTENDED_EXTENDED_CACHE",
};

const COLUMNS_ORDER: string[] = [
  "username",
  "matches",
  "hltv_rating",
  "first_weapon",
  "second_weapon",
  "averagewinscore",
  "winlossratio",
  "kills",
  "rws",
  "kast",
  "headshot_percentage",
  "_1vnp",
  "ebt",
  "mvp",
  "assists",
  "deaths",
  "kdr",
  "adr",
  "kpr",
  "dpr",
  "headshots",
  "rounds",
  "wins",
  "loss",
  "ff",
  "ek",
  "bp",
  "bd",
  "hr",
  "_5k",
  "_4k",
  "_3k",
  "_2k",
  "_1k",
  "tk",
  "td",
  "tdh",
  "tda",
  "ffd",
  "fbt",
  "ud",
  "fkr",
  "fa",
  "_1v1",
  "_1v2",
  "_1v3",
  "_1v4",
  "_1v5",
];

function createCustomHeader(tooltipText: string) {
  return ({ column }: { column: any }) => (
    <Tooltip title={tooltipText}>
      <span>{column.columnDef.header}</span>
    </Tooltip>
  );
}

export const useLeaderboardContent = (): ILeaderboardContent => {
  const { t } = useTranslation();
  const [data, setData] = useState<IPlayerStats[]>([]);

  // Get the data
  const qUsersRequest = useGetStats(USERS_REQUEST);
  const qPLayersStatsRequest = useGetStats(PLAYERS_STATS__REQUEST);

  // Merge the queries statuses into one
  const queriesState = combineQueryStatuses([qUsersRequest, qPLayersStatsRequest]);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<IPlayerStats>[]>(() => {
    const cols: MRT_ColumnDef<IPlayerStats>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push({
        id: key,
        accessorFn: (row) => {
          if (!row.hasOwnProperty("hltv_rating")) {
            console.log(row);
          }
          const value = row[key as keyof IPlayerStats];
          return value;
        },
        header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
        size: SMALL_COL_SIZE,
        Header: createCustomHeader(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
      });
    });

    return cols;
  }, [t]);

  const refetch = useCallback(() => {
    qUsersRequest.refetch();
    qPLayersStatsRequest.refetch();
  }, [qUsersRequest, qPLayersStatsRequest]);

  // Create the data
  useEffect(() => {
    if (queriesState === QueryStatus.success) {
      const users = qUsersRequest.data?.data?.view_data;
      const playerStats = qPLayersStatsRequest.data?.data?.view_data;
      if (!users || !playerStats) {
        return;
      }

      const data: IPlayerStats[] = [];
      playerStats.forEach((playerStat) => {
        const user = users.find((user) => user.steam_id === playerStat.steamid);
        if (!user) {
          return;
        }

        data.push({
          ...playerStat,
          username: user.user_name,
        });
      });

      setData(data);
    }
  }, [queriesState, qUsersRequest.data, qPLayersStatsRequest.data]);

  return {
    columns,
    state: queriesState,
    data,
    refetch,
  };
};
