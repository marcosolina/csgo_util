import { Box } from "@mui/material";
import AvgScorePerMap from "./avg-score-per-map/AvgScorePerMap";
import MapsPlayedCounter from "./MapsPlayedCounter";
import { DEFAULT_SPACING } from "../../lib/constants";
import TeamScorePerMap from "./team-score-per-map/TeamScorePerMap";

const ChartsContent = () => {
  return (
    <>
      <MapsPlayedCounter />
      <Box padding={DEFAULT_SPACING * 2} />
      <AvgScorePerMap />
      <Box padding={DEFAULT_SPACING * 2} />
      <TeamScorePerMap />
    </>
  );
};

export default ChartsContent;
