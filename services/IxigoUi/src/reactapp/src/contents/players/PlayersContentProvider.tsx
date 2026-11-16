import { createContext } from "react";
import { IPlayersContent } from "./interfaces";
import { usePlayersContent } from "./usePlayersContent";

export const PlayersContentContext = createContext<IPlayersContent>({} as IPlayersContent);
const { Provider } = PlayersContentContext;

export const PlayersContentProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const result = usePlayersContent();
  return <Provider value={result}>{children}</Provider>;
};
