import { Route, Routes } from "react-router-dom";
import LeaderboardContent from "./leaderboard/LeaderboardContent";
import PlayerContent from "./players/PlayerContent";

const StatsContent = () => {
  return (
    <>
      <Routes>
        <Route
          path="/"
          element={
            <>
              <LeaderboardContent />
              {
                //<Box textAlign="center">
                //<Typography variant="h5">Recent Matches</Typography>
                //</Box>
                //<MatchContent setSelectedTab={setSelectedTab} selectedTab={selectedTab} />
                //<Box textAlign="center">
                //<Typography variant="h5">Kills Matrix</Typography>
                //</Box>
                //<KillMatrixContent setSelectedTab={setSelectedTab} selectedTab={selectedTab} />
              }
            </>
          }
        />
        <Route path="/player/:steamid" element={<PlayerContent />} />
      </Routes>
    </>
  );
};

export default StatsContent;
