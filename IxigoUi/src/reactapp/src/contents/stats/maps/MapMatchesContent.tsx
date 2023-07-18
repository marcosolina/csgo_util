
import React from 'react';

interface MapMatchesContentProps {
  mapName: string;
}

const MapMatchesContent: React.FC<MapMatchesContentProps> = ({ mapName }) => {
  return <div>Map Matches Content for { mapName }</div>;
};

export default MapMatchesContent;
