import { useEffect, useRef, useState } from "react";
import { QueryStatus } from "../../lib/http-requests";
import { useCheckErrorsInResponse } from "../../lib/http-requests/httpRequests";
import { IRconRequest, IRconResponse, useSendRconCommand } from "../../services";
import { useGetIxigoServerConfig } from "../../services/ixigo-server";
import { IUseRconContentResult } from "./interfaces";

const DEFAULT_STATE: IRconRequest = {
  rcon_command: "",
  rcon_host: "",
  rcon_passw: "",
  rcon_port: 0,
};

export const useRconContent = (): IUseRconContentResult => {
  const rconHook = useSendRconCommand();
  const qCsgoSrvConfig = useGetIxigoServerConfig();
  const { checkResp } = useCheckErrorsInResponse();
  const checkRespFunc = useRef(checkResp);

  const [errorFields, setErrorFields] = useState<string[]>([]);
  const [rconResponse, setRconResponse] = useState<IRconResponse>();

  const [request, setRequest] = useState<IRconRequest>(DEFAULT_STATE);

  // Fill in the game server address once, from its config. While the game server is off the gateway
  // answers this request with an empty 200, so there is no host: leave the request as it is. Setting
  // it anyway (a new object each time) with "request" as a dependency re-ran this effect forever, and
  // that endless stream of renders starved the router's navigation transitions (links changed the URL
  // but not the page).
  useEffect(() => {
    if (qCsgoSrvConfig.status !== QueryStatus.success) {
      return;
    }
    const host = qCsgoSrvConfig.data.data?.csgo_host;
    const port = qCsgoSrvConfig.data.data?.csgo_port || 0;
    if (!host) {
      return;
    }
    setRequest((current) => (current.rcon_host === "" ? { ...current, rcon_host: host, rcon_port: port } : current));
  }, [qCsgoSrvConfig.status, qCsgoSrvConfig.data]);

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
