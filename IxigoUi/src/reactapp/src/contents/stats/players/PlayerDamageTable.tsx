import React from 'react';
import { useQuery } from 'react-query';
import { MaterialReactTable } from 'material-react-table';
import { useMemo } from "react";
import { Typography, Box, LinearProgress, Tooltip } from '@mui/material';


// Define your types for the player data
interface PlayerData {
    steamid: string;
    adr: number;
    tdh: number;
    tda: number;
    ffd: number;
    rounds: number;
}

interface RadarChartProps {
    steamid: string;
}

const PlayerDamageTable: React.FC<RadarChartProps> = ({ steamid }) => {
    const { data: playerData, isError, isLoading, isFetching } = useQuery<PlayerData[], Error>({
        queryKey: ['leaderboard'],
        queryFn: async () => {
            const url1 = new URL("https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/PLAYER_OVERALL_STATS_EXTENDED_EXTENDED");
    
            const responses = await Promise.all([
                fetch(url1.href),
            ]);
    
            const jsons = await Promise.all(responses.map(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            }));
    
            const playerData = jsons[0].view_data.map((player: any) => {
                return {
                    ...player,
                } as PlayerData;
            });

            return playerData;
        },
        keepPreviousData: true,
    });

    const player = useMemo(() => {
        if (playerData) {
            return playerData.find(p => p.steamid === steamid);
        }
        return undefined;
    }, [playerData, steamid]);

    const maxAdr = useMemo(() => {
        if (playerData) {
            return Math.max(...playerData.map(p => p.adr));
        }
        return undefined;
    }, [playerData, steamid]);

    const columns = useMemo(
        () => [
            {accessorKey: 'label' as const, header: '', size: 5},
            {accessorKey: 'value' as const, header: '', size: 5},
        ],
        [],
    );

    // Define your table data
    const tableData = useMemo(() => {
        if (!player) return [];
        return [
            {
                'label': `Damage Health`,
                'value': `${(player.tdh).toFixed(0)}`,
            },
            {
                'label': `Damage Armour`,
                'value': `${(player.tda).toFixed(0)}`,

            },
            {
                'label': `Team Damage`,
                'value': `${(player.ffd).toFixed(0)}`,

            },
            {
                'label': `Rounds`,
                'value': `${(player.rounds).toFixed(0)}`,

            }
            // Add more rows if needed
        ];
    }, [player]);

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (isError || !playerData) {
        return <div>Error loading data</div>;
    }

    if (!player) {
        return <div>Player not found</div>;
    }


    return (
        <MaterialReactTable
            columns={columns}
            data={tableData}
            enableColumnActions={false}
            enableColumnFilters={false}
            enablePagination={false}
            enableSorting={false}
            enableBottomToolbar={false}
            enableTopToolbar={true}
            enableDensityToggle={false}
            enableGlobalFilter={false}
            enableFullScreenToggle={false}
            enableHiding={false}
            muiTableBodyRowProps={{ hover: false }}
            initialState={{ density: 'compact' }}
            state={{
                isLoading,
                showAlertBanner: isError,
                showProgressBars: isFetching,
            }}
            renderTopToolbarCustomActions={() => (
            <Box  width="100%" >
                <Typography variant="h5" component="h2" align="center" gutterBottom>
                    Damage/Round
                </Typography>
                <Box display="flex" width="100%" alignItems="center">
                    <Typography width="100%" component="span">
                        ADR: {(player.adr).toFixed(0)}
                    </Typography>
                    <Box width="100%" mx={2}>
                        <Tooltip title={`${(player.adr).toFixed(0)}`} placement="top">
                            <LinearProgress variant="determinate" value={(player && maxAdr ? (player.adr / maxAdr) * 100 : 0)} color='primary'/>
                        </Tooltip>
                    </Box>
                </Box></Box>
            )}
        />
    );
};

export default PlayerDamageTable;
