import { useTranslation } from "react-i18next";
import { ITableRound, IMatchRoundContentRequest } from "./interfaces";
import {
  IMatchRound,
  IRoundEvent,
  IRoundKillEvent,
  ITeamMatchResults,
} from "../../../../services";
import { useEffect, useMemo, useState } from "react";
import {
  MATCH_TEAM_RESULT_REQUEST,
  ROUND_SCORECARD_REQUEST,
  ROUND_KILL_EVENTS_REQUEST,
  ROUND_EVENTS_REQUEST,
  useGetStats,
} from "../../../../services";
import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "../../../../lib/http-requests";
import customHeader from "../../../../common/material-table/custom-header/customHeader";
import { TFunction } from "i18next";
import DetailPanel from "./DetailPanel";
import FinanceCell from "./FinanceCell";
import PlayerCountCell from "./PlayerCountCell";
import ScoreCell from "./ScoreCell";
import RoundTypeCell from "./RoundTypeCell";
import RoundEndReasonCell from "./RoundEndReasonCell";


import { IUseMatchRoundsContentResponse } from "./interfaces";

const COL_HEADERS_BASE_TRANSLATION_KEY =
  "page.stats.match.roundscorecard.column-headers";
const SMALL_COL_SIZE = 5;

const COLUMNS_ORDER: string[] = [
  "team1.total_equipment_value",
  "team1.round_type",
  "team1.player_count",
  "team1.team1_score",
  "team1.round_end_reason",
  "team2.team2_score",
  "team2.player_count",
  "team2.round_type",
  "team2.total_equipment_value",
  "round",
];


function createColumnDefinition(
  key: string,
  maxValue: number,
  t: TFunction<"translation", undefined, "translation">
): MRT_ColumnDef<ITableRound> {
  const cell: MRT_ColumnDef<ITableRound> = {
    id: key,
    accessorFn: (row) => row[key as keyof ITableRound],
    header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
    size: SMALL_COL_SIZE,
    Header: customHeader<ITableRound>(
      t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)
    ),
  };
  console.log(key);
  if (key.endsWith(".total_equipment_value")) {
    cell.Cell = (props: { cell: any; row: { index: number } }) => (
      <FinanceCell
        {...props}
        team={key.startsWith("team1") ? "team1" : "team2"}
        maxValues={maxValue}
      />
    );
  }

  if (key.endsWith(".player_count")) {
    cell.Cell = (props: { cell: any; row: { index: number } }) => (
      <PlayerCountCell
        {...props}
        team={key.startsWith("team1") ? "team1" : "team2"}
      />
    );
  }

  if (key.endsWith(".team1_score") || key.endsWith(".team2_score")) {
    cell.Cell = (props: { cell: any; row: { index: number } }) => (
      <ScoreCell
        {...props}
        team={key.startsWith("team1") ? "team1" : "team2"}
      />
    );
  }

  if (key.endsWith(".round_type")) {
    console.log("round_type INNN");
    cell.Cell = (props: { cell: any }) => (
      <RoundTypeCell {...props} />
    );
  }
  
  if (key === "team1.round_end_reason") {
    cell.Cell = (props: { cell: any }) => (
      <RoundEndReasonCell {...props} />
    );
  }


  return cell;
}


