import { useEffect } from "react";
import PlayersGraphIntputs from "./PlayersGraphInputs";
import PlayersGraphStatsChart from "./PlayersGraphStatsChart";
import { usePlayerGraphContentProvider } from "./usePlayersGraphContentProvider";
import { useParams } from "react-router-dom";

const PlayerGraphsContent = () => {
  const { steamid } = useParams();
  const contentProvider = usePlayerGraphContentProvider();
  useEffect(() => {
    contentProvider.setSteamId(steamid);
  }, [steamid, contentProvider]);

  if (!steamid) {
    return null; // return a better component here
  }

  return (
    <>
      <PlayersGraphIntputs />
      <PlayersGraphStatsChart />
    </>
  );
};

export default PlayerGraphsContent;
