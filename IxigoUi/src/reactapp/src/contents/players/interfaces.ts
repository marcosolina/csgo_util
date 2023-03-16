import { QueryStatus } from "react-query";

export interface IPlayersContent {
  state: QueryStatus;
  scoreTypes?: Record<string, string>;
}
