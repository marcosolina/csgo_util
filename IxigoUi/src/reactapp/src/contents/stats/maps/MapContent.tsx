import React from 'react';
import { Box, Tab, Tabs, Typography } from '@mui/material';
import MapLeaderboardsContent from './MapLeaderboardsContent';
import MapWeaponsContent from './MapWeaponsContent';
import MapMatchesContent from './MapMatchesContent';

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

interface MapContentProps {
  mapName: string;
}

export default function MapContent({ mapName }: MapContentProps) {
  const [value, setValue] = React.useState(0);
  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  return (
    <Box sx={{ display: 'flex' }}>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={handleChange} aria-label="basic tabs example" orientation="vertical">
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
