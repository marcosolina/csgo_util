import { Grid, List, ListItem, ListSubheader } from "@mui/material";
import { IxigoTextType } from "../../common/input";
import IxigoText from "../../common/input/IxigoText";
import IxigoSelect from "../../common/select/IxigoSelect";
import Case from "../../common/switch-case/Case";
import Switch from "../../common/switch-case/Switch";
import { DEFAULT_SPACING } from "../../lib/constants";
import { QueryStatus } from "../../lib/http-requests";
import ct from "../../assets/bots/counterterrorist.jpg";
import terr from "../../assets/bots/terrorist.jpg";
import Loading from "./Loading";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import IxigoSwitch from "../../common/switch/IxigoSwitch";
import IxigoTeam from "./IxigoTeam";
import { useGetTeams } from "../../services/players-manager";
import SendIcon from "@mui/icons-material/Send";
import { useRconContentProvider } from "../rcon/useRconContentProvider";
import { useTranslation } from "react-i18next";
import IxigoFloatingButton from "../../common/floating-button/IxigoFloatingButton";
import { usePlayersContentProvider } from "./usePlayersContetProvider";

const XS = 12;
const SM = 12;
const MD = 6;
const LG = 4;
const XL = 3;

const MIN_SELECTED_PLAYERS = 3;
const BASE_LANGUAGE_PATH = "page.players";

let timeOut = setTimeout(() => {}, 100);

const PlayersContent = () => {
  const { t } = useTranslation();
  const pContent = usePlayersContentProvider();

  const { getTeams, status: getTeamsStatus, response: getTeamsResp } = useGetTeams();
  const { request, queryState, sendCommand } = useRconContentProvider();

  const setPlayersHandler = () => {
    const ids = getTeamsResp?.data?.teams[0].team_members.map((t) => t.steam_id).join('" "');
    const cmd = `sm_move_players "${ids}" dummy`;
    const newReq = { ...request };
    newReq.rcon_command = cmd;
    sendCommand(newReq);
  };

  const onSelectedPlayer = (stramId: string, addUser: boolean) => {
    const players = [...pContent.listOfSelectedPlayers];

    const index = players.indexOf(stramId);
    if (addUser && index < 0) {
      players.push(stramId);
    }

    if (index >= 0) {
      players.splice(index, 1);
    }

    if (players.length >= MIN_SELECTED_PLAYERS) {
      clearTimeout(timeOut);
      timeOut = setTimeout(() => {
        getTeams({
          steamIDs: players,
          minPercPlayed: pContent.percPlayed,
          numberOfMatches: pContent.matchesToConsider,
          partitionScore: pContent.scoreType,
          penaltyWeigth: pContent.penaltyWeight,
        });
      }, 1000);
    }

    pContent.setListOfSelectedPlayers(players);
  };

  return (
    <Switch value={pContent.state}>
      <Case case={QueryStatus.loading}>
        <Loading />
      </Case>
      <Case case={QueryStatus.success}>
        <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <IxigoText
              label={t(`${BASE_LANGUAGE_PATH}.labels.lblMatchesToConsider`) as string}
              value={`${pContent.matchesToConsider}`}
              type={IxigoTextType.number}
              step={1}
              onChange={(v) => pContent.setMatchesToConsider(parseInt(v))}
            />
          </Grid>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <IxigoSelect
              label={t(`${BASE_LANGUAGE_PATH}.labels.lblScoreType`) as string}
              possibleValues={pContent.possibleScoreTypesValues}
              selectedValue={pContent.scoreType}
              onChange={(v) => pContent.setScoreType(v)}
            />
          </Grid>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <IxigoSelect
              label={t(`${BASE_LANGUAGE_PATH}.labels.lblMinPercPlayed`) as string}
              possibleValues={pContent.possiblePercPlayedValues}
              selectedValue={`${pContent.percPlayed}`}
              onChange={(v) => pContent.setPercPlayed(parseInt(v))}
            />
          </Grid>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <IxigoText
              label={t(`${BASE_LANGUAGE_PATH}.labels.lblPenalty`) as string}
              type={IxigoTextType.number}
              value={`${pContent.penaltyWeight}`}
              step={0.1}
              onChange={(v) => pContent.setPenaltyWeight(parseFloat(v))}
            />
          </Grid>
          <Grid item xs={12}></Grid>
          <Grid item xs={XS} sm={SM} md={4} lg={LG} xl={4}>
            <List
              sx={{ width: "100%", bgcolor: "background.paper" }}
              subheader={<ListSubheader>{t(`${BASE_LANGUAGE_PATH}.labels.lblListPlayers`)}</ListSubheader>}
            >
              {pContent.csgoPlayers?.map((player) => (
                <ListItem key={player.steam_id}>
                  <IxigoSwitch
                    key={player.steam_id}
                    label={player.user_name}
                    value={player.steam_id}
                    checked={pContent.listOfSelectedPlayers.includes(player.steam_id)}
                    onChange={onSelectedPlayer}
                  />
                </ListItem>
              ))}
            </List>
          </Grid>
          <Grid item xs={XS} sm={6} md={4} lg={LG} xl={4}>
            <IxigoTeam
              picture={terr}
              title={t(`${BASE_LANGUAGE_PATH}.labels.lblTeamTerrorist`)}
              team={getTeamsResp?.data?.teams[0]}
              status={getTeamsStatus}
            />
          </Grid>
          <Grid item xs={XS} sm={6} md={4} lg={LG} xl={4}>
            <IxigoTeam
              picture={ct}
              title={t(`${BASE_LANGUAGE_PATH}.labels.lblTeamCt`)}
              team={getTeamsResp?.data?.teams[1]}
              status={getTeamsStatus}
            />
          </Grid>
        </Grid>
        {pContent.listOfSelectedPlayers.length >= MIN_SELECTED_PLAYERS && (
          <IxigoFloatingButton
            onClick={setPlayersHandler}
            tooltip={t(`${BASE_LANGUAGE_PATH}.labels.lblBtnSetPlayers`) as string}
            loading={queryState === QueryStatus.loading}
            icon={<SendIcon />}
          />
        )}
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
