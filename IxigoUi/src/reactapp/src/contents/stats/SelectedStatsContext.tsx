import React from 'react';

interface SelectedStatsContextProps {
  selectedPlayerSteamID: string | null;
  setSelectedPlayerSteamID: (steamID: string | null) => void;
  selectedMatch: string | null;
  setSelectedMatch: (steamID: string | null) => void;
  selectedMap: string | null;
  setSelectedMap: (steamID: string | null) => void;
  selectedSubpage: string | null;
  setSelectedSubpage: (steamID: string | null) => void;
}

export const SelectedStatsContext = React.createContext<SelectedStatsContextProps | undefined>(undefined);
