import React, { useState } from "react";
import {
  Box,
  Tab,
  Tabs,
  Typography,
  Grid,
  Paper,
  Skeleton,
} from "@mui/material";
import MatchScoreboardContent from "./match-scoreboard/MatchScoreboardContent";
import MatchRoundsContent from "./match-rounds/MatchRoundsContent";
import MatchWeaponsContent from "./match-weapons/MatchWeaponsContent";
import MatchKillMatrixContent from "./match-kill-matrix/MatchKillMatrixContent";
import { QueryStatus } from "../../../lib/http-requests";
import terroristLogo from "../../../assets/icons/T.png";
import ctLogo from "../../../assets/icons/CT.png";
import { UI_CONTEXT_PATH } from "../../../lib/constants";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { useMatchStatsContent } from "./useMatchStatsContent";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import { TeamScoreCard } from "./match-scoreboard/TeamScoreCard";
import { useTranslation } from "react-i18next";
import Switch from "../../../common/switch-case/Switch";
import Case from "../../../common/switch-case/Case";

const LANG_BASE_PATH = "page.stats.match";

const HEIGHT = 60;
const TABS = {
  SCOREBOARD: "scoreboard",
  ROUNDS: "rounds",
  WEAPONS: "weapons",
  DUELS: "duels",
};

const MatchPage = () => {
  const { t } = useTranslation();
  let { match_id, matchtab } = useParams();
  const matchIdNumber = Number(match_id);
  const [selectedTab, setSelectedTab] = useState(matchtab || TABS.SCOREBOARD);
  const history = useNavigate();
  const location = useLocation();

  const {
    state,
    data: matchData,
    matchMetadata,
  } = useMatchStatsContent({ match_id: matchIdNumber });

  const handleChange = (event: React.SyntheticEvent, newValue: string) => {
    const pathParts = location.pathname.split("/").filter((p) => p);

    if (pathParts.length > 3) {
      pathParts[3] = newValue;
    } else {
      pathParts.push(newValue);
    }

    const newPath = pathParts.join("/");
    history(`/${newPath}`);
    setSelectedTab(newValue);
  };

  if (state === QueryStatus.loading) {
    return <Skeleton animation="wave" height={HEIGHT} />;
  }

  if (state === QueryStatus.success && matchData) {
    return (
      <Box sx={{ display: "flex", flexDirection: "column" }}>
        <Box
          sx={{ borderRadius: "4px" }}
          textAlign="center"
          style={{
            backgroundImage: `url(${UI_CONTEXT_PATH}/maps/${matchMetadata.mapImageName}.jpg)`,
            backgroundPosition: "center",
            backgroundSize: "100%",
            backgroundRepeat: "no-repeat",
          }}
        >
          <Grid item xs={2}>
            <Paper
              elevation={3}
              style={{ backgroundColor: "rgba(0, 0, 0, 0.6)" }}
            >
              <Typography variant="h4">Match: {matchData.mapname}</Typography>
              <Typography variant="h6">{`${matchMetadata.formattedDate}, ${matchMetadata.formattedTime}`}</Typography>
            </Paper>
          </Grid>
          <Grid
            container
            spacing={3}
            alignItems="center"
            justifyContent="center"
          >
            <Grid
              item
              xs={6}
              style={{ display: "flex", justifyContent: "right" }}
            >
                  <TeamScoreCard
                    totalWins={matchData.team1_total_wins}
                    winsAsT={matchData.team1_wins_as_t}
                    winsAsCt={matchData.team1_wins_as_ct}
                    teamName={`${t(`${LANG_BASE_PATH}.team`)} 1`}
                    color="#90caf9"
                    alignment="right"
                    terroristLogo={UI_CONTEXT_PATH + terroristLogo}
                    ctLogo={UI_CONTEXT_PATH + ctLogo}
                  />
            </Grid>
            <Grid
              item
              xs={6}
              style={{ display: "flex", justifyContent: "left" }}
            >
                  <TeamScoreCard
                    totalWins={matchData.team2_total_wins}
                    winsAsT={matchData.team2_wins_as_t}
                    winsAsCt={matchData.team2_wins_as_ct}
                    teamName={`${t(`${LANG_BASE_PATH}.team`)} 2`}
                    color="orange"
                    alignment="left"
                    terroristLogo={UI_CONTEXT_PATH + terroristLogo}
                    ctLogo={UI_CONTEXT_PATH + ctLogo}
                  />
            </Grid>
          </Grid>
        </Box>
        <Box sx={{ width: "100%" }}>
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
            <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.SCOREBOARD}`)} value={TABS.SCOREBOARD} />
            <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.ROUNDS}`)} value={TABS.ROUNDS} />
            <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.WEAPONS}`)} value={TABS.WEAPONS} />
            <Tab label={t(`${LANG_BASE_PATH}.tabs.${TABS.DUELS}`)} value={TABS.DUELS} />
            </Tabs>
          </Box>
          <Box sx={{ m: "2rem" }} />
            <Switch value={selectedTab}>
              <Case case={TABS.SCOREBOARD}>
                <MatchScoreboardContent match_id={Number(match_id)} />
              </Case>
              <Case case={TABS.ROUNDS}>
                <MatchRoundsContent match_id={Number(match_id)} />
              </Case>
              <Case case={TABS.WEAPONS}>
                <MatchWeaponsContent match_id={Number(match_id)} />
              </Case>
              <Case case={TABS.DUELS}>
                <MatchKillMatrixContent match_id={Number(match_id)} />
              </Case>
            </Switch>
        </Box>
      </Box>
    );
  }

  return <ErrorOutlineIcon color="error" fontSize="large" />;
}

export default MatchPage;