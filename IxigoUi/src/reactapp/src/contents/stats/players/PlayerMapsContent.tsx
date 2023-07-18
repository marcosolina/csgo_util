
import React from 'react';

interface PlayerMapsContentProps {
  steamid: string;
}

const PlayerMapsContent: React.FC<PlayerMapsContentProps> = ({ steamid }) => {
  return <div>Player Maps Content for { steamid }</div>;
};

export default PlayerMapsContent;
