import React from "react";
import { Box, Tab, Tabs, Typography } from "@mui/material";
import PlayerStatsContent from "./PlayerStatsContent";
import PlayerGraphsContent from "./PlayerGraphsContent";
import PlayerWeaponsContent from "./PlayerWeaponsContent";
import PlayerMapsContent from "./PlayerMapsContent";
import PlayerMatchesContent from "./PlayerMatchesContent";
import { useQuery } from "react-query";
import { SERVICES_URLS } from "../../../lib/constants/paths";
import { useParams } from "react-router-dom";

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

export default function PlayerPage() {
  let { steamid } = useParams();
  const [value, setValue] = React.useState(0);
  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  // useQuery hook to fetch the user name
  const { data: userData } = useQuery(["user", steamid], async () => {
    const url = new URL(`${SERVICES_URLS["dem-manager"]["get-stats"]}USERS`);
    const response = await fetch(url.href);

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const json = await response.json();
    return json.view_data.find((user: any) => user.steam_id === steamid)?.user_name;
  });

  // TODO get the user name with the generic hook: const qUsersRequest = useGetStats(USERS_REQUEST);
  const userName = userData || steamid; // use user_name if available, fallback to steamid

  if (!steamid) {
    return null; // TODO return a "warining" component or something similar
  }

  return (
    <Box sx={{ display: "flex", flexDirection: "column" }}>
      <Box textAlign="center">
        <Typography variant="h5">Player: {userName}</Typography>
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
            <Tab label="Overall" />
            <Tab label="Graphs" />
            <Tab label="Weapons" />
            <Tab label="Maps" />
            <Tab label="Matches" />
          </Tabs>
        </Box>
        <Box sx={{ width: "100%" }}>
          <TabPanel value={value} index={0}>
            <PlayerStatsContent steamid={steamid} />
          </TabPanel>
          <TabPanel value={value} index={1}>
            <PlayerGraphsContent steamid={steamid} />
          </TabPanel>
          <TabPanel value={value} index={2}>
            <PlayerWeaponsContent steamid={steamid} />
          </TabPanel>
          <TabPanel value={value} index={3}>
            <PlayerMapsContent steamid={steamid} />
          </TabPanel>
          <TabPanel value={value} index={4}>
            <PlayerMatchesContent steamid={steamid} />
          </TabPanel>
        </Box>
      </Box>
    </Box>
  );
}
