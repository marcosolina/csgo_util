import { useMemo } from "react";
import { combineQueryStatuses } from "../../lib/queries";
import { ICsgoUser, useGetCsgoPlayers, useGetScoreTypes } from "../../services";
import { IPlayersContent } from "./interfaces";

export const usePlayersContent = (): IPlayersContent => {
  const qScoreTypes = useGetScoreTypes();
  const qCsgoPlayers = useGetCsgoPlayers();

  const ixigoUsers = useMemo((): ICsgoUser[] => {
    if (!qCsgoPlayers.data?.data?.users) {
      return [];
    }

    const arr = [...qCsgoPlayers.data.data.users];
    return arr.sort((o1, o2) => o1.user_name.toLocaleLowerCase().localeCompare(o2.user_name.toLocaleLowerCase()));
  }, [qCsgoPlayers.data?.data?.users]);

  return {
    state: combineQueryStatuses([qScoreTypes.status, qCsgoPlayers.status]),
    scoreTypes: qScoreTypes.data?.data?.types,
    csgoPlayers: ixigoUsers,
  };
};
