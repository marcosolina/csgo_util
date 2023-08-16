import { Grid } from "@mui/material";
import IxigoSelectMultiple from "../../../../common/select/IxigoSelectMultiple";
import IxigoSelect from "../../../../common/select/IxigoSelect";
import IxigoText from "../../../../common/input/IxigoText";
import IxigoDate from "../../../../common/date/IxigoDate";

const PlayersGraphIntputs = () => {
  return (
    <Grid container spacing={3}>
      <Grid item md={6} xs={6}>
        <IxigoSelectMultiple label="Graphs" possibleValues={[]} selectedValues={[]} />
      </Grid>
      <Grid item md={2} xs={6}>
        <IxigoSelect label="Binning Level" possibleValues={[]} selectedValue="" />
      </Grid>
      <Grid item md={2} xs={6}>
        <IxigoDate label="Start date" />
      </Grid>
      <Grid item md={2} xs={6}>
        <IxigoDate label="End date" />
      </Grid>
    </Grid>
  );
};

export default PlayersGraphIntputs;
