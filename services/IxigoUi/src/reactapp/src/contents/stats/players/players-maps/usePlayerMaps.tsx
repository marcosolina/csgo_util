import { useCallback, useEffect, useMemo, useState } from "react";
import { IPlayersMapsContentRequest, IPlayersMapsContentResponse } from "./interfaces";
import {
  IPlayerMatch,
  PLAYER_MAP_STATS_EXTENDED_EXTENDED,
  USERS_REQUEST,
  useGetStats,
} from "../../../../services/stats";
import { TFunction } from "i18next";
import { MRT_Cell, MRT_ColumnDef } from "material-react-table";
import customHeader from "../../../../common/material-table/custom-header/customHeader";
import TableLink from "../../../../common/table-link/TableLink";
import CellChip from "../../../../common/cell-chip/CellChip";
import { WEAPONG_IMAGE } from "../../weaponImage";
import PieChartMini from "../../../../common/pie-chart-mini/PieChartMini";
import { useTranslation } from "react-i18next";
import { QueryStatus } from "../../../../lib/http-requests";
import { combineQueryStatuses } from "../../../../lib/queries";
import { UI_CONTEXT_PATH } from "../../../../lib/constants";

const COL_HEADERS_BASE_TRANSLATION_KEY = "page.stats.player.content.maps.column-headers";
const SMALL_COL_SIZE = 5;

const COLUMNS_ORDER: string[] = [
  "mapname",
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
  mapPathUpdater: (mapName: string) => string
): MRT_ColumnDef<IPlayerMatch> {
  const cell: MRT_ColumnDef<IPlayerMatch> = {
    id: key,
    accessorFn: (row) => row[key as keyof IPlayerMatch],
    header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
    size: SMALL_COL_SIZE,
    Header: customHeader<IPlayerMatch>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
  };

  if (key === "mapname") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayerMatch> }) => {
      const mapname = cell.getValue() as string;
      return <TableLink text={mapname} to={mapPathUpdater(mapname)} />;
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

export function usePlayerMaps(request: IPlayersMapsContentRequest): IPlayersMapsContentResponse {
  const { t } = useTranslation();
  const getStatsRequest = useMemo(() => {
    const copy = { ...PLAYER_MAP_STATS_EXTENDED_EXTENDED };
    copy.queryParams = { steamid: request.steamId };
    copy.enabled = !!request.steamId;
    return copy;
  }, [request]);

  const [data, setData] = useState<IPlayerMatch[]>([]);

  const mapPathUpdater = useCallback((mapName: string) => {
    return `${UI_CONTEXT_PATH}/stats/map/${mapName}`;
  }, []);

  const qUsersRequest = useGetStats(USERS_REQUEST);
  const qGetStats = useGetStats(getStatsRequest);

  const refetch = useCallback(() => {
    qUsersRequest.refetch();
    qGetStats.refetch();
  }, [qUsersRequest, qGetStats]);

  // Merge the queries statuses into one
  const queriesState = combineQueryStatuses([qUsersRequest, qGetStats]);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<IPlayerMatch>[]>(() => {
    const cols: MRT_ColumnDef<IPlayerMatch>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, t, mapPathUpdater));
    });
    return cols;
  }, [t, mapPathUpdater]);

  // Create the data
  useEffect(() => {
    if (queriesState === QueryStatus.success) {
      const users = qUsersRequest.data?.data?.view_data;
      const playerStats = qGetStats.data?.data?.view_data;
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
  }, [queriesState, qUsersRequest.data?.data, qGetStats.data?.data]);

  return {
    state: queriesState,
    columns,
    data,
    refetch,
  };
}
