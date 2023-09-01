import { IUseStatsContentResult } from "./interfaces";
import { useGetCsgoPlayers } from "../../services/dem-manager";

export const useStatsContent = (): IUseStatsContentResult => {
  const qUsers = useGetCsgoPlayers();

  return {
    state: qUsers.status,
    steamUsers: qUsers.data?.data?.users || [],
  };
};
