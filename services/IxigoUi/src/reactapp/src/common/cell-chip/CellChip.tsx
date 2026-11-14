import { Chip } from "@mui/material";
import { IChipProps } from "./interfaces";

const CustomChip: React.FC<IChipProps> = ({ value, type }) => {
  let color: "success" | "error" | "warning" | "info" = "error";;
  let label = value.toString();

  if (type === "score") {
    color = value >= 8 ? "success" : "error";
  } else if (type === "rating") {
    if (value >= 1.5) {
      color = "info";
    } else if (value >= 0.85 && value < 1.1) {
      color = "warning";
    } else if (value >= 1.1 && value < 1.5) {
      color = "success";
    } else {
      color = "error";
    }
    label = value.toFixed(2);
  }

  return (
    <Chip size="small" label={label} color={color} style={{ borderRadius: "4px" }} />
  );
};

export default CustomChip;

