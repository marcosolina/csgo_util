import React from "react";
import { Box, Tab, Tabs, Typography, Grid, Paper } from "@mui/material";
import MatchScoreboardContent from "./MatchScoreboardContent";
import MatchRoundsContent from "./MatchRoundsContent";
import MatchWeaponsContent from "./MatchWeaponsContent";
import MatchKillMatrixContent from "./MatchKillMatrixContent";
import { useQuery } from "react-query";
import terroristLogo from "../../../assets/icons/T.png";
import ctLogo from "../../../assets/icons/CT.png";
import { UI_CONTEXT_PATH } from "../../../lib/constants";
import { SERVICES_URLS } from "../../../lib/constants/paths";

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

interface MatchStatsContentProps {
  match_id: number;
}

export default function MatchPage({ match_id }: MatchStatsContentProps) {
  const [value, setValue] = React.useState(0);
  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  const { data: matchData, isLoading } = useQuery(["match", match_id], async () => {
    const url = new URL(`${SERVICES_URLS["dem-manager"]["get-stats"]}MATCH_RESULTS?match_id=${match_id}`);
    const response = await fetch(url.href);

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const json = await response.json();
    return json.view_data.find((match: any) => match.match_id === match_id);
  });

  if (isLoading) {
    return <Typography>Loading...</Typography>;
  }
  const date = new Date(matchData.match_date);
  const formattedDate = date.toLocaleDateString("en-GB", { year: "numeric", month: "long", day: "numeric" });
  const formattedTime = date.toLocaleTimeString("en-GB", { hour: "2-digit", minute: "2-digit" });

  let mapImageName: string = matchData.mapname;

  if (mapImageName.startsWith("workshop_")) {
    let count: number = 0;
    mapImageName = mapImageName.replace(/_/g, (match: string): string => {
      count++;
      return count <= 2 ? "-" : match;
    });
  }

  return (
    <Box sx={{ display: "flex", flexDirection: "column" }}>
      <Box
        sx={{ borderRadius: "4px" }}
        textAlign="center"
        style={{
          backgroundImage: `url(${UI_CONTEXT_PATH}/maps/${mapImageName}.jpg)`,
          backgroundPosition: "center",
          backgroundSize: "100%",
          backgroundRepeat: "no-repeat",
        }}
      >
        <Grid item xs={2}>
          <Paper elevation={3} style={{ backgroundColor: "rgba(0, 0, 0, 0.6)" }}>
            <Typography variant="h4">Match: {matchData.mapname}</Typography>
            <Typography variant="h6">{`${formattedDate}, ${formattedTime}`}</Typography>
          </Paper>
        </Grid>
        <Grid container spacing={3} alignItems="center" justifyContent="center">
          <Grid item xs={6} style={{ display: "flex", justifyContent: "right" }}>
            <Paper elevation={3} style={{ width: "50%", backgroundColor: "rgba(0, 0, 0, 0.6)", padding: "5px" }}>
              <Typography
                variant="h2"
                align="center"
                style={{ color: matchData.team1_total_wins > 7 ? "white" : "grey" }}
              >
                {matchData.team1_total_wins}
              </Typography>
              <Typography variant="h6" align="center" style={{ color: "#90caf9" }}>
                Team 1
              </Typography>
              <div style={{ display: "flex", justifyContent: "center", gap: "10px" }}>
                <div style={{ position: "relative", width: 25, height: 25 }}>
                  <img
                    src={UI_CONTEXT_PATH + terroristLogo}
                    alt="Terrorist logo"
                    style={{ width: "100%", height: "100%", opacity: 0.5 }}
                  />
                  <div
                    style={{
                      position: "absolute",
                      top: "50%",
                      left: "50%",
                      transform: "translate(-50%, -50%)",
                      color: "white",
                      fontWeight: "bold",
                    }}
                  >
                    {matchData.team1_wins_as_t}
                  </div>
                </div>
                <div style={{ position: "relative", width: 25, height: 25 }}>
                  <img
                    src={UI_CONTEXT_PATH + ctLogo}
                    alt="CT logo"
                    style={{ width: "100%", height: "100%", opacity: 0.5 }}
                  />
                  <div
                    style={{
                      position: "absolute",
                      top: "50%",
                      left: "50%",
                      transform: "translate(-50%, -50%)",
                      color: "white",
                      fontWeight: "bold",
                    }}
                  >
                    {matchData.team1_wins_as_ct}
                  </div>
                </div>
              </div>
            </Paper>
          </Grid>
          <Grid item xs={6} style={{ display: "flex", justifyContent: "left" }}>
            <Paper elevation={3} style={{ width: "50%", backgroundColor: "rgba(0, 0, 0, 0.6)", padding: "5px" }}>
              <Typography
                variant="h2"
                align="center"
                style={{ color: matchData.team2_total_wins > 7 ? "white" : "grey" }}
              >
                {matchData.team2_total_wins}
              </Typography>
              <Typography variant="h6" align="center" style={{ color: "orange" }}>
                Team 2
              </Typography>
              <div style={{ display: "flex", justifyContent: "center", gap: "10px" }}>
                <div style={{ position: "relative", width: 25, height: 25 }}>
                  <img
                    src={UI_CONTEXT_PATH + terroristLogo}
                    alt="Terrorist logo"
                    style={{ width: "100%", height: "100%", opacity: 0.5 }}
                  />
                  <div
                    style={{
                      position: "absolute",
                      top: "50%",
                      left: "50%",
                      transform: "translate(-50%, -50%)",
                      color: "white",
                      fontWeight: "bold",
                    }}
                  >
                    {matchData.team2_wins_as_t}
                  </div>
                </div>
                <div style={{ position: "relative", width: 25, height: 25 }}>
                  <img
                    src={UI_CONTEXT_PATH + ctLogo}
                    alt="CT logo"
                    style={{ width: "100%", height: "100%", opacity: 0.5 }}
                  />
                  <div
                    style={{
                      position: "absolute",
                      top: "50%",
                      left: "50%",
                      transform: "translate(-50%, -50%)",
                      color: "white",
                      fontWeight: "bold",
                    }}
                  >
                    {matchData.team2_wins_as_ct}
                  </div>
                </div>
              </div>
            </Paper>
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
          <MatchScoreboardContent match_id={match_id} />
        </TabPanel>
        <TabPanel value={value} index={1}>
          <MatchRoundsContent match_id={match_id} />
        </TabPanel>
        <TabPanel value={value} index={2}>
          <MatchWeaponsContent match_id={match_id} />
        </TabPanel>
        <TabPanel value={value} index={3}>
          <MatchKillMatrixContent match_id={match_id} />
        </TabPanel>
      </Box>
    </Box>
  );
}
