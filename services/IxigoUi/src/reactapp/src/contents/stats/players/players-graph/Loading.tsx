import { Grid, Skeleton } from "@mui/material";
import { DEFAULT_SPACING } from "../../../../lib/constants";
const XS = 3;
const HEIGHT = 60;
const COLUMNS = [1, 2, 3, 4];
const ROWS = [1];

const Loading = () => {
  return (
    <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
      {ROWS.map((row) => {
        return COLUMNS.map((column) => {
          return (
            <Grid item xs={XS} key={`r_${row}_c${column}`}>
              <Skeleton animation="wave" height={HEIGHT} />
            </Grid>
          );
        });
      })}
      <Grid item xs={12}>
        <Skeleton animation="wave" height={HEIGHT * 3} />
      </Grid>
    </Grid>
  );
};

export default Loading;
