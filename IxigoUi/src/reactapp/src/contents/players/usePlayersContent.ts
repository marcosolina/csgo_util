import { combineQueryStatuses } from "../../lib/queries";
import { useGetScoreTypes } from "../../services";
import { IPlayersContent } from "./interfaces";

export const usePlayersContent = (): IPlayersContent => {
  const qScoreTypes = useGetScoreTypes();

  return {
    state: combineQueryStatuses([qScoreTypes.status]),
    scoreTypes: qScoreTypes.data?.data?.types,
  };
};
