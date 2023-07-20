import React from 'react';
import { Grid, Box, Paper } from '@mui/material';
import PlayerRadarChart from "./PlayerRadarChart";
import PlayerClutchTable from "./PlayerClutchTable";
import PlayerEntryKIllTable from './PlayerEntryKillTable';
import PlayerWinRateTable from './PlayerWinRateTable';
import PlayerHeadShotTable from './PlayerHeadShotTable';
import PlayerDamageTable from './PlayerDamageTable';
import PlayerUtilityTable from './PlayerUtilityTable';
import PlayerWeaponSummaryTable from './PlayerWeaponSummaryTable';

interface PlayerStatsContentProps {
  steamid: string;
}

const PlayerStatsContent: React.FC<PlayerStatsContentProps> = ({ steamid }) => {
  return (
    <Grid container spacing={1}>
      {/* Row 1: 3 4x4 Boxes */}
      <Grid item xs={12} sm={6} md={4}>
        <Paper>
          <PlayerRadarChart steamid={steamid} />
        </Paper>
      </Grid>
      <Grid item xs={12} sm={6} md={4}>
        <Paper>
          <PlayerClutchTable steamid={steamid} />
        </Paper>
      </Grid>
      <Grid item xs={12} sm={6} md={4}>
        <Paper>
        <PlayerEntryKIllTable steamid={steamid} />
        </Paper>
      </Grid>

      {/* Row 2: 1 12 full width box */}
      <Grid item xs={12}>
        <Paper>
          <PlayerWeaponSummaryTable steamid={steamid} />
        </Paper>
      </Grid>

      {/* Row 3: 4 3x2 Boxes */}
      <Grid item xs={12} sm={6} md={3}>
        <Paper>
          <PlayerWinRateTable steamid={steamid}/>
        </Paper>
      </Grid>
      <Grid item xs={12} sm={6} md={3}>
        <Paper>
        <PlayerHeadShotTable steamid={steamid}/>
        </Paper>
      </Grid>
      <Grid item xs={12} sm={6} md={3}>
        <Paper>
          <PlayerDamageTable steamid={steamid}/>
        </Paper>
      </Grid>
      <Grid item xs={12} sm={6} md={3}>
        <Paper>
          <PlayerUtilityTable steamid={steamid}/>
        </Paper>
      </Grid>
    </Grid>
  );
};

export default PlayerStatsContent;
