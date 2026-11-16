import { useMutation } from "react-query";
import { SERVICES_URLS } from "../../lib/constants";
import { performPost } from "../../lib/http-requests/httpRequests";
import { IRconResponse, IRconRequest, ISendRconCommand } from "./interfaces";

export const useSendRconCommand = (): ISendRconCommand => {
  const mutation = useMutation(async (rconReq: IRconRequest) => {
    return await performPost<IRconResponse, IRconRequest>(SERVICES_URLS["rcon-api"]["post-command"], rconReq);
  });

  return {
    sendCommand: mutation.mutate,
    status: mutation.status,
    response: mutation.data,
  };
};
