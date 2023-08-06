import React from 'react';
import { Tooltip } from "@mui/material";
import bomb from "../../../../assets/icons/bomb.png";
import death from "../../../../assets/icons/death.png";
import defuse from "../../../../assets/icons/defuse.png";
import rescue from "../../../../assets/icons/rescue.png";
import time from "../../../../assets/icons/time.png";


const roundIconImage: { [key: number]: string } = {
    1: bomb,
    7: defuse,
    8: death,
    9: death,
    11: rescue,
    12: time,
    13: time,
  };

const round_end_reasons: { [key: number]: string } = {
    1: "Target Successfully Bombed!",
    7: "The bomb has been defused!",
    8: "Counter-Terrorists Win!",
    9: "Terrorists Win!",
    11: "All Hostages have been rescued!",
    12: "Target has been saved!",
    13: "Hostages have not been rescued!",
  };

  const RoundEndReasonCell: React.FC<{ cell: any; row: { index: number }; team: "team1" | "team2" }> = ({
    cell,
    row,
    team,
  }) => {
    const teamData = cell.row.original[team];
    if (!teamData) {
      // The data for this team does not exist, possibly because the match is not over yet
      return null;
    }
    const { round_end_reason} = teamData;
    const imageUrl = roundIconImage[round_end_reason];
    const reasonText = round_end_reasons[round_end_reason];
    console.log("Rendering RoundEndReasonCell", {
        round_end_reason,
        imageUrl,
        reasonText,
      });
    return imageUrl ? (
      <Tooltip title={reasonText}>
        <img src={imageUrl} alt={reasonText} style={{ height: '30px' }} />
      </Tooltip>
    ) : null;
  };

export default RoundEndReasonCell;
