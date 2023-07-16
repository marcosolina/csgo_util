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
import ServerInfoContent from "../../contents/server-info/ServerInfoContent";
import Case from "../switch-case/Case";
import Switch from "../switch-case/Switch";
import { useSearchParams } from "react-router-dom";
import { QUERY_PARAMS } from "../../lib/constants";

const LANG_BASE_PATH = "page.home";

const BaseLayout = () => {
  const { t } = useTranslation();
  const [value, setValue] = useState(0);
  const [searchParams] = useSearchParams();

  useEffect(() => {
    if (searchParams.has(QUERY_PARAMS.TAB) && parseInt(searchParams.get(QUERY_PARAMS.TAB) as string) < 6) {
      setValue(parseInt(searchParams.get(QUERY_PARAMS.TAB) as string));
    }
  }, [searchParams]);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
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
        <Box sx={{ m: "2rem" }} />
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
            <StatsContent />
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
    </>
  );
};

export default BaseLayout;
