import {
  Card,
  CardContent,
  CardMedia,
  Chip,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Stack,
  Typography,
} from "@mui/material";
import Case from "../../common/switch-case/Case";
import Switch from "../../common/switch-case/Switch";
import { UI_CONTEXT_PATH } from "../../lib/constants";
import { QueryStatus } from "../../lib/http-requests";
import { IIxigoTeamProps } from "./interfaces";
import LoadingTeam from "./LoadingTeam";

const IxigoTeam: React.FC<IIxigoTeamProps> = (props) => {
  return (
    <Card variant="outlined">
      <CardMedia component="img" image={`${UI_CONTEXT_PATH}${props.picture}`} />
      <CardContent>
        <Stack direction="row" spacing={2} justifyContent="space-between" alignItems={"center"}>
          <Typography gutterBottom variant="h5" component="div">
            {props.title}
          </Typography>
          {props.team?.team_score && <Chip label={props.team?.team_score} color="primary" size="small" />}
        </Stack>
        <Switch value={props.status}>
          <Case case={QueryStatus.loading}>
            <LoadingTeam />
          </Case>
          <Case case={QueryStatus.success}>
            <List>
              {props.team?.team_members?.map((player) => {
                const playerScoreFixed = player.split_score.toFixed(2);
                const score = playerScoreFixed.length < 5 ? "0" + playerScoreFixed : playerScoreFixed;
                return (
                  <ListItem disablePadding key={player.steam_id}>
                    <ListItemButton sx={{ paddingRight: 0 }}>
                      <ListItemText primary={player.user_name} />
                      <Chip label={score} size="small" variant="outlined" color="primary" />
                    </ListItemButton>
                  </ListItem>
                );
              })}
            </List>
          </Case>
        </Switch>
      </CardContent>
    </Card>
  );
};

export default IxigoTeam;
