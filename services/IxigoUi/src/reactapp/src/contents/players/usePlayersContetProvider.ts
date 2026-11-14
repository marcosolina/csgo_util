import { useContext } from "react";
import { PlayersContentContext } from "./PlayersContentProvider";
import { IPlayersContent } from "./interfaces";

export function usePlayersContentProvider(): IPlayersContent {
  return useContext(PlayersContentContext);
}
