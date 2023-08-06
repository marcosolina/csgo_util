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
  

  const RoundTypeCell: React.FC<{ cell: any }> = ({ cell }) => {
    const type = cell.getValue() as string;
    const imageUrl = roundTypeIconImage[type];
    console.log(cell);
    return imageUrl ? (
      <Tooltip title={type}>
        <img src={imageUrl} alt={type} style={{ height: '30px' }} />
      </Tooltip>
    ) : null;
  };
  
  export default RoundTypeCell;
