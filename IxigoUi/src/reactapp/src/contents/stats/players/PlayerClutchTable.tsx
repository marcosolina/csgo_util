import React from 'react';
import { useQuery } from 'react-query';
import { MaterialReactTable } from 'material-react-table';
import { useMemo } from "react";
import PieChartMini from '../PieChartMini';
import { Typography, Box, LinearProgress, Tooltip } from '@mui/material';
import { SERVICES_URLS } from "../../../lib/constants/paths";

interface PlayerData {
    _1v1p: number;
    _1v1w: number;
    _1v1l: number;
    _1v2p: number;
    _1v2w: number;
    _1v2l: number;
    _1v3p: number;
    _1v3w: number;
    _1v3l: number;
    _1v4p: number;
    _1v4w: number;
    _1v4l: number;
    _1v5p: number;
    _1v5w: number;
    _1v5l: number;
    _1vnp: number;
    steamid: string;
}

interface RadarChartProps {
    steamid: string;
}

const PlayerClutchTable: React.FC<RadarChartProps> = ({ steamid }) => {
    const { data: playerData, isError, isLoading, isFetching } = useQuery<PlayerData[], Error>({
        queryKey: ['clutch'+steamid],
        queryFn: async (): Promise<PlayerData[]> => {
            const url1 = new URL(`${SERVICES_URLS["dem-manager"]["get-stats-view"]}PLAYER_CLUTCH_STATS_CACHE?steamid=${steamid}`);

            const responses = await Promise.all([
                fetch(url1.href),
            ]);

            const jsons = await Promise.all(responses.map(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            }));

            const clutchData = jsons[0].view_data.map((player: any) => {
                return {
                    ...player,
                } as PlayerData;
            });

            return clutchData;
        },
        keepPreviousData: true,
    });

    const player = useMemo(() => {
        if (playerData) {
            return playerData.find(p => p.steamid === steamid);
        }
        return undefined;
    }, [playerData, steamid]);

    const columns = useMemo(
        () => [
            {
                accessorKey: '1v1' as const, header: '1v1', size: 5, Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
                    const percentageString = cell.getValue() as string;
                    // Parse the percentage value from the string.
                    const percentage = Number(percentageString.slice(0, -1));
                    return row.index === 0 ? <PieChartMini percentage={percentage} color='darkturquoise' size={35} /> : percentageString;
                },
            },
            {
                accessorKey: '1v2' as const, header: '1v2', size: 5, Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
                    const percentageString = cell.getValue() as string;
                    // Parse the percentage value from the string.
                    const percentage = Number(percentageString.slice(0, -1));
                    return row.index === 0 ? <PieChartMini percentage={percentage} color='darkturquoise' size={35} /> : percentageString;
                },
            },
            {
                accessorKey: '1v3' as const, header: '1v3', size: 5, Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
                    const percentageString = cell.getValue() as string;
                    // Parse the percentage value from the string.
                    const percentage = Number(percentageString.slice(0, -1));
                    return row.index === 0 ? <PieChartMini percentage={percentage} color='darkturquoise' size={35} /> : percentageString;
                },
            },
            {
                accessorKey: '1v4' as const, header: '1v4', size: 5, Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
                    const percentageString = cell.getValue() as string;
                    // Parse the percentage value from the string.
                    const percentage = Number(percentageString.slice(0, -1));
                    return row.index === 0 ? <PieChartMini percentage={percentage} color='darkturquoise' size={35} /> : percentageString;
                },
            },
            {
                accessorKey: '1v5' as const, header: '1v5', size: 5, Cell: ({ cell, row }: { cell: any, row: { index: number } }) => {
                    const percentageString = cell.getValue() as string;
                    // Parse the percentage value from the string.
                    const percentage = Number(percentageString.slice(0, -1));
                    return row.index === 0 ? <PieChartMini percentage={percentage} color='darkturquoise' size={35} /> : percentageString;
                },
            },
        ],
        [],
    );

    // Define your table data
    const tableData = useMemo(() => {
        if (!player) return [];
        return [
            {
                '1v1': `${player._1v1p.toFixed(0)}%`,
                '1v2': `${player._1v2p.toFixed(0)}%`,
                '1v3': `${player._1v3p.toFixed(0)}%`,
                '1v4': `${player._1v4p.toFixed(0)}%`,
                '1v5': `${player._1v5p.toFixed(0)}%`,
            },
            {
                '1v1': `${player._1v1p.toFixed(0)}%`,
                '1v2': `${player._1v2p.toFixed(0)}%`,
                '1v3': `${player._1v3p.toFixed(0)}%`,
                '1v4': `${player._1v4p.toFixed(0)}%`,
                '1v5': `${player._1v5p.toFixed(0)}%`,
            },
            {
                '1v1': `W:${player._1v1w}`,
                '1v2': `W:${player._1v2w}`,
                '1v3': `W:${player._1v3w}`,
                '1v4': `W:${player._1v4w}`,
                '1v5': `W:${player._1v5w}`,
            },
            {
                '1v1': `L:${player._1v1l}`,
                '1v2': `L:${player._1v2l}`,
                '1v3': `L:${player._1v3l}`,
                '1v4': `L:${player._1v4l}`,
                '1v5': `L:${player._1v5l}`,
            },
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
                    Clutch Stats
                </Typography>
                <Box display="flex" width="100%" alignItems="center">
                    <Typography width="100%" component="span">
                        1vX : {player._1vnp}%
                    </Typography>
                    <Box width="100%" mx={2}>
                        <Tooltip title={`${player._1vnp}%`} placement="top">
                            <LinearProgress variant="determinate" value={player._1vnp} color='primary'/>
                        </Tooltip>
                    </Box>
                </Box></Box>
            )}
        />
    );
};

export default PlayerClutchTable;
