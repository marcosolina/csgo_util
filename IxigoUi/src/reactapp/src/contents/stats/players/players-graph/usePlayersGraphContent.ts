import { useMemo, useState } from "react";
import { IUsePlayersGraphContentResult } from "./interfaces";
import { useGetScoreTypes } from "../../../../services";
import { IxigoPossibleValue } from "../../../../common/select";
import { combineQueryStatuses } from "../../../../lib/queries/queriesFunctions";
import { ar } from "date-fns/locale";

export const usePlayersGraphContent = (): IUsePlayersGraphContentResult => {
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [binningLevel, setBinningLevel] = useState<string>();
  const [graphsSelected, setGraphsSelected] = useState<string[]>();

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
    state: combineQueryStatuses([qScoreTypes]),
    startDate,
    endDate,
    binningLevel,
    graphsSelected,
    possibleScoreTypesValues,
    possibleBinningValues,

    setStartDate,
    setEndDate,
    setBinningLevel,
    setGraphsSelected,
  };
};
