import React from "react";
import { useQuery } from "react-query";
import { MaterialReactTable } from "material-react-table";
import { useMemo } from "react";
import { Typography, Box, LinearProgress, Tooltip } from "@mui/material";
import { SERVICES_URLS } from "../../../../lib/constants/paths";
import PieChartMini from "../../../../common/pie-chart-mini/PieChartMini";
import { IPlayerStats } from "./interfaces";

interface PlayerData {
  steamid: string;
  total_rounds: number;
  total_rounds_t: number;
  total_rounds_ct: number;
  ek_attempts: number;
  ek_success: number;
  ekt_attempts: number;
  ekt_success: number;
  ekct_attempts: number;
  ekct_success: number;
  ek_success_rate: number;
  ek_success_rate_overall: number;
  ekt_success_rate: number;
  ekt_success_rate_overall: number;
  ekct_success_rate: number;
  ekct_success_rate_overall: number;
}

const PlayerEntryKIllTable: React.FC<IPlayerStats> = ({ steamid }) => {
  const {
    data: playerData,
    isError,
    isLoading,
    isFetching,
  } = useQuery<PlayerData[], Error>({
    queryKey: ["entrykill" + steamid],
    queryFn: async (): Promise<PlayerData[]> => {
      const url1 = new URL(
        `${SERVICES_URLS["dem-manager"]["get-stats"]}/ENTRY_KILL_STATS_EXTENDED_CACHE?steamid=${steamid}`
      );

      const responses = await Promise.all([fetch(url1.href)]);

      const jsons = await Promise.all(
        responses.map((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok");
          }
          return response.json();
        })
      );

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
      return playerData.find((p) => p.steamid === steamid);
    }
    return undefined;
  }, [playerData, steamid]);

  const columns = useMemo(
    () => [
      {
        accessorKey: "label" as const,
        header: "",
        size: 5,
      },
      {
        accessorKey: "Combined" as const,
        header: "All",
        size: 5,
        Cell: ({ cell, row }: { cell: any; row: { index: number } }) => {
          const percentageString = cell.getValue() as string;
          // Parse the percentage value from the string.
          const percentage = Number(percentageString.slice(0, -1));
          return row.index <= 1 ? (
            <Box display="flex" flexDirection="column" width="100%" alignItems="center">
              <PieChartMini percentage={percentage} color="darkturquoise" size={40} />
              <Typography component="span" align="center">
                {percentageString}
              </Typography>
            </Box>
          ) : (
            percentageString
          );
        },
      },
      {
        accessorKey: "Ts" as const,
        header: "Ts",
        size: 5,
        Cell: ({ cell, row }: { cell: any; row: { index: number } }) => {
          const percentageString = cell.getValue() as string;
          // Parse the percentage value from the string.
          const percentage = Number(percentageString.slice(0, -1));
          return row.index <= 1 ? (
            <Box display="flex" flexDirection="column" width="100%" alignItems="center">
              <PieChartMini percentage={percentage} color="darkturquoise" size={40} />
              <Typography component="span" align="center">
                {percentageString}
              </Typography>
            </Box>
          ) : (
            percentageString
          );
        },
      },
      {
        accessorKey: "CTs" as const,
        header: "CTs",
        size: 5,
        Cell: ({ cell, row }: { cell: any; row: { index: number } }) => {
          const percentageString = cell.getValue() as string;
          // Parse the percentage value from the string.
          const percentage = Number(percentageString.slice(0, -1));
          return row.index <= 1 ? (
            <Box display="flex" flexDirection="column" width="100%" alignItems="center">
              <PieChartMini percentage={percentage} color="darkturquoise" size={40} />
              <Typography component="span" align="center">
                {percentageString}
              </Typography>
            </Box>
          ) : (
            percentageString
          );
        },
      },
    ],
    []
  );

  // Define your table data
  const tableData = useMemo(() => {
    if (!player) return [];
    return [
      {
        label: `Success`,
        Combined: `${player.ek_success_rate ? player.ek_success_rate.toFixed(0) : 0}%`,
        Ts: `${player.ekt_success_rate ? player.ekt_success_rate.toFixed(0) : 0}%`,
        CTs: `${player.ekct_success_rate ? player.ekct_success_rate.toFixed(0) : 0}%`,
      },
      {
        label: `Attempts`,
        Combined: `${
          player.ek_success_rate_overall
            ? ((100 * player.ek_success_rate_overall) / player.ek_success_rate).toFixed(0)
            : 0
        }%`,
        Ts: `${
          player.ekt_success_rate_overall
            ? ((100 * player.ekt_success_rate_overall) / player.ekt_success_rate).toFixed(0)
            : 0
        }%`,
        CTs: `${
          player.ekct_success_rate_overall
            ? ((100 * player.ekct_success_rate_overall) / player.ekct_success_rate).toFixed(0)
            : 0
        }%`,
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
      initialState={{ density: "compact" }}
      state={{
        isLoading,
        showAlertBanner: isError,
        showProgressBars: isFetching,
      }}
      renderTopToolbarCustomActions={() => (
        <Box width="100%">
          <Typography variant="h5" component="h2" align="center" gutterBottom>
            Entry Kill Stats
          </Typography>
          <Box display="flex" width="100%" alignItems="center">
            <Typography width="100%" component="span">
              Overall : {player.ek_success_rate_overall}%
            </Typography>
            <Box width="100%" mx={2}>
              <Tooltip title={`${player.ek_success_rate_overall}%`} placement="top">
                <LinearProgress variant="determinate" value={player.ek_success_rate_overall} color="primary" />
              </Tooltip>
            </Box>
          </Box>
        </Box>
      )}
    />
  );
};

export default PlayerEntryKIllTable;
