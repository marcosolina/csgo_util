import { useEffect, useMemo, useState } from "react";
import { IPlayerEntryKillStats, PLAYER_ENTRY_KILL_STATS_REQUEST, useGetStats } from "../../../../../services";
import { IEntryKillData, IPlayerEntryKillRequest, IPlayerEntryKillResponse } from "./interfaces";
import { useTranslation } from "react-i18next";
import { TFunction } from "i18next";
import { MRT_Cell, MRT_ColumnDef } from "material-react-table";
import customHeader from "../../../../../common/material-table/custom-header/customHeader";
import PieChartMini from "../../../../../common/pie-chart-mini/PieChartMini";
import { QueryStatus } from "../../../../../lib/http-requests";
import { Box, Typography } from "@mui/material";

const BASE_TRANSLATION_KEY = "page.stats.player.content.overall.entry-kill-stats";
const COL_HEADERS_BASE_TRANSLATION_KEY = `${BASE_TRANSLATION_KEY}.column-headers`;
const SMALL_COL_SIZE = 5;

const COLUMNS_ORDER: string[] = ["desc", "all", "ts", "ct"];

function createColumnDefinition(
  key: string,
  t: TFunction<"translation", undefined, "translation">
): MRT_ColumnDef<IEntryKillData> {
  const cell: MRT_ColumnDef<IEntryKillData> = {
    id: key,
    accessorFn: (row) => row[key as keyof IEntryKillData],
    header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
    size: SMALL_COL_SIZE,
    Header: customHeader<IEntryKillData>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
    Cell: ({ cell, row }: { cell: MRT_Cell<IEntryKillData>; row: { index: number } }) => {
      const percentageString = cell.getValue() as string;
      const percentage = Number(percentageString.slice(0, -1));
      return row.index < 2 ? (
        <Box display="flex" flexDirection="column" width="100%" alignItems="center">
          <PieChartMini percentage={percentage} color="darkturquoise" size={40} />
          <Typography component="span" align="center">
            {percentageString}
          </Typography>
        </Box>
      ) : (
        percentageString
      );
    },
  };

  if (key === "desc") {
    cell.Cell = undefined;
  }

  return cell;
}

export function usePlayerEntryKill(request: IPlayerEntryKillRequest): IPlayerEntryKillResponse {
  const { t } = useTranslation();
  const getStatsRequest = useMemo(() => {
    const copy = { ...PLAYER_ENTRY_KILL_STATS_REQUEST };
    copy.queryParams = { steamid: request.steamid };
    return copy;
  }, [request]);

  const [data, setData] = useState<IEntryKillData[]>([]);
  const [entryKillStats, setEntryKillStats] = useState<IPlayerEntryKillStats>();

  const qGetStats = useGetStats(getStatsRequest);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<IEntryKillData>[]>(() => {
    const cols: MRT_ColumnDef<IEntryKillData>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, t));
    });
    return cols;
  }, [t]);

  // Setting the table data
  useEffect(() => {
    if (qGetStats.status === QueryStatus.success) {
      const data = qGetStats.data.data?.view_data;
      if (!data || data.length < 1) return;

      const entryKillStats = data[0];

      const playerData: IEntryKillData[] = [];
      playerData.push({
        desc: t(`${BASE_TRANSLATION_KEY}.lblSuccess`),
        all: `${entryKillStats.ek_success_rate ? entryKillStats.ek_success_rate.toFixed(0) : 0}%`,
        ts: `${entryKillStats.ekt_success_rate ? entryKillStats.ekt_success_rate.toFixed(0) : 0}%`,
        ct: `${entryKillStats.ekct_success_rate ? entryKillStats.ekct_success_rate.toFixed(0) : 0}%`,
      });

      playerData.push({
        desc: t(`${BASE_TRANSLATION_KEY}.lblAttempts`),
        all: `${
          entryKillStats.ek_success_rate_overall
            ? ((100 * entryKillStats.ek_success_rate_overall) / entryKillStats.ek_success_rate).toFixed(0)
            : 0
        }%`,
        ts: `${
          entryKillStats.ekt_success_rate_overall
            ? ((100 * entryKillStats.ekt_success_rate_overall) / entryKillStats.ekt_success_rate).toFixed(0)
            : 0
        }%`,
        ct: `${
          entryKillStats.ekct_success_rate_overall
            ? ((100 * entryKillStats.ekct_success_rate_overall) / entryKillStats.ekct_success_rate).toFixed(0)
            : 0
        }%`,
      });

      setData(playerData);
      setEntryKillStats(entryKillStats);
    }
  }, [qGetStats, t]);

  return {
    state: qGetStats.status,
    data,
    columns,
    entryKillStats,
  };
}
