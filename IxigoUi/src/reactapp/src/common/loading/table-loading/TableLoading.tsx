import { Grid, Skeleton } from "@mui/material";
import { DEFAULT_SPACING } from "../../../lib/constants/style";

const XS = 3;
const HEIGHT = 60;
const COLUMNS = [1, 2, 3, 4, 5, 6];
const ROWS = [1, 2, 3, 4];

const TableLoading = () => {
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
    </Grid>
  );
};

export default TableLoading;
