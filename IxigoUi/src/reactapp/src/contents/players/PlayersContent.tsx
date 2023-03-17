import {
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Grid,
  List,
  ListItem,
  ListSubheader,
  Typography,
} from "@mui/material";
import { useMemo, useState } from "react";
import { IxigoTextType } from "../../common/input";
import IxigoText from "../../common/input/IxigoText";
import { IxigoPossibleValue } from "../../common/select";
import IxigoSelect from "../../common/select/IxigoSelect";
import Case from "../../common/switch-case/Case";
import Switch from "../../common/switch-case/Switch";
import { DEFAULT_SPACING } from "../../lib/constants";
import { QueryStatus } from "../../lib/http-requests";
import ct from "../../assets/bots/counterterrorist.jpg";
import terr from "../../assets/bots/terrorist.jpg";
import Loading from "./Loading";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import { usePlayersContent } from "./usePlayersContent";
import IxigoSwitch from "../../common/switch/IxigoSwitch";
import IxigoTeam from "./IxigoTeam";

const XS = 12;
const SM = 12;
const MD = 6;
const LG = 4;
const XL = 3;

const PlayersContent = () => {
  const [penaltyWeight, setPenaltyWeight] = useState<number>(0.4);
  const [percPlayed, setPercPlayed] = useState<number>(90);
  const [roundsToConsider, setRoundsToConsider] = useState<number>(50);
  const [scoreType, setScoreType] = useState<string>("HLTV");

  const pContent = usePlayersContent();

  const typesResp = pContent.scoreTypes;

  const types = useMemo(() => {
    const arr: IxigoPossibleValue[] = [];
    if (!typesResp) {
      return arr;
    }
    Object.keys(typesResp).forEach(function (key, index) {
      arr.push({
        value: key,
        label: typesResp[key],
      });
    });
    arr.sort((a, b) => a.label.localeCompare(b.label));
    return arr;
  }, [typesResp]);

  const percPlayedArr = useMemo(() => {
    let arr = [];
    for (let i = 1; i < 101; i++) {
      arr.push(i);
    }
    return arr;
  }, []);

  return (
    <Switch value={pContent.state}>
      <Case case={QueryStatus.loading}>
        <Loading />
      </Case>
      <Case case={QueryStatus.success}>
        <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <IxigoText
              label={"Rounds to consider"}
              value={`${roundsToConsider}`}
              type={IxigoTextType.number}
              step={1}
              onChange={(v) => setRoundsToConsider(parseInt(v))}
            />
          </Grid>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <IxigoSelect
              label="Score type"
              possibleValues={types}
              selectedValue={scoreType}
              onChange={(v) => setScoreType(v)}
            />
          </Grid>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <IxigoSelect
              label="Min % played"
              possibleValues={percPlayedArr.map((i) => ({ value: `${i}`, label: `${i} %` }))}
              selectedValue={`${percPlayed}`}
              onChange={(v) => setPercPlayed(parseInt(v))}
            />
          </Grid>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <IxigoText
              label={"Penalty Weight"}
              type={IxigoTextType.number}
              value={`${penaltyWeight}`}
              step={0.1}
              onChange={(v) => setPenaltyWeight(parseFloat(v))}
            />
          </Grid>
          <Grid item xs={12}></Grid>
          <Grid item xs={XS} sm={SM} md={12} lg={LG} xl={XL}>
            <List
              sx={{ width: "100%", bgcolor: "background.paper" }}
              subheader={<ListSubheader>Steam Players</ListSubheader>}
            >
              {pContent.csgoPlayers?.map((player) => (
                <ListItem key={player.steam_id}>
                  <IxigoSwitch key={player.steam_id} label={player.user_name} value={player.steam_id} checked={false} />
                </ListItem>
              ))}
            </List>
          </Grid>
          <Grid item xs={XS} sm={6} md={MD} lg={LG} xl={XL}>
            <IxigoTeam picture={terr} title="Terrorists" />
          </Grid>
          <Grid item xs={XS} sm={6} md={MD} lg={LG} xl={XL}>
            <IxigoTeam picture={ct} title="Counter Terrorists" />
          </Grid>
        </Grid>
      </Case>
      <Case case={QueryStatus.error}>
        <Grid item xs={12} textAlign={"center"}>
          <ErrorOutlineIcon color="error" fontSize="large" />
        </Grid>
      </Case>
    </Switch>
  );
};

export default PlayersContent;
