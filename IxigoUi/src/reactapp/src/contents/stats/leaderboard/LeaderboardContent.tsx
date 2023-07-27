import React, { useContext } from 'react';
import { SelectedStatsContext } from '../SelectedStatsContext';
import { useMemo } from "react";
import { IconButton, Tooltip, Box } from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { weaponImage } from '../weaponImage';
import PieChartMini from '../PieChartMini';
import { MaterialReactTable , type MRT_ColumnDef } from 'material-react-table';
import {
    useQuery,
  } from 'react-query';
import { Link } from 'react-router-dom';
import { SERVICES_URLS } from "../../../lib/constants/paths";


  interface User {
    steam_id: string;
    user_name: string;
  }

  interface LeaderboardContentProps {
    setSelectedTab: React.Dispatch<React.SetStateAction<number | null>>;
    selectedTab: number | null;
  }

  interface PlayerStats {
    username: string;
    steamid: string;
    mapname: string
    matches: string;
    rounds: number;
  
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
    fkr: number;
    ek: number;
    tk: number;

    mvp: number;
    hltv_rating: number;
    rws: number;
  
    adr: number;
    kast: number;
  
    _1v1: number;
    _1v2: number;
    _1v3: number;
    _1v4: number;
    _1v5: number;
    _1vnp: number;
  
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

  const smallColSize = 5;
  const LeaderboardContent: React.FC<LeaderboardContentProps> = ({ setSelectedTab, selectedTab }) => {

    const { data, isError, isFetching, isLoading, refetch } = useQuery({
        queryKey: ['leaderboard'],
        queryFn: async () => {
            const url1 = new URL(`${SERVICES_URLS["dem-manager"]["get-stats-view"]}PLAYER_OVERALL_STATS_EXTENDED_EXTENDED_CACHE`);
            const url2 = new URL(`${SERVICES_URLS["dem-manager"]["get-stats-view"]}USERS`);
    
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
    
            const playerOverallStatsExtended = jsons[0].view_data;
            const users = jsons[1].view_data;
    
            // Create a lookup table for usernames
            const usernameLookup: { [steamID: string]: string } = {};
            users.forEach((user: User) => {
                usernameLookup[user.steam_id] = user.user_name;
            });
    
            // Replace steamid with username in playerOverallStatsExtended
            playerOverallStatsExtended.forEach((player: any) => {
                player.username = usernameLookup[player.steamid] || player.steamid;
            });
    
            return playerOverallStatsExtended;
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

      const columns = useMemo<MRT_ColumnDef<PlayerStats>[]>(
        () => [
            { accessorKey: "username" as const, header: "Name",
            Cell: ({ cell }: { cell: any }) => {
              const username = cell.getValue() as string;
              const steamid = cell.row.original.steamid;
              const selectedStatsContext = useContext(SelectedStatsContext);
              if (!selectedStatsContext) {
                throw new Error('useContext was called outside of the selectedStatsContext provider');
              }
              const { setSelectedPlayerSteamID, setSelectedSubpage } = selectedStatsContext;
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
            { accessorKey: 'matches' as const, header: 'M', size: smallColSize, Header: createCustomHeader('Matches'), filterVariant: 'text', filterFn: 'greaterThan'},
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
        { accessorKey: "averagewinscore" as const, header: "SCR+" ,size: smallColSize, Header: createCustomHeader("Average Match Score Difference") },
          { accessorKey: "winlossratio" as const, header: "W/L"  ,size: smallColSize, Header: createCustomHeader("Win Loss Ratio")},
          { accessorKey: "kills" as const, header: "KILLS" ,size: smallColSize, Header: createCustomHeader("Total kills") },
          { accessorKey: "rws" as const, header: "RWS" ,size: smallColSize, Header: createCustomHeader("Round Win Share - average ratio of the amount of damage done by the player as a proportion of the total damage done by the winning team in each round")},
          { accessorKey: "kast" as const, header: "KAST" ,size: smallColSize, Header: createCustomHeader("Ratio of rounds with either a kill, an assist, survived or a trade death averaged across all matches.") },
          { accessorKey: "headshot_percentage" as const, header: "HS%",size: smallColSize , Header: createCustomHeader("Headshot kill percentage"), Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
            return <PieChartMini percentage={cell.getValue()} color='darkturquoise' size={22} />;
          }},
          { accessorKey: "_1vnp" as const, header: "1vN%" ,size: smallColSize, Header: createCustomHeader("Total successful clutch rounds (rounds won where a player's last teammate died with players still alive on the opponents team)"), Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
            return <PieChartMini percentage={cell.getValue()} color='darkturquoise' size={22} />;
          }},
          { accessorKey: "ebt" as const, header: "EBT",size: smallColSize , Header: createCustomHeader("Average enemy blind time per match") },
          { accessorKey: "mvp" as const, header: "MVP" ,size: smallColSize, Header: createCustomHeader("Total MVP (Most Valuable Player) rounds")},
          { accessorKey: "assists" as const, header: "A" ,size: smallColSize, Header: createCustomHeader("Total assists") },
          { accessorKey: "deaths" as const, header: "D" ,size: smallColSize , Header: createCustomHeader("Total deaths")},
          { accessorKey: "kdr" as const, header: "K/D",size: smallColSize , Header: createCustomHeader("Kill/Death ratio")},
          { accessorKey: "adr" as const, header: "ADR" ,size: smallColSize, Header: createCustomHeader("Average damage per round") },
          { accessorKey: "kpr" as const, header: "K/R" ,size: smallColSize , Header: createCustomHeader("Average kills per round")},
          { accessorKey: "dpr" as const, header: "D/R" ,size: smallColSize , Header: createCustomHeader("Average deaths per round")},
          { accessorKey: "headshots" as const, header: "HS" ,size: smallColSize , Header: createCustomHeader("Total headshot kills")},
          { accessorKey: "rounds" as const, header: "R"  ,size: smallColSize, Header: createCustomHeader("Number of rounds played")},
          { accessorKey: "wins" as const, header: "Won" ,size: smallColSize, Header: createCustomHeader("Total matches won") },
          { accessorKey: "loss" as const, header: "Lost" ,size: smallColSize, Header: createCustomHeader("Total matches lost") },
          { accessorKey: "ff" as const, header: "FF",size: smallColSize , Header: createCustomHeader("Total friendly fire/team kills")},
          { accessorKey: "ek" as const, header: "EK",size: smallColSize , Header: createCustomHeader("Total entry kills (first kills of the round)")},
          { accessorKey: "bp" as const, header: "BP" ,size: smallColSize , Header: createCustomHeader("Total bombs planted")},
          { accessorKey: "bd" as const, header: "BD",size: smallColSize , Header: createCustomHeader("Total bombs defused")},
          { accessorKey: "hr" as const, header: "HR",size: smallColSize , Header: createCustomHeader("Total hostages rescued")},
          { accessorKey: "_5k" as const, header: "5K",size: smallColSize , Header: createCustomHeader("Total rounds with 5 or more kills")},
          { accessorKey: "_4k" as const, header: "4K",size: smallColSize , Header: createCustomHeader("Total rounds with exactly 4 kills")},
          { accessorKey: "_3k" as const, header: "3K" ,size: smallColSize, Header: createCustomHeader("Total rounds with exactly 3 kills")},
          { accessorKey: "_2k" as const, header: "2K",size: smallColSize , Header: createCustomHeader("Total rounds with exactly 2 kills")},
          { accessorKey: "_1k" as const, header: "1K",size: smallColSize , Header: createCustomHeader("Total rounds with exactly 1 kills")},
          { accessorKey: "tk" as const, header: "TK",size: smallColSize , Header: createCustomHeader("Total trade kills (kills within 5 seconds of a teammate dying)")},
          { accessorKey: "td" as const, header: "TD",size: smallColSize , Header: createCustomHeader("Total trade deaths (deaths within 5 seconds of an opponent dying)")},
          { accessorKey: "tdh" as const, header: "TDH",size: smallColSize , Header: createCustomHeader("Total damage to health") },
          { accessorKey: "tda" as const, header: "TDA",size: smallColSize , Header: createCustomHeader("Total damage to armour") },
          { accessorKey: "ffd" as const, header: "FFD",size: smallColSize , Header: createCustomHeader("Total damage to team mates") },
          { accessorKey: "fbt" as const, header: "FBT" ,size: smallColSize, Header: createCustomHeader("Average team mate blind time per match") },
          { accessorKey: "ud" as const, header: "UD" ,size: smallColSize, Header: createCustomHeader("Average utility damage (HE grenades, molotovs plus incediary) damage per match") },
          { accessorKey: "fkr" as const, header: "EK%" ,size: smallColSize, Header: createCustomHeader("Percentage of first kill rounds") },
          { accessorKey: "fa" as const, header: "FA",size: smallColSize , Header: createCustomHeader("Total flash assist kills - where teammate kills an opponent blinded by your flash") },
          { accessorKey: "_1v1" as const, header: "1v1" ,size: smallColSize, Header: createCustomHeader("Total successful 1v1 clutch rounds (rounds won where a player's last teammate died with 1 player still alive on the opponents team)")},
          { accessorKey: "_1v2" as const, header: "1v2" ,size: smallColSize, Header: createCustomHeader("Total successful 1v1 clutch rounds (rounds won where a player's last teammate died with 2 players still alive on the opponents team)")},
          { accessorKey: "_1v3" as const, header: "1v3" ,size: smallColSize, Header: createCustomHeader("Total successful 1v1 clutch rounds (rounds won where a player's last teammate died with 3 players still alive on the opponents team)")},
          { accessorKey: "_1v4" as const, header: "1v4",size: smallColSize , Header: createCustomHeader("Total successful 1v1 clutch rounds (rounds won where a player's last teammate died with 4 players still alive on the opponents team)")},
          { accessorKey: "_1v5" as const, header: "1v5" ,size: smallColSize, Header: createCustomHeader("Total successful 1v1 clutch rounds (rounds won where a player's last teammate died with 5 or more player still alive on the opponents team)")},
        ],
        [],
      );
      

      return (
        <MaterialReactTable
          columns={columns}
          data={data ?? []} //data is undefined on first render  ?.data ?? []
          initialState={{ showColumnFilters: false,
            density: 'compact',
            pagination: { pageIndex: 0, pageSize: 10 },
            sorting: [{ id: 'hltv_rating', desc: true }], //sort by state by default
            columnPinning: { left: ['username','matches', 'hltv_rating'] },
             }}
          enableColumnFilterModes
          enableDensityToggle={false}
          enablePinning
          enableMultiSort
          enableFilterMatchHighlighting={false}
          enablePagination
          enableGlobalFilter={true}
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
          rowCount={data?.length ?? 0}
          state={{
            isLoading,
            showAlertBanner: isError,
            showProgressBars: isFetching,
          }}
        />
      );
  };

  export default LeaderboardContent;