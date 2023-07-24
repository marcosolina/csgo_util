import React, { useContext } from 'react';
import { SelectedStatsContext } from '../SelectedStatsContext';
import { useMemo } from "react";
import { Tooltip, Box } from '@mui/material';
import { weaponImage } from '../weaponImage';
import { MaterialReactTable } from 'material-react-table';
import PieChartMini from '../PieChartMini';
import {
    useQuery,
  } from 'react-query';
import { Link } from 'react-router-dom';


  interface MatchWeaponProps {
    match_id: number;
  }

  interface User {
    steam_id: string;
    username: string;
  }
  interface WeaponData {
    steamid: string;
    username: string;
    match_id: number;
    kills: number;
    headshotkills: number;
    damage_per_shot: number;
    accuracy: number;
    hits: number;
    weapon: string;
    weapon_img: string;
    total_damage: number;
    damage_per_hit: number;
    headshotkills_percentage: number;
    shots_fired: number;
    headshot_percentage: number;
    chest_hit_percentage: number;
    leg_hit_percentage: number;
    stomach_hit_percentage: number;
    arm_hit_percentage: number;
  }

  const smallColSize = 5;
  const MatchWeaponsContent: React.FC<MatchWeaponProps> = ({ match_id }) => {

    const { data: weaponData, isError, isLoading } = useQuery<WeaponData[], Error>({
        queryKey: ['matchplayerweapon' +match_id],
        queryFn: async () => {
            const url1 = new URL(`https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/MATCH_PLAYER_WEAPON_STATS_CACHE?match_id=${match_id}`);
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

            let weaponData = playerOverallStatsExtended.filter ((p: WeaponData) => p.match_id === match_id);
            // Add the weapon_img property
              weaponData = weaponData.map((wd: WeaponData) => ({
                ...wd,
                weapon_img: wd.weapon,
            }));

            // Create a lookup table for usernames
            const usernameLookup: { [steamID: string]: string } = {};
            users.forEach((user: User) => {
                usernameLookup[user.steam_id] = user.username;
            });
    
            // Replace steamid with username in playerOverallStatsExtended
            playerOverallStatsExtended.forEach((player: any) => {
                player.username = usernameLookup[player.steamid] || player.steamid;
            });
            return weaponData;
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
          { accessorKey: 'weapon' as const, header: 'WEAPON', size: smallColSize, Header: createCustomHeader('Weapon')},
          { accessorKey: 'weapon_img' as const, header: '', size: smallColSize, Header: createCustomHeader('Weapon'), Cell: ({ cell }: { cell: any }) => {
            const weapon = cell.getValue() as string;
            const imageUrl = weaponImage[weapon];
            return imageUrl ? <img src={imageUrl} alt={weapon} style={{  height: '18px' }} /> : null;
          }},
          { accessorKey: 'kills' as const, header: 'K', size: smallColSize, Header: createCustomHeader('Total kills'),aggregationFn: 'sum' as any,
          AggregatedCell: ({ cell }: { cell: any }) => <div>{cell.getValue()}</div>,},
          { accessorKey: 'headshotkills' as const, header: 'HSK', size: smallColSize, Header: createCustomHeader('Total headshot kills') },
          { accessorKey: 'damage_per_shot' as const, header: 'DPS', size: smallColSize, Header: createCustomHeader('Damage per shot') },
          { accessorKey: 'accuracy' as const, header: 'ACC', size: smallColSize, Header: createCustomHeader('Accuracy'), Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
            return <PieChartMini percentage={cell.getValue()} color='darkturquoise' size={22} />;
          },},
          { accessorKey: 'hits' as const, header: 'HITS', size: smallColSize, Header: createCustomHeader('Total hits') },
          { accessorKey: 'total_damage' as const, header: 'TDMG', size: smallColSize, Header: createCustomHeader('Total damage'),aggregationFn: 'sum' as any,
          AggregatedCell: ({ cell }: { cell: any }) => <div>{cell.getValue()}</div>,},
          { accessorKey: 'damage_per_hit' as const, header: 'DPH', size: smallColSize, Header: createCustomHeader('Damage per hit') },
          { accessorKey: 'headshotkills_percentage' as const, header: 'HSKP', size: smallColSize, Header: createCustomHeader('Headshot kills percentage'), Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
            return <PieChartMini percentage={cell.getValue()} color='darkturquoise' size={22} />;
          }},
          { accessorKey: 'shots_fired' as const, header: 'SHOTS', size: smallColSize, Header: createCustomHeader('Shots fired') },
          { accessorKey: 'headshot_percentage' as const, header: 'HHP', size: smallColSize, Header: createCustomHeader('Head hit percentage'), Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
            return <PieChartMini percentage={cell.getValue()} color='darkturquoise' size={22} />;
          }},
          { accessorKey: 'chest_hit_percentage' as const, header: 'CHP', size: smallColSize, Header: createCustomHeader('Chest hit percentage'), Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
            return <PieChartMini percentage={cell.getValue()} color='darkturquoise' size={22} />;
          }},
          { accessorKey: 'stomach_hit_percentage' as const, header: 'SHP', size: smallColSize, Header: createCustomHeader('Stomach hit percentage'), Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
            return <PieChartMini percentage={cell.getValue()} color='darkturquoise' size={22} />;
          }},
          { accessorKey: 'arm_hit_percentage' as const, header: 'AHP', size: smallColSize, Header: createCustomHeader('Arm hit percentage'), Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
            return <PieChartMini percentage={cell.getValue()} color='darkturquoise' size={22} />;
          }},
          { accessorKey: 'leg_hit_percentage' as const, header: 'LHP', size: smallColSize, Header: createCustomHeader('Leg hit percentage'), Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
            return <PieChartMini percentage={cell.getValue()} color='darkturquoise' size={22} />;
          }},
        ],
        [],
      );
    

      return (
        <MaterialReactTable
          columns={columns}
          data={weaponData ?? []} //data is undefined on first render  ?.data ?? []
          initialState={{ showColumnFilters: false,
            density: 'compact',
            sorting: [{ id: 'kills', desc: true },{ id: 'total_damage', desc: true }], //sort by state by default
            columnPinning: { left: ['weapon_img','weapon','username'] },
            expanded: true,
            grouping: ['weapon_img'],
            pagination: { pageIndex: 0, pageSize: 20 },
          }}
             enableGrouping
             enableColumnFilters={true}
             enableColumnFilterModes
             enableSorting={true}
             enableMultiSort
             enableBottomToolbar={true}
             enablePagination={true}
             enableTopToolbar={true}
             enableDensityToggle={false}
             enableGlobalFilter={false}
             enableFullScreenToggle={true}
             enableColumnOrdering={false}
             enableColumnActions={false}
             enableColumnDragging={false}
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
          rowCount={weaponData?.length ?? 0}
          state={{
            isLoading,
            showAlertBanner: isError,
            showProgressBars: isLoading,
          }}
        />
      );
  };

  export default MatchWeaponsContent;