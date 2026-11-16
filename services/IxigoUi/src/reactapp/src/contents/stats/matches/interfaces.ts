import { QueryStatus } from "react-query";
import { IMatchResults } from "../../../services/stats";

export interface IMatchContentResponse {
  state: QueryStatus;
  data?: IMatchResults;
  matchMetadata: IMatchMetadata;
}

export interface IMatchContentRequest {
  match_id: number;
}

export interface IMatchMetadata {
  mapImageName: string;
  formattedDate: string;
  formattedTime: string;
}
