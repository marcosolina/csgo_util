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
import { MAIN_TABS, UI_CONTEXT_PATH } from "../../lib/constants";

const LANG_BASE_PATH = "page.home";

const BaseLayout = () => {
  const { t } = useTranslation();
  const { tabid } = useParams();
  const [selectedTab, setSelectedTab] = useState(tabid || MAIN_TABS.DEMFILES);
  const history = useNavigate();

  const handleChange = (event: React.SyntheticEvent, newValue: string) => {
    const newPath = `${UI_CONTEXT_PATH}/${newValue}`;
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
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${MAIN_TABS.DEMFILES}`)} value={MAIN_TABS.DEMFILES} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${MAIN_TABS.TEAMS}`)} value={MAIN_TABS.TEAMS} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${MAIN_TABS.STATS}`)} value={MAIN_TABS.STATS} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${MAIN_TABS.RCON}`)} value={MAIN_TABS.RCON} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${MAIN_TABS.DISCORDBOT}`)} value={MAIN_TABS.DISCORDBOT} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.${MAIN_TABS.JOINUS}`)} value={MAIN_TABS.JOINUS} />
        </Tabs>
      </Box>

      <Container>
        <Box sx={{ m: "2rem" }} />
        <Switch value={selectedTab}>
          <Case case={MAIN_TABS.DEMFILES}>
            <DemFilesContent />
          </Case>
          <Case case={MAIN_TABS.TEAMS}>
            <PlayersContent />
          </Case>
          <Case case={MAIN_TABS.STATS}>
            <StatsContent />
          </Case>
          <Case case={MAIN_TABS.RCON}>
            <RconContent />
          </Case>
          <Case case={MAIN_TABS.DISCORDBOT}>
            <DiscordBotContent />
          </Case>
          <Case case={MAIN_TABS.JOINUS}>
            <ServerInfoContent />
          </Case>
        </Switch>
      </Container>
    </>
  );
};

export default BaseLayout;
