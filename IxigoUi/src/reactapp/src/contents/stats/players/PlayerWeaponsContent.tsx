
import React from 'react';

interface PlayerWeaponsContentProps {
  steamid: string;
}

const PlayerWeaponsContent: React.FC<PlayerWeaponsContentProps> = ({ steamid }) => {
  return <div>Player Weapons Content for { steamid }</div>;
};

export default PlayerWeaponsContent;
