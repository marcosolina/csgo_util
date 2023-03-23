import { QueryStatus } from "react-query";
import { QueryStatus as IxigoQueryStatus } from "../http-requests";

export function combineQueryStatuses(statuses: QueryStatus[]): QueryStatus {
  const error = (status: QueryStatus) => status === IxigoQueryStatus.error;
  const loading = (status: QueryStatus) => status === IxigoQueryStatus.loading;
  const success = (status: QueryStatus) => status === IxigoQueryStatus.success;
  console.log(statuses);
  if (statuses.some(error)) {
    return IxigoQueryStatus.error;
  }

  if (statuses.some(loading)) {
    return IxigoQueryStatus.loading;
  }

  if (statuses.every(success)) {
    return IxigoQueryStatus.success;
  }

  return IxigoQueryStatus.idle;
}
