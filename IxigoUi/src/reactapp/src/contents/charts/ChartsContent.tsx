import { Box } from "@mui/material";
import { UI_CONTEXT_PATH } from "../../lib/constants";
import wkp from "../../assets/pictures/work-in-progress.png";

const ChartsContent = () => {
  return (
    <Box sx={{ width: "100%" }}>
      <img src={`${UI_CONTEXT_PATH}${wkp}`} width={"100%"} alt="" />
    </Box>
  );
};

export default ChartsContent;
