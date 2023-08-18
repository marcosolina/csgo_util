import { IPlayerGraphsContentProps } from "./interfaces";
import PlayersGraphIntputs from "./players-graph/PlayersGraphInputs";
import PlayersGraphStatsChart from "./players-graph/PlayersGraphStatsChart";
import { usePlayerGraphContentProvider } from "./players-graph/usePlayersGraphContentProvider";

const PlayerGraphsContent: React.FC<IPlayerGraphsContentProps> = ({ steamId }) => {
  const contentProvider = usePlayerGraphContentProvider();
  contentProvider.setSteamId(steamId);

  return (
    <>
      <PlayersGraphIntputs />
      <PlayersGraphStatsChart />
    </>
  );
};

export default PlayerGraphsContent;
