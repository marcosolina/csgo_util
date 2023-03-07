import { useEffect, useRef, useState } from "react";
import { useCheckErrorsInResponse } from "../../lib/http-requests/httpRequests";
import { IRconRequest, IRconResponse, useSendRconCommand } from "../../services";
import { IUseRconContentResult } from "./interfaces";

const DEFAULT_STATE: IRconRequest = {
  rcon_command: "",
  rcon_host: "",
  rcon_passw: "",
  rcon_port: 0,
};

export const useRconContent = (): IUseRconContentResult => {
  const rconHook = useSendRconCommand();
  const { checkResp } = useCheckErrorsInResponse();
  const checkRespFunc = useRef(checkResp);

  const [errorFields, setErrorFields] = useState<string[]>([]);
  const [rconResponse, setRconResponse] = useState<IRconResponse>();

  const [request, setRequest] = useState<IRconRequest>(DEFAULT_STATE);

  useEffect(() => {
    if (rconHook.response) {
      const checkOutput = checkRespFunc.current(rconHook.response);
      setErrorFields(checkOutput.errorFields);
      setRconResponse(rconHook.response?.data);
      return;
    }
  }, [rconHook.status, rconHook.response]);

  return {
    sendCommand: rconHook.sendCommand,
    errorFields,
    request,
    setRequest,
    rconResponse,
    queryState: rconHook.status,
  };
};
