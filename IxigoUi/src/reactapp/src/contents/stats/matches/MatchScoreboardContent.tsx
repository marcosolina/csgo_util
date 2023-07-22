
import React from 'react';
import { Grid, Paper } from '@mui/material';
import MatchTeamTable from './MatchTeamTable';

interface MatchScoreboardContentProps {
  match_id: number;
}

const MatchScoreboardContent: React.FC<MatchScoreboardContentProps> = ({ match_id }) => {
  return (
    <Grid container spacing={1}>
      {/* Row 1: 3 4x4 Boxes */}
      <Grid item xs={12} sm={12} md={12}>
        <Paper>
          <MatchTeamTable match_id={match_id} team="team1"/>
        </Paper>
      </Grid>
      <Grid item xs={12} sm={12} md={12}>
        <Paper>
          <MatchTeamTable match_id={match_id} team="team2"/>
        </Paper>
      </Grid>
    </Grid>
  );
};

export default MatchScoreboardContent;
