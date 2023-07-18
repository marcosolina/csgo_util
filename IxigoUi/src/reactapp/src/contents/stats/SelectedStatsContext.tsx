import React from 'react';

interface SelectedStatsContextProps {
  selectedPlayerSteamID: string | null;
  setSelectedPlayerSteamID: (selectedPlayerSteamID: string | null) => void;
  selectedMatch: string | null;
  setSelectedMatch: (selectedMatch: string | null) => void;
  selectedMap: string | null;
  setSelectedMap: (selectedMap: string | null) => void;
  selectedSubpage: string | null;
  setSelectedSubpage: (selectedSubpage: string | null) => void;
}

export const SelectedStatsContext = React.createContext<SelectedStatsContextProps | undefined>(undefined);
