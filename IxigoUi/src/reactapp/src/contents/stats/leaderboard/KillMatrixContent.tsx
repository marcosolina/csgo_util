import React, { useContext } from 'react';
import { SelectedStatsContext } from '../SelectedStatsContext';
import { useState, useMemo } from "react";
import { IconButton, Tooltip } from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { weaponImage } from '../weaponImage';
import { MaterialReactTable } from 'material-react-table';
import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';

interface User {
  steam_id: string;
  user_name: string;
}

interface KillCount {
  kill_count: number;
  victim: string;
  killer: string;
  killerusername: string;
  victimusername: string;
}

interface KillMatrixContentProps {
  setSelectedTab: React.Dispatch<React.SetStateAction<number | null>>;
  selectedTab: number | null;
}

interface RowData {
  "Killer/Victim": string;
  [player: string]: string | number;
}

const smallColSize = 5;

const KillMatrixContent: React.FC<KillMatrixContentProps> = ({ setSelectedTab, selectedTab }) => {
  const { data, isError, isFetching, isLoading, refetch } = useQuery({
    queryKey: ['killmatrix'],
    queryFn: async () => {
        const url1 = new URL("https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/player_kill_count");
        const url2 = new URL("https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/USERS");

        const responses = await Promise.all([
            fetch(url1.href),
            fetch(url2.href),
        ]);

        const jsons = await Promise.all(responses.map(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        }));

        const killCounts = jsons[0].view_data;
        const users = jsons[1].view_data;

        // Create a lookup table for usernames
        const usernameLookup: { [steamID: string]: string } = {};
        users.forEach((user: User) => {
            usernameLookup[user.steam_id] = user.user_name;
        });

        // Replace steamid with username in playerOverallStatsExtended
        killCounts.forEach((player: any) => {
            player.killerusername = usernameLookup[player.killer] || player.killer;
            player.victimusername = usernameLookup[player.victim] || player.victim;
        });

        return killCounts;
    },
    keepPreviousData: true,
});

  // First, create a list of all unique players
  const players = useMemo(() => Array.from(new Set(data?.map((kill: KillCount) => kill.killerusername).concat(data?.map((kill: KillCount) => kill.victimusername)))), [data]);

  // Then, create a matrix of kills
  const killMatrix = useMemo(() => players?.map(player => {
    return players?.map(victim => {
      // Find the kill count between the current player and victim
      const killCountObj = data?.find((kill: KillCount) => kill.killerusername === player && kill.victimusername === victim);
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
    const row: RowData = { "Killer/Victim": player as string }; // type assertion here
    players?.forEach((victim, colIndex) => {
      row[victim as string] = killMatrix ? killMatrix[rowIndex][colIndex] : 0;
    });
    return row;
  }), [players, killMatrix]);


// Generate the columns dynamically based on the players
const columns = useMemo(() => [
  { id: "Killer/Victim", header: "Killer/Victim", accessorFn: (row: RowData) => row["Killer/Victim"] },
  ...(players?.map(player => ({
    id: player,
    header: player,
    size: smallColSize,
    minSize: smallColSize,
    accessorFn: (row: RowData) => row[player as string],
    Cell: ({ cell }: { cell: any }) => {
      const killCount = cell.getValue() as number;
      // Interpolate the kill count to a color
      const [minKillCount, maxKillCount] = minMaxKillCounts[player as any];
      const ratio = Math.min((killCount - minKillCount) / (maxKillCount - minKillCount), 1);
      const red = Math.round(150 * ratio);
      const green = Math.round(150 * (1 - ratio));
      const backgroundColor = `rgb(${red},${green},0)`;
      return (
        <div style={{ backgroundColor, height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <span style={{
              borderRadius: '4px',
              padding: '2px',
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
], [players]);




  return (
    <MaterialReactTable
      columns={columns as any} // cast to 'any' for now
      data={flattenedData ?? []}
      initialState={{ showColumnFilters: false,
        density: 'compact',
        pagination: { pageIndex: 0, pageSize: 10 },
        columnPinning: { left: ['Killer/Victim'] },
         }}
      //enableColumnOrdering
      //enableGrouping
      enableColumnFilterModes
      enablePinning
      enableMultiSort
      //enableColumnDragging
      enablePagination
      muiToolbarAlertBannerProps={
        isError
          ? {
              color: 'error',
              children: 'Error loading data',
            }
          : undefined
      }
      //onColumnFiltersChange={setColumnFilters}
      //onGlobalFilterChange={setGlobalFilter}
      //onPaginationChange={(newPagination) => setPagination(newPagination)}
      //onSortingChange={setSorting}
      renderTopToolbarCustomActions={() => (
        <Tooltip arrow title="Refresh Data">
          <IconButton onClick={() => refetch()}>
            <RefreshIcon />
          </IconButton>
        </Tooltip>
      )}
      rowCount={flattenedData?.length ?? 0}
      state={{
        //columnFilters,
        //globalFilter,
        isLoading,
        //pagination,
        showAlertBanner: isError,
        showProgressBars: isFetching,
        //sorting,
      }}
    />
  );
};

export default KillMatrixContent;