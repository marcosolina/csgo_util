import React from 'react';
import { Paper, Typography } from "@mui/material";

interface TeamScoreCardProps {
  totalWins: number;
  winsAsT: number;
  winsAsCt: number;
  teamName: string;
  color: string;
  alignment: "left" | "right";
  terroristLogo: string;
  ctLogo: string;
}

export const TeamScoreCard: React.FC<TeamScoreCardProps> = ({ totalWins, winsAsT, winsAsCt, teamName, color, alignment, terroristLogo, ctLogo }) => {
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
      <Typography
        variant="h6"
        align="center"
        style={{ color: color }}
      >
        {teamName}
      </Typography>
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          gap: "10px",
        }}
      >
        <div style={{ position: "relative", width: 25, height: 25 }}>
          <img
            src={terroristLogo}
            alt="Terrorist logo"
            style={{ width: "100%", height: "100%", opacity: 0.5 }}
          />
          <div
            style={{
              position: "absolute",
              top: "50%",
              left: "50%",
              transform: "translate(-50%, -50%)",
              color: "white",
              fontWeight: "bold",
            }}
          >
            {winsAsT}
          </div>
        </div>
        <div style={{ position: "relative", width: 25, height: 25 }}>
          <img
            src={ctLogo}
            alt="CT logo"
            style={{ width: "100%", height: "100%", opacity: 0.5 }}
          />
          <div
            style={{
              position: "absolute",
              top: "50%",
              left: "50%",
              transform: "translate(-50%, -50%)",
              color: "white",
              fontWeight: "bold",
            }}
          >
            {winsAsCt}
          </div>
        </div>
      </div>
    </Paper>
  );
}
