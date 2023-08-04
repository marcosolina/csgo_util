import React from "react";
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
import { useParams } from "react-router-dom";
import { useMatchStatsContent } from "./useMatchStatsContent";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import { TeamScoreCard } from "./match-scoreboard/TeamScoreCard";

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;
  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 3 }}>
          <Typography component="div">{children}</Typography>
        </Box>
      )}
    </div>
  );
}

export default function MatchPage() {
  let { match_id } = useParams();
  const matchIdNumber = Number(match_id);
  const [value, setValue] = React.useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  const {
    state,
    data: matchData,
    matchMetadata,
  } = useMatchStatsContent({ match_id: matchIdNumber });

  if (state === QueryStatus.loading) {
    return <Skeleton animation="wave" style={{ height: "100%" }} />;
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
                    teamName="Team 1"
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
                    teamName="Team 2"
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
              value={value}
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
              <Tab label="Scoreboard" />
              <Tab label="Rounds" />
              <Tab label="Weapons" />
              <Tab label="Duels" />
            </Tabs>
          </Box>
          <TabPanel value={value} index={0}>
            <MatchScoreboardContent match_id={Number(match_id)} />
          </TabPanel>
          <TabPanel value={value} index={1}>
            <MatchRoundsContent match_id={Number(match_id)} />
          </TabPanel>
          <TabPanel value={value} index={2}>
            <MatchWeaponsContent match_id={Number(match_id)} />
          </TabPanel>
          <TabPanel value={value} index={3}>
            <MatchKillMatrixContent match_id={Number(match_id)} />
          </TabPanel>
        </Box>
      </Box>
    );
  }

  return <ErrorOutlineIcon color="error" fontSize="large" />;
}
