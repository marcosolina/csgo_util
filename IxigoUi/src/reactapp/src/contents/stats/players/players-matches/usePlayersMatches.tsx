import { useTranslation } from "react-i18next";
import { IPlayersMatchesContentRequest, IPlayersMatchesContentResponse, IPlayersMatchesData } from "./interfaces";
import { useCallback, useEffect, useMemo, useState } from "react";
import {
  IMatchResults,
  ITeamMatchResults,
  MATCH_RESULTS_REQUEST,
  MATCH_TEAM_RESULT_REQUEST,
  useGetStats,
} from "../../../../services/stats";
import { TFunction } from "i18next";
import { MRT_Cell, MRT_ColumnDef } from "material-react-table";
import customHeader from "../../../../common/material-table/custom-header/customHeader";
import TableLink from "../../../../common/table-link/TableLink";
import PieChartMini from "../../../../common/pie-chart-mini/PieChartMini";
import CellChip from "../../../../common/cell-chip/CellChip";
import { combineQueryStatuses } from "../../../../lib/queries";
import { QueryStatus } from "../../../../lib/http-requests";

const COL_HEADERS_BASE_TRANSLATION_KEY = "page.stats.player.content.matches.column-headers";
const SMALL_COL_SIZE = 5;

const COLUMNS_ORDER: string[] = [
  "match_date",
  "mapName",
  "roundsplayed",
  "last_round_team",
  "rounds_on_team1",
  "rounds_on_team2",
  "kills",
  "deaths",
  "assists",
  "score",
  "rws",
  "headshots",
  "headshot_percentage",
  "mvp",
  "hltv_rating",
  "adr",
  "kpr",
  "dpr",
  "kdr",
  "hr",
  "bp",
  "ud",
  "ffd",
  "td",
  "tda",
  "tdh",
  "fa",
  "ebt",
  "fbt",
  "ek",
  "tk",
  "_1k",
  "_2k",
  "_3k",
  "_4k",
  "_5k",
  "_1v1",
  "_1v2",
  "_1v3",
  "_1v4",
  "_1v5",
];

function createColumnDefinition(
  key: string,
  t: TFunction<"translation", undefined, "translation">,
  matchPathUpdater: (matchId: number) => string,
  mapPathUpdater: (mapName: string) => string
): MRT_ColumnDef<IPlayersMatchesData> {
  const cell: MRT_ColumnDef<IPlayersMatchesData> = {
    id: key,
    accessorFn: (row) => row[key as keyof IPlayersMatchesData],
    header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
    size: SMALL_COL_SIZE,
    Header: customHeader<IPlayersMatchesData>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
  };

  if (key === "match_date") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayersMatchesData> }) => {
      const date = cell.getValue() as string;
      const match_id = cell.row.original.match_id;
      const dateF = new Date(date);
      const formattedDate = dateF.toLocaleDateString(undefined, { year: "numeric", month: "long", day: "numeric" });
      const formattedTime = dateF.toLocaleTimeString(undefined, { hour: "2-digit", minute: "2-digit" });

      return <TableLink text={`${formattedDate}, ${formattedTime}`} to={matchPathUpdater(match_id)} />;
    };
  }

  if (key === "mapName") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayersMatchesData> }) => {
      const mapname = cell.getValue() as string;
      return <TableLink text={mapname} to={mapPathUpdater(mapname)} />;
    };
  }

  if (key === "headshot_percentage") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayersMatchesData> }) => {
      const value = cell.getValue() as number;
      return <PieChartMini percentage={value} color="darkturquoise" size={22} />;
    };
  }

  if (key === "hltv_rating") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IPlayersMatchesData> }) => {
      const rating = cell.getValue() as number;
      return <CellChip value={rating} type="rating" />;
    };
  }

  return cell;
}

export function usePlayerMatches(request: IPlayersMatchesContentRequest): IPlayersMatchesContentResponse {
  const { t } = useTranslation();
  const getStatsRequest = useMemo(() => {
    const copy = { ...MATCH_TEAM_RESULT_REQUEST };
    copy.queryParams = { steamid: request.steamId };
    copy.enabled = !!request.steamId;
    return copy;
  }, [request]);

  const qMatchTeam = useGetStats(getStatsRequest);
  const qMatchResult = useGetStats(MATCH_RESULTS_REQUEST);

  const [data, setData] = useState<IPlayersMatchesData[]>([]);

  const matchPathUpdater = useCallback((matchId: number) => {
    return `/stats/matches/${matchId}`; // TODO add context prefix
  }, []);

  const mapPathUpdater = useCallback((mapName: string) => {
    return `/stats/map/${mapName}`; // TODO add context prefix
  }, []);

  const queriesState = combineQueryStatuses([qMatchTeam, qMatchResult]);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<IPlayersMatchesData>[]>(() => {
    const cols: MRT_ColumnDef<IPlayersMatchesData>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, t, matchPathUpdater, mapPathUpdater));
    });
    return cols;
  }, [t, matchPathUpdater, mapPathUpdater]);

  // Create the data
  useEffect(() => {
    if (queriesState === QueryStatus.success) {
      const matchesTeamData = qMatchTeam.data?.data?.view_data;
      const matchesResults = qMatchResult.data?.data?.view_data;
      if (!matchesTeamData || !matchesResults) {
        return;
      }

      // Create a lookup for mapName from match_id in matches
      const mapNameLookup = matchesResults.reduce((lookup: { [key: number]: string }, match: IMatchResults) => {
        lookup[match.match_id] = match.mapname;
        return lookup;
      }, {});

      const matchData = matchesTeamData
        .filter((p: ITeamMatchResults) => p.steamid === request.steamId)
        .map((p: ITeamMatchResults) => {
          const mergedData: IPlayersMatchesData = { ...p, ...{ mapName: mapNameLookup[p.match_id] } };
          return mergedData;
        });

      setData(matchData);
    }
  }, [queriesState, qMatchTeam.data?.data, qMatchResult.data?.data, request.steamId]);

  return {
    state: queriesState,
    columns,
    data,
  };
}
