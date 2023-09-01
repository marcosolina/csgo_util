import { QueryStatus } from "react-query";
import { ICsgoUser } from "../../services/dem-manager";

export interface IUseStatsContentResult {
  state: QueryStatus;
  steamUsers: ICsgoUser[];
}
