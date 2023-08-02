import { Grid, Paper } from "@mui/material";
import PlayerClutchTable from "./PlayerClutchTable";
import PlayerEntryKIllTable from "./PlayerEntryKillTable";
import PlayerWinRateTable from "./PlayerWinRateTable";
import PlayerHeadShotTable from "./PlayerHeadShotTable";
import PlayerDamageTable from "./PlayerDamageTable";
import PlayerUtilityTable from "./PlayerUtilityTable";
import PlayerWeaponSummaryTable from "./PlayerWeaponSummaryTable";
import { useParams } from "react-router-dom";
import PlayerRadarChart from "./player-radar-chart/PlayerRadarChart";

const XS = 12;
const SM = 6;
const MD = 4;

const PlayerStatsContent = () => {
  let { steamid } = useParams();

  if (!steamid) {
    return null; // return a better component here
  }

  return (
    <Grid container spacing={1}>
      {/* Row 1: 3 4x4 Boxes */}
      <Grid item xs={XS} sm={SM} md={MD}>
        <Paper>
          <PlayerRadarChart steamid={steamid} />
        </Paper>
      </Grid>
      <Grid item xs={XS} sm={SM} md={MD}>
        <Paper>
          <PlayerClutchTable steamid={steamid} />
        </Paper>
      </Grid>
      <Grid item xs={XS} sm={SM} md={MD}>
        <Paper>
          <PlayerEntryKIllTable steamid={steamid} />
        </Paper>
      </Grid>

      {/* Row 2: 1 XS full width box */}
      <Grid item xs={XS}>
        <Paper>
          <PlayerWeaponSummaryTable steamid={steamid} />
        </Paper>
      </Grid>

      {/* Row 3: 4 3x2 Boxes */}
      <Grid item xs={XS} sm={SM} md={3}>
        <Paper>
          <PlayerWinRateTable steamid={steamid} />
        </Paper>
      </Grid>
      <Grid item xs={XS} sm={SM} md={3}>
        <Paper>
          <PlayerHeadShotTable steamid={steamid} />
        </Paper>
      </Grid>
      <Grid item xs={XS} sm={SM} md={3}>
        <Paper>
          <PlayerDamageTable steamid={steamid} />
        </Paper>
      </Grid>
      <Grid item xs={XS} sm={SM} md={3}>
        <Paper>
          <PlayerUtilityTable steamid={steamid} />
        </Paper>
      </Grid>
    </Grid>
  );
};

export default PlayerStatsContent;
