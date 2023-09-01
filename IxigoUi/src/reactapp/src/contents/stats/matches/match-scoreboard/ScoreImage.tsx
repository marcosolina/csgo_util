import React from 'react';
import terroristLogo from "../../../../assets/icons/T.png";
import ctLogo from "../../../../assets/icons/CT.png";
import { ScoreImageProps } from './interfaces';



const ScoreImage: React.FC<ScoreImageProps> = ({ teamType, score, opacity = 0.5, color = "white" }) => {
  const logo = teamType === "terrorist" ? terroristLogo : ctLogo;
  const altText = teamType === "terrorist" ? "Terrorist logo" : "CT logo";

  return (
    <div style={{ position: "relative", width: 25, height: 25 }}>
      <img
        src={logo}
        alt={altText}
        style={{ width: "100%", height: "100%", opacity: opacity }}
      />
      <div
        style={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          color: color,
          fontWeight: "bold",
        }}
      >
        {score}
      </div>
    </div>
  );
}

export default ScoreImage;
