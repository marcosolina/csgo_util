import { combineQueryStatuses } from "../../lib/queries";
import { useGetCsgoPlayers, useGetScoreTypes } from "../../services";
import { IPlayersContent } from "./interfaces";

export const usePlayersContent = (): IPlayersContent => {
  const qScoreTypes = useGetScoreTypes();
  const qCsgoPlayers = useGetCsgoPlayers();

  return {
    state: combineQueryStatuses([qScoreTypes.status, qCsgoPlayers.status]),
    scoreTypes: qScoreTypes.data?.data?.types,
    csgoPlayers: qCsgoPlayers.data?.data?.users,
  };
};
