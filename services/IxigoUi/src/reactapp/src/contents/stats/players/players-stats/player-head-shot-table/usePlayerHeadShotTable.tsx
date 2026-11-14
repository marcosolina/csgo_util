import { useEffect, useMemo, useState } from "react";
import { IPlayerOverallStats, PLAYER_OVERALL_STATS_REQUEST, useGetStats } from "../../../../../services/stats";
import { IHeadShotData, IPlayerHeadShotTableRequest, IPlayerHeadShotTableResponse } from "./interfaces";
import { QueryStatus } from "../../../../../lib/http-requests";
import { TFunction } from "i18next";
import { MRT_ColumnDef } from "material-react-table";
import customHeader from "../../../../../common/material-table/custom-header/customHeader";
import { useTranslation } from "react-i18next";
const COL_HEADERS_BASE_TRANSLATION_KEY = "page.stats.player.content.overall.head-shots.column-headers";
const SMALL_COL_SIZE = 5;

const COLUMNS_ORDER: string[] = ["desc", "value"];

function createColumnDefinition(
  key: string,
  t: TFunction<"translation", undefined, "translation">
): MRT_ColumnDef<IHeadShotData> {
  const cell: MRT_ColumnDef<IHeadShotData> = {
    id: key,
    accessorFn: (row) => row[key as keyof IHeadShotData],
    header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
    size: SMALL_COL_SIZE,
    Header: customHeader<IHeadShotData>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
  };

  return cell;
}

export function usePlayerHeadShotTable(request: IPlayerHeadShotTableRequest): IPlayerHeadShotTableResponse {
  const { t } = useTranslation();
  const getStatsRequest = useMemo(() => {
    const copy = { ...PLAYER_OVERALL_STATS_REQUEST };
    copy.queryParams = { steamid: request.steamid };
    return copy;
  }, [request]);
  const [data, setData] = useState<IHeadShotData[]>([]);
  const [playerOverall, setPlayerOverall] = useState<IPlayerOverallStats>();
  const qUsersRequest = useGetStats(getStatsRequest);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<IHeadShotData>[]>(() => {
    const cols: MRT_ColumnDef<IHeadShotData>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, t));
    });
    return cols;
  }, [t]);

  useEffect(() => {
    if (qUsersRequest.status === QueryStatus.success && qUsersRequest.data?.data?.view_data.length) {
      const userData = qUsersRequest.data.data.view_data[0];
      if (!userData) return;

      const playerData: IHeadShotData[] = [];
      playerData.push({
        desc: `${t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.row1.label`)}`,
        value: `${userData.kills.toFixed(0)}`,
      });
      playerData.push({
        desc: `${t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.row2.label`)}`,
        value: `${userData.deaths.toFixed(0)}`,
      });
      playerData.push({
        desc: `${t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.row3.label`)}`,
        value: `${userData.assists.toFixed(0)}`,
      });
      playerData.push({
        desc: `${t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.row4.label`)}`,
        value: `${userData.headshots.toFixed(0)}`,
      });
      setData(playerData);
      setPlayerOverall(userData);
    }
  }, [qUsersRequest.status, qUsersRequest.data, request.steamid, t]);

  return {
    state: qUsersRequest.status,
    data,
    columns,
    playerOverall,
  };
}
