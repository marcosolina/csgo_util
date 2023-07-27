import { useContext, useMemo } from "react";
import { IconButton, Tooltip } from '@mui/material';
import { Box } from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { SelectedStatsContext } from '../SelectedStatsContext';
import { MaterialReactTable } from 'material-react-table';
import { Link } from 'react-router-dom';
import {
    useQuery,
  } from 'react-query';
import { SERVICES_URLS } from "../../../lib/constants/paths";

  interface MapMatchesContentProps {
    mapName: string;
  }
  const smallColSize = 5;
  const MapMatchesContent: React.FC<MapMatchesContentProps> = ({ mapName }) => {

    const { data, isError, isFetching, isLoading, refetch } = useQuery({
        queryKey: ['matches' + mapName],
        queryFn: async () => {
            const url1 = new URL(`${SERVICES_URLS["dem-manager"]["get-stats-view"]}MATCH_RESULTS_CACHE?mapname=${mapName}`);
    
            const responses = await Promise.all([
                fetch(url1.href),
            ]);
    
            const jsons = await Promise.all(responses.map(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            }));
    
            let matchResults = jsons[0].view_data;
                    // Add score_differential to each item in matchResults
        matchResults = matchResults.map((match: any) => ({
          ...match,
          score_differential: match.total_t_wins - match.total_ct_wins
      }));
    
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
          { accessorKey: "match_date", header: "Date", size: smallColSize, Header: createCustomHeader("Match Date") ,enableGrouping: false,
          Cell: ({ cell }: { cell: any }) => {
            const matchDate = cell.getValue() as string;
            const match_id = cell.row.original.match_id;
            const selectedStatsContext = useContext(SelectedStatsContext);
            if (!selectedStatsContext) {
              throw new Error('useContext was called outside of the SelectedStatsContext provider');
            }
            const { setSelectedMatch, setSelectedSubpage } = selectedStatsContext;
            const date = new Date(matchDate);
            const formattedDate = date.toLocaleDateString('en-GB', { year: 'numeric', month: 'long', day: 'numeric' });
            const formattedTime = date.toLocaleTimeString('en-GB', { hour: '2-digit', minute: '2-digit' });
            return (
              <Box 
                component={Link}
                to={`/match/${match_id}`}
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
                  setSelectedMatch(match_id);
                  setSelectedSubpage("match");
                }}
              >
                {`${formattedDate}, ${formattedTime}`}
              </Box>
            );
          }},
          { accessorKey: "mapname", header: "Map", size: smallColSize, Header: createCustomHeader("Map Name") ,
          Cell: ({ cell }: { cell: any }) => {
            const map = cell.getValue() as string;
            const selectedStatsContext = useContext(SelectedStatsContext);
            if (!selectedStatsContext) {
              throw new Error('useContext was called outside of the SelectedStatsContext provider');
            }
            const { setSelectedMap, setSelectedSubpage } = selectedStatsContext;
            return (
              <Box 
              component={Link}
              to={`/map/${map}`}
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
                setSelectedMap(map);
                setSelectedSubpage("map");
              }}
            >
              {map}
            </Box>
            );
          }},
          { accessorKey: "team1_total_wins", header: "", enableColumnActions: false, size: smallColSize, Header: createCustomHeader("Team 1 Round Wins"),enableGrouping: false, enableFilter: false,
          Cell: ({ cell }: { cell: any }) => {
            const score = cell.getValue() as number;
            let backgroundColor = '#D0021B';
            if (score >= 8) {
                backgroundColor = '#7ED321';
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
          { accessorKey: "team2_total_wins", header: "", enableColumnActions: false, size: smallColSize, Header: createCustomHeader("Team 2 Round Wins"),enableGrouping: false, enableFilter: false,
          Cell: ({ cell }: { cell: any }) => {
            const score = cell.getValue() as number;
            let backgroundColor = '#D0021B';
            if (score >= 8) {
                backgroundColor = '#7ED321';
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
          {
            accessorKey: "score_differential", 
            header: "Score Differential", 
            size: smallColSize, 
            Header: createCustomHeader("Score Differential between Terrorists and CTs"),
            enableGrouping: false,
            Cell: ({ cell }: { cell: any }) => cell.getValue(),
            aggregationFn: 'mean' as any,
            AggregatedCell: ({ cell }: { cell: any }) => {
              return <Box>Mean score differential: {cell.getValue().toFixed(2)}</Box>
            },
          },
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
          enableColumnFilters={true}
          enableColumnFilterModes
          enableFilterMatchHighlighting={false}
          enablePagination
          enableDensityToggle={false}
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

  export default MapMatchesContent;