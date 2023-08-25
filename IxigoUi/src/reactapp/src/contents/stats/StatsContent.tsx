import { Route, Routes, useLocation } from "react-router-dom";
import LeaderboardContent from "./leaderboard/LeaderboardContent";
import PlayerContent from "./players/PlayerContent";
import MatchStatsContent from "./matches/MatchStatsContent";
import MapContent from "./maps/MapContent";
import { Breadcrumbs } from "@mui/material";
import Link from "@mui/material/Link";
import { useMemo } from "react";
import { useTranslation } from "react-i18next";
import MatchContent from "./match/MatchContent";
import PlayersContent from "./players/PlayersContent";
import KillMatrixContent from "./kill-matrix/KillMatrixContent";
import MapsContent from "./maps/MapsContent";

const BREAD_CRUMBS_TEXT = "page.stats.breadcrumbs";

const StatsContent = () => {
  const { t } = useTranslation();
  const location = useLocation();
  const pathnames = location.pathname.split("/").filter((x) => x);

  const paths = useMemo(() => {
    const paths = [];
    for (let i = 0; i < pathnames.length; i++) {
      paths.push(`/${pathnames.slice(0, i + 1).join("/")}`);
    }
    return paths;
  }, [pathnames]);

  return (
    <>
      <Breadcrumbs aria-label="breadcrumb">
        {paths.map((path, index) => (
          <Link underline="hover" color="inherit" href={path} key={index}>
            {t(`${BREAD_CRUMBS_TEXT}.${path}`, pathnames[index])}
          </Link>
        ))}
      </Breadcrumbs>
      <Routes>
        <Route
          path="/"
          element={
            <>
              <LeaderboardContent />
              <MatchContent />
              <KillMatrixContent />
            </>
          }
        />
        <Route path="/player" element={<PlayersContent />} />
        <Route path="/player/:steamid" element={<PlayerContent />} />
        <Route path="/player/:steamid/:playertab" element={<PlayerContent />} />
        <Route path="/match" element={<MatchContent />} />
        <Route path="/match/:match_id" element={<MatchStatsContent />} />
        <Route path="/match/:match_id/:matchtab" element={<MatchStatsContent />} />
        <Route path="/map" element={<MapsContent />} />
        <Route path="/map/:mapName" element={<MapContent />} />
      </Routes>
    </>
  );
};

export default StatsContent;
