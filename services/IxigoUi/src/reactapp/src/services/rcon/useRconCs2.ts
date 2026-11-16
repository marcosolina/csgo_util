import { useMutation } from "react-query";
import { SERVICES_URLS } from "../../lib/constants";
import { performPost } from "../../lib/http-requests/httpRequests";
import { IRconResponse, ICs2RconRequest, ISendCs2RconCommand } from "./interfaces";

export const useSendCs2RconCommand = (): ISendCs2RconCommand => {
  const mutation = useMutation(async (rconReq: ICs2RconRequest) => {
    return await performPost<IRconResponse, ICs2RconRequest>(SERVICES_URLS["server-helper"]["send-rcon"], rconReq);
  });

  return {
    sendCommand: mutation.mutate,
    status: mutation.status,
    response: mutation.data,
  };
};
