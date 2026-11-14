import React from 'react';
import PlayerBlob from './PlayerBlob'; // adjust the path as needed

const PlayerCountCell: React.FC<{ cell: any; row: { index: number }; team: "team1" | "team2" }> = ({
  cell,
  row,
  team,
}) => {
    const teamData = cell.row.original[team];
    if (!teamData) {
      // The data for this team does not exist, possibly because the match is not over yet
      return null;
    }

    const { player_count, death_count } = teamData;
    const playerBlobs = [];

    for (let i = 0; i < player_count; i++) {
      playerBlobs.push(<PlayerBlob key={i} isAlive={i >= death_count} team={team} />);
    }

    return (
      <div style={{ display: "flex", justifyContent: team === "team1" ? "flex-end" : "flex-start" }}>
        {team === "team1" ? playerBlobs : playerBlobs.reverse()}
      </div>
    );
};

export default PlayerCountCell;
