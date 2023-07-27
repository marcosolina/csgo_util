import { useState } from "react";
import { IUseStatsContentResult } from "./interfaces";

export const useStatsContent = (): IUseStatsContentResult => {
  const [selectedPlayerSteamID, setSelectedPlayerSteamID] = useState<string>("");
  const [selectedMatch, setSelectedMatch] = useState<number>(0);
  const [selectedMap, setSelectedMap] = useState<string>("");
  const [selectedSubpage, setSelectedSubpage] = useState<string>("");

  return {
    selectedPlayerSteamID,
    setSelectedPlayerSteamID,
    selectedMatch,
    setSelectedMatch,
    selectedMap,
    setSelectedMap,
    selectedSubpage,
    setSelectedSubpage,
  };
};
