
import React from 'react';

interface PlayerMatchesContentProps {
  steamid: string;
}

const PlayerMatchesContent: React.FC<PlayerMatchesContentProps> = ({ steamid }) => {
  return <div>Player Matches Content for { steamid }</div>;
};

export default PlayerMatchesContent;
