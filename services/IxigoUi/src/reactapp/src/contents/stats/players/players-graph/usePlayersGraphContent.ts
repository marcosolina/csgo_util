import { useMemo, useState } from "react";
import { BinningLevel, IChartData, IUsePlayersGraphContentResult } from "./interfaces";
import { ITeamMatchResults, MATCH_TEAM_RESULT_REQUEST, useGetScoreTypes, useGetStats } from "../../../../services";
import { IxigoPossibleValue } from "../../../../common/select";
import { combineQueryStatuses } from "../../../../lib/queries/queriesFunctions";
import { QueryStatus } from "../../../../lib/http-requests";
import { startOfMonth, startOfWeek, format } from "date-fns";
import { useTranslation } from "react-i18next";

const SCORE_TYPE_TRANSLATION_PREFIX_PATH = "scoreType";
const TRANSLATION_PREFIX = "page.stats.player.content.graphs";

export const usePlayersGraphContent = (): IUsePlayersGraphContentResult => {
  const { t } = useTranslation();
  const [steamId, setSteamId] = useState<string>();
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [binningLevel, setBinningLevel] = useState<string>(BinningLevel.week);
  const [graphsSelected, setGraphsSelected] = useState<string[]>([
    "hltv_rating",
    "kills",
    "headshot_percentage",
    "adr",
  ]);

  const getPlayerStatsRequest = useMemo(() => {
    const copy = { ...MATCH_TEAM_RESULT_REQUEST };
    copy.queryParams = { steamid: steamId };
    copy.enabled = !!steamId;
    return copy;
  }, [steamId]);

  const qPlayerStatsRequest = useGetStats(getPlayerStatsRequest);
  const qScoreTypes = useGetScoreTypes();

  const scoreTypes = qScoreTypes.data?.data?.types;
  const possibleScoreTypesValues = useMemo(() => {
    const arr: IxigoPossibleValue[] = [];
    if (!scoreTypes) {
      return arr;
    }
    Object.keys(scoreTypes).forEach(function (key, index) {
      arr.push({
        value: key,
        label: t(`${SCORE_TYPE_TRANSLATION_PREFIX_PATH}.${key}`),
      });
    });
    arr.sort((a, b) => a.label.localeCompare(b.label));
    return arr;
  }, [scoreTypes, t]);

  const possibleBinningValues = useMemo(() => {
    const arr: IxigoPossibleValue[] = [];
    arr.push({ value: BinningLevel.week, label: t(`${TRANSLATION_PREFIX}.lblBinningLevelWeek`) as string });
    arr.push({ value: BinningLevel.month, label: t(`${TRANSLATION_PREFIX}.lblBinningLevelMonth`) as string });
    return arr;
  }, [t]);

  const chartsData = useMemo(() => {
    const chartsData: IChartData[] = [];
    if (qPlayerStatsRequest.status === QueryStatus.success && qPlayerStatsRequest.data?.data?.view_data) {
      const stats = qPlayerStatsRequest.data.data.view_data;

      // Sort the data by date
      stats.sort((a, b) => new Date(a.match_date).getTime() - new Date(b.match_date).getTime());

      // Bin the data by week or month
      const binnedData = new Map<string, ITeamMatchResults[]>();
      stats.forEach((item: ITeamMatchResults) => {
        const dateFn = binningLevel === BinningLevel.week ? startOfWeek : startOfMonth; // Choose the correct date function
        const binnedDate = format(dateFn(new Date(item.match_date)), "yyyy-MM-dd");
        if (!binnedData.has(binnedDate)) {
          binnedData.set(binnedDate, []);
        }
        binnedData.get(binnedDate)?.push(item);
      });

      let filteredData: ITeamMatchResults[] = stats;
      let dates = Array.from(binnedData.keys());

      if (startDate) {
        const filteredDates = dates.filter((date) => new Date(date) >= startDate);
        const filteredFieldValues = filteredData.filter((_, i) => new Date(dates[i]) >= startDate);
        dates = filteredDates;
        filteredData = filteredFieldValues;
      }

      // If endDate is defined, filter out any dates after it
      if (endDate) {
        const filteredDates = dates.filter((date) => new Date(date) <= endDate);
        const filteredFieldValues = filteredData.filter((_, i) => new Date(dates[i]) <= endDate);
        dates = filteredDates;
        filteredData = filteredFieldValues;
      }

      graphsSelected.forEach((graph) => {
        const fieldValues = Array.from(binnedData.values()).map((items) => {
          const total = items.reduce((sum: number, item: any) => sum + item[graph], 0);
          return total / items.length;
        });

        // Calculate the trendline
        const xMean = dates.reduce((sum, _, i) => sum + i, 0) / dates.length;
        const yMean = fieldValues.reduce((sum, y) => sum + y, 0) / fieldValues.length;

        let numerator = 0;
        let denominator = 0;
        for (let i = 0; i < dates.length; i++) {
          numerator += (i - xMean) * (fieldValues[i] - yMean);
          denominator += (i - xMean) ** 2;
        }

        const slope = numerator / denominator;
        const yIntercept = yMean - slope * xMean;

        // Generate points for the trendline
        const trendline = dates.map((_, i) => slope * i + yIntercept);

        const chartData: IChartData = {
          labels: dates,
          datasets: [
            // This is your original data
            {
              label: possibleScoreTypesValues.find((x) => x.value === graph)?.label || "",
              data: fieldValues,
              fill: false,
              borderColor: "rgb(75, 192, 192)",
              tension: 0.1,
            },
            // This is your trendline
            {
              label: t(`${TRANSLATION_PREFIX}.lblTrendline`) as string,
              data: trendline,
              fill: false,
              pointRadius: 0,
              borderColor: "rgba(255, 255, 255, 0.5)",
              borderDash: [5, 5],
              tension: 0.1,
            },
          ],
        };
        chartsData.push(chartData);
      });
    }
    return chartsData;
  }, [
    qPlayerStatsRequest.status,
    binningLevel,
    startDate,
    endDate,
    graphsSelected,
    possibleScoreTypesValues,
    qPlayerStatsRequest.data?.data?.view_data,
    t,
  ]);

  return {
    state: combineQueryStatuses([qScoreTypes, qPlayerStatsRequest]),
    startDate,
    endDate,
    binningLevel,
    graphsSelected,
    possibleScoreTypesValues,
    possibleBinningValues,
    chartsData,

    setSteamId,
    setStartDate,
    setEndDate,
    setBinningLevel,
    setGraphsSelected,
  };
};
