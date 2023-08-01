import React, { useState } from "react";
import { Box, Tab, Tabs, Typography, Container, Skeleton } from "@mui/material";
import PlayerStatsContent from "./PlayerStatsContent";
import PlayerGraphsContent from "./PlayerGraphsContent";
import PlayerWeaponsContent from "./PlayerWeaponsContent";
import PlayerMapsContent from "./PlayerMapsContent";
import PlayerMatchesContent from "./PlayerMatchesContent";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { USERS_REQUEST, useGetStats } from "../../../services/stats";
import { useTranslation } from "react-i18next";
import Switch from "../../../common/switch-case/Switch";
import Case from "../../../common/switch-case/Case";
import { QueryStatus } from "../../../lib/http-requests";

const LANG_BASE_PATH = "page.stats.player";

const HEIGHT = 60;
const TABS = {
  OVERALL: "overall",
  GRAPHS: "graphs",
  WEAPONS: "weapons",
  MAPS: "maps",
  MATCHES: "matches",
};

export default function PlayerPage() {
  const { t } = useTranslation();
  let { steamid, playertab } = useParams();
  const [selectedTab, setSelectedTab] = useState(playertab || TABS.OVERALL);
  const history = useNavigate();
  const location = useLocation();

  const qUsersRequest = useGetStats(USERS_REQUEST);

  const handleChange = (event: React.SyntheticEvent, newValue: string) => {
    const pathParts = location.pathname.split("/").filter((p) => p);

    console.log(pathParts);
    if (pathParts.length > 3) {
      pathParts[3] = newValue;
    } else {
      pathParts.push(newValue);
    }

    const newPath = pathParts.join("/");
    history(`/${newPath}`);
    setSelectedTab(newValue);
  };

  const userName = qUsersRequest.data?.data?.view_data.find((u) => u.steam_id === steamid)?.user_name || steamid;

  if (!steamid) {
    return null; // TODO return a "warining" component or something similar
  }

  return (
    <Container>
      <Box textAlign="center">
        <Switch value={qUsersRequest.status}>
          <Case case={QueryStatus.loading}>
            <Skeleton animation="wave" height={HEIGHT} />
          </Case>
          <Case case={QueryStatus.success}>
            <Typography variant="h5">{`${t(`${LANG_BASE_PATH}.title`)}${userName}`}</Typography>
          </Case>
        </Switch>
      </Box>
      <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
        <Tabs
          value={selectedTab}
          onChange={handleChange}
          aria-label="basic tabs example"
          orientation="horizontal"
          centered
          sx={{
            "& .MuiTabs-flexContainer": {
              flexWrap: "wrap",
            },
          }}
        >
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.OVERALL}`)} value={TABS.OVERALL} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.GRAPHS}`)} value={TABS.GRAPHS} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.WEAPONS}`)} value={TABS.WEAPONS} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.MAPS}`)} value={TABS.MAPS} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.MATCHES}`)} value={TABS.MATCHES} />
        </Tabs>
      </Box>
      <Box sx={{ m: "2rem" }} />
      <Switch value={selectedTab}>
        <Case case={TABS.OVERALL}>
          <PlayerStatsContent steamid={steamid} />
        </Case>
        <Case case={TABS.GRAPHS}>
          <PlayerGraphsContent steamid={steamid} />
        </Case>
        <Case case={TABS.WEAPONS}>
          <PlayerWeaponsContent steamid={steamid} />
        </Case>
        <Case case={TABS.MAPS}>
          <PlayerMapsContent steamid={steamid} />
        </Case>
        <Case case={TABS.MATCHES}>
          <PlayerMatchesContent steamid={steamid} />
        </Case>
      </Switch>
    </Container>
  );
}
