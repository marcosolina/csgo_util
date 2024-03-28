import { Grid } from "@mui/material";
import RconCommand from "../rcon/RconCommand";
import { useCs2ServerContent } from "./useCs2ServerContent";
import { DEFAULT_SPACING } from "../../lib/constants";

const TRANSLATIONS_BASE_PATH = "page.cs2server.labels";
const IMG_MAP_PREFIX = `/maps/cs2`;

const XS = 12;
const SM = 6;
const MD = 4;
const LG = 4;
const XL = 3;

const Cs2ServerContent = () => {
  const { maps } = useCs2ServerContent();

  return (
    <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
      {maps.map((map, j) => (
        <Grid key={j} item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <RconCommand
            cmd={map.is_workshop_map ? `host_workshop_map ${map.workshop_id}` : `map ${map.map_name}`}
            image={
              map.is_workshop_map
                ? `${IMG_MAP_PREFIX}/${map.workshop_id}.jpeg`
                : `${IMG_MAP_PREFIX}/${map.map_name}.jpeg`
            }
            label={map.map_name}
          />
        </Grid>
      ))}
    </Grid>
  );
};

export default Cs2ServerContent;
