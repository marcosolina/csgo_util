import { createContext } from "react";
import { useStatsContent } from "./useStatsContent";
import { IUseStatsContentResult } from "./interfaces";

export const StatsContentContext = createContext<IUseStatsContentResult>({} as IUseStatsContentResult);
const { Provider } = StatsContentContext;

export const RconContentProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const result = useStatsContent();
  return <Provider value={result}>{children}</Provider>;
};
