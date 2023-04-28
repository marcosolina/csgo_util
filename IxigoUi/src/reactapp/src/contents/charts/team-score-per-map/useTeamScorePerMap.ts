import { combineQueryStatuses } from "../../../lib/queries/queriesFunctions";
import { ITeamScorePerMapRequest, useGetMapsPlayedCount, useGetTeamScorePerMap } from "../../../services/charts";
import { useGetCsgoPlayers } from "../../../services/dem-manager";
import { IUseTeamScorePerMapResult } from "./interfaces";

export const useTeamScorePerMap = (request: ITeamScorePerMapRequest): IUseTeamScorePerMapResult => {
  const qTeamScore = useGetTeamScorePerMap(request);
  const qMapsPlayed = useGetMapsPlayedCount();
  const qCsgoPlayers = useGetCsgoPlayers();

  const status = combineQueryStatuses([qTeamScore, qMapsPlayed, qCsgoPlayers]);

  const users = qCsgoPlayers.data?.data?.users || [];
  const mapsPlayed = qMapsPlayed.data?.data?.maps || [];
  mapsPlayed.sort((a, b) => a.map_name.localeCompare(b.map_name));

  return {
    status,
    mapsPlayed,
    matches: qTeamScore.data?.data?.matches || {},
    users,
  };
};
