import { Grid, Skeleton } from "@mui/material";
import { DEFAULT_SPACING } from "../../lib/constants";

const XS = 12;
const SM = 12;
const MD = 6;
const LG = 4;
const XL = 3;
const HEIGHT = 60;

const Loading = () => {
  return (
    <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
      <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
        <Skeleton animation="wave" height={HEIGHT} />
      </Grid>
      <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
        <Skeleton animation="wave" height={HEIGHT} />
      </Grid>
      <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
        <Skeleton animation="wave" height={HEIGHT} />
      </Grid>
      <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
        <Skeleton animation="wave" height={HEIGHT} />
      </Grid>
    </Grid>
  );
};

export default Loading;
