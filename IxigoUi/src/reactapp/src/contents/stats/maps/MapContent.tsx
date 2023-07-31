import React from 'react';
import { Box, Tab, Tabs, Typography, Paper } from '@mui/material';
import MapLeaderboardsContent from './MapLeaderboardsContent';
import MapWeaponsContent from './MapWeaponsContent';
import MapMatchesContent from './MapMatchesContent';
import { UI_CONTEXT_PATH } from "../../../lib/constants";
import { useParams } from "react-router-dom";

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;
  return (
    <div role="tabpanel" hidden={value !== index} id={`simple-tabpanel-${index}`} aria-labelledby={`simple-tab-${index}`} {...other}>
      {value === index && (
        <Box sx={{ p: 3 }}>
          <Typography component="div">{children}</Typography>
        </Box>
      )}
    </div>
  );
}


export default function MapContent() {
  let { mapName } = useParams();
  const [value, setValue] = React.useState(0);
  if (!mapName) {
    return null; // TODO return a "warining" component or something similar
  }
  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  let mapImageName: string = mapName;

  if (mapImageName.startsWith("workshop_")) {
    let count: number = 0;
    mapImageName = mapImageName.replace(/_/g, (match: string): string => {
      count++;
      return (count <= 2) ? '-' : match;
    });
  }

  return (
    <Box sx={{ width: '100%'}}>
      <Box sx={{borderRadius: '4px' }} textAlign="center" height="70px" style={{ display: 'flex', justifyContent: 'center', backgroundImage: `url(${UI_CONTEXT_PATH}/maps/${mapImageName}.jpg)`,backgroundPosition: 'center', backgroundSize: '100%',backgroundRepeat: 'no-repeat'}}>
        <Paper elevation={3} style={{  height: "50%", width: "50%", backgroundColor: 'rgba(0, 0, 0, 0.6)' , padding: "5px" }}>
          <Typography variant="h5">Map: { mapName }</Typography>
          </Paper>
        </Box>
        <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={handleChange} aria-label="basic tabs example" orientation="horizontal"          centered
          sx={{
            "& .MuiTabs-flexContainer": {
              flexWrap: "wrap",
            },
          }}>
          <Tab label="Leaderboards" />
          <Tab label="Weapons" />
          <Tab label="Matches" />
        </Tabs>
      </Box>
      <Box sx={{ width: '100%' }}>
        <TabPanel value={value} index={0}>
          <MapLeaderboardsContent mapName={mapName} />
        </TabPanel>
        <TabPanel value={value} index={1}>
          <MapWeaponsContent mapName={mapName} />
        </TabPanel>
        <TabPanel value={value} index={2}>
          <MapMatchesContent mapName={mapName} />
        </TabPanel>
      </Box>
    </Box>
  );
}
