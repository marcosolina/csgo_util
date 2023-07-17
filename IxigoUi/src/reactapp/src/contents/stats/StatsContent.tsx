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
          { "accessorKey": "steamid", "header": "steamid" },
          { "accessorKey": "matches", "header": "matches" },
          { "accessorKey": "rounds", "header": "rounds" },
          { "accessorKey": "kills", "header": "kills" },
          { "accessorKey": "assists", "header": "assists" },
          { "accessorKey": "deaths", "header": "deaths" },
          { "accessorKey": "kpr", "header": "kpr" },
          { "accessorKey": "dpr", "header": "dpr" },
          { "accessorKey": "adr", "header": "adr" },
          { "accessorKey": "hltv_rating", "header": "hltv_rating" },
          { "accessorKey": "rws", "header": "rws" },
          { "accessorKey": "kast", "header": "kast" },
          { "accessorKey": "headshots", "header": "headshots" },
          { "accessorKey": "kdr", "header": "kdr" },
          { "accessorKey": "headshot_percentage", "header": "headshot_percentage" },
          { "accessorKey": "ff", "header": "ff" },
          { "accessorKey": "ek", "header": "ek" },
          { "accessorKey": "bp", "header": "bp" },
          { "accessorKey": "bd", "header": "bd" },
          { "accessorKey": "hr", "header": "hr" },
          { "accessorKey": "mvp", "header": "mvp" },
          { "accessorKey": "_5k", "header": "_5k" },
          { "accessorKey": "_4k", "header": "_4k" },
          { "accessorKey": "_3k", "header": "_3k" },
          { "accessorKey": "_2k", "header": "_2k" },
          { "accessorKey": "_1k", "header": "_1k" },
          { "accessorKey": "tk", "header": "tk" },
          { "accessorKey": "td", "header": "td" },
          { "accessorKey": "tdh", "header": "tdh" },
          { "accessorKey": "tda", "header": "tda" },
          { "accessorKey": "ffd", "header": "ffd" },
          { "accessorKey": "ebt", "header": "ebt" },
          { "accessorKey": "fbt", "header": "fbt" },
          { "accessorKey": "ud", "header": "ud" },
          { "accessorKey": "fa", "header": "fa" },
          { "accessorKey": "_1v1", "header": "_1v1" },
          { "accessorKey": "_1v2", "header": "_1v2" },
          { "accessorKey": "_1v3", "header": "_1v3" },
          { "accessorKey": "_1v4", "header": "_1v4" },
          { "accessorKey": "_1v5", "header": "_1v5" },
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