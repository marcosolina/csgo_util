import { useMemo, useState } from "react";
import { IxigoPossibleValue } from "../../common/select";
import { combineQueryStatuses } from "../../lib/queries";
import { ICsgoUser, useGetCsgoPlayers, useGetScoreTypes } from "../../services";
import { IPlayersContent } from "./interfaces";

export const usePlayersContent = (): IPlayersContent => {
  const [penaltyWeight, setPenaltyWeight] = useState<number>(0.4);
  const [percPlayed, setPercPlayed] = useState<number>(0.9);
  const [matchesToConsider, setMatchesToConsider] = useState<number>(20);
  const [scoreType, setScoreType] = useState<string>("HLTV");
  const [listOfSelectedPlayers, setListOfSelectedPlayers] = useState<string[]>([]);

  const qScoreTypes = useGetScoreTypes();
  const qCsgoPlayers = useGetCsgoPlayers();

  const ixigoUsers = useMemo((): ICsgoUser[] => {
    if (!qCsgoPlayers.data?.data?.users) {
      return [];
    }

    const arr = [...qCsgoPlayers.data.data.users];
    return arr.sort((o1, o2) => o1.user_name.toLocaleLowerCase().localeCompare(o2.user_name.toLocaleLowerCase()));
  }, [qCsgoPlayers.data?.data?.users]);

  const possiblePercPlayedValues = useMemo((): IxigoPossibleValue[] => {
    let arr = [];
    for (let i = 1; i < 101; i++) {
      arr.push({ value: `${i / 100}`, label: `${i} %` });
    }
    return arr;
  }, []);

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

  return {
    state: combineQueryStatuses([qScoreTypes, qCsgoPlayers]),
    penaltyWeight,
    percPlayed,
    matchesToConsider: matchesToConsider,
    scoreType,
    listOfSelectedPlayers,
    possiblePercPlayedValues,
    possibleScoreTypesValues,

    setPenaltyWeight,
    setPercPlayed,
    setMatchesToConsider,
    setScoreType,
    setListOfSelectedPlayers,

    scoreTypes,
    csgoPlayers: ixigoUsers,
  };
};
