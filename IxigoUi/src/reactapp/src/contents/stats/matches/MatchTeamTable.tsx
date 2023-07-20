import React, { useContext } from 'react';
import { SelectedStatsContext } from '../SelectedStatsContext';
import { useState, useMemo } from "react";
import { IconButton, Tooltip } from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { weaponImage } from '../weaponImage';
import { MaterialReactTable } from 'material-react-table';
import PieChartMini from '../PieChartMini';
import {
    useQuery,
  } from 'react-query';
import { Link } from 'react-router-dom';

interface TeamMatchProps {
  match_id: number;
  team: string;
}

interface TeamMatch {
    usernames: string;
    kills: number;
    deaths: number;
    assists: number;
    _1k: number;
    _2k: number;
    _3k: number;
    _4k: number;
    _5k: number;
    headshots: number;
    headshot_percentage: number;
    mvp: number;
    hltv_rating: number;
    adr: number;
    kast: number;
    kasttotal: number;
    score: number;
    rws: number;
    rwstotal: number;
    match_date: string;
    match_id: number;
    team: number;
    ff: number;
    bd: number;
    _1v5: number;
    _1v4: number;
    _1v3: number;
    _1v2: number;
    _1v1: number;
    hr: number;
    bp: number;
    ud: number;
    last_round_team: string;
    rounds_on_team2: number;
    rounds_on_team1: number;
    ffd: number;
    roundsplayed: number;
    ek: number;
    dpr: number;
    kpr: number;
    steamid: string;
    td: number;
    tda: number;
    ebt: number;
    tk: number;
    kdr: number;
    tdh: number;
    fbt: number;
    fa: number;
  }
  

const smallColSize = 5;
const MatchTeamTable: React.FC<TeamMatchProps> = ({ match_id, team }) => {

  const { data: matchData, isError, isLoading } = useQuery<TeamMatch[], Error>({
      queryKey: ['matchteam' + team],
      queryFn: async () => {
          const url1 = new URL("https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/PLAYER_MATCH_STATS_EXTENDED");
  
          const responses = await Promise.all([
              fetch(url1.href),
          ]);
  
          const jsons = await Promise.all(responses.map(response => {
              if (!response.ok) {
                  throw new Error('Network response was not ok');
              }
              return response.json();
          }));
  
          const playerMatchStatsExtended = jsons[0].view_data;
          console.log(match_id);
          console.log(team);
          let matchData = playerMatchStatsExtended.filter ((p: TeamMatch) => p.match_id === match_id && p.last_round_team === team);
          console.log(matchData);
          return matchData;
      },
      keepPreviousData: true,
  });

  function createCustomHeader(tooltipText: string) {
      return ({ column }: { column: any }) => (
        <Tooltip title={tooltipText}>
          <span>{column.columnDef.header}</span>
        </Tooltip>
      );
    }

    const columns = useMemo(
        () => [
          { accessorKey: 'usernames' as const, header: 'PLAYER', minSize: 100, Header: createCustomHeader('Player') },
          { accessorKey: 'kills' as const, header: 'K', size: smallColSize, Header: createCustomHeader('Total kills') },
          { accessorKey: 'deaths' as const, header: 'D', size: smallColSize, Header: createCustomHeader('Total deaths') },
          { accessorKey: 'assists' as const, header: 'A', size: smallColSize, Header: createCustomHeader('Total assists') },
          { accessorKey: 'score' as const, header: 'SCR', size: smallColSize, Header: createCustomHeader('Score') },
          { accessorKey: 'rws' as const, header: 'RWS', size: smallColSize, Header: createCustomHeader('Round Win Share') },
        
          { accessorKey: 'headshots' as const, header: 'HS', size: smallColSize, Header: createCustomHeader('Headshots') },
          { accessorKey: 'headshot_percentage' as const, header: 'HS%', size: smallColSize, Header: createCustomHeader('Headshot Percentage'), Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
            return <PieChartMini percentage={cell.getValue()} color='darkturquoise' size={22} />;
          } },
          { accessorKey: 'mvp' as const, header: 'MVP', size: smallColSize, Header: createCustomHeader('Most Valuable Player') },
          { accessorKey: "hltv_rating" as const, header: "HLTV" , Header: createCustomHeader("HLTV Rating - a weighted sum of K/D ratio, rounds survived ratio and rounds with multiple kills. This is the default rating used in the auto team balancing."),size: smallColSize,
          Cell: ({ cell }: { cell: any }) => {
            const rating = cell.getValue() as number;
            let backgroundColor = '#D0021B';
            if (rating >= 0.85 && rating < 1.1) {
                backgroundColor = '#D39121'; // Amber in hexadecimal
            } else if (rating >= 1.1) {
                backgroundColor = '#7ED321';
            }
            return (
                    <span style={{
                        borderRadius: '4px',
                        padding: '2px',
                        display: 'block',
                        color: '#fff',
                        width: '43px',
                        textAlign: 'center',
                        backgroundColor: backgroundColor
                    }}>
                        {rating}
                    </span>
            );
            },
          },
          { accessorKey: 'adr' as const, header: 'ADR', size: smallColSize, Header: createCustomHeader('Average Damage per Round') },
          { accessorKey: '_1k' as const, header: '1K', size: smallColSize, Header: createCustomHeader('One Kill') },
          { accessorKey: '_2k' as const, header: '2K', size: smallColSize, Header: createCustomHeader('Two Kills') },
          { accessorKey: '_3k' as const, header: '3K', size: smallColSize, Header: createCustomHeader('Three Kills') },
          { accessorKey: '_4k' as const, header: '4K', size: smallColSize, Header: createCustomHeader('Four Kills') },
          { accessorKey: '_5k' as const, header: '5K', size: smallColSize, Header: createCustomHeader('Five Kills') },
          //... Add more columns as necessary
        ],
        [],
    );
    

    return (
      <MaterialReactTable
        columns={columns}
        data={matchData ?? []} 
        initialState={{ showColumnFilters: false,
            columnVisibility: { _1k: false },
            density: 'compact',
            sorting: [{ id: 'hltv_rating', desc: true }], //sort by state by default
            columnPinning: { left: ['usernames'] },
             }}
             enableColumnActions={false}
             enableColumnFilters={false}
             enableSorting={true}
             enableTopToolbar={true}
             enableBottomToolbar={false}
             enableDensityToggle={true}
             enableGlobalFilter={false}
             enableFullScreenToggle={false}
             enableHiding={true}
             muiTableBodyRowProps={{ hover: false }}
          muiToolbarAlertBannerProps={
            isError
              ? {
                  color: 'error',
                  children: 'Error loading data',
                }
              : undefined
          }
          rowCount={matchData?.length ?? 0}
          state={{
            isLoading,
            showAlertBanner: isError,
            showProgressBars: isLoading,
          }}
      />
    );
};

export default MatchTeamTable;
