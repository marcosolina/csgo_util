import { useQuery, UseQueryResult } from "react-query";
import { SERVICES_URLS } from "../../lib/constants/paths";
import { IxigoResponse } from "../../lib/http-requests";
import { performGet } from "../../lib/http-requests/httpRequests";
import { IIxigoServerConfig } from "./interfaces";

/**
 * It returns the CSGO server config
 * @returns
 */
export const useGetIxigoServerConfig = (): UseQueryResult<IxigoResponse<IIxigoServerConfig>, unknown> => {
  return useQuery(
    "getCsgoServerConfig",
    async () => await performGet<IIxigoServerConfig>(SERVICES_URLS["ixigo-server"]["get-csgo-config"])
  );
};
