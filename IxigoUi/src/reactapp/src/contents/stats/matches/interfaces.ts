import { QueryStatus } from "react-query";
import { IMatchResult } from "../../../services/stats";

export interface IMatchContent {
  state: QueryStatus;
  data: IMatchResult;
  refetch: () => void;
}