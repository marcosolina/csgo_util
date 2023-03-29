import { QueryStatus, UseQueryResult } from "react-query";
import { IxigoResponse, QueryStatus as IxigoQueryStatus } from "../http-requests";

export function combineQueryStatuses(results: UseQueryResult<IxigoResponse<unknown>, unknown>[]): QueryStatus {
  const error = (result: UseQueryResult<IxigoResponse<unknown>, unknown>) => result.isError;
  const loading = (result: UseQueryResult<IxigoResponse<unknown>, unknown>) =>
    result.status === IxigoQueryStatus.loading;
  const success = (result: UseQueryResult<IxigoResponse<unknown>, unknown>) =>
    result.status === IxigoQueryStatus.success;

  if (results.some(error)) {
    return IxigoQueryStatus.error;
  }

  if (results.some(loading)) {
    return IxigoQueryStatus.loading;
  }

  if (results.every(success)) {
    return IxigoQueryStatus.success;
  }

  return IxigoQueryStatus.idle;
}
