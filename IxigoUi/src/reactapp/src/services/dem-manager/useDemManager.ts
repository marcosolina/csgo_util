import { useQuery } from "react-query";
import { SERVICES_URLS } from "../../lib/constants/paths";
import { performGet } from "../../lib/http-requests/httpRequests";
import { IRestGetDemFilesResponse } from "./interfaces";

export const useGetDemFiles = () => {
  const { data, status } = useQuery(
    "getAllDemFiles",
    async () => await performGet<IRestGetDemFilesResponse>(SERVICES_URLS["dem-manager"]["get-all-dem-files"])
  );
  console.log(status, data);
};
