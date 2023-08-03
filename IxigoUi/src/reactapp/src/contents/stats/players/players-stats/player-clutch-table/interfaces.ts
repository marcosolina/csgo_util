import { QueryStatus } from "react-query";

export interface IPlayerClutchTableProps {
  steamid: string;
}

export interface IPlayerClutchRequest {
  steamid: string;
}

export interface IPlayerClutchResponse {
  state: QueryStatus;
}
