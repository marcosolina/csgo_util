import { Chip } from "@mui/material";
import { IScoreChipProps } from "./interfaces";

const ScoreChip: React.FC<IScoreChipProps> = ({ score }) => {
  let color: "success" | "error" = "error";
  if (score >= 8) {
    color = "success";
  }

  return <Chip size="small" label={score} color={color} style={{ borderRadius: "4px" }} />;
};

export default ScoreChip;
