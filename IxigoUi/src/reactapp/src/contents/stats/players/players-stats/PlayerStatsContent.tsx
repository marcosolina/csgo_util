import { Grid, Paper } from "@mui/material";
import PlayerDamageTable from "./PlayerDamageTable";
import PlayerUtilityTable from "./PlayerUtilityTable";
import { useParams } from "react-router-dom";
import PlayerRadarChart from "./player-radar-chart/PlayerRadarChart";
import PlayerClutchTable from "./player-clutch-table/PlayerClutchTable";
import PLayerEntryKillTable from "./player-entry-kill-table/PlayerEntryKillTable";
import PlayerWeaponSummaryTable from "./weapon-summary-table/PlayerWeaponSummaryTable";
import PlayerWinRateTable from "./player-win-rate-table/PlayerWinRateTable";
import PlayerHeadShotTable from "./player-head-shot-table/PlayerHeadShotTable";

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
        <PlayerRadarChart steamid={steamid} />
      </Grid>
      <Grid item xs={XS} sm={SM} md={MD}>
        <PlayerClutchTable steamid={steamid} />
      </Grid>
      <Grid item xs={XS} sm={SM} md={MD}>
        <PLayerEntryKillTable steamid={steamid} />
      </Grid>

      {/* Row 2: 1 XS full width box */}
      <Grid item xs={XS}>
        <PlayerWeaponSummaryTable steamid={steamid} />
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
