import React, { useContext } from 'react';
import { SelectedStatsContext } from '../SelectedStatsContext';
import { useState, useMemo } from "react";
import { IconButton, Tooltip } from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { weaponImage } from '../weaponImage';
import { MaterialReactTable } from 'material-react-table';
import {
    useQuery,
  } from '@tanstack/react-query';
import { Link } from 'react-router-dom';

  interface User {
    steam_id: string;
    user_name: string;
    // include other properties if any
  }

  interface LeaderboardContentProps {
    setSelectedTab: React.Dispatch<React.SetStateAction<number | null>>;
    selectedTab: number | null;
  }

  const smallColSize = 5;
  const LeaderboardContent: React.FC<LeaderboardContentProps> = ({ setSelectedTab, selectedTab }) => {
    //const [columnFilters, setColumnFilters] = useState([]);
    //const [globalFilter, setGlobalFilter] = useState('');
    //const [sorting, setSorting] = useState([]);
    //const [pagination, setPagination] = useState({
    //    pageIndex: 0,
    //    pagesize: 10,
    //});
  

    const { data, isError, isFetching, isLoading, refetch } = useQuery({
        queryKey: ['leaderboard'],
        queryFn: async () => {
            const url1 = new URL("https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/PLAYER_OVERALL_STATS_EXTENDED");
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

      const columns = useMemo(
        () => [
            { accessorKey: "username", header: "Name",
            Cell: ({ cell }: { cell: any }) => {
              const username = cell.getValue() as string;
              const steamid = cell.row.original.steamid;
              const selectedStatsContext = useContext(SelectedStatsContext);
              if (!selectedStatsContext) {
                throw new Error('useContext was called outside of the selectedStatsContext provider');
              }
              const { selectedPlayerSteamID, setSelectedPlayerSteamID, selectedSubpage, setSelectedSubpage } = selectedStatsContext;
              return (
                <Link 
                  to={`/player/${steamid}`} 
                  onClick={(e) => {
                    e.preventDefault();  // Prevent the link from navigating
                    setSelectedPlayerSteamID(steamid);
                    setSelectedSubpage("player");
                    setSelectedTab(3);
                  }}
                >
                  {username}
                </Link>
              );
            }},
          { accessorKey: "matches", header: "M", Header: createCustomHeader("Matches played") ,size: smallColSize},
          { accessorKey: "hltv_rating", header: "HLTV" , Header: createCustomHeader("HLTV Rating - a weighted sum of K/D ratio, rounds survived ratio and rounds with multiple kills. This is the default rating used in the auto team balancing."),size: smallColSize,
          Cell: ({ cell }: { cell: any }) => {
            const rating = cell.getValue() as number;
            let backgroundColor = '#D0021B';
            if (rating >= 0.85 && rating < 1.1) {
                backgroundColor = '#D39121'; // Amber in hexadecimal
            } else if (rating >= 1.1) {
                backgroundColor = '#7ED321';
            }
            return (
                <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
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
                </div>
            );
        },
          },
          { accessorKey: "rounds", header: "R"  ,size: smallColSize, Header: createCustomHeader("Number of rounds played")},
          { accessorKey: "kills", header: "K" ,size: smallColSize, Header: createCustomHeader("Total kills") },
          { accessorKey: "assists", header: "A" ,size: smallColSize, Header: createCustomHeader("Total assists") },
          { accessorKey: "deaths", header: "D" ,size: smallColSize , Header: createCustomHeader("Total deaths")},
          { accessorKey: "kdr", header: "K/D",size: smallColSize , Header: createCustomHeader("Kill/Death ratio")},
          { accessorKey: "kpr", header: "K/R" ,size: smallColSize , Header: createCustomHeader("Average kills per round")},
          { accessorKey: "dpr", header: "D/R" ,size: smallColSize , Header: createCustomHeader("Average deaths per round")},
          { accessorKey: "adr", header: "ADR" ,size: smallColSize, Header: createCustomHeader("Average damage per round") },
          { accessorKey: "rws", header: "RWS" ,size: smallColSize, Header: createCustomHeader("Round Win Share - average ratio of the amount of damage done by the player as a proportion of the total damage done by the winning team in each round")},
          { accessorKey: "kast", header: "KAST%" ,size: smallColSize, Header: createCustomHeader("Percentage of rounds with either a kill, an assist, survived or a trade death averaged across all matches.") },
          { accessorKey: "headshots", header: "HS" ,size: smallColSize , Header: createCustomHeader("Total headshot kills")},
          { accessorKey: "headshot_percentage", header: "HS%",size: smallColSize , Header: createCustomHeader("Headshot kill percentage")},
          { accessorKey: "ff", header: "FF",size: smallColSize , Header: createCustomHeader("Total friendly fire/team kills")},
          { accessorKey: "ek", header: "EK",size: smallColSize , Header: createCustomHeader("Total entry kills (first kills of the round)")},
          { accessorKey: "bp", header: "BP" ,size: smallColSize , Header: createCustomHeader("Total bombs planted")},
          { accessorKey: "bd", header: "BD",size: smallColSize , Header: createCustomHeader("Total bombs defused")},
          { accessorKey: "hr", header: "HR",size: smallColSize , Header: createCustomHeader("Total hostages rescued")},
          { accessorKey: "mvp", header: "MVP" ,size: smallColSize, Header: createCustomHeader("Total MVP (Most Valuable Player) rounds")},
          { accessorKey: "_5k", header: "5K",size: smallColSize , Header: createCustomHeader("Total rounds with 5 or more kills")},
          { accessorKey: "_4k", header: "4K",size: smallColSize , Header: createCustomHeader("Total rounds with exactly 4 kills")},
          { accessorKey: "_3k", header: "3K" ,size: smallColSize, Header: createCustomHeader("Total rounds with exactly 3 kills")},
          { accessorKey: "_2k", header: "2K",size: smallColSize , Header: createCustomHeader("Total rounds with exactly 2 kills")},
          { accessorKey: "_1k", header: "1K",size: smallColSize , Header: createCustomHeader("Total rounds with exactly 1 kills")},
          { accessorKey: "tk", header: "TK",size: smallColSize , Header: createCustomHeader("Total trade kills (kills within 5 seconds of a teammate dying)")},
          { accessorKey: "td", header: "TD",size: smallColSize , Header: createCustomHeader("Total trade deaths (deaths within 5 seconds of an opponent dying)")},
          { accessorKey: "tdh", header: "TDH",size: smallColSize , Header: createCustomHeader("Total damage to health") },
          { accessorKey: "tda", header: "TDA",size: smallColSize , Header: createCustomHeader("Total damage to armour") },
          { accessorKey: "ffd", header: "FFD",size: smallColSize , Header: createCustomHeader("Total damage to team mates") },
          { accessorKey: "ebt", header: "EBT",size: smallColSize , Header: createCustomHeader("Average enemy blind time per match") },
          { accessorKey: "fbt", header: "FBT" ,size: smallColSize, Header: createCustomHeader("Average team mate blind time per match") },
          { accessorKey: "ud", header: "UD" ,size: smallColSize, Header: createCustomHeader("Average utility damage (HE grenades, molotovs plus incediary) damage per match") },
          { accessorKey: "fa", header: "FA",size: smallColSize , Header: createCustomHeader("Total flash assist kills - where teammate kills an opponent blinded by your flash") },
          { accessorKey: "_1v1", header: "1v1" ,size: smallColSize, Header: createCustomHeader("Total successful 1v1 clutch rounds (rounds won where a player's last teammate died with 1 player still alive on the opponents team)")},
          { accessorKey: "_1v2", header: "1v2" ,size: smallColSize, Header: createCustomHeader("Total successful 1v1 clutch rounds (rounds won where a player's last teammate died with 2 players still alive on the opponents team)")},
          { accessorKey: "_1v3", header: "1v3" ,size: smallColSize, Header: createCustomHeader("Total successful 1v1 clutch rounds (rounds won where a player's last teammate died with 3 players still alive on the opponents team)")},
          { accessorKey: "_1v4", header: "1v4",size: smallColSize , Header: createCustomHeader("Total successful 1v1 clutch rounds (rounds won where a player's last teammate died with 4 players still alive on the opponents team)")},
          { accessorKey: "_1v5", header: "1v5" ,size: smallColSize, Header: createCustomHeader("Total successful 1v1 clutch rounds (rounds won where a player's last teammate died with 5 or more player still alive on the opponents team)")},
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
          rowCount={data?.length ?? 0}
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

  export default LeaderboardContent;