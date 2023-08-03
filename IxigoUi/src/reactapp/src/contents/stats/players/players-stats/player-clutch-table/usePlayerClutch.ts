import { useMemo } from "react";
import { PLAYER_CLUTCH_STATS_REQUEST, useGetStats } from "../../../../../services";
import { IPlayerClutchRequest, IPlayerClutchResponse } from "./interfaces";

export function usePlayerClutch(request: IPlayerClutchRequest): IPlayerClutchResponse {
  const getStatsRequest = useMemo(() => {
    return { ...PLAYER_CLUTCH_STATS_REQUEST, ...{ queryParams: { steamid: request.steamid } } };
  }, [request]);
  console.log("getStatsRequest", getStatsRequest);
  const qUsersRequest = useGetStats(getStatsRequest);

  return {
    state: qUsersRequest.status,
  };
}
