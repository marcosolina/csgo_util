import { useQuery, UseQueryResult } from "react-query";
import { SERVICES_URLS } from "../../lib/constants";
import { IxigoResponse } from "../../lib/http-requests";
import { performGet } from "../../lib/http-requests/httpRequests";
import { IGetMapsResponse } from "./interfaces";

export const useGetServerMaps = (): UseQueryResult<IxigoResponse<IGetMapsResponse>, unknown> => {
  return useQuery(
    "getServerMaps",
    async () => await performGet<IGetMapsResponse>(SERVICES_URLS["server-helper"]["get-maps"])
  );
};
