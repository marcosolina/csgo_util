import { useMutation } from "react-query";
import { SERVICES_URLS } from "../../lib/constants/paths";
import { createQueryParamString, performGet } from "../../lib/http-requests/httpRequests";
import { IGetTeamsRequest, IGetTeamsResponse, IGetTeamsResult } from "./interfaces";

export const useGetTeams = (): IGetTeamsResult => {
  const mutation = useMutation(async (request: IGetTeamsRequest) => {
    const queryString = createQueryParamString({ ...request });
    return await performGet<IGetTeamsResponse>(`${SERVICES_URLS["players-manager"]["get-teams"]}${queryString}`);
  });

  return {
    getTeams: mutation.mutate,
    status: mutation.status,
    response: mutation.data,
  };
};
