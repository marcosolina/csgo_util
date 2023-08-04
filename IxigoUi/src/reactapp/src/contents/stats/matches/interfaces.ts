import { QueryStatus } from "react-query";
import { IMatchResult } from "../../../services/stats";

export interface IMatchContentResponse {
  state: QueryStatus;
  data?: IMatchResult;
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
