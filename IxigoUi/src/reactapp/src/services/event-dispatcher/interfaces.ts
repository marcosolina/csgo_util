import { IxigoResponse } from "../../lib/http-requests";
import { QueryStatus } from "react-query";

export interface ISendEventCommand {
  sendEvent: (request: ISendEventRequest) => void;
  status: QueryStatus;
  response?: IxigoResponse<void>;
}

export interface ISendEventRequest {
  event_name: string;
}
