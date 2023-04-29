import { Box } from "@mui/material";
import PlayersAvgScorePerMap from "./players-avg-score-per-map/PlayersAvgScorePerMap";
import MapsPlayedCounter from "./MapsPlayedCounter";
import { DEFAULT_SPACING } from "../../lib/constants";
import TeamScorePerMap from "./team-score-per-map/TeamScorePerMap";
import TeamsAvgScorePerMap from "./teams-avg-score-per-map/TeamsAvgScorePerMap";

const ChartsContent = () => {
  return (
    <>
      <MapsPlayedCounter />
      <Box padding={DEFAULT_SPACING * 2} />
      <PlayersAvgScorePerMap />
      <Box padding={DEFAULT_SPACING * 2} />
      <TeamScorePerMap />
      <Box padding={DEFAULT_SPACING * 2} />
      <TeamsAvgScorePerMap />
    </>
  );
};

export default ChartsContent;
