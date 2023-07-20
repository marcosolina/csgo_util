import React from 'react';
import { Box, Tab, Tabs, Typography } from '@mui/material';
import MatchScoreboardContent from './MatchScoreboardContent';
import MatchRoundsContent from './MatchRoundsContent';
import MatchWeaponsContent from './MatchWeaponsContent';
import MatchDuelsContent from './MatchDuelsContent';

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

interface MatchStatsContentProps {
  match_id: number;
}

export default function MatchPage({ match_id }: MatchStatsContentProps) {
  const [value, setValue] = React.useState(0);
  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  return (
    <Box sx={{display: 'flex' }}>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={handleChange} aria-label="basic tabs example" orientation="vertical">
          <Tab label="Scoreboard" />
          <Tab label="Rounds" />
          <Tab label="Weapons" />
          <Tab label="Duels" />
        </Tabs>
      </Box>
      <Box sx={{ width: '100%' }}>
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
          <MatchDuelsContent match_id={match_id} />
        </TabPanel>
      </Box>
    </Box>
  );
}
