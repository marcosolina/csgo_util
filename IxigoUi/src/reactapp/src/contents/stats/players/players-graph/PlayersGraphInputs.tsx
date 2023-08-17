import { Grid } from "@mui/material";
import IxigoSelectMultiple from "../../../../common/select/IxigoSelectMultiple";
import IxigoSelect from "../../../../common/select/IxigoSelect";
import IxigoDate from "../../../../common/date/IxigoDate";
import { usePlayerGraphContentProvider } from "./usePlayersGraphContentProvider";
import Switch from "../../../../common/switch-case/Switch";
import Case from "../../../../common/switch-case/Case";
import { QueryStatus } from "../../../../lib/http-requests";
import Loading from "./Loading";

const PlayersGraphIntputs = () => {
  const contentProvider = usePlayerGraphContentProvider();
  console.log(contentProvider);

  return (
    <Switch value={contentProvider.state}>
      <Case case={QueryStatus.success}>
        <Grid container spacing={3}>
          <Grid item md={6} xs={6}>
            <IxigoSelectMultiple
              label="Graphs"
              possibleValues={contentProvider.possibleScoreTypesValues}
              selectedValues={contentProvider.graphsSelected || []}
              onChange={contentProvider.setGraphsSelected}
            />
          </Grid>
          <Grid item md={2} xs={6}>
            <IxigoSelect
              label="Binning Level"
              possibleValues={contentProvider.possibleBinningValues}
              selectedValue={contentProvider.binningLevel || ""}
              onChange={contentProvider.setBinningLevel}
            />
          </Grid>
          <Grid item md={2} xs={6}>
            <IxigoDate label="Start date" value={contentProvider.startDate} onChange={contentProvider.setStartDate} />
          </Grid>
          <Grid item md={2} xs={6}>
            <IxigoDate label="End date" value={contentProvider.endDate} onChange={contentProvider.setEndDate} />
          </Grid>
        </Grid>
      </Case>
      <Case case={QueryStatus.loading}>
        <Loading />
      </Case>
    </Switch>
  );
};

export default PlayersGraphIntputs;
