import { Typography, Box } from "@mui/material";
import LeaderboardContent from "./leaderboard/LeaderboardContent";

const StatsContent = () => {
  return (
    <>
      <Box textAlign="center">
        <Typography variant="h5">Leaderboard</Typography>
      </Box>
      <LeaderboardContent />
      <Box textAlign="center">
        <Typography variant="h5">Recent Matches</Typography>
      </Box>
      {
        //<MatchContent setSelectedTab={setSelectedTab} selectedTab={selectedTab} />
      }
      <Box textAlign="center">
        <Typography variant="h5">Kills Matrix</Typography>
      </Box>
      {
        //<KillMatrixContent setSelectedTab={setSelectedTab} selectedTab={selectedTab} />
      }
    </>
  );
};

export default StatsContent;
