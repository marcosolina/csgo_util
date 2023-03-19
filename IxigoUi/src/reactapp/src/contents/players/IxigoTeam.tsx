import {
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Typography,
} from "@mui/material";
import { IIxigoTeamProps } from "./interfaces";

const IxigoTeam: React.FC<IIxigoTeamProps> = (props) => {
  return (
    <Card>
      <Box sx={{ width: "100%" }}>
        <img src={props.picture} width={"100%"} />
      </Box>
      <CardContent>
        <Typography gutterBottom variant="h5" component="div">
          {props.title}
        </Typography>
        <List>
          {props.team?.team_members?.map((player) => (
            <ListItem disablePadding key={player.steam_id}>
              <ListItemButton>
                <ListItemText primary={player.user_name} />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
      </CardContent>
      <CardActions>
        <Button size="small">Share</Button>
        <Button size="small">Learn More</Button>
      </CardActions>
    </Card>
  );
};

export default IxigoTeam;
