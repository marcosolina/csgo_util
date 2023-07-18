
import React from 'react';

interface MapWeaponsContentProps {
  mapName: string;
}

const MapWeaponsContent: React.FC<MapWeaponsContentProps> = ({ mapName }) => {
  return <div>Map Weapons Content for { mapName }</div>;
};

export default MapWeaponsContent;