export const useMatchRoundsContent = (
  request: IMatchRoundContentRequest
): IUseMatchRoundsContentResponse => {
  const { t } = useTranslation();
  const [data, setData] = useState<ITableRound[]>([]);
  const [maxValues, setMaxValues] = useState<{ maxValue: number }>({
    maxValue: 0,
  });
  const [playerStats, setPlayerStats] = useState<ITeamMatchResults[] | null>(
    null
  );

  // Define the requests
  const qRoundScorecardRequest = useGetStats<IMatchRound>({
    ...ROUND_SCORECARD_REQUEST,
    queryParams: { match_id: request.match_id },
  });
  const qRoundKillEventsRequest = useGetStats<IRoundKillEvent>({
    ...ROUND_KILL_EVENTS_REQUEST,
    queryParams: { match_id: request.match_id },
  });
  const qRoundEventsRequest = useGetStats<IRoundEvent>({
    ...ROUND_EVENTS_REQUEST,
    queryParams: { match_id: request.match_id },
  });
  const qPlayerStatsRequest = useGetStats<ITeamMatchResults>({
    ...MATCH_TEAM_RESULT_REQUEST,
    queryParams: { match_id: request.match_id },
  });

  // Process the data
  useEffect(() => {
    if (
      qRoundScorecardRequest.status === QueryStatus.success &&
      qRoundKillEventsRequest.status === QueryStatus.success &&
      qRoundEventsRequest.status === QueryStatus.success &&
      qPlayerStatsRequest.status === QueryStatus.success
    ) {
      // Extract the data
      const matchRounds = qRoundScorecardRequest.data?.data?.view_data;
      const killEvents = qRoundKillEventsRequest.data?.data?.view_data;
      const roundEvents = qRoundEventsRequest.data?.data?.view_data;
      const playerStats = qPlayerStatsRequest.data?.data?.view_data;

      const groupedData: ITableRound[] = (matchRounds || []).reduce(
        (acc: ITableRound[], round: IMatchRound) => {
          let foundRound = acc.find((r) => r.round === round.round);
          if (!foundRound) {
            foundRound = {
              round: round.round,
              killEvents: [],
              roundEvents: [],
            };
            acc.push(foundRound);
          }
          foundRound = foundRound!;
          if (
            (round.team === 2 && round.round <= 7) ||
            (round.team === 3 && round.round > 7)
          ) {
            foundRound.team1 = round;
            if (foundRound.killEvents && killEvents) {
              foundRound.killEvents.push(
                ...killEvents.filter(
                  (event: any) => event.round === round.round
                )
              );
            }
            if (foundRound.roundEvents && roundEvents) {
              foundRound.roundEvents.push(
                ...roundEvents.filter(
                  (event: any) => event.round === round.round
                )
              );
            }
          } else {
            foundRound.team2 = round;
          }
          return acc;
        },
        []
      );

      // Calculate maximum values
      const getMaxValues = function () {
        let maxValue = 0;
        const matchRoundsSafe = matchRounds || [];
        for (const round of matchRoundsSafe) {
          maxValue = Math.max(maxValue, round.total_equipment_value);
          maxValue = Math.max(maxValue, round.total_money_spent);
        }

        return { maxValue };
      };
      const calculatedMaxValues = getMaxValues();
      setData(groupedData);
      setMaxValues(calculatedMaxValues);
      setPlayerStats(playerStats ?? null);
    }
  }, [
    qRoundScorecardRequest,
    qRoundKillEventsRequest,
    qRoundEventsRequest,
    qPlayerStatsRequest,
    request,
  ]);

  const getUsernameAndTeam = (steamid: string) => {
    if (playerStats) {
      const player = playerStats.find(
        (player: any) => player.steamid === steamid
      );
      return player
        ? { username: player.usernames, team: player.last_round_team }
        : { username: "Bot", team: "unknown" };
    }
    return { username: "Bot", team: "unknown" };
  };

  const renderDetailPanel = ({ row }: { row: any }) => {
    const { killEvents, roundEvents } = row.original; // Make sure row.original is defined
    const events = [...killEvents, ...roundEvents];
    events.sort((a, b) => a.eventtime - b.eventtime);

    return (
      <DetailPanel events={events} getUsernameAndTeam={getUsernameAndTeam} />
    );
  };

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<ITableRound>[]>(() => {
    const cols: MRT_ColumnDef<ITableRound>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, maxValues.maxValue, t));
    });
    return cols;
  }, [t, maxValues.maxValue]);

  return {
    columns,
    state: qRoundScorecardRequest.status,
    data,
    refetch: qRoundScorecardRequest.refetch,
    renderDetailPanel,
  };
};
