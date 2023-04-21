import { UseQueryResult, useQuery } from "react-query";
import { IxigoResponse } from "../../lib/http-requests";
import { IMapsPlayed } from "./interfaces";
import { performGet } from "../../lib/http-requests/httpRequests";
import { SERVICES_URLS } from "../../lib/constants";

/**
 * It returns the number of times the maps were played
 * @returns
 */
export const useGetMapsPlayedCount = (): UseQueryResult<IxigoResponse<IMapsPlayed>, unknown> => {
  return useQuery(
    "getMapsPlayedCount",
    async () => await performGet<IMapsPlayed>(SERVICES_URLS["dem-manager"]["get-charts-data-maps-played"])
  );
};
