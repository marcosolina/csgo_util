
import React from 'react';

interface PlayerStatsContentProps {
  steamid: string;
}

const PlayerStatsContent: React.FC<PlayerStatsContentProps> = ({ steamid }) => {
  return <div>Player Stats Content for { steamid }</div>;
};

export default PlayerStatsContent;
