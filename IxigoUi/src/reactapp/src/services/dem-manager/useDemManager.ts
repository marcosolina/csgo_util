import { useQuery, UseQueryResult } from "react-query";
import { SERVICES_URLS } from "../../lib/constants/paths";
import { IxigoResponse } from "../../lib/http-requests";
import { performGet } from "../../lib/http-requests/httpRequests";
import { IRestGetDemFilesResponse, IRestGetScoreTypesResponse } from "./interfaces";

export const useGetDemFiles = (): UseQueryResult<IxigoResponse<IRestGetDemFilesResponse>, unknown> => {
  return useQuery(
    "getAllDemFiles",
    async () => await performGet<IRestGetDemFilesResponse>(SERVICES_URLS["dem-manager"]["get-all-dem-files"])
  );
};

export const useGetScoreTypes = (): UseQueryResult<IxigoResponse<IRestGetScoreTypesResponse>, unknown> => {
  return useQuery(
    "getScoreTypes",
    async () => await performGet<IRestGetScoreTypesResponse>(SERVICES_URLS["dem-manager"]["get-dem-data-scores-type"])
  );
};
