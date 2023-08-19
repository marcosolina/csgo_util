import { Grid } from "@mui/material";
import IxigoSelectMultiple from "../../../../common/select/IxigoSelectMultiple";
import IxigoSelect from "../../../../common/select/IxigoSelect";
import IxigoDate from "../../../../common/date/IxigoDate";
import { usePlayerGraphContentProvider } from "./usePlayersGraphContentProvider";
import Switch from "../../../../common/switch-case/Switch";
import Case from "../../../../common/switch-case/Case";
import { QueryStatus } from "../../../../lib/http-requests";
import Loading from "./Loading";
import { useTranslation } from "react-i18next";

const TRANSLATION_PREFIX = "page.stats.player.content.graphs.inputs";
const XS = 12;
const SM = 6;
const MD = 6;
const LG = 3;
const XL = 3;

const PlayersGraphIntputs = () => {
  const { t } = useTranslation();
  const contentProvider = usePlayerGraphContentProvider();

  return (
    <Switch value={contentProvider.state}>
      <Case case={QueryStatus.success}>
        <Grid container spacing={3}>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <IxigoSelectMultiple
              label={t(`${TRANSLATION_PREFIX}.lblScoreTypeDropDown`) as string}
              possibleValues={contentProvider.possibleScoreTypesValues}
              selectedValues={contentProvider.graphsSelected || []}
              onChange={contentProvider.setGraphsSelected}
            />
          </Grid>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <IxigoSelect
              label={t(`${TRANSLATION_PREFIX}.lblBinningLevel`) as string}
              possibleValues={contentProvider.possibleBinningValues}
              selectedValue={contentProvider.binningLevel || ""}
              onChange={contentProvider.setBinningLevel}
            />
          </Grid>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <IxigoDate
              label={t(`${TRANSLATION_PREFIX}.lblStartDate`) as string}
              value={contentProvider.startDate}
              onChange={contentProvider.setStartDate}
            />
          </Grid>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <IxigoDate
              label={t(`${TRANSLATION_PREFIX}.lblEndDate`) as string}
              value={contentProvider.endDate}
              onChange={contentProvider.setEndDate}
            />
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
