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
import { UI_CONTEXT_PATH } from "../../lib/constants";
import { ICsgoUser } from "../../services/dem-manager";
import { useStatsContent } from "./useStatsContent";

const BREAD_CRUMBS_TEXT = "page.stats.breadcrumbs";

interface IBreadCrumbPaths {
  keys: string[];
  values: string[];
}

const findSteamName = (steamid: string, steamUsers: ICsgoUser[]): string | undefined => {
  return steamUsers.find((user) => user.steam_id === steamid)?.user_name;
};

const StatsContent = () => {
  const { t } = useTranslation();
  const location = useLocation();
  const { steamUsers } = useStatsContent();
  const pathnames = location.pathname.split("/").filter((x) => x);

  const breadcrumbsPaths = useMemo(() => {
    const paths = [];
    const startSlice = UI_CONTEXT_PATH === "" ? 0 : 1;
    for (let i = startSlice; i < pathnames.length; i++) {
      paths.push(`/${pathnames.slice(startSlice, i + 1).join("/")}`);
    }

    const keys = pathnames.slice(startSlice);
    const steamId = keys.find((key) => !!findSteamName(key, steamUsers));

    const breadcrumbsPaths: IBreadCrumbPaths = {
      keys: keys.map((key) => (key === steamId ? findSteamName(key, steamUsers) || key : key)),
      values: paths,
    };
    return breadcrumbsPaths;
  }, [pathnames, steamUsers]);

  return (
    <>
      <Breadcrumbs aria-label="breadcrumb">
        {breadcrumbsPaths.values.map((path, index) => (
          <Link underline="hover" color="inherit" href={`${UI_CONTEXT_PATH}${path}`} key={index}>
            {t(`${BREAD_CRUMBS_TEXT}.${path}`, breadcrumbsPaths.keys[index])}
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
