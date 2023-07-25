import React, { useContext } from 'react';
import { SelectedStatsContext } from '../SelectedStatsContext';
import { useMemo } from "react";
import { IconButton, Tooltip, Box } from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { weaponImage } from '../weaponImage';
import { MaterialReactTable , type MRT_ColumnDef } from 'material-react-table';
import PieChartMini from '../PieChartMini';
import {
    useQuery,
  } from 'react-query';
import { Link } from 'react-router-dom';

interface MapLeaderboardsContentProps {
  mapName: string;
}

interface PlayerMatch {
    username: string;
    steamid: string;
    mapname: string
    matches: string;
  
    first_weapon: string;
    second_weapon: string;
    wins: number;
    loss: number;
  
    kills: number;
    deaths: number;
    assists: number;
    kpr: number;
    dpr: number;
    kdr: number;
    ek: number;
    tk: number;

    mvp: number;
    hltv_rating: number;
  
    adr: number;
    kast: number;
  
    _1v1: number;
    _1v2: number;
    _1v3: number;
    _1v4: number;
    _1v5: number;
  
    _1k: number;
    _2k: number;
    _3k: number;
    _4k: number;
    _5k: number;
  
    headshots: number;
    headshot_percentage: number;
    hr: number;
  
    bp: number;
    ud: number;
    ffd: number;
  
    winlossratio: number;
    averagewinscore: number;
  
    td: number;
    tda: number;
    tdh: number;
  
    fbt: number;
    fa: number;
    ebt: number;
  
    ff: number;
    bd: number;
  }

  interface User {
    steam_id: string;
    user_name: string;
    // include other properties if any
  }
  
  

