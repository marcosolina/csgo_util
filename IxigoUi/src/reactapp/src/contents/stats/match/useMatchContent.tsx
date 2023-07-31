import { useTranslation } from "react-i18next";
import { IMatchContent } from "./interfaces";
import { useCallback, useEffect, useMemo, useState } from "react";
import { MRT_Cell, MRT_ColumnDef } from "material-react-table";
import { useGetStats, IMatchResults, MATCH_RESULTS_REQUEST } from "../../../services";
import { QueryStatus } from "../../../lib/http-requests";
import { Box } from "@mui/material";
import { TFunction } from "i18next";
import customHeader from "../../../common/material-table/custom-header/customHeader";
import { useLocation, useNavigate } from "react-router-dom";
import ScoreChip from "../../../common/score-chip/ScoreChip";
import TableLink from '../../../common/table-link/TableLink';

const COL_HEADERS_BASE_TRANSLATION_KEY = "page.stats.match.column-headers";
const SMALL_COL_SIZE = 5;

const COLUMNS_ORDER: string[] = [
  "match_date",
  "mapname",
  "team1_total_wins",
  "team2_total_wins",
  "total_t_wins",
  "total_ct_wins",
  "score_differential",
];

function createColumnDefinition(
  key: string,
  t: TFunction<"translation", undefined, "translation">,
  mapNameClickHandler: (map: string) => void,
  matchClickHandler: (matchId: number) => void
): MRT_ColumnDef<IMatchResults> {
  const cell: MRT_ColumnDef<IMatchResults> = {
    id: key,
    accessorFn: (row) => row[key as keyof IMatchResults],
    header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
    size: SMALL_COL_SIZE,
    Header: customHeader<IMatchResults>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
  };

  if (key === "match_date") {
    cell.enableGrouping = false;
    cell.Cell = ({ cell }: { cell: MRT_Cell<IMatchResults> }) => {
      const matchDate = cell.getValue() as string;
      const match_id = cell.row.original.match_id;
  
      const date = new Date(matchDate);
      const formattedDate = date.toLocaleDateString("en-GB", { year: "numeric", month: "long", day: "numeric" });
      const formattedTime = date.toLocaleTimeString("en-GB", { hour: "2-digit", minute: "2-digit" });
  
      return (
        <TableLink text={`${formattedDate}, ${formattedTime}`} onClickHandler={() => matchClickHandler(match_id)} />
      );
    };
  }
  
  if (key === "mapname") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IMatchResults> }) => {
      const map = cell.getValue() as string;
  
      return (
        <TableLink text={map} onClickHandler={() => mapNameClickHandler(map)} />
      );
    };
  }

  if (key === "team1_total_wins" || key === "team2_total_wins") {
    cell.enableGrouping = false;
    cell.enableColumnActions = false;
    cell.Cell = ({ cell }: { cell: MRT_Cell<IMatchResults> }) => {
      const score = cell.getValue() as number;
      const alignment = key === "team1_total_wins" ? "flex-end" : "flex-start";
      return (
        <div style={{ display: "flex", justifyContent: alignment, alignItems: "center", height: "100%" }}>
          <ScoreChip score={score} />
        </div>
      );
    };
  }
  
  if (key === "total_t_wins" || key === "total_ct_wins") {
    cell.enableGrouping = false;
    cell.aggregationFn = "mean" as any;
    cell.AggregatedCell = ({ cell }: { cell: any }) => {
      const meanScore = cell.getValue().toFixed(2);
      const team = key === "total_t_wins" ? "Terrorist" : "CT";
      return <Box>{`${team} Mean Score: ${meanScore}`}</Box>;
    };
  }

  if (key === "score_differential") {
    cell.enableGrouping = false;
    cell.Cell = ({ cell }: { cell: any }) => cell.getValue();
    cell.aggregationFn = "mean" as any;
    cell.AggregatedCell = ({ cell }: { cell: any }) => {
      const meanScoreDiff = cell.getValue().toFixed(2);
      return <Box>{`Mean score differential: ${meanScoreDiff}`}</Box>;
    };
  }

  return cell;
}


export const useMatchContent = (): IMatchContent => {
  const { t } = useTranslation();
  const history = useNavigate();
  const location = useLocation();
  const [data, setData] = useState<IMatchResults[]>([]);

  const mapNameClickHandler = useCallback(
    (mapName: string) => {
      const newPath = `${location.pathname}/map/${mapName}`;
      history(newPath);
    },
    [history, location.pathname]
  );

  const matchClickHandler = useCallback(
    (match_id: number) => {
      const newPath = `${location.pathname}/match/${match_id}`;
      history(newPath);
    },
    [history, location.pathname]
  );

  // Get the data
  const qMatchRequest = useGetStats(MATCH_RESULTS_REQUEST);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<IMatchResults>[]>(() => {
    const cols: MRT_ColumnDef<IMatchResults>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, t, mapNameClickHandler, matchClickHandler));
    });
    return cols;
  }, [t, mapNameClickHandler, matchClickHandler]);

  const refetch = useCallback(() => {
    qMatchRequest.refetch();
  }, [qMatchRequest]);

  // Create the data
  useEffect(() => {
    if (qMatchRequest.status === QueryStatus.success) {
      let matchResults = qMatchRequest.data?.data?.view_data;
      if (!matchResults) {
        return;
      }

      matchResults = matchResults.map((match: any) => ({
        ...match,
        score_differential: match.total_t_wins - match.total_ct_wins,
      }));
      
      setData(matchResults);
    }
  }, [qMatchRequest.status, qMatchRequest.data]);

  return {
    columns,
    state: qMatchRequest.status,
    data,
    refetch,
  };
};
