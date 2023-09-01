import { useQuery, UseQueryResult } from "react-query";
import { SERVICES_URLS } from "../../lib/constants/paths";
import { IxigoResponse } from "../../lib/http-requests";
import { performGet } from "../../lib/http-requests/httpRequests";
import { IRestGetCsgoUsersResponse, IRestGetDemFilesResponse } from "./interfaces";

/**
 * It returns the available DEM files
 * @returns
 */
export const useGetDemFiles = (): UseQueryResult<IxigoResponse<IRestGetDemFilesResponse>, unknown> => {
  return useQuery(
    "getAllDemFiles",
    async () => await performGet<IRestGetDemFilesResponse>(SERVICES_URLS["dem-manager"]["get-all-dem-files"])
  );
};

/**
 * It returns a list of Steam IDs extracted from the DEM files
 * @returns
 */
export const useGetCsgoPlayers = (): UseQueryResult<IxigoResponse<IRestGetCsgoUsersResponse>, unknown> => {
  return useQuery(
    "getCsgoPlayers",
    async () => await performGet<IRestGetCsgoUsersResponse>(SERVICES_URLS["dem-manager"]["get-dem-data-users"])
  );
};
