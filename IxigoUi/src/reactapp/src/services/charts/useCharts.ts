import { UseQueryResult, useQuery } from "react-query";
import { IxigoResponse, QueryParamType } from "../../lib/http-requests";
import {
  IAvgScoresPerMap,
  IGetAvgScoresPerMapRequest,
  IMapsPlayed,
  ITeamScorePerMapRequest,
  ITeamScorePerMapResponse,
} from "./interfaces";
import { performGet } from "../../lib/http-requests/httpRequests";
import { SERVICES_URLS } from "../../lib/constants";
import { createQueryParamString } from "../../lib/http-requests/httpRequests";

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

export const useGetAvgScoresPerMap = (
  request: IGetAvgScoresPerMapRequest
): UseQueryResult<IxigoResponse<IAvgScoresPerMap>, unknown> => {
  const queryParam = createQueryParamString(request as unknown as Record<string, QueryParamType>);
  return useQuery(
    ["getAvgScorePerMap", queryParam],
    async () =>
      await performGet<IAvgScoresPerMap>(
        `${SERVICES_URLS["dem-manager"]["get-charts-avg-scores-per-map"]}${queryParam}`
      ),
    {
      enabled: request.steamIds.length > 0,
    }
  );
};

export const useGetTeamScorePerMap = (
  request: ITeamScorePerMapRequest
): UseQueryResult<IxigoResponse<ITeamScorePerMapResponse>, unknown> => {
  const queryParam = createQueryParamString(request as unknown as Record<string, QueryParamType>);
  return useQuery(
    ["getTeamScorePerMap", queryParam],
    async () =>
      await performGet<ITeamScorePerMapResponse>(
        `${SERVICES_URLS["dem-manager"]["get-charts-team-score-per-map"]}${queryParam}`
      ),
    {
      enabled: !!request.map,
    }
  );
};
