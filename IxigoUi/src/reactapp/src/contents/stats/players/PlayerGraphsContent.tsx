import React, { useState } from 'react';
import PlayerGraphsStatsContent from './PlayerGraphsStatsContent';
import IxigoSelect from "../../../common/select/IxigoSelect";
import IxigoSelectMultiple from "../../../common/select/IxigoSelectMultiple";
import { Box } from '@mui/material';

interface PlayerGraphsContentProps {
  steamid: string;
}

const PlayerGraphsContent: React.FC<PlayerGraphsContentProps> = ({ steamid }) => {
  const binningLevels = [
    { value: 'week', label: 'Week' },
    { value: 'month', label: 'Month' },
  ];
  const [binningLevel, setBinningLevel] = useState<'week' | 'month'>('week');


  return (
    <Box sx={{ display: 'flex', flexDirection: 'column' }}> 
      <IxigoSelect
        label="Binning Level"
        possibleValues={binningLevels}
        selectedValue={binningLevel}
        onChange={value => setBinningLevel(value as 'week' | 'month')}
      />
      <Box sx={{ height: 200 }}>
        <PlayerGraphsStatsContent steamid={steamid} fieldName="hltv_rating" label="HLTV Rating" binningLevel={binningLevel}  showXAxisLabels={false}/>
      </Box>
      <Box sx={{ height: 200 }}>
        <PlayerGraphsStatsContent steamid={steamid} fieldName="kills" label="Kills" binningLevel={binningLevel}  showXAxisLabels={false}/>
      </Box>
      <Box sx={{ height: 200 }}>
        <PlayerGraphsStatsContent steamid={steamid} fieldName="headshot_percentage" label="Headshot %" binningLevel={binningLevel} showXAxisLabels={false}/>
      </Box>
      <Box sx={{ height: 290 }}>
        <PlayerGraphsStatsContent steamid={steamid} fieldName="adr" label="Average Damage/Round" binningLevel={binningLevel} showXAxisLabels={true} />
      </Box>
    </Box>
  );
};

export default PlayerGraphsContent;
