import { useQuery } from "react-query";
import { SERVICES_URLS } from "../../lib/constants/paths";
import { performGet } from "../../lib/http-requests/httpRequests";
import { IRestGetDemFilesResponse } from "./interfaces";

export const useGetDemFiles = () => {
  const { data, status } = useQuery(
    "getAllDemFiles",
    async () => await performGet<IRestGetDemFilesResponse>(SERVICES_URLS.DEM_MANAGER.GET_DEM_FILES)
  );
  console.log(status, data);
};
