import { AppBar, Container, Tab, Tabs, Typography } from "@mui/material";
import { Box } from "@mui/system";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import ChartsContent from "../../contents/charts/ChartsContent";
import DemFilesContent from "../../contents/dem-files/DemFilesContent";
import DiscordBotContent from "../../contents/discord-bot/DiscordBotContent";
import PlayersContent from "../../contents/players/PlayersContent";
import RconContent from "../../contents/rcon/RconContent";
import StatsContent from "../../contents/stats/StatsContent";
import MatchStatsContent from "../../contents/stats/matches/MatchStatsContent";
import MapContent from "../../contents/stats/maps/MapContent";
import PlayerContent from "../../contents/stats/players/PlayerContent";
import ServerInfoContent from "../../contents/server-info/ServerInfoContent";
import Case from "../switch-case/Case";
import Switch from "../switch-case/Switch";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";
import { QUERY_PARAMS } from "../../lib/constants";
import { SelectedStatsContext } from '../../contents/stats/SelectedStatsContext';

const LANG_BASE_PATH = "page.home";

const BaseLayout = () => {
  const { t } = useTranslation();
  const [value, setValue] = useState(0);
  const [selectedTab, setSelectedTab] = useState<number | null>(null);
  const [searchParams] = useSearchParams();
  const location = useLocation();
  const history = useNavigate();

  const [selectedPlayerSteamID, setSelectedPlayerSteamID] = useState<string | null>(null);
  const [selectedMatch, setSelectedMatch] = useState<string | null>(null);
  const [selectedMap, setSelectedMap] = useState<string | null>(null);
  const [selectedSubpage, setSelectedSubpage] = useState<string | null>(null);


  useEffect(() => {

    if (searchParams.has(QUERY_PARAMS.TAB) && parseInt(searchParams.get(QUERY_PARAMS.TAB) as string) < 6) {
      const newValue = parseInt(searchParams.get(QUERY_PARAMS.TAB) as string);
      setValue(newValue);
      setSelectedTab(newValue);
    }
  }, [searchParams, selectedPlayerSteamID, value]);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    console.log(`newValue: ${newValue}`);
    setValue(newValue);
    setSelectedTab(newValue);
    const newPath = `${location.pathname}?${QUERY_PARAMS.TAB}=${newValue}`;
    location.search = `?${QUERY_PARAMS.TAB}=${newValue}`;
    history(newPath);
  };

  return (
    <SelectedStatsContext.Provider value={{ selectedPlayerSteamID, setSelectedPlayerSteamID, selectedMatch, setSelectedMatch, selectedMap, setSelectedMap, selectedSubpage,
      setSelectedSubpage}}>
      <AppBar position="sticky">
        <Box padding={"8px 16px"}>
          <Typography variant="h5">{t(`${LANG_BASE_PATH}.title`)}</Typography>
        </Box>
      </AppBar>
      <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
        <Tabs
          value={value}
          onChange={handleChange}
          aria-label="basic tabs example"
          centered
          sx={{
            "& .MuiTabs-flexContainer": {
              flexWrap: "wrap",
            },
          }}
        >
          <Tab label={t(`${LANG_BASE_PATH}.tabs.0`)} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.1`)} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.2`)} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.3`)} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.4`)} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.5`)} />
          <Tab label={t(`${LANG_BASE_PATH}.tabs.6`)} />
        </Tabs>
      </Box>

      <Container>
        
        <Switch value={value}>
          <Case case={0}>
            <DemFilesContent />
          </Case>
          <Case case={1}>
            <PlayersContent />
          </Case>
          <Case case={2}>
            <ChartsContent />
          </Case>
          <Case case={3}>
            {selectedSubpage === "player" && selectedPlayerSteamID !== null && <PlayerContent steamid={selectedPlayerSteamID} />}
            {selectedSubpage === "match" && selectedMatch !== null && <MatchStatsContent match_id={selectedMatch} />}
            {selectedSubpage === "map" && selectedMap !== null && <MapContent mapName={selectedMap} />}
            {selectedSubpage === null && <StatsContent setSelectedTab={setSelectedTab} selectedTab={value} />}
          </Case>

          <Case case={4}>
            <RconContent />
          </Case>
          <Case case={5}>
            <DiscordBotContent />
          </Case>
          <Case case={6}>
            <ServerInfoContent />
          </Case>
        </Switch>
      </Container>
    </SelectedStatsContext.Provider>
  );
};

export default BaseLayout;
