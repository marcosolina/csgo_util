import { useTranslation } from "react-i18next";
import { ILeaderboardContent, IPlayerStats, ISteamUser } from "./interfaces";
import { useCallback, useEffect, useMemo, useState } from "react";
import { MRT_ColumnDef } from "material-react-table";
import { IGetStatsRequest, useGetStats } from "../../../services";
import { combineQueryStatuses } from "../../../lib/queries/queriesFunctions";
import { QueryStatus } from "../../../lib/http-requests";
import { Chip } from "@mui/material";
import { TFunction } from "i18next";
import { WEAPONG_IMAGE } from "../weaponImage";
import PieChartMini from "../../../common/pie-chart-mini/PieChartMini";
import customHeader from "../../../common/material-table/custom-header/customHeader";

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

function createColumnDefinition(
  key: string,
  t: TFunction<"translation", undefined, "translation">
): MRT_ColumnDef<IPlayerStats> {
  const cell: MRT_ColumnDef<IPlayerStats> = {
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
    Header: customHeader<IPlayerStats>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
  };

  if (key === "hltv_rating") {
    cell.Cell = ({ cell }: { cell: any }) => {
      const rating = cell.getValue() as number;
      const decimals = 2;

      if (rating >= 1.5) {
        return <Chip label={rating.toFixed(decimals)} color="info" />;
      }

      if (rating >= 0.85 && rating < 1.1) {
        return <Chip label={rating.toFixed(decimals)} color="warning" />;
      }
      if (rating >= 1.1 && rating < 1.5) {
        return <Chip label={rating.toFixed(decimals)} color="success" />;
      }

      return <Chip label={rating.toFixed(decimals)} color="error" />;
    };
  }

  if (key === "first_weapon" || key === "second_weapon") {
    cell.Cell = ({ cell }: { cell: any }) => {
      const value = cell.getValue() as string;
      const imageUrl = WEAPONG_IMAGE[value];
      return imageUrl ? <img src={imageUrl} alt={value} style={{ height: "18px" }} /> : null;
    };
  }

  if (key === "headshot_percentage" || key === "_1vnp") {
    cell.Cell = ({ cell }: { cell: any }) => {
      const value = cell.getValue() as number;
      return <PieChartMini percentage={value} color="darkturquoise" size={22} />;
    };
  }

  return cell;
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
      cols.push(createColumnDefinition(key, t));
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
