import { createContext } from "react";
import { usePlayersGraphContent } from "./usePlayersGraphContent";
import { IUsePlayersGraphContentResult } from "./interfaces";

export const PlayersGraphContext = createContext<IUsePlayersGraphContentResult>({} as IUsePlayersGraphContentResult);
const { Provider } = PlayersGraphContext;

export const PlayersGraphProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const result = usePlayersGraphContent();
  return <Provider value={result}>{children}</Provider>;
};
