import { useEffect, useMemo, useState } from "react";
import { IPlayerOverallStats, PLAYER_OVERALL_STATS_REQUEST, useGetStats } from "../../../../../services/stats";
import { IDamageData, IPlayerDamageTableRequest, IPlayerDamageTableResponse } from "./interfaces";
import { QueryStatus } from "../../../../../lib/http-requests";
import { TFunction } from "i18next";
import { MRT_ColumnDef } from "material-react-table";
import customHeader from "../../../../../common/material-table/custom-header/customHeader";
import { useTranslation } from "react-i18next";

const COL_HEADERS_BASE_TRANSLATION_KEY = "page.stats.player.content.overall.damage.column-headers";
const SMALL_COL_SIZE = 5;

const COLUMNS_ORDER: string[] = ["desc", "value"];

function createColumnDefinition(
  key: string,
  t: TFunction<"translation", undefined, "translation">
): MRT_ColumnDef<IDamageData> {
  const cell: MRT_ColumnDef<IDamageData> = {
    id: key,
    accessorFn: (row) => row[key as keyof IDamageData],
    header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
    size: SMALL_COL_SIZE,
    Header: customHeader<IDamageData>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
  };

  return cell;
}

export function usePlayerDamageTable(request: IPlayerDamageTableRequest): IPlayerDamageTableResponse {
  const { t } = useTranslation();
  const [data, setData] = useState<IDamageData[]>([]);
  const [playerOverall, setPlayerOverall] = useState<IPlayerOverallStats>();
  const [maxAdr, setMaxAdr] = useState<number>(0);
  const qUsersRequest = useGetStats(PLAYER_OVERALL_STATS_REQUEST);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<IDamageData>[]>(() => {
    const cols: MRT_ColumnDef<IDamageData>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, t));
    });
    return cols;
  }, [t]);

  useEffect(() => {
    if (qUsersRequest.status === QueryStatus.success && qUsersRequest.data?.data?.view_data.length) {
      const userData = qUsersRequest.data.data.view_data.find((p) => p.steamid === request.steamid);
      if (!userData) return;

      const playerData: IDamageData[] = [];
      playerData.push({
        desc: `${t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.row1.label`)}`,
        value: `${userData.tdh.toFixed(0)}`,
      });
      playerData.push({
        desc: `${t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.row2.label`)}`,
        value: `${userData.tda.toFixed(0)}`,
      });
      playerData.push({
        desc: `${t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.row3.label`)}`,
        value: `${userData.ffd.toFixed(0)}`,
      });
      playerData.push({
        desc: `${t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.row4.label`)}`,
        value: `${userData.rounds.toFixed(0)}`,
      });
      setData(playerData);
      setPlayerOverall(userData);
      setMaxAdr(Math.max(...qUsersRequest.data.data.view_data.map((p) => p.adr)));
    }
  }, [qUsersRequest.status, qUsersRequest.data, request.steamid, t]);

  return {
    state: qUsersRequest.status,
    data,
    columns,
    maxAdr,
    playerOverall,
  };
}
