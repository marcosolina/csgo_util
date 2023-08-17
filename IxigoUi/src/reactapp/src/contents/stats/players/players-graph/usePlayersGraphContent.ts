import { useMemo, useState } from "react";
import { IUsePlayersGraphContentResult } from "./interfaces";
import { MATCH_TEAM_RESULT_REQUEST, useGetScoreTypes, useGetStats } from "../../../../services";
import { IxigoPossibleValue } from "../../../../common/select";
import { combineQueryStatuses } from "../../../../lib/queries/queriesFunctions";

const BINNIN_LEVELS: IxigoPossibleValue[] = [
  { value: "week", label: "Week" },
  { value: "month", label: "Month" },
];

export const usePlayersGraphContent = (): IUsePlayersGraphContentResult => {
  const [steamId, setSteamId] = useState<string>();
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [binningLevel, setBinningLevel] = useState<string>(BINNIN_LEVELS[0].value);
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
        label: scoreTypes[key],
      });
    });
    arr.sort((a, b) => a.label.localeCompare(b.label));
    return arr;
  }, [scoreTypes]);

  const possibleBinningValues = useMemo(() => {
    const arr: IxigoPossibleValue[] = [];
    arr.push({ value: "week", label: "Week" });
    arr.push({ value: "month", label: "Month" });
    return arr;
  }, []);

  return {
    state: combineQueryStatuses([qScoreTypes, qPlayerStatsRequest]),
    startDate,
    endDate,
    binningLevel,
    graphsSelected,
    possibleScoreTypesValues,
    possibleBinningValues,
    chartData: [],

    setSteamId,
    setStartDate,
    setEndDate,
    setBinningLevel,
    setGraphsSelected,
  };
};
