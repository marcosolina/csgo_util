import { useState, useMemo } from "react";
import { IconButton, Tooltip } from '@mui/material';
import { Box } from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { weaponImage } from '../weaponImage';
import { MaterialReactTable } from 'material-react-table';
import {
    QueryClient,
    QueryClientProvider,
    useQuery,
  } from '@tanstack/react-query';


  const smallColSize = 5;
  const MatchContent = () => {

    const { data, isError, isFetching, isLoading, refetch } = useQuery({
        queryKey: ['matches'],
        queryFn: async () => {
            const url1 = new URL("https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/MATCH_RESULTS");
    
            const responses = await Promise.all([
                fetch(url1.href),
            ]);
    
            const jsons = await Promise.all(responses.map(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            }));
    
            const matchResults = jsons[0].view_data;
    
            return matchResults;
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
          //{ accessorKey: "match_filename", header: "Match Filename", size: smallColSize, Header: createCustomHeader("Match Filename") },
          { accessorKey: "match_date", header: "Date", size: smallColSize, Header: createCustomHeader("Match Date") ,enableGrouping: false},
          { accessorKey: "mapname", header: "Map", size: smallColSize, Header: createCustomHeader("Map Name") },
          { accessorKey: "team1_total_wins", header: "T1", size: smallColSize, Header: createCustomHeader("Team 1 Round Wins"),enableGrouping: false, enableFilter: false,
          Cell: ({ cell }: { cell: any }) => {
            const score = cell.getValue() as number;
            let backgroundColor = '#A04141';
            if (score >= 8) {
                backgroundColor = '#96C626';
            }
            return (
                <div style={{ display: 'flex', justifyContent: 'right', alignItems: 'right', height: '100%' }}>
                    <span style={{
                        borderRadius: '4px',
                        padding: '2px',
                        display: 'block',
                        color: '#fff',
                        width: '43px',
                        textAlign: 'center',
                        backgroundColor: backgroundColor
                    }}>
                        {score}
                    </span>
                </div>
            );
        },
          },
          //{ accessorKey: "team1_wins_as_t", header: "T1 Wins as T", size: smallColSize, Header: createCustomHeader("Team 1 Wins as T") },
          { accessorKey: "team2_total_wins", header: "T2", size: smallColSize, Header: createCustomHeader("Team 2 Round Wins"),enableGrouping: false, enableFilter: false,
          Cell: ({ cell }: { cell: any }) => {
            const score = cell.getValue() as number;
            let backgroundColor = '#A04141';
            if (score >= 8) {
                backgroundColor = '#96C626';
            }
            return (
                <div style={{ display: 'flex', justifyContent: 'left', alignItems: 'left', height: '100%' }}>
                    <span style={{
                        borderRadius: '4px',
                        padding: '2px',
                        display: 'block',
                        color: '#fff',
                        width: '43px',
                        textAlign: 'center',
                        backgroundColor: backgroundColor
                    }}>
                        {score}
                    </span>
                </div>
            );
        },
          },
          { accessorKey: "total_t_wins", header: "T Wins", size: smallColSize, Header: createCustomHeader("Total Terrorist Wins") ,enableGrouping: false, enableFilter: false,aggregationFn: 'mean' as any,
          AggregatedCell: ({ cell }: { cell: any }) => <Box>Terrorist Mean Score: {cell.getValue().toFixed(2)}</Box>,},
          { accessorKey: "total_ct_wins", header: "CT Wins", size: smallColSize, Header: createCustomHeader("Total Counter Terrorist Wins") ,enableGrouping: false, enableFilter: false,aggregationFn: 'mean' as any,
          AggregatedCell: ({ cell }: { cell: any }) => <Box>CT Mean Score: {cell.getValue().toFixed(2)}</Box>,},
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
            //grouping: ['mapname'],
            sorting: [{ id: 'match_date', desc: true }], //sort by state by default
             }}
          enableGrouping
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
          rowCount={data?.length ?? 0}
          state={{
            isLoading,
            showAlertBanner: isError,
            showProgressBars: isFetching,
          }}
        />
      );
  };

  export default MatchContent;