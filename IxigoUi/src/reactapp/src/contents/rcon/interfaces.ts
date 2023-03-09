import { QueryStatus } from "react-query";
import { IRconRequest, IRconResponse } from "../../services";

export interface IUseRconContentResult {
  sendCommand: (request: IRconRequest) => void;
  errorFields: string[];
  request: IRconRequest;
  setRequest: (request: IRconRequest) => void;
  rconResponse?: IRconResponse;
  queryState: QueryStatus;
}

export interface IRconCommand {
  cmd: string;
  name: string;
  image: string;
}
