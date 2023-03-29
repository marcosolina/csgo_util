import { useContext } from "react";
import { IUseRconContentResult } from "./interfaces";
import { RconContentContext } from "./RconContentProvider";

export function useRconContentProvider(): IUseRconContentResult {
  return useContext(RconContentContext);
}
