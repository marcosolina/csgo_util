import { useTranslation } from "react-i18next";
import { ILeaderboardContent } from "./interfaces";
import { useCallback, useEffect, useMemo, useState } from "react";
import { MRT_Cell, MRT_ColumnDef } from "material-react-table";
import { IPlayerStats, useGetStats, USERS_REQUEST, PLAYERS_STATS__REQUEST } from "../../../services";
import { combineQueryStatuses } from "../../../lib/queries/queriesFunctions";
import { QueryStatus } from "../../../lib/http-requests";
import TableLink from '../../../common/table-link/TableLink';
import { TFunction } from "i18next";
import { WEAPONG_IMAGE } from "../weaponImage";
import PieChartMini from "../../../common/pie-chart-mini/PieChartMini";
import customHeader from "../../../common/material-table/custom-header/customHeader";
import { useLocation, useNavigate } from "react-router-dom";
import HltvChip from "../../../common/hltv-chip/HltvChip";

const COL_HEADERS_BASE_TRANSLATION_KEY = "page.stats.leaderboard.column-headers";
const SMALL_COL_SIZE = 5;

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
  t: TFunction<"translation", undefined, "translation">,
  userClickHandler: (steamid: string) => void
): MRT_ColumnDef<IPlayerStats> {
  const cell: MRT_ColumnDef<IPlayerStats> = {
    id: key,
    accessorFn: (row) => row[key as keyof IPlayerStats],
    header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
    size: SMALL_COL_SIZE,
    Header: customHeader<IPlayerStats>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
  };

  if (key === "username") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayerStats> }) => {
      const username = cell.getValue() as string;
      const steamid = cell.row.original.steamid;
      return (
        <TableLink text={username} onClickHandler={() => userClickHandler(steamid)} />
      );
    };
  }

  if (key === "hltv_rating") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayerStats> }) => {
      const rating = cell.getValue() as number;
      return <HltvChip rating={rating} />;
    };
  }

  if (key === "first_weapon" || key === "second_weapon") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayerStats> }) => {
      const value = cell.getValue() as string;
      const imageUrl = WEAPONG_IMAGE[value];
      return imageUrl ? <img src={imageUrl} alt={value} style={{ height: "18px" }} /> : null;
    };
  }

  if (key === "headshot_percentage" || key === "_1vnp") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayerStats> }) => {
      const value = cell.getValue() as number;
      return <PieChartMini percentage={value} color="darkturquoise" size={22} />;
    };
  }

  return cell;
}

export const useLeaderboardContent = (): ILeaderboardContent => {
  const { t } = useTranslation();
  const history = useNavigate();
  const location = useLocation();
  const [data, setData] = useState<IPlayerStats[]>([]);

  const userNameClickHandler = useCallback(
    (steamid: string) => {
      const newPath = `${location.pathname}/player/${steamid}`;
      history(newPath);
    },
    [history, location.pathname]
  );

  // Get the data
  const qUsersRequest = useGetStats(USERS_REQUEST);
  const qPLayersStatsRequest = useGetStats(PLAYERS_STATS__REQUEST);

  // Merge the queries statuses into one
  const queriesState = combineQueryStatuses([qUsersRequest, qPLayersStatsRequest]);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<IPlayerStats>[]>(() => {
    const cols: MRT_ColumnDef<IPlayerStats>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, t, userNameClickHandler));
    });
    return cols;
  }, [t, userNameClickHandler]);

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
