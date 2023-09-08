import { useState, useEffect, useCallback, useMemo } from "react";
import { useGetStats } from "../../../../services";
import { combineQueryStatuses } from "../../../../lib/queries/queriesFunctions";
import { QueryStatus } from "../../../../lib/http-requests";
import { ISteamUser, USERS_REQUEST } from "../../../../services";
import { MATCH_TEAM_RESULT_REQUEST, PLAYER_MATCH_KILL_COUNT } from "../../../../services";
import { ITeamMatchResults, IKillCount } from "../../../../services";
import { IRowData, IMatchKillMatrixRequest, IMatchKillMatrixResponse } from "./interfaces";
import { useTranslation } from "react-i18next";

const COL_HEADERS_BASE_TRANSLATION_KEY = "page.stats.match.killmatrix";
const SMALL_COL_SIZE = 5;

export const useMatchKillMatrixContent = (request: IMatchKillMatrixRequest): IMatchKillMatrixResponse => {
  const { t } = useTranslation();
  const getKillCountsRequest = useMemo(() => {
    const copy = { ...PLAYER_MATCH_KILL_COUNT };
    copy.queryParams = { match_id: request.match_id };
    return copy;
  }, [request]);

  const getPlayerStatsRequest = useMemo(() => {
    const copy = { ...MATCH_TEAM_RESULT_REQUEST };
    copy.queryParams = { match_id: request.match_id };
    return copy;
  }, [request]);

  const [data, setData] = useState<any>(null); // Adjust the type as needed

  // Get the data
  const qKillCountsRequest = useGetStats(getKillCountsRequest);
  const qUsersRequest = useGetStats(USERS_REQUEST);
  const qPlayerStatsRequest = useGetStats(getPlayerStatsRequest); // Define this request similar to getKillCountsRequest

  // Merge the queries statuses into one
  const queriesState = combineQueryStatuses([qKillCountsRequest, qUsersRequest, qPlayerStatsRequest]);

  const refetch = useCallback(() => {
    qKillCountsRequest.refetch();
    qUsersRequest.refetch();
    qPlayerStatsRequest.refetch();
  }, [qKillCountsRequest, qUsersRequest, qPlayerStatsRequest]);

  // Create the data
  useEffect(() => {
    if (queriesState === QueryStatus.success) {
      let killCounts = qKillCountsRequest.data?.data?.view_data;
      const users = qUsersRequest.data?.data?.view_data;
      const playerStats = qPlayerStatsRequest.data?.data?.view_data;
      if (!users || !playerStats || !killCounts) {
        return;
      }

      // Create a lookup table for usernames and player stats
      const usernameLookup: { [steamID: string]: string } = {};
      const statsLookup: { [steamID: string]: ITeamMatchResults } = {};
      users.forEach((user: ISteamUser) => {
        usernameLookup[user.steam_id] = user.user_name;
      });
      playerStats.forEach((stat: ITeamMatchResults) => {
        statsLookup[stat.steamid] = stat;
      });
      // Create a lookup table for the team of each player
      const teamLookup: { [player: string]: string } = {};
      playerStats.forEach((stat: ITeamMatchResults) => {
        const playerName = usernameLookup[stat.steamid] || stat.steamid;
        teamLookup[playerName] = stat.last_round_team;
      });

      // Replace steamid with username in playerOverallStatsExtended
      killCounts.forEach((player: any) => {
        player.killerusername = usernameLookup[player.killer] || player.killer;
        player.victimusername = usernameLookup[player.victim] || player.victim;
      });

      setData({ killCounts, statsLookup, teamLookup });
    }
  }, [queriesState, qKillCountsRequest.data, qUsersRequest.data, qPlayerStatsRequest.data]);

  const players = useMemo(() => {
    if (!data) {
      return [];
    }

    const playerSet = new Set(
      data?.killCounts
        .map((kill: IKillCount) => kill.killerusername)
        .concat(data?.killCounts.map((kill: IKillCount) => kill.victimusername))
    );
    const playerList = Array.from(playerSet) as string[]; // Add type assertion here

    playerList.sort((a: string, b: string) => {
      const teamA = data?.teamLookup[a];
      const teamB = data?.teamLookup[b];

      if (teamA && teamB) {
        return -1 * teamA.localeCompare(teamB);
      } else {
        return 0;
      }
    });

    return playerList;
  }, [data]);

  // Then, create a matrix of kills
  const killMatrix = useMemo(
    () =>
      players?.map((player) => {
        return players?.map((victim) => {
          // Find the kill count between the current player and victim
          const killCountObj = data?.killCounts.find(
            (kill: IKillCount) => kill.killerusername === player && kill.victimusername === victim
          );
          return killCountObj ? killCountObj.kill_count : 0;
        });
      }),
    [players, data]
  );

  // Calculate the minimum and maximum kill counts per player
  const minMaxKillCounts = useMemo(() => {
    const minMax: { [player: string]: [number, number] } = {};
    players?.forEach((player, index) => {
      let min = Infinity;
      let max = -Infinity;
      killMatrix?.forEach((row) => {
        const killCount = row[index];
        min = Math.min(min, killCount);
        max = Math.max(max, killCount);
      });
      minMax[player as any] = [min, max];
    });
    return minMax;
  }, [players, killMatrix]);

  // Flatten the matrix into an array of objects
  const flattenedData = useMemo(
    () =>
      players?.map((player, rowIndex) => {
        const row: IRowData = { "Killer/Victim": player as string, Team: data?.teamLookup[player] || "Unknown" };
        players?.forEach((victim, colIndex) => {
          row[victim as string] = killMatrix ? killMatrix[rowIndex][colIndex] : 0;
        });
        return row;
      }),
    [players, killMatrix, data]
  );

  // Generate the columns dynamically based on the players
  const columns = useMemo(
    () => [
      {
        id: "Killer/Victim",
        header:
          t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.killer`) +
          "    \\    " +
          t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.victim`) +
          "->",
        accessorFn: (row: IRowData) => row["Killer/Victim"],
      },
      { id: "Team", header: "Team", accessorFn: (row: IRowData) => row["Team"] },
      ...(players?.map((player) => ({
        id: player,
        header: player,
        size: SMALL_COL_SIZE,
        accessorFn: (row: IRowData) => row[player as string],
        Cell: ({ cell }: { cell: any }) => {
          const killCount = cell.getValue() as number;
          // Interpolate the kill count to a color
          const [minKillCount, maxKillCount] = minMaxKillCounts[player as any];
          const ratio = Math.min((killCount - minKillCount) / (maxKillCount - minKillCount), 1);
          const red = Math.round(27 + 150 * ratio);
          //const green = Math.round(150 * (1 - ratio));
          const backgroundColor = `rgb(${red},27,27)`;
          return (
            <div
              style={{
                backgroundColor,
                height: "100%",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <span
                style={{
                  padding: "4px",
                  display: "block",
                  color: "#fff",
                  width: "43px",
                  textAlign: "center",
                }}
              >
                {killCount}
              </span>
            </div>
          );
        },
      })) ?? []),
    ],
    [players, minMaxKillCounts, t]
  );

  return {
    state: queriesState,
    columns,
    refetch,
    flattenedData,
  };
};
