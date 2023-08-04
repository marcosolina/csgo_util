import { useEffect, useMemo, useState } from "react";
import { MATCH_RESULT_REQUEST, useGetStats } from "../../../services";
import { IMatchContentRequest, IMatchContentResponse, IMatchMetadata } from "./interfaces";
import { useTranslation } from "react-i18next";
import { QueryStatus } from "../../../lib/http-requests";
import { IMatchResult } from "../../../services/stats";

export function useMatchStatsContent(request: IMatchContentRequest): IMatchContentResponse {
    const { t } = useTranslation();
    const getStatsRequest = useMemo(() => {
      const copy = { ...MATCH_RESULT_REQUEST };
      copy.queryParams = { match_id: request.match_id };
      return copy;
    }, [request]);
  
    const [data, setData] = useState<IMatchResult>();
    const [matchMetadata, setMatchMetadata] = useState<IMatchMetadata>({
        mapImageName: "",
        formattedDate: "",
        formattedTime: ""
      });
  
    const qGetStats = useGetStats(getStatsRequest);

    useEffect(() => {
        if (qGetStats.status === QueryStatus.success && qGetStats.data) {
            const data = qGetStats.data.data?.view_data;
            if (!data) return;
            const matchResult = data[0];
            if (!matchResult) return;
            setData(matchResult);
            const date = new Date(matchResult.match_date);
            const formattedDate = date.toLocaleDateString("en-GB", { year: "numeric", month: "long", day: "numeric" });
            const formattedTime = date.toLocaleTimeString("en-GB", { hour: "2-digit", minute: "2-digit" });
        
            let mapImageName: string = matchResult.mapname;
        
            if (mapImageName.startsWith("workshop_")) {
            let count: number = 0;
            mapImageName = mapImageName.replace(/_/g, (match: string): string => {
                count++;
                return count <= 2 ? "-" : match;
            });
            }
            matchMetadata.mapImageName = mapImageName;
            matchMetadata.formattedDate = formattedDate;
            matchMetadata.formattedTime = formattedTime;
        }
    }, [qGetStats, t]);


    return {
        state: qGetStats.status,
        data,
        matchMetadata,
      };
}