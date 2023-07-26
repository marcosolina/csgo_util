import { UseQueryResult, useMutation, useQuery } from "react-query";
import { SERVICES_URLS } from "../../lib/constants/paths";
import { createQueryParamString, performGet, useCheckErrorsInResponse } from "../../lib/http-requests/httpRequests";
import { IGetTeamsRequest, IGetTeamsResponse, IGetTeamsResult, IRestGetScoreTypesResponse } from "./interfaces";
import { useEffect, useRef } from "react";
import { IxigoResponse, QueryStatus } from "../../lib/http-requests";

export const useGetTeams = (): IGetTeamsResult => {
  const { checkResp } = useCheckErrorsInResponse();
  const checkRespRef = useRef(checkResp);

  const mutation = useMutation(async (request: IGetTeamsRequest) => {
    const queryString = createQueryParamString({ ...request });
    return await performGet<IGetTeamsResponse>(`${SERVICES_URLS["players-manager"]["get-teams"]}${queryString}`);
  });

  const data = mutation.error as IxigoResponse<IGetTeamsResponse>;

  useEffect(() => {
    if (mutation.status === QueryStatus.error && data && data.data) {
      checkRespRef.current(data);
    }
  }, [mutation.status, data, checkRespRef]);

  return {
    getTeams: mutation.mutate,
    status: mutation.status,
    response: mutation.status === QueryStatus.error ? undefined : mutation.data,
  };
};

/**
 * It returns the score types extracted from the DEM files
 * @returns
 */
export const useGetScoreTypes = (): UseQueryResult<IxigoResponse<IRestGetScoreTypesResponse>, unknown> => {
  return useQuery(
    "getScoreTypes",
    async () => await performGet<IRestGetScoreTypesResponse>(SERVICES_URLS["players-manager"]["get-scores-type"])
  );
};
