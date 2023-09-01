import { useEffect, useMemo, useState } from "react";
import { IPlayerOverallStats, PLAYER_OVERALL_STATS_REQUEST, useGetStats } from "../../../../../services/stats";
import { IPlayerRadarChartData, IPlayerRadarChartRequest, IPlayerRadarChartResponse } from "./interfaces";
import * as ss from "simple-statistics";
import { QueryStatus } from "../../../../../lib/http-requests";

export function usePlayerRadarChart(request: IPlayerRadarChartRequest): IPlayerRadarChartResponse {
  const [playerStats, setPlayerStats] = useState<IPlayerOverallStats>();
  const [playersStats, setPlayersStats] = useState<IPlayerOverallStats[]>();
  const qUsersRequest = useGetStats(PLAYER_OVERALL_STATS_REQUEST);

  useEffect(() => {
    if (qUsersRequest.status === QueryStatus.success && qUsersRequest.data?.data?.view_data.length) {
      setPlayerStats(qUsersRequest.data.data.view_data.find((player) => player.steamid === request.steamid));
      setPlayersStats(qUsersRequest.data.data.view_data);
    }
  }, [qUsersRequest.status, qUsersRequest.data, request.steamid]);

  // Calculate the chart data
  const chartData = useMemo((): IPlayerRadarChartData | undefined => {
    if (!playerStats || !playersStats) return undefined;

    // Calculate the mean and standard deviation of the metrics
    const kprMean = ss.mean(playersStats.map((p) => p.kpr));
    const hltv_ratingMean = ss.mean(playersStats.map((p) => p.hltv_rating));
    const adrMean = ss.mean(playersStats.map((p) => p.adr));
    const kastMean = ss.mean(playersStats.map((p) => p.kast));
    const dprMean = ss.mean(playersStats.map((p) => p.dpr));
    const hspMean = ss.mean(playersStats.map((p) => p.headshot_percentage));
    const udMean = ss.mean(playersStats.map((p) => p.ud));
    const ebtMean = ss.mean(playersStats.map((p) => p.ebt));

    const kprStdDev = ss.standardDeviation(playersStats.map((p) => p.kpr));
    const hltv_ratingStdDev = ss.standardDeviation(playersStats.map((p) => p.hltv_rating));
    const adrStdDev = ss.standardDeviation(playersStats.map((p) => p.adr));
    const kastStdDev = ss.standardDeviation(playersStats.map((p) => p.kast));
    const dprStdDev = ss.standardDeviation(playersStats.map((p) => p.dpr));
    const hspStdDev = ss.standardDeviation(playersStats.map((p) => p.headshot_percentage));
    const udStdDev = ss.standardDeviation(playersStats.map((p) => p.ud));
    const ebtStdDev = ss.standardDeviation(playersStats.map((p) => p.ebt));

    // Calculate z-scores for the specific player
    const kprZScore = (playerStats.kpr - kprMean) / kprStdDev;
    const hltv_ratingZScore = (playerStats.hltv_rating - hltv_ratingMean) / hltv_ratingStdDev;
    const adrZScore = (playerStats.adr - adrMean) / adrStdDev;
    const kastZScore = (playerStats.kast - kastMean) / kastStdDev;
    const dprZScore = (dprMean - playerStats.dpr) / dprStdDev;
    const hspZScore = (playerStats.headshot_percentage - hspMean) / hspStdDev;
    const udZScore = (playerStats.ud - udMean) / udStdDev;
    const ebtZScore = (playerStats.ebt - ebtMean) / ebtStdDev;

    return {
      kprZScore,
      hltv_ratingZScore,
      adrZScore,
      kastZScore,
      dprZScore,
      hspZScore,
      udZScore,
      ebtZScore,
    };
  }, [playerStats, playersStats]);

  const originalData = useMemo((): IPlayerRadarChartData | undefined => {
    if (!playerStats) return undefined;
    return {
      kprZScore: playerStats.kpr,
      hltv_ratingZScore: playerStats.hltv_rating,
      adrZScore: playerStats.adr,
      kastZScore: playerStats.kast,
      dprZScore: playerStats.dpr,
      hspZScore: playerStats.headshot_percentage,
      udZScore: playerStats.ud,
      ebtZScore: playerStats.ebt,
    };
  }, [playerStats]);

  return {
    state: qUsersRequest.status,
    chartData,
    originalData,
  };
}
