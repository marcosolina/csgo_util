import { createContext } from "react";
import { IUseRconContentResult } from "./interfaces";
import { useRconContent } from "./useRconContent";

export const RconContentContext = createContext<IUseRconContentResult>({} as IUseRconContentResult);
const { Provider } = RconContentContext;

export const RconContentProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const result = useRconContent();
  return <Provider value={result}>{children}</Provider>;
};
