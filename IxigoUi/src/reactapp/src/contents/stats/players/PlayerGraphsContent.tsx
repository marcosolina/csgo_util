
import React from 'react';

interface PlayerGraphsContentProps {
  steamid: string;
}

const PlayerGraphsContent: React.FC<PlayerGraphsContentProps> = ({ steamid }) => {
  return <div>Player Graphs Content for { steamid }</div>;
};

export default PlayerGraphsContent;
