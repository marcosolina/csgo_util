import { QueryStatus, UseQueryResult } from "react-query";
import { IxigoResponse, QueryStatus as IxigoQueryStatus } from "../http-requests";

export function combineQueryStatuses(results: UseQueryResult<IxigoResponse<unknown>, unknown>[]): QueryStatus {
  const error = (result: UseQueryResult<IxigoResponse<unknown>, unknown>) => result.isError;
  const loading = (result: UseQueryResult<IxigoResponse<unknown>, unknown>) =>
    result.status === IxigoQueryStatus.loading;
  const success = (result: UseQueryResult<IxigoResponse<unknown>, unknown>) =>
    result.status === IxigoQueryStatus.success;

  const refeching = (result: UseQueryResult<IxigoResponse<unknown>, unknown>) => result.isRefetching;

  if (results.some(error)) {
    return IxigoQueryStatus.error;
  }

  if (results.some(loading) || results.some(refeching)) {
    return IxigoQueryStatus.loading;
  }

  if (results.every(success)) {
    return IxigoQueryStatus.success;
  }

  return IxigoQueryStatus.idle;
}
