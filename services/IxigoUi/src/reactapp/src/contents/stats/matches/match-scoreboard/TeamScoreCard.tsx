import React from "react";
import { Paper, Typography } from "@mui/material";
import ScoreImage from "./ScoreImage";
import { TeamScoreCardProps } from "./interfaces";

export const TeamScoreCard: React.FC<TeamScoreCardProps> = ({ totalWins, winsAsT, winsAsCt, teamName, color }) => {
  return (
    <Paper
      elevation={3}
      style={{
        width: "50%",
        backgroundColor: "rgba(0, 0, 0, 0.6)",
        padding: "5px",
      }}
    >
      <Typography
        variant="h2"
        align="center"
        style={{
          color: totalWins > 7 ? "white" : "grey",
        }}
      >
        {totalWins}
      </Typography>
      <Typography variant="h6" align="center" style={{ color: color }}>
        {teamName}
      </Typography>
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          gap: "10px",
        }}
      >
        <ScoreImage teamType="terrorist" score={winsAsT} />
        <ScoreImage teamType="counterTerrorist" score={winsAsCt} />
      </div>
    </Paper>
  );
};
