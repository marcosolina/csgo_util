import { Box } from "@mui/material";
import AvgScorePerMap from "./avg-score-per-map/AvgScorePerMap";
import MapsPlayedCounter from "./MapsPlayedCounter";
import { DEFAULT_SPACING } from "../../lib/constants";

const ChartsContent = () => {
  return (
    <>
      <MapsPlayedCounter />
      <Box padding={DEFAULT_SPACING * 2} />
      <AvgScorePerMap />
    </>
  );
};

export default ChartsContent;
