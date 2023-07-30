import { Chip } from "@mui/material";
import { IHltvChipProps } from "./interfaces";

const HltvChip: React.FC<IHltvChipProps> = ({ rating }) => {
  const decimals = 2;

  let color: "info" | "warning" | "success" | "error" = "error";
  if (rating >= 1.5) {
    color = "info";
  }
  if (rating >= 0.85 && rating < 1.1) {
    color = "warning";
  }
  if (rating >= 1.1 && rating < 1.5) {
    color = "success";
  }

  return <Chip size="small" label={rating.toFixed(decimals)} color={color} style={{ borderRadius: "4px" }} />;
};

export default HltvChip;
