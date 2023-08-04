import { useEffect, useMemo, useState } from "react";
import { IPlayerClutchStats, PLAYER_CLUTCH_STATS_REQUEST, useGetStats } from "../../../../../services";
import { IPlayerClutchRequest, IPlayerClutchResponse, IClutchData } from "./interfaces";
import { QueryStatus } from "../../../../../lib/http-requests";
import { TFunction } from "i18next";
import { MRT_Cell, MRT_ColumnDef } from "material-react-table";
import customHeader from "../../../../../common/material-table/custom-header/customHeader";
import PieChartMini from "../../../../../common/pie-chart-mini/PieChartMini";
import { useTranslation } from "react-i18next";

const COL_HEADERS_BASE_TRANSLATION_KEY = "page.stats.player.content.overall.clutch-stats.column-headers";
const SMALL_COL_SIZE = 5;

const COLUMNS_ORDER: string[] = ["1v1", "1v2", "1v3", "1v4", "1v5"];

function createColumnDefinition(
  key: string,
  t: TFunction<"translation", undefined, "translation">
): MRT_ColumnDef<IClutchData> {
  const cell: MRT_ColumnDef<IClutchData> = {
    id: key,
    accessorFn: (row) => row[key as keyof IClutchData],
    header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
    size: SMALL_COL_SIZE,
    Header: customHeader<IClutchData>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
    Cell: ({ cell, row }: { cell: MRT_Cell<IClutchData>; row: { index: number } }) => {
      const percentageString = cell.getValue() as string;
      const percentage = Number(percentageString.slice(0, -1));
      return row.index === 0 ? (
        <PieChartMini percentage={percentage} color="darkturquoise" size={35} />
      ) : (
        percentageString
      );
    },
  };

  return cell;
}

export function usePlayerClutch(request: IPlayerClutchRequest): IPlayerClutchResponse {
  const { t } = useTranslation();
  const getStatsRequest = useMemo(() => {
    const copy = { ...PLAYER_CLUTCH_STATS_REQUEST };
    copy.queryParams = { steamid: request.steamid };
    return copy;
  }, [request]);
  const [data, setData] = useState<IClutchData[]>([]);
  const [playerClutchStats, setPlayerClutchStats] = useState<IPlayerClutchStats>();
  const qGetStats = useGetStats(getStatsRequest);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<IClutchData>[]>(() => {
    const cols: MRT_ColumnDef<IClutchData>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, t));
    });
    return cols;
  }, [t]);

  useEffect(() => {
    if (qGetStats.status === QueryStatus.success) {
      const data = qGetStats.data.data?.view_data;
      if (!data) return;

      const player = data[0];

      const playerData: IClutchData[] = [];
      playerData.push({
        "1v1": `${player._1v1p.toFixed(0)}%`,
        "1v2": `${player._1v2p.toFixed(0)}%`,
        "1v3": `${player._1v3p.toFixed(0)}%`,
        "1v4": `${player._1v4p.toFixed(0)}%`,
        "1v5": `${player._1v5p.toFixed(0)}%`,
      });
      playerData.push({
        "1v1": `${player._1v1p.toFixed(0)}%`,
        "1v2": `${player._1v2p.toFixed(0)}%`,
        "1v3": `${player._1v3p.toFixed(0)}%`,
        "1v4": `${player._1v4p.toFixed(0)}%`,
        "1v5": `${player._1v5p.toFixed(0)}%`,
      });
      playerData.push({
        "1v1": `W:${player._1v1w}`,
        "1v2": `W:${player._1v2w}`,
        "1v3": `W:${player._1v3w}`,
        "1v4": `W:${player._1v4w}`,
        "1v5": `W:${player._1v5w}`,
      });
      playerData.push({
        "1v1": `L:${player._1v1l}`,
        "1v2": `L:${player._1v2l}`,
        "1v3": `L:${player._1v3l}`,
        "1v4": `L:${player._1v4l}`,
        "1v5": `L:${player._1v5l}`,
      });

      setData(playerData);
      setPlayerClutchStats(player);
    }
  }, [qGetStats]);

  return {
    state: qGetStats.status,
    columns,
    data,
    playerClutchStats,
  };
}
