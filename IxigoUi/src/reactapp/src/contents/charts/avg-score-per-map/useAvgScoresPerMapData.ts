import { useMemo } from "react";
import { combineQueryStatuses } from "../../../lib/queries";
import { useGetCsgoPlayers, useGetScoreTypes } from "../../../services";
import { useGetAvgScoresPerMap, useGetMapsPlayedCount } from "../../../services/charts";
import { IUseAvgScoresPerMapDataRequest, IUseAvgScoresPerMapDataResult } from "./interfaces";
import { IxigoPossibleValue } from "../../../common";

export const useAvgScoresPerMapData = (request: IUseAvgScoresPerMapDataRequest): IUseAvgScoresPerMapDataResult => {
  const qAvgScores = useGetAvgScoresPerMap({
    steamIds: request.steamIds,
    scoreType: request.scoreType,
    maps: request.maps,
    matchesToConsider: request.matchesToConsider,
  });
  const qCsgoPlayers = useGetCsgoPlayers();
  const qScoreTypes = useGetScoreTypes();
  const qMapsPlayed = useGetMapsPlayedCount();

  const status = combineQueryStatuses([qAvgScores, qCsgoPlayers, qScoreTypes, qMapsPlayed]);

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

  const users = qCsgoPlayers.data?.data?.users || [];
  users.sort((a, b) => a.user_name.localeCompare(b.user_name));

  const mapsPlayed = qMapsPlayed.data?.data?.maps || [];
  mapsPlayed.sort((a, b) => a.map_name.localeCompare(b.map_name));

  return {
    status,
    users,
    avgScores: qAvgScores.data?.data,
    scoreTypes: possibleScoreTypesValues,
    mapsPlayed,
  };
};
