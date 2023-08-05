import { useTranslation } from "react-i18next";
import { ITeamMatchContentResponse, ITeamMatchContentRequest } from "./interfaces";
import { ITeamMatchResults } from "../../../../services/stats";
import { useLocation, useNavigate } from "react-router-dom";
import { useCallback, useEffect, useMemo, useState } from "react";
import { MATCH_TEAM_RESULT_REQUEST, useGetStats } from "../../../../services";
import { MRT_Cell, MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "../../../../lib/http-requests";
import customHeader from "../../../../common/material-table/custom-header/customHeader";
import { TFunction } from "i18next";
import TableLink from '../../../../common/table-link/TableLink';
import CellChip from "../../../../common/cell-chip/CellChip";
import PieChartMini from "../../../../common/pie-chart-mini/PieChartMini";

const COL_HEADERS_BASE_TRANSLATION_KEY = "page.stats.match.teamtable.column-headers";
const SMALL_COL_SIZE = 5;

const COLUMNS_ORDER: string[] = [
    "usernames",
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
    team: string,
    t: TFunction<"translation", undefined, "translation">,
    playerPathUpdater: (steamid: string) => string
  ): MRT_ColumnDef<ITeamMatchResults> {
    const cell: MRT_ColumnDef<ITeamMatchResults> = {
      id: key,
      accessorFn: (row) => row[key as keyof ITeamMatchResults],
      header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
      size: SMALL_COL_SIZE,
      Header: customHeader<ITeamMatchResults>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
    };

    if (key === "usernames") {
        cell.header = t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.${team}header`);
        cell.size = 100;
        cell.Cell = ({ cell }: { cell: MRT_Cell<ITeamMatchResults> }) => {
          const username = cell.getValue() as string;
          const steamid = cell.row.original.steamid;
          return (
            <TableLink text={username} color={cell.row.original.last_round_team === "team1" ? "#90caf9" : "orange"} to={playerPathUpdater(steamid)} />
          );
        };
      }
      

      if (key === "hltv_rating") {
        cell.Cell = ({ cell }: { cell: MRT_Cell<ITeamMatchResults> }) => {
          const rating = cell.getValue() as number;
          return <CellChip value={rating} type="rating" />;
        };
      }

      if (key === "headshot_percentage") {
        cell.Cell = ({ cell }: { cell: MRT_Cell<ITeamMatchResults> }) => {
          const value = cell.getValue() as number;
          return <PieChartMini percentage={value} color="darkturquoise" size={22} />;
        };
      }
  
    return cell;
  }

export const useMatchTeamTable = (
  request: ITeamMatchContentRequest
): ITeamMatchContentResponse => {
  const { t } = useTranslation();
  const getStatsRequest = useMemo(() => {
    const copy = { ...MATCH_TEAM_RESULT_REQUEST };
    copy.queryParams = { match_id: request.match_id, last_round_team: request.team };
    return copy;
  }, [request]);
  const history = useNavigate();
  const location = useLocation();
  const [data, setData] = useState<ITeamMatchResults[]>([]);

  const playerPathUpdater = useCallback(
    (steamid: string) => {
      const pathParts = location.pathname.split("/").filter((p) => p);
      let newPath = `${location.pathname}/player/${steamid}`;
      if (pathParts.length > 1) {
        newPath = `/stats/player/${steamid}`;
      }
      //history(newPath);
      console.log(location);
      console.log(newPath);
      return newPath;
    },
    [location.pathname]
  );

  // Get the data
  const qMatchRequest = useGetStats(getStatsRequest);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<ITeamMatchResults>[]>(() => {
    const cols: MRT_ColumnDef<ITeamMatchResults>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, request.team, t, playerPathUpdater));
    });
    return cols;
  }, [t, playerPathUpdater, request]);

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
      setData(matchResults);
    }
  }, [qMatchRequest, request]);

  return {
    columns,
    state: qMatchRequest.status,
    data,
    refetch,
  };
};