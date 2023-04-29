import { useMemo } from "react";
import { combineQueryStatuses } from "../../../lib/queries";
import { useGetScoreTypes } from "../../../services";
import {
  IGetAvgTeamsScoresPerMapRequest,
  useGetMapsPlayedCount,
  useGetTeamssAvgScoresPerMap,
} from "../../../services/charts";
import { IUseTeamsAvgScoresPerMapDataResult } from "./interfaces";
import { IxigoPossibleValue } from "../../../common";

export const useTeamsAvgScoresPerMapData = (
  request: IGetAvgTeamsScoresPerMapRequest
): IUseTeamsAvgScoresPerMapDataResult => {
  const qAvgScores = useGetTeamssAvgScoresPerMap(request);
  const qScoreTypes = useGetScoreTypes();
  const qMapsPlayed = useGetMapsPlayedCount();

  const status = combineQueryStatuses([qAvgScores, qScoreTypes, qMapsPlayed]);

  const scoreTypes = qScoreTypes.data?.data?.types;
  const possibleScoreTypesValues = useMemo(() => {
    const arr: IxigoPossibleValue[] = [];
    if (!scoreTypes) {
      return arr;
    }
    Object.keys(scoreTypes).forEach(function (key) {
      arr.push({
        value: key,
        label: scoreTypes[key],
      });
    });
    arr.sort((a, b) => a.label.localeCompare(b.label));
    return arr;
  }, [scoreTypes]);

  const mapsPlayed = qMapsPlayed.data?.data?.maps || [];
  mapsPlayed.sort((a, b) => a.map_name.localeCompare(b.map_name));

  return {
    status,
    avgScores: qAvgScores.data?.data,
    scoreTypes: possibleScoreTypesValues,
    mapsPlayed,
  };
};