const smallColSize = 5;
const MapLeaderboardsContent: React.FC<MapLeaderboardsContentProps> = ({ mapName }) => {

  const { data: matchData, isError, isLoading, refetch } = useQuery<PlayerMatch[], Error>({
      queryKey: ['mapleaderboard' + mapName],
      queryFn: async () => {
          const url1 = new URL(`https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/PLAYER_MAP_STATS_EXTENDED_EXTENDED?mapname=${mapName}`);
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
  
          const playerMapStatsExtended = jsons[0].view_data;
          const users = jsons[1].view_data;
    
            // Create a lookup table for usernames
            const usernameLookup: { [steamID: string]: string } = {};
            users.forEach((user: User) => {
                usernameLookup[user.steam_id] = user.user_name;
            });
    
            // Replace steamid with username in playerOverallStatsExtended
            playerMapStatsExtended.forEach((player: any) => {
                player.username = usernameLookup[player.steamid] || player.steamid;
            });

          let matchData = playerMapStatsExtended.filter ((p: PlayerMatch) => p.mapname === mapName);
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

    const columns = useMemo<MRT_ColumnDef<PlayerMatch>[]>(
        () => [
          { accessorKey: "username" as const, header: "Name",
          Cell: ({ cell }: { cell: any }) => {
            const username = cell.getValue() as string;
            const steamid = cell.row.original.steamid;
            const selectedStatsContext = useContext(SelectedStatsContext);
            if (!selectedStatsContext) {
              throw new Error('useContext was called outside of the selectedStatsContext provider');
            }
            const { selectedPlayerSteamID, setSelectedPlayerSteamID, selectedSubpage, setSelectedSubpage } = selectedStatsContext;
            return (
              <Box 
                component={Link}
                to={`/player/${steamid}`}
                sx={{
                  color: 'white',
                  fontWeight: 'bold',
                  textDecoration: 'none',
                  cursor: 'pointer',
                  '&:hover': {
                    textDecoration: 'underline',
                  },
                }}
                onClick={(e) => {
                  e.preventDefault();  // Prevent the link from navigating
                  setSelectedPlayerSteamID(steamid);
                  setSelectedSubpage("player");
                }}
              >
                {username}
              </Box>
            );
          }},
          { accessorKey: 'matches' as const, header: 'M', size: smallColSize, Header: createCustomHeader('Matches'), filterVariant: 'text',
          filterFn: 'between'},
          { accessorKey: "first_weapon" as const, header: "W1" ,size: smallColSize, Header: createCustomHeader("Primary weapon - most kills"), 
            Cell: ({ cell }: { cell: any }) => {
              const weapon = cell.getValue() as string;
              const imageUrl = weaponImage[weapon];
              return imageUrl ? <img src={imageUrl} alt={weapon} style={{  height: '18px' }} /> : null;
            }
          },
          { accessorKey: "second_weapon" as const, header: "W2" ,size: smallColSize, Header: createCustomHeader("Secondary weapon"),
            Cell: ({ cell }: { cell: any }) => {
              const weapon = cell.getValue() as string;
              const imageUrl = weaponImage[weapon];
              return imageUrl ? <img src={imageUrl} alt={weapon} style={{  height: '18px' }} /> : null;
            }
          },
          { accessorKey: 'wins' as const, header: 'W', size: smallColSize, Header: createCustomHeader('Wins') },
          { accessorKey: 'loss' as const, header: 'L', size: smallColSize, Header: createCustomHeader('Loss') }, 
          { accessorKey: 'winlossratio' as const, header: 'W/L', size: smallColSize, Header: createCustomHeader('Win/Loss Ratio') },
          { accessorKey: 'averagewinscore' as const, header: 'SCR+', size: smallColSize, Header: createCustomHeader('Average Win Score') },
          { accessorKey: 'adr' as const, header: 'ADR', size: smallColSize, Header: createCustomHeader('Average Damage per Round') },
          { accessorKey: 'kpr' as const, header: 'KPR', size: smallColSize, Header: createCustomHeader('Kills Per Round') },
          { accessorKey: 'dpr' as const, header: 'DPR', size: smallColSize, Header: createCustomHeader('Deaths Per Round') },
          { accessorKey: 'kdr' as const, header: 'K/D', size: smallColSize, Header: createCustomHeader('Kill/Death Ratio') },
          { accessorKey: 'headshot_percentage' as const, header: 'HS%', size: smallColSize, Header: createCustomHeader('Headshot Percentage'), Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
            return <PieChartMini percentage={cell.getValue()} color='darkturquoise' size={22} />;
          } },
          { accessorKey: "hltv_rating" as const, header: "HLTV" , Header: createCustomHeader("HLTV Rating - a weighted sum of K/D ratio, rounds survived ratio and rounds with multiple kills. This is the default rating used in the auto team balancing."),size: smallColSize,
          Cell: ({ cell }: { cell: any }) => {
            const rating = cell.getValue() as number;
            let backgroundColor = '#D0021B';
            if (rating >= 0.85 && rating < 1.1) {
                backgroundColor = '#D39121'; // Amber in hexadecimal
            } else if (rating >= 1.1 && rating < 1.5) {
                backgroundColor = '#7ED321';
            } else if (rating >= 1.5){
              backgroundColor = '#90caf9';
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
          { accessorKey: 'mvp' as const, header: 'MVP', size: smallColSize, Header: createCustomHeader('Most Valuable Player') },
          { accessorKey: 'kills' as const, header: 'K', size: smallColSize, Header: createCustomHeader('Total kills') },
          { accessorKey: 'deaths' as const, header: 'D', size: smallColSize, Header: createCustomHeader('Total deaths') },
          { accessorKey: 'assists' as const, header: 'A', size: smallColSize, Header: createCustomHeader('Total assists') },
          { accessorKey: 'headshots' as const, header: 'HS', size: smallColSize, Header: createCustomHeader('Headshots') },
          { accessorKey: 'hr' as const, header: 'HR', size: smallColSize, Header: createCustomHeader('Hostages Rescued') },
          { accessorKey: 'bp' as const, header: 'BP', size: smallColSize, Header: createCustomHeader('Bombs Planted') },
          { accessorKey: 'bd' as const, header: 'BD', size: smallColSize, Header: createCustomHeader('Bombs Defused') },
          { accessorKey: 'ud' as const, header: 'UD', size: smallColSize, Header: createCustomHeader('Utility Damage') },
          { accessorKey: 'ffd' as const, header: 'FFD', size: smallColSize, Header: createCustomHeader('Friendly Fire Damage') },
          { accessorKey: 'td' as const, header: 'TD', size: smallColSize, Header: createCustomHeader('Trade Deaths') },
          { accessorKey: 'tda' as const, header: 'TDA', size: smallColSize, Header: createCustomHeader('Total Damage Armour') },
          { accessorKey: 'tdh' as const, header: 'TDH', size: smallColSize, Header: createCustomHeader('Total Damage Health') },
          { accessorKey: 'fa' as const, header: 'FA', size: smallColSize, Header: createCustomHeader('Flash Assists') },
          { accessorKey: 'ebt' as const, header: 'EBT', size: smallColSize, Header: createCustomHeader('Enemy Blind Time') },
          { accessorKey: 'fbt' as const, header: 'FBT', size: smallColSize, Header: createCustomHeader('Friendly Blind Time') },
          { accessorKey: 'ek' as const, header: 'EK', size: smallColSize, Header: createCustomHeader('Entry Kills') },
          { accessorKey: 'tk' as const, header: 'TK', size: smallColSize, Header: createCustomHeader('Trade Kills') },
          { accessorKey: '_1k' as const, header: '1K', size: smallColSize, Header: createCustomHeader('One Kill') },
          { accessorKey: '_2k' as const, header: '2K', size: smallColSize, Header: createCustomHeader('Two Kills') },
          { accessorKey: '_3k' as const, header: '3K', size: smallColSize, Header: createCustomHeader('Three Kills') },
          { accessorKey: '_4k' as const, header: '4K', size: smallColSize, Header: createCustomHeader('Four Kills') },
          { accessorKey: '_5k' as const, header: '5K', size: smallColSize, Header: createCustomHeader('Five Kills') },
          { accessorKey: '_1v1' as const, header: '1v1', size: smallColSize, Header: createCustomHeader('1v1 Clutches') },
          { accessorKey: '_1v2' as const, header: '1v2', size: smallColSize, Header: createCustomHeader('1v2 Clutches') },
          { accessorKey: '_1v3' as const, header: '1v3', size: smallColSize, Header: createCustomHeader('1v3 Clutches') },
          { accessorKey: '_1v4' as const, header: '1v4', size: smallColSize, Header: createCustomHeader('1v4 Clutches') },
          { accessorKey: '_1v5' as const, header: '1v5', size: smallColSize, Header: createCustomHeader('1v5 Clutches') },
        ],
        [],
    );
    

    return (
      <MaterialReactTable
        columns={columns}
        data={matchData ?? []} 
        initialState={{ showColumnFilters: false,
            columnVisibility: {
                steamid: false,
              },
            density: 'compact',
            sorting: [{ id: 'hltv_rating', desc: true }], //sort by state by default
            pagination: { pageIndex: 0, pageSize: 20 },
            columnPinning: { left: ['username'] },
             }}
             enableColumnActions={false}
             enableColumnFilters={true}
             enableColumnFilterModes
             enableFilterMatchHighlighting={false}
             enableDensityToggle={false}
             enableSorting={true}
             enableTopToolbar={true}
             enableBottomToolbar={true}
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
          renderTopToolbarCustomActions={() => (
            <Tooltip arrow title="Refresh Data">
              <IconButton onClick={() => refetch()}>
                <RefreshIcon />
              </IconButton>
            </Tooltip>
          )}
          rowCount={matchData?.length ?? 0}
          state={{
            isLoading,
            showAlertBanner: isError,
            showProgressBars: isLoading,
          }}
      />
    );
};

export default MapLeaderboardsContent;

