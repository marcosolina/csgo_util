export interface IUseStatsContentResult {
  selectedPlayerSteamID: string;
  setSelectedPlayerSteamID: (selectedPlayerSteamID: string) => void;

  selectedMatch: number;
  setSelectedMatch: (selectedMatch: number) => void;

  selectedMap: string;
  setSelectedMap: (selectedMap: string) => void;

  selectedSubpage: string;
  setSelectedSubpage: (selectedSubpage: string) => void;
}
