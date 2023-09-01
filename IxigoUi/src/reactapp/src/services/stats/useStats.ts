import { UseQueryResult, useQuery } from "react-query";
import { IGetStatsRequest, IGetStatsResponse } from "./interfaces";
import { IxigoResponse } from "../../lib/http-requests";
import { SERVICES_URLS } from "../../lib/constants/paths";
import { createQueryParamString, performGet } from "../../lib/http-requests/httpRequests";

/**
 * It returns the stats for the given view.
 * If the request contains a queryParam, it will be added to the request.
 * @param request
 * @returns
 */
export function useGetStats<T>(
  request: IGetStatsRequest<T>
): UseQueryResult<IxigoResponse<IGetStatsResponse<T>>, unknown> {
  const queryString = createQueryParamString({ ...request.queryParams });
  return useQuery(
    ["getStats", request.viewName, queryString],
    async () =>
      await performGet<IGetStatsResponse<T>>(
        `${SERVICES_URLS["dem-manager"]["get-stats"]}/${request.viewName}${queryString}`
      ),
    {
      enabled: !!request.viewName && (request.enabled ?? true),
    }
  );
}
