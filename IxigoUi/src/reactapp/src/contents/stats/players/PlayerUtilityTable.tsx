import React from "react";
import { useQuery } from "react-query";
import { MaterialReactTable } from "material-react-table";
import { useMemo } from "react";
import { Typography, Box, LinearProgress, Tooltip } from "@mui/material";
import { SERVICES_URLS } from "../../../lib/constants/paths";

// Define your types for the player data
interface PlayerData {
  steamid: string;
  fa: number;
  ebt: number;
  ud: number;
  fbt: number;
  rounds: number;
}

interface RadarChartProps {
  steamid: string;
}

const PlayerUtilityTable: React.FC<RadarChartProps> = ({ steamid }) => {
  const {
    data: playerData,
    isError,
    isLoading,
    isFetching,
  } = useQuery<PlayerData[], Error>({
    queryKey: ["leaderboard" + steamid],
    queryFn: async () => {
      const url1 = new URL(`${SERVICES_URLS["dem-manager"]["get-stats"]}/PLAYER_OVERALL_STATS_EXTENDED_EXTENDED_CACHE`);

      const responses = await Promise.all([fetch(url1.href)]);

      const jsons = await Promise.all(
        responses.map((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok");
          }
          return response.json();
        })
      );

      let playerData = jsons[0].view_data.map((player: any) => {
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
      const foundPlayer = playerData.find((p) => p.steamid === steamid);
      return foundPlayer;
    }
    return undefined;
  }, [playerData, steamid]);

  const maxUD = useMemo(() => {
    if (playerData) {
      const max = Math.max(...playerData.map((p) => Number(p.ud) || 0));
      return max;
    }
    return undefined;
  }, [playerData]);

  const columns = useMemo(
    () => [
      { accessorKey: "label" as const, header: "", size: 5 },
      { accessorKey: "value" as const, header: "", size: 5 },
    ],
    []
  );

  // Define your table data
  const tableData = useMemo(() => {
    if (!player) return [];
    return [
      {
        label: `Flash Assists`,
        value: `${player.fa.toFixed(0)}`,
      },
      {
        label: `Blind Time (avg)`,
        value: `${player.ebt.toFixed(2)}s`,
      },
      {
        label: `Team Blind (avg)`,
        value: `${player.fbt.toFixed(2)}s`,
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
            Utility
          </Typography>
          <Box display="flex" width="100%" alignItems="center">
            <Typography width="100%" component="span">
              UD: {player.ud}
            </Typography>
            <Box width="100%" mx={2}>
              <Tooltip title={`${player.ud}`} placement="top">
                <LinearProgress
                  variant="determinate"
                  value={player && maxUD ? (player.ud / maxUD) * 100 : 0}
                  color="primary"
                />
              </Tooltip>
            </Box>
          </Box>
        </Box>
      )}
    />
  );
};

export default PlayerUtilityTable;
