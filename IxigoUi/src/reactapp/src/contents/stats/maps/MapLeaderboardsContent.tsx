
import React from 'react';

interface MapLeaderboardsContentProps {
  mapName: string;
}

const MapLeaderboardsContent: React.FC<MapLeaderboardsContentProps> = ({ mapName }) => {
  return <div>Map Leaderboards Content for { mapName }</div>;
};

export default MapLeaderboardsContent;
