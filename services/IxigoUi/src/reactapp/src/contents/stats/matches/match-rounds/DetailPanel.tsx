import { Box, Typography } from '@mui/material';
import headshot from "../../../../assets/icons/hs.png";
import flashbang from "../../../../assets/icons/flashbang.png";
import { WEAPONG_IMAGE } from "../../weaponImage";

type DetailPanelProps = {
  events: any[];
  getUsernameAndTeam: (steamid: string) => { username: string; team: string };
};

const eventNameDesc: { [key: string]: string } = {
    hostage_rescued: "rescued the hostage",
    bomb_planted: "planted the bomb",
    bomb_defused: "defused the bomb",
  };

const DetailPanel: React.FC<DetailPanelProps> = ({ events, getUsernameAndTeam }) => {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        margin: "auto",
        width: "100%",
      }}
    >
      {events.map((event: any, i) => {
            const { username: steamidUsername, team: steamidTeam } = getUsernameAndTeam(event.steamid);
            if (event.victimsteamid) {
              const { username: victimsteamidUsername, team: victimsteamidTeam } = getUsernameAndTeam(
                event.victimsteamid
              );
              const { username: assisterUsername, team: assisterTeam } = getUsernameAndTeam(event.assister);
              const { username: flashAssisterUsername, team: flashAssisterTeam } = getUsernameAndTeam(
                event.flashassister
              );
              return (
                <Box key={i} sx={{ display: "flex", flexDirection: "row", alignItems: "center" }}>
                  <Typography>
                    {`${new Date(event.eventtime * 1000).toISOString().substr(14, 5)}: `}
                    <span style={{ color: steamidTeam === "team1" ? "#90caf9" : "orange" }}>{steamidUsername}</span>
                    <span style={{ color: assisterTeam === "team1" ? "#90caf9" : "orange" }}>
                      {event.assister && ` + ${assisterUsername}`}{" "}
                    </span>
                    {event.flashassist && (
                      <img height="20px" style={{ padding: "0 5px" }} src={flashbang} alt="Flash Assist" />
                    )}
                    <span style={{ color: flashAssisterTeam === "team1" ? "#90caf9" : "orange" }}>
                      {event.flashassist && ` + ${flashAssisterUsername}`}
                    </span>
                  </Typography>
                  <img
                    height="20px"
                    style={{ transform: "scaleX(-1)", padding: "0 5px" }}
                    src={WEAPONG_IMAGE[event.weapon]}
                    alt={event.weapon}
                  />
                  {event.headshot && <img height="20px" style={{ padding: "0 5px" }} src={headshot} alt="Headshot" />}
                  <Typography>
                    <span style={{ color: victimsteamidTeam === "team1" ? "#90caf9" : "orange" }}>
                      {victimsteamidUsername}
                    </span>
                  </Typography>
                </Box>
              );
            } else {
              return (
                <Typography key={i}>
                  {`${new Date(event.eventtime * 1000).toISOString().substr(14, 5)}: `}
                  <span style={{ color: steamidTeam === "team1" ? "#90caf9" : "orange" }}>{steamidUsername + " "}</span>
                  {eventNameDesc[event.eventtype] ? eventNameDesc[event.eventtype] : event.eventtype}
                </Typography>
              );
            }
          })}
    </Box>
  );
};

export default DetailPanel;
