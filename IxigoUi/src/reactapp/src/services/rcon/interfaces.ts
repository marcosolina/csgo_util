import { QueryStatus } from "react-query";
import { IxigoResponse } from "../../lib/http-requests";

export interface IRconRequest {
  rcon_host: string;
  rcon_port: number;
  rcon_passw: string;
  rcon_command: string;
}

export interface ISendRconCommand {
  sendCommand: (request: IRconRequest) => void;
  status: QueryStatus;
  response?: IxigoResponse<IRconResponse>;
}

export interface IRconResponse {
  rcon_response: string;
}

export interface IGetMapsResponse {
  server_maps: IMapResponse[];
}

export interface IMapResponse {
  map_name: string;
  is_workshop_map: boolean;
  workshop_id?: string;
}
