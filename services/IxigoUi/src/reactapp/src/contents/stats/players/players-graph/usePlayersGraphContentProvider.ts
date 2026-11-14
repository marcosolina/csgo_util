import { useContext } from "react";
import { PlayersGraphContext } from "./PlayersGraphContentProvider";
import { IUsePlayersGraphContentResult } from "./interfaces";

export function usePlayerGraphContentProvider(): IUsePlayersGraphContentResult {
  return useContext(PlayersGraphContext);
}
