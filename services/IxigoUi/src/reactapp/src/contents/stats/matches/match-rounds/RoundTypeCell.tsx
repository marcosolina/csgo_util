import React from 'react';
import { Tooltip } from "@mui/material";
import eco from "../../../../assets/icons/eco.png";
import force from "../../../../assets/icons/forcebuy.png";
import full from "../../../../assets/icons/fullbuy.png";
import pistol from "../../../../assets/icons/pistol.png";
import { UI_CONTEXT_PATH } from "../../../../lib/constants";

  
  const roundTypeIconImage: { [key: string]: string } = {
    pistol: pistol,
    eco: UI_CONTEXT_PATH + eco,
    "force buy": force,
    "full buy": full,
  };
  

  const RoundTypeCell: React.FC<{ cell: any; row: { index: number }; team: "team1" | "team2" }> = ({
    cell,
    row,
    team,
  }) => {
    const teamData = cell.row.original[team];
    if (!teamData) {
      // The data for this team does not exist, possibly because the match is not over yet
      return null;
    }
    const { round_type} = teamData;
    const imageUrl = roundTypeIconImage[round_type];
    return imageUrl ? (
      <Tooltip title={round_type}>
        <img src={imageUrl} alt={round_type} style={{ height: '30px' }} />
      </Tooltip>
    ) : null;
  };
  
  export default RoundTypeCell;
