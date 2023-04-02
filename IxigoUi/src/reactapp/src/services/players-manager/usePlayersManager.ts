import { useMutation } from "react-query";
import { SERVICES_URLS } from "../../lib/constants/paths";
import { createQueryParamString, performGet, useCheckErrorsInResponse } from "../../lib/http-requests/httpRequests";
import { IGetTeamsRequest, IGetTeamsResponse, IGetTeamsResult } from "./interfaces";
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
      console.log("mutation", data);
      checkRespRef.current(data);
    }
  }, [mutation.status, data, checkRespRef]);

  return {
    getTeams: mutation.mutate,
    status: mutation.status,
    response: mutation.status === QueryStatus.error ? undefined : mutation.data,
  };
};
