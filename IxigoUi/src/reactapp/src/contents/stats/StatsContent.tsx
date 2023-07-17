import { useState, useMemo } from "react";
import { IconButton, Tooltip } from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { weaponImage } from './weaponImage';
import { MaterialReactTable } from 'material-react-table';
import {
    QueryClient,
    QueryClientProvider,
    useQuery,
  } from '@tanstack/react-query';


  const StatsContent = () => {
    //const [columnFilters, setColumnFilters] = useState([]);
    //const [globalFilter, setGlobalFilter] = useState('');
    //const [sorting, setSorting] = useState([]);
    //const [pagination, setPagination] = useState({
    //    pageIndex: 0,
    //    pageSize: 10,
    //});
  

    const { data, isError, isFetching, isLoading, refetch } = useQuery({
        queryKey: [
          'table-data',
          //columnFilters, //refetch when columnFilters changes
          //globalFilter, //refetch when globalFilter changes
          //pagination.pageIndex, //refetch when pagination.pageIndex changes
          //pagination.pageSize, //refetch when pagination.pageSize changes
          //sorting, //refetch when sorting changes
        ],
        queryFn: async () => {
          const fetchURL = new URL("https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/PLAYER_OVERALL_STATS_EXTENDED");
          //fetchURL.searchParams.set(
          //  'start',
          //  `${pagination.pageIndex * pagination.pageSize}`,
          //);
          //fetchURL.searchParams.set('size', `${pagination.pageSize}`);
          //fetchURL.searchParams.set('filters', JSON.stringify(columnFilters ?? []));
          //fetchURL.searchParams.set('globalFilter', globalFilter ?? '');
          //fetchURL.searchParams.set('sorting', JSON.stringify(sorting ?? []));
          const response = await fetch(fetchURL.href);
          const json = await response.json();
          return json
        },
        keepPreviousData: true,
      });
    
    

      const columns = useMemo(
        () => [
          { accessorKey: "steamid", header: "steamid" },
          { accessorKey: "matches", header: "matches" ,size: 10},
          { accessorKey: "rounds", header: "rounds"  ,size: 10},
          { accessorKey: "kills", header: "kills" ,size: 10 },
          { accessorKey: "assists", header: "assists" ,size: 10 },
          { accessorKey: "deaths", header: "deaths" ,size: 10 },
          { accessorKey: "hltv_rating", header: "hltv" ,size: 10,
          Cell: ({ cell }: { cell: any }) => {
            const rating = cell.getValue() as number;
            return <div style={{ color: rating >= 1 ? 'green' : 'red' }}>{rating}</div>;
          },
          },
          { accessorKey: "kpr", header: "kpr" ,size: 10 },
          { accessorKey: "dpr", header: "dpr" ,size: 10 },
          { accessorKey: "adr", header: "adr" ,size: 10 },
          { accessorKey: "rws", header: "rws" ,size: 10},
          { accessorKey: "kast", header: "kast" ,size: 10},
          { accessorKey: "headshots", header: "hs" ,size: 10},
          { accessorKey: "kdr", header: "kdr",size: 10 },
          { accessorKey: "headshot_percentage", header: "hsp",size: 10 },
          { accessorKey: "ff", header: "ff",size: 10 },
          { accessorKey: "ek", header: "ek",size: 10 },
          { accessorKey: "bp", header: "bp" ,size: 10},
          { accessorKey: "bd", header: "bd",size: 10 },
          { accessorKey: "hr", header: "hr",size: 10 },
          { accessorKey: "mvp", header: "mvp" ,size: 10},
          { accessorKey: "_5k", header: "5k",size: 10 },
          { accessorKey: "_4k", header: "4k",size: 10 },
          { accessorKey: "_3k", header: "3k" ,size: 10},
          { accessorKey: "_2k", header: "2k",size: 10 },
          { accessorKey: "_1k", header: "1k",size: 10 },
          { accessorKey: "tk", header: "tk",size: 10 },
          { accessorKey: "td", header: "td",size: 10 },
          { accessorKey: "tdh", header: "tdh",size: 10 },
          { accessorKey: "tda", header: "tda",size: 10 },
          { accessorKey: "ffd", header: "ffd",size: 10 },
          { accessorKey: "ebt", header: "ebt",size: 10 },
          { accessorKey: "fbt", header: "fbt" ,size: 10},
          { accessorKey: "ud", header: "ud" ,size: 10},
          { accessorKey: "fa", header: "fa",size: 10 },
          { accessorKey: "_1v1", header: "1v1" ,size: 10},
          { accessorKey: "_1v2", header: "1v2" ,size: 10},
          { accessorKey: "_1v3", header: "1v3" ,size: 10},
          { accessorKey: "_1v4", header: "1v4",size: 10 },
          { accessorKey: "_1v5", header: "1v5" ,size: 10},
        ],
        [],
      );
      

      return (
        <MaterialReactTable
          columns={columns}
          data={data?.view_data ?? []} //data is undefined on first render  ?.data ?? []
          initialState={{ showColumnFilters: false,
            density: 'compact',
            pagination: { pageIndex: 0, pageSize: 20 },
            sorting: [{ id: 'hltv_rating', desc: true }], //sort by state by default
            columnPinning: { left: ['steamid'] },
             }}
          enableColumnOrdering
          //enableGrouping
          enablePinning
          enableColumnDragging
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
          rowCount={data?.view_data?.length ?? 0}
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

  const queryClient = new QueryClient();

    const StatsContentWithReactQueryProvider = () => (
        <QueryClientProvider client={queryClient}>
            <StatsContent />
        </QueryClientProvider>
    );

  export default StatsContentWithReactQueryProvider;