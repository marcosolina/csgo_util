import React from 'react';
import terroristLogo from "../../../../assets/icons/T.png";
import ctLogo from "../../../../assets/icons/CT.png";
import { UI_CONTEXT_PATH } from "../../../../lib/constants";

const ScoreCell: React.FC<{ cell: any; row: { index: number }; team: "team1" | "team2" }> = ({ cell, row, team }) => {
    const teamData = cell.row.original[team];
    if (!teamData) {
      // The data for this team does not exist, possibly because the match is not over yet
      return null;
    }

    const { round, team1_score, team2_score, winner_team } = teamData;
    const isTerrorist = (team === "team1" && round <= 7) || (team === "team2" && round > 7);
    const logo = isTerrorist ? terroristLogo : ctLogo;
    const isWinner = (winner_team === 2 && isTerrorist) || (winner_team === 3 && !isTerrorist);
    const imageOpacity = isWinner ? 0.5 : 0.05;
    const colour = isWinner ? "white" : "dimgrey";

    return (
      <div style={{ position: "relative", width: 25, height: 25 }}>
        <img
          src={UI_CONTEXT_PATH + logo}
          alt={isTerrorist ? "Terrorist logo" : "CT logo"}
          style={{ width: "100%", height: "100%", opacity: imageOpacity }}
        />
        <div
          style={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            color: colour,
            fontWeight: "bold",
          }}
        >
          {team === "team1" ? team1_score : team2_score}
        </div>
      </div>
    );
};

export default ScoreCell;
