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

  const RoundEndReasonCell: React.FC<{ cell: any }> = ({ cell }) => {
    const reason = cell.getValue() as number;
    const imageUrl = roundIconImage[reason];
    const reasonText = round_end_reasons[reason];
    return imageUrl ? (
      <Tooltip title={reasonText}>
        <img src={imageUrl} alt={reasonText} style={{ height: '30px' }} />
      </Tooltip>
    ) : null;
  };

export default RoundEndReasonCell;
