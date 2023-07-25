import React, { useState } from 'react';
import PlayerGraphsStatsContent from './PlayerGraphsStatsContent';
import IxigoSelect from "../../../common/select/IxigoSelect";
import IxigoSelectMultiple from "../../../common/select/IxigoSelectMultiple";
import { Box, Grid, TextField  } from '@mui/material';

interface PlayerGraphsContentProps {
  steamid: string;
}

const PlayerGraphsContent: React.FC<PlayerGraphsContentProps> = ({ steamid }) => {
  const binningLevels = [
    { value: 'week', label: 'Week' },
    { value: 'month', label: 'Month' },
  ];
  const [binningLevel, setBinningLevel] = useState<'week' | 'month'>('week');

  const fieldNamesOptions = [
    { value: 'kills', label: 'Total Kills' },
    { value: 'deaths', label: 'Total Deaths' },
    { value: 'assists', label: 'Total Assists' },
    { value: 'score', label: 'Score' },
    { value: 'rws', label: 'Round Win Share' },
    { value: 'headshots', label: 'Headshots' },
    { value: 'headshot_percentage', label: 'Headshot Percentage' },
    { value: 'mvp', label: 'Most Valuable Player' },
    { value: 'hltv_rating', label: 'HLTV Rating' },
    { value: 'adr', label: 'Average Damage per Round' },
    { value: 'kpr', label: 'Kills Per Round' },
    { value: 'dpr', label: 'Deaths Per Round' },
    { value: 'kdr', label: 'Kill/Death Ratio' },
    { value: 'hr', label: 'Hostages Rescued' },
    { value: 'bp', label: 'Bomb Planted' },
    { value: 'ud', label: 'Utility Damage' },
    { value: 'ffd', label: 'Friendly Fire Damage' },
    { value: 'td', label: 'Trade Deaths' },
    { value: 'tda', label: 'Total Damage Armour' },
    { value: 'tdh', label: 'Total Damage Health' },
    { value: 'fa', label: 'Flash Assists' },
    { value: 'ebt', label: 'Enemy Blind Time' },
    { value: 'fbt', label: 'Friendly Blind Time' },
    { value: 'ek', label: 'Entry Kills' },
    { value: 'tk', label: 'Trade Kills' },
    { value: '_1k', label: 'One Kill' },
    { value: '_2k', label: 'Two Kills' },
    { value: '_3k', label: 'Three Kills' },
    { value: '_4k', label: 'Four Kills' },
    { value: '_5k', label: 'Five Kills' },
    { value: '_1v1', label: '1v1 Clutches' },
    { value: '_1v2', label: '1v2 Clutches' },
    { value: '_1v3', label: '1v3 Clutches' },
    { value: '_1v4', label: '1v4 Clutches' },
    { value: '_1v5', label: '1v5 Clutches' },
  ];
  const [selectedFieldNames, setSelectedFieldNames] = useState<string[]>(['hltv_rating', 'kills', 'headshot_percentage', 'adr']);

  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column' }}> 
      <Grid container spacing={3}>
        <Grid item xs={6}>
          <IxigoSelectMultiple
            label="Graphs"
            possibleValues={fieldNamesOptions}
            selectedValues={selectedFieldNames}
            onChange={setSelectedFieldNames}
          />
        </Grid>
        <Grid item xs={2}>
          <IxigoSelect
            label="Binning Level"
            possibleValues={binningLevels}
            selectedValue={binningLevel}
            onChange={value => setBinningLevel(value as 'week' | 'month')}
          />
        </Grid>
        <Grid item xs={2}>
          <TextField
            label="Start Date"
            type="date"
            value={startDate ? startDate.toISOString().substring(0, 10) : ""}
            onChange={e => setStartDate(new Date(e.target.value))}
            InputLabelProps={{ shrink: true }}
          />
        </Grid>
        <Grid item xs={2}>
          <TextField
            label="End Date"
            type="date"
            value={endDate ? endDate.toISOString().substring(0, 10) : ""}
            onChange={e => setEndDate(new Date(e.target.value))}
            InputLabelProps={{ shrink: true }}
          />
        </Grid>

      </Grid>
      {selectedFieldNames.map((fieldName) => {
        const isLastGraph = fieldName === selectedFieldNames[selectedFieldNames.length - 1];
        return (
          <Box key={`${fieldName}-${isLastGraph}`} sx={{ height: isLastGraph ? 290 : 200 }}>
            <PlayerGraphsStatsContent
              steamid={steamid}
              fieldName={fieldName}
              label={fieldNamesOptions.find(option => option.value === fieldName)?.label || ''}
              binningLevel={binningLevel}
              showXAxisLabels={isLastGraph}
              startDate={startDate}
              endDate={endDate}
            />
          </Box>
        );
      })}
    </Box>
  );
};

export default PlayerGraphsContent;
