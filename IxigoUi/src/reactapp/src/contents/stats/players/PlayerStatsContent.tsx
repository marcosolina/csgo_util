import React from 'react';
import { Grid, Box, Paper } from '@mui/material';
import PlayerRadarChart from "./PlayerRadarChart";
import PlayerClutchTable from "./PlayerClutchTable";
import PlayerEntryKIllTable from './PlayerEntryKillTable';

interface PlayerStatsContentProps {
  steamid: string;
}

const PlayerStatsContent: React.FC<PlayerStatsContentProps> = ({ steamid }) => {
  return (
    <Grid container spacing={3}>
      {/* Row 1: 3 4x4 Boxes */}
      <Grid item xs={4}>
        <Paper>
          <PlayerRadarChart steamid={steamid} />
        </Paper>
      </Grid>
      <Grid item xs={4}>
        <Paper>
          <PlayerClutchTable steamid={steamid} />
        </Paper>
      </Grid>
      <Grid item xs={4}>
        <Paper>
        <PlayerEntryKIllTable steamid={steamid} />
        </Paper>
      </Grid>

      {/* Row 2: 1 12 full width box */}
      <Grid item xs={12}>
        <Paper>
          <Box height={160} bgcolor="lightgreen" />
        </Paper>
      </Grid>

      {/* Row 3: 4 3x2 Boxes */}
      <Grid item xs={3}>
        <Paper>
          <Box height={80} bgcolor="lightgrey" />
        </Paper>
      </Grid>
      <Grid item xs={3}>
        <Paper>
          <Box height={80} bgcolor="lightgrey" />
        </Paper>
      </Grid>
      <Grid item xs={3}>
        <Paper>
          <Box height={80} bgcolor="lightgrey" />
        </Paper>
      </Grid>
      <Grid item xs={3}>
        <Paper>
          <Box height={80} bgcolor="lightgrey" />
        </Paper>
      </Grid>

      {/* Row 4: 2 6x4 Boxes */}
      <Grid item xs={6}>
        <Paper>
          <Box height={160} bgcolor="lightyellow" />
        </Paper>
      </Grid>
      <Grid item xs={6}>
        <Paper>
          <Box height={160} bgcolor="lightyellow" />
        </Paper>
      </Grid>
    </Grid>
  );
};

export default PlayerStatsContent;
