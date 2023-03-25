import { Grid, Skeleton, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";
import Case from "../../common/switch-case/Case";
import Switch from "../../common/switch-case/Switch";
import { DEFAULT_SPACING, UI_CONTEXT_PATH } from "../../lib/constants";
import { QueryStatus } from "../../lib/http-requests";
import { useGetServerMaps } from "../../services/rcon";
import RconCommand from "./RconCommand";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";

const XS = 12;
const SM = 6;
const MD = 4;
const LG = 4;
const XL = 3;

const LNG_PREFIX = "page.rcon.commands.groups.maps";
const IMG_MAP_PREFIX = `/maps`;
const LOADING = [1, 2, 3, 4];

const RconMaps = () => {
  const { t } = useTranslation();
  const { status, data } = useGetServerMaps();

  return (
    <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
      <Grid item xs={12} textAlign={"center"}>
        <Typography variant="h3">{t(`${LNG_PREFIX}.title`)}</Typography>
      </Grid>
      <Switch value={status}>
        <Case case={QueryStatus.success}>
          {data?.data?.server_maps.map((map, j) => (
            <Grid key={j} item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
              <RconCommand
                cmd={map.is_workshop_map ? `map workshop/${map.workshop_id}/${map.map_name}` : `map ${map.map_name}`}
                image={
                  map.is_workshop_map
                    ? `${IMG_MAP_PREFIX}/workshop-${map.workshop_id}-${map.map_name}.jpg`
                    : `${IMG_MAP_PREFIX}/${map.map_name}.jpg`
                }
                label={map.map_name}
              />
            </Grid>
          ))}
        </Case>
        <Case case={QueryStatus.loading}>
          {LOADING.map((element, index) => (
            <Grid key={element} item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
              <Skeleton animation="wave" height={300} />
            </Grid>
          ))}
        </Case>
        <Case case={QueryStatus.error}>
          <Grid item xs={12} textAlign={"center"}>
            <ErrorOutlineIcon color="error" fontSize="large" />
          </Grid>
        </Case>
      </Switch>
    </Grid>
  );
};

export default RconMaps;
