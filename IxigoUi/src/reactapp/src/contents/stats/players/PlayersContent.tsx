import { IconButton, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Skeleton } from "@mui/material";
import Case from "../../../common/switch-case/Case";
import Switch from "../../../common/switch-case/Switch";
import { QueryStatus } from "../../../lib/http-requests";
import { USERS_REQUEST, useGetStats } from "../../../services/stats";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import { useNavigate } from "react-router-dom";
import { useMemo } from "react";

const LANG_BASE_PATH = "page.stats.player";

const HEIGHT = 60;

const PlayersContent = () => {
  const history = useNavigate();
  const qUsersRequest = useGetStats(USERS_REQUEST);
  const onClickHandler = (steamid: string) => {
    history(`${steamid}`);
  };

  const users = useMemo(() => {
    if (!qUsersRequest.data?.data?.view_data) return [];
    return qUsersRequest.data?.data?.view_data.sort((a, b) => a.user_name.localeCompare(b.user_name));
  }, [qUsersRequest.data?.data?.view_data]);

  return (
    <Switch value={qUsersRequest.status}>
      <Case case={QueryStatus.loading}>
        <Skeleton animation="wave" height={HEIGHT} />
        <Skeleton animation="wave" height={HEIGHT} />
        <Skeleton animation="wave" height={HEIGHT} />
        <Skeleton animation="wave" height={HEIGHT} />
      </Case>
      <Case case={QueryStatus.success}>
        <List>
          {users.map((user) => (
            <ListItem
              key={user.steam_id}
              secondaryAction={
                <IconButton edge="end" aria-label="comments">
                  <ChevronRightIcon />
                </IconButton>
              }
              disablePadding
              onClick={() => onClickHandler(user.steam_id)}
            >
              <ListItemButton>
                <ListItemIcon>
                  <AccountCircleIcon />
                </ListItemIcon>
                <ListItemText primary={user.user_name} />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
      </Case>
    </Switch>
  );
};

export default PlayersContent;
