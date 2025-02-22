import { IRconCommandProps } from "./interfaces";
import { useRconContentProvider } from "./useRconContentProvider";
import { styled } from "@mui/material/styles";
import ButtonBase from "@mui/material/ButtonBase";
import Typography from "@mui/material/Typography";
import { NotistackVariant, UI_CONTEXT_PATH } from "../../lib/constants";
import { useSendCs2RconCommand } from "../../services/rcon/useRconCs2";
import { useSnackbar } from "notistack";
import { useTranslation } from "react-i18next";

const ImageButton = styled(ButtonBase)(({ theme }) => ({
  position: "relative",
  height: 200,
  [theme.breakpoints.down("sm")]: {
    width: "100% !important", // Overrides inline-style
    height: 100,
  },
  "&:hover, &.Mui-focusVisible": {
    zIndex: 1,
    "& .MuiImageBackdrop-root": {
      opacity: 0.15,
    },
    "& .MuiImageMarked-root": {
      opacity: 0,
    },
    "& .MuiTypography-root": {
      border: "4px solid currentColor",
    },
  },
}));

const ImageSrc = styled("span")({
  position: "absolute",
  left: 0,
  right: 0,
  top: 0,
  bottom: 0,
  backgroundSize: "cover",
  backgroundPosition: "center 40%",
});

const Image = styled("span")(({ theme }) => ({
  position: "absolute",
  left: 0,
  right: 0,
  top: 0,
  bottom: 0,
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  color: theme.palette.common.white,
}));

const ImageBackdrop = styled("span")(({ theme }) => ({
  position: "absolute",
  left: 0,
  right: 0,
  top: 0,
  bottom: 0,
  backgroundColor: theme.palette.common.black,
  opacity: 0.4,
  transition: theme.transitions.create("opacity"),
}));

const ImageMarked = styled("span")(({ theme }) => ({
  height: 3,
  width: 18,
  backgroundColor: theme.palette.common.white,
  position: "absolute",
  bottom: -2,
  left: "calc(50% - 9px)",
  transition: theme.transitions.create("opacity"),
}));

const RconCommand: React.FC<IRconCommandProps> = (props) => {
  const { t } = useTranslation();
  const { request, setRequest, sendCommand } = useRconContentProvider();
  const { enqueueSnackbar } = useSnackbar();
  const { sendCommand: sendCs2Command } = useSendCs2RconCommand();

  const clickHandler = () => {
    if (props.isCs2) {
      enqueueSnackbar(t("page.rcon.cs2.sendingCommand"), { variant: NotistackVariant.info });
      sendCs2Command({ cs2Input: props.cmd });
      return;
    }
    const newRequest = { ...request };
    newRequest.rcon_command = props.cmd;
    setRequest(newRequest);
    sendCommand(newRequest);
  };

  return (
    <ImageButton
      focusRipple
      onClick={clickHandler}
      style={{
        width: "100%",
      }}
    >
      {props.image && <ImageSrc style={{ backgroundImage: `url(${UI_CONTEXT_PATH}${props.image})` }} />}
      <ImageBackdrop className="MuiImageBackdrop-root" />
      <Image>
        <>
          <Typography
            component="span"
            variant="subtitle1"
            color="inherit"
            sx={{
              position: "relative",
              p: 4,
              pt: 2,
              pb: (theme) => `calc(${theme.spacing(1)} + 6px)`,
            }}
          >
            {props.label}
            <ImageMarked className="MuiImageMarked-root" />
          </Typography>
          {props.icon}
        </>
      </Image>
    </ImageButton>
  );
};

export default RconCommand;
