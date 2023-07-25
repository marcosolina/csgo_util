import React from 'react';
import { useMemo } from "react";
import { IconButton, Tooltip } from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { MaterialReactTable } from 'material-react-table';
import { useQuery } from 'react-query';

interface User {
  steam_id: string;
  user_name: string;
}

interface KillCount {
  match_id: number;
  kill_count: number;
  victim: string;
  killer: string;
  killerusername: string;
  victimusername: string;
}

interface PlayerStats {
  steamid: string;
  kills: number;
  last_round_team: string;
}

interface KillMatrixContentProps {
  match_id: number;
}

interface RowData {
  "Killer/Victim": string;
  [player: string]: string | number;
}

const smallColSize = 5;

const MatchKillMatrixContent: React.FC<KillMatrixContentProps> = ({ match_id }) => {
  const { data, isError, isFetching, isLoading, refetch } = useQuery({
    queryKey: ['killmatrix' + match_id],
    queryFn: async () => {
        const url1 = new URL(`https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/PLAYER_MATCH_KILL_COUNT_CACHE?match_id=${match_id}`);
        const url2 = new URL("https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/USERS");
        const url3 = new URL(`https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/PLAYER_MATCH_STATS_EXTENDED_CACHE?match_id=${match_id}`);

        const responses = await Promise.all([
            fetch(url1.href),
            fetch(url2.href),
            fetch(url3.href),
        ]);

        const jsons = await Promise.all(responses.map(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        }));

        let killCounts = jsons[0].view_data;
        const users = jsons[1].view_data;
        const playerStats = jsons[2].view_data;

        // Filter killCounts by match_id
        killCounts = killCounts.filter((killCount: any) => killCount.match_id === match_id);

        // Create a lookup table for usernames and player stats
        const usernameLookup: { [steamID: string]: string } = {};
        const statsLookup: { [steamID: string]: PlayerStats } = {};
        users.forEach((user: User) => {
            usernameLookup[user.steam_id] = user.user_name;
        });
        playerStats.forEach((stat: PlayerStats) => {
            statsLookup[stat.steamid] = stat;
        });
        // Create a lookup table for the team of each player
        const teamLookup: { [player: string]: string } = {};
        playerStats.forEach((stat: PlayerStats) => {
            const playerName = usernameLookup[stat.steamid] || stat.steamid;
            teamLookup[playerName] = stat.last_round_team;
        });

        // Replace steamid with username in playerOverallStatsExtended
        killCounts.forEach((player: any) => {
            player.killerusername = usernameLookup[player.killer] || player.killer;
            player.victimusername = usernameLookup[player.victim] || player.victim;
        });

        return {killCounts, statsLookup, teamLookup} ;
    },
    keepPreviousData: true,
});

const players = useMemo(() => {
  if (!data) {
    return [];
  }

  const playerSet = new Set(data?.killCounts.map((kill: KillCount) => kill.killerusername).concat(data?.killCounts.map((kill: KillCount) => kill.victimusername)));
  const playerList = Array.from(playerSet) as string[];  // Add type assertion here

  playerList.sort((a: string, b: string) => {
    const teamA = data?.teamLookup[a];
    const teamB = data?.teamLookup[b];
    
    if (teamA && teamB) {
      return -1*teamA.localeCompare(teamB);
    } else {
      return 0;
    }
  });

  return playerList;
}, [data]);


// Then, create a matrix of kills
const killMatrix = useMemo(() => players?.map(player => {
  return players?.map(victim => {
    // Find the kill count between the current player and victim
    const killCountObj = data?.killCounts.find((kill: KillCount) => kill.killerusername === player && kill.victimusername === victim);
    return killCountObj ? killCountObj.kill_count : 0;
  });
}), [players, data]);


  // Calculate the minimum and maximum kill counts per player
  const minMaxKillCounts = useMemo(() => {
    const minMax: { [player: string]: [number, number] } = {};
    players?.forEach((player, index) => {
      let min = Infinity;
      let max = -Infinity;
      killMatrix?.forEach(row => {
        const killCount = row[index];
        min = Math.min(min, killCount);
        max = Math.max(max, killCount);
      });
      minMax[player as any] = [min, max];
    });
    return minMax;
  }, [players, killMatrix]);


// Flatten the matrix into an array of objects
const flattenedData = useMemo(() => players?.map((player, rowIndex) => {
  const row: RowData = { "Killer/Victim": player as string, "Team": data?.teamLookup[player] || "Unknown" };
  players?.forEach((victim, colIndex) => {
    row[victim as string] = killMatrix ? killMatrix[rowIndex][colIndex] : 0;
  });
  return row;
}), [players, killMatrix, data]);



// Generate the columns dynamically based on the players
const columns = useMemo(() => [
  { id: "Killer/Victim", header: "Killer    \\    Victim->", accessorFn: (row: RowData) => row["Killer/Victim"] },
  { id: "Team", header: "Team", accessorFn: (row: RowData) => row["Team"] },
  ...(players?.map(player => ({
    id: player,
    header: player,
    size: smallColSize,
    accessorFn: (row: RowData) => row[player as string],
    Cell: ({ cell }: { cell: any }) => {
      const killCount = cell.getValue() as number;
      // Interpolate the kill count to a color
      const [minKillCount, maxKillCount] = minMaxKillCounts[player as any];
      const ratio = Math.min((killCount - minKillCount) / (maxKillCount - minKillCount), 1);
      const red = Math.round(27+150 * ratio);
      //const green = Math.round(150 * (1 - ratio));
      const backgroundColor = `rgb(${red},27,27)`;
      return (
        <div style={{ backgroundColor, height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <span style={{
              padding: '4px',
              display: 'block',
              color: '#fff',
              width: '43px',
              textAlign: 'center',
          }}>
            {killCount}
          </span>
        </div>
      );
    },
  })) ?? [])
], [players, minMaxKillCounts]);




  return (
    <MaterialReactTable
      columns={columns as any} // cast to 'any' for now
      data={flattenedData ?? []}
      initialState={{ showColumnFilters: false,
        density: 'compact',
        columnVisibility: {
          Team: false,
        },
        sorting: [{ id: 'Team', desc: false }],
        pagination: { pageIndex: 0, pageSize: 10 },
        columnPinning: { left: ['Killer/Victim'] },
         }}
      enableColumnFilterModes
      enablePinning
      enableMultiSort
      enableDensityToggle={false}
      enablePagination
      muiToolbarAlertBannerProps={
        isError
          ? {
              color: 'error',
              children: 'Error loading data',
            }
          : undefined
      }
      renderTopToolbarCustomActions={() => (
        <Tooltip arrow title="Refresh Data">
          <IconButton onClick={() => refetch()}>
            <RefreshIcon />
          </IconButton>
        </Tooltip>
      )}
      rowCount={flattenedData?.length ?? 0}
      state={{
        isLoading,
        showAlertBanner: isError,
        showProgressBars: isFetching,
      }}
    />
  );
};

export default MatchKillMatrixContent;