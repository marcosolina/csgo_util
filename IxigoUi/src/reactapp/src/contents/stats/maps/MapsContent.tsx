import { useNavigate } from "react-router-dom";
import { MATCH_RESULTS_REQUEST, useGetStats } from "../../../services/stats";
import { useMemo } from "react";
import { QueryStatus } from "../../../lib/http-requests";
import Switch from "../../../common/switch-case/Switch";
import Case from "../../../common/switch-case/Case";
import MapIcon from "@mui/icons-material/Map";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import { IconButton, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Skeleton } from "@mui/material";

const HEIGHT = 60;

interface IMap {
  mapName: string;
  displayName: string;
}

const MapsContent = () => {
  const history = useNavigate();
  const qGetStats = useGetStats(MATCH_RESULTS_REQUEST);
  const onClickHandler = (mapName: string) => {
    history(`${mapName}`);
  };

  const maps = useMemo(() => {
    if (!qGetStats.data?.data?.view_data) return [];
    const uniqueNames: Map<string, IMap> = new Map();
    qGetStats.data?.data?.view_data
      .map((match) => match.mapname)
      .forEach((mapName) => {
        if (uniqueNames.has(mapName)) return;
        const myArray = mapName.split("_");
        let map: IMap;
        if (myArray[0] === "workshop") {
          map = {
            mapName: mapName,
            displayName: myArray.slice(2).join("_"),
          };
        } else {
          map = {
            mapName: mapName,
            displayName: mapName,
          };
        }
        uniqueNames.set(mapName, map);
      });

    const uniqueMaps = Array.from(uniqueNames.values());
    return uniqueMaps.sort((a, b) => a.displayName.localeCompare(b.displayName));
  }, [qGetStats.data?.data]);

  return (
    <Switch value={qGetStats.status}>
      <Case case={QueryStatus.loading}>
        <Skeleton animation="wave" height={HEIGHT} />
        <Skeleton animation="wave" height={HEIGHT} />
        <Skeleton animation="wave" height={HEIGHT} />
        <Skeleton animation="wave" height={HEIGHT} />
      </Case>
      <Case case={QueryStatus.success}>
        <List>
          {maps.map((map) => (
            <ListItem
              key={map.mapName}
              secondaryAction={
                <IconButton edge="end" aria-label="comments">
                  <ChevronRightIcon />
                </IconButton>
              }
              disablePadding
              onClick={() => onClickHandler(map.mapName)}
            >
              <ListItemButton>
                <ListItemIcon>
                  <MapIcon />
                </ListItemIcon>
                <ListItemText primary={map.displayName} />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
      </Case>
    </Switch>
  );
};

export default MapsContent;
