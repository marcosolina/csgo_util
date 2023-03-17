import { Box, Button, Card, CardActions, CardContent, CardMedia, Typography } from "@mui/material";
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
        <Typography variant="body2" color="text.secondary">
          Lizards are a widespread group of squamate reptiles, with over 6,000 species, ranging across all continents
          except Antarctica
        </Typography>
      </CardContent>
      <CardActions>
        <Button size="small">Share</Button>
        <Button size="small">Learn More</Button>
      </CardActions>
    </Card>
  );
};

export default IxigoTeam;
