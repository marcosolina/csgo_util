import { UseQueryResult, useQuery } from "react-query";
import { IxigoResponse, QueryParamType } from "../../lib/http-requests";
import {
  IAvgPlayersScoresPerMap,
  IAvgTeamsScoresPerMap,
  IGetAvgPlayersScoresPerMapRequest,
  IGetAvgTeamsScoresPerMapRequest,
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

export const useGetPlayersAvgScoresPerMap = (
  request: IGetAvgPlayersScoresPerMapRequest
): UseQueryResult<IxigoResponse<IAvgPlayersScoresPerMap>, unknown> => {
  const queryParam = createQueryParamString(request as unknown as Record<string, QueryParamType>);
  return useQuery(
    ["getAvgPlayersScorePerMap", queryParam],
    async () =>
      await performGet<IAvgPlayersScoresPerMap>(
        `${SERVICES_URLS["dem-manager"]["get-charts-avg-players-scores-per-map"]}${queryParam}`
      ),
    {
      enabled: request.steamIds.length > 0,
    }
  );
};

export const useGetTeamsAvgScoresPerMap = (
  request: IGetAvgTeamsScoresPerMapRequest
): UseQueryResult<IxigoResponse<IAvgTeamsScoresPerMap>, unknown> => {
  const queryParam = createQueryParamString(request as unknown as Record<string, QueryParamType>);
  return useQuery(
    ["getAvgTeamsScorePerMap", queryParam],
    async () =>
      await performGet<IAvgTeamsScoresPerMap>(
        `${SERVICES_URLS["dem-manager"]["get-charts-avg-teams-scores-per-map"]}${queryParam}`
      ),
    {
      enabled: !!request.scoreType,
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
