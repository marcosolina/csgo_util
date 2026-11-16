import { useTranslation } from "react-i18next";
import { IMapLeaderboardRequest, IMapLeaderboardResponse } from "./interfaces";
import { useCallback, useEffect, useMemo, useState } from "react";
import { MRT_Cell, MRT_ColumnDef } from "material-react-table";
import { IPlayerMatch, useGetStats, USERS_REQUEST, PLAYER_MAP_STATS_EXTENDED_EXTENDED } from "../../../../services";
import { combineQueryStatuses } from "../../../../lib/queries/queriesFunctions";
import { QueryStatus } from "../../../../lib/http-requests";
import TableLink from "../../../../common/table-link/TableLink";
import { TFunction } from "i18next";
import { WEAPONG_IMAGE } from "../../weaponImage";
import PieChartMini from "../../../../common/pie-chart-mini/PieChartMini";
import customHeader from "../../../../common/material-table/custom-header/customHeader";
import CellChip from "../../../../common/cell-chip/CellChip";
import { UI_CONTEXT_PATH } from "../../../../lib/constants";

const COL_HEADERS_BASE_TRANSLATION_KEY = "page.stats.leaderboard.column-headers";
const SMALL_COL_SIZE = 5;

const COLUMNS_ORDER = [
  "username",
  "matches",
  "first_weapon",
  "second_weapon",
  "wins",
  "loss",
  "winlossratio",
  "averagewinscore",
  "adr",
  "kpr",
  "dpr",
  "kdr",
  "headshot_percentage",
  "hltv_rating",
  "mvp",
  "kills",
  "deaths",
  "assists",
  "headshots",
  "hr",
  "bp",
  "bd",
  "ud",
  "ffd",
  "td",
  "tda",
  "tdh",
  "fa",
  "ebt",
  "fbt",
  "ek",
  "tk",
  "_1k",
  "_2k",
  "_3k",
  "_4k",
  "_5k",
  "_1v1",
  "_1v2",
  "_1v3",
  "_1v4",
  "_1v5",
];

function createColumnDefinition(
  key: string,
  t: TFunction<"translation", undefined, "translation">,
  userPathUpdater: (steamid: string) => string
): MRT_ColumnDef<IPlayerMatch> {
  const cell: MRT_ColumnDef<IPlayerMatch> = {
    id: key,
    accessorFn: (row) => row[key as keyof IPlayerMatch],
    header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
    size: SMALL_COL_SIZE,
    Header: customHeader<IPlayerMatch>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
  };

  if (key === "username") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayerMatch> }) => {
      const username = cell.getValue() as string;
      const steamid = cell.row.original.steamid;
      return <TableLink text={username} to={userPathUpdater(steamid)} />;
    };
  }

  if (key === "hltv_rating") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayerMatch> }) => {
      const rating = cell.getValue() as number;
      return <CellChip value={rating} type="rating" />;
    };
  }

  if (key === "first_weapon" || key === "second_weapon") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayerMatch> }) => {
      const value = cell.getValue() as string;
      const imageUrl = WEAPONG_IMAGE[value];
      return imageUrl ? <img src={imageUrl} alt={value} style={{ height: "18px" }} /> : null;
    };
  }

  if (key === "headshot_percentage") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayerMatch> }) => {
      const value = cell.getValue() as number;
      return <PieChartMini percentage={value} color="darkturquoise" size={22} />;
    };
  }

  return cell;
}

export const useLeaderboardContent = (request: IMapLeaderboardRequest): IMapLeaderboardResponse => {
  const { t } = useTranslation();
  const [data, setData] = useState<IPlayerMatch[]>([]);

  const getStatsRequest = useMemo(() => {
    const copy = { ...PLAYER_MAP_STATS_EXTENDED_EXTENDED };
    copy.queryParams = { mapname: request.mapName };
    return copy;
  }, [request]);

  const pathUpdater = useCallback((steamid: string) => {
    return `${UI_CONTEXT_PATH}/stats/player/${steamid}`;
  }, []);

  // Get the data
  const qUsersRequest = useGetStats(USERS_REQUEST);
  const qPLayersStatsRequest = useGetStats(getStatsRequest);

  // Merge the queries statuses into one
  const queriesState = combineQueryStatuses([qUsersRequest, qPLayersStatsRequest]);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<IPlayerMatch>[]>(() => {
    const cols: MRT_ColumnDef<IPlayerMatch>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, t, pathUpdater));
    });
    return cols;
  }, [t, pathUpdater]);

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

      const data: IPlayerMatch[] = [];
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
