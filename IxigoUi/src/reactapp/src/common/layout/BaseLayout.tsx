import { AppBar, Container, Tab, Tabs, Typography } from "@mui/material";
import { Box } from "@mui/system";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import DemFilesContent from "../../contents/dem-files/DemFilesContent";
import DiscordBotContent from "../../contents/discord-bot/DiscordBotContent";
import PlayersContent from "../../contents/players/PlayersContent";
import RconContent from "../../contents/rcon/RconContent";
import StatsContent from "../../contents/stats/StatsContent";
import ServerInfoContent from "../../contents/server-info/ServerInfoContent";
import Case from "../switch-case/Case";
import Switch from "../switch-case/Switch";
import { useNavigate, useParams } from "react-router-dom";

const LANG_BASE_PATH = "page.home";

const TABS = {
  DEMFILES: "demfiles",
  TEAMS: "teams",
  STATS: "stats",
  RCON: "rcon",
  DISCORDBOT: "discordbot",
  JOINUS: "joinus",
};

const BaseLayout = () => {
  const { t } = useTranslation();
  let { tabid } = useParams();
  const [selectedTab, setSelectedTab] = useState(tabid || TABS.DEMFILES);
  const history = useNavigate();

  const handleChange = (event: React.SyntheticEvent, newValue: string) => {
    const newPath = `/${newValue}`;
    console.log(newPath);
    history(newPath);
    setSelectedTab(newValue);
  };

  return (
    <>
      <AppBar position="sticky">
        <Box padding={"8px 16px"}>
          <Typography variant="h5">{t(`${LANG_BASE_PATH}.title`)}</Typography>
        </Box>
      </AppBar>
      <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
        <Tabs
          value={selectedTab}
          onChange={handleChange}
          aria-label="basic tabs example"
          centered
          sx={{
            "& .MuiTabs-flexContainer": {
              flexWrap: "wrap",
            },
          }}
        >
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.DEMFILES}`)} value={TABS.DEMFILES} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.TEAMS}`)} value={TABS.TEAMS} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.STATS}`)} value={TABS.STATS} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.RCON}`)} value={TABS.RCON} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.DISCORDBOT}`)} value={TABS.DISCORDBOT} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.JOINUS}`)} value={TABS.JOINUS} />
        </Tabs>
      </Box>

      <Container>
        <Box sx={{ m: "2rem" }} />
        <Switch value={selectedTab}>
          <Case case={TABS.DEMFILES}>
            <DemFilesContent />
          </Case>
          <Case case={TABS.TEAMS}>
            <PlayersContent />
          </Case>
          <Case case={TABS.STATS}>
            <StatsContent />
          </Case>
          <Case case={TABS.RCON}>
            <RconContent />
          </Case>
          <Case case={TABS.DISCORDBOT}>
            <DiscordBotContent />
          </Case>
          <Case case={TABS.JOINUS}>
            <ServerInfoContent />
          </Case>
        </Switch>
      </Container>
    </>
  );
};

export default BaseLayout;
