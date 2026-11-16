import React from 'react';
import ScoreImage from "../match-scoreboard/ScoreImage";

const ScoreCell: React.FC<{ cell: any; row: { index: number }; team: "team1" | "team2" }> = ({ cell, row, team }) => {
    const teamData = cell.row.original[team];
    if (!teamData) {
      // The data for this team does not exist, possibly because the match is not over yet
      return null;
    }

    const { round, team1_score, team2_score, winner_team } = teamData;
    const isTerrorist = (team === "team1" && round <= 7) || (team === "team2" && round > 7);
    const isWinner = (winner_team === 2 && isTerrorist) || (winner_team === 3 && !isTerrorist);
    const imageOpacity = isWinner ? 0.5 : 0.05;
    const colour = isWinner ? "white" : "dimgrey";

    return (
      <div style={{ position: "relative", width: 25, height: 25, display: "flex", justifyContent: team === "team1" ? "flex-end" : "flex-start" }}>
        <ScoreImage
          teamType={isTerrorist ? "terrorist" : "counterTerrorist"}
          score={team === "team1" ? team1_score : team2_score}
          opacity={imageOpacity}
          color={colour}
        />
      </div>
    );
};

export default ScoreCell;
