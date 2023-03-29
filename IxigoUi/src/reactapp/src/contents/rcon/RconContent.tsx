import Grid from "@mui/material/Grid";
import { IxigoTextState, IxigoTextType } from "../../common/input";
import IxigoText from "../../common/input/IxigoText";
import { DEFAULT_SPACING, NotistackVariant } from "../../lib/constants";
import SendIcon from "@mui/icons-material/Send";
import { CircularProgress, IconButton, InputAdornment } from "@mui/material";
import { useCallback, useEffect, useState } from "react";
import { IRconRequest } from "../../services";
import { QueryStatus } from "../../lib/http-requests";
import { useTranslation } from "react-i18next";
import { useSnackbar } from "notistack";
import IxigoDialog from "../../common/dialog/IxigoDialog";
import DefaultCommands from "./DefaultCommands";
import { useRconContentProvider } from "./useRconContentProvider";
import RconMaps from "./RconMaps";

const XS = 12;
const SM = 12;
const MD = 6;
const LG = 4;
const XL = 3;

const RconContent = () => {
  const { t } = useTranslation();
  const { enqueueSnackbar } = useSnackbar();

  const [dialogOpen, setDialogOpen] = useState<boolean>(false);
  const { request, setRequest, rconResponse, queryState, errorFields, sendCommand } = useRconContentProvider();

  const dialogButtonHandler = () => setDialogOpen(false);

  const onChangeInputHandler = useCallback(
    (inputField: string, value: string) => {
      const newRequest = { ...request } as any;
      newRequest[inputField] = value;
      setRequest(newRequest as IRconRequest);
    },
    [request, setRequest]
  );

  const sendRequestHandler = () => {
    sendCommand(request);
  };

  useEffect(() => {
    if (queryState === QueryStatus.success && rconResponse?.rcon_response) {
      setDialogOpen(true);
    }

    if (queryState === QueryStatus.error) {
      enqueueSnackbar(t("error.generic.message"), { variant: NotistackVariant.error });
      return;
    }
  }, [queryState, rconResponse?.rcon_response, t, enqueueSnackbar]);

  return (
    <>
      <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoText
            label={t("page.rcon.inputs.labels.serverAddr") as string}
            value={request?.rcon_host}
            state={IxigoTextState.mandatory}
            onChange={(value) => onChangeInputHandler("rcon_host", value)}
            isError={errorFields.includes("rcon_host")}
          />
        </Grid>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoText
            label={t("page.rcon.inputs.labels.serverPort") as string}
            value={request.rcon_port.toString()}
            state={IxigoTextState.mandatory}
            type={IxigoTextType.number}
            onChange={(value) => onChangeInputHandler("rcon_port", value)}
            isError={errorFields.includes("rcon_port")}
          />
        </Grid>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoText
            label={t("page.rcon.inputs.labels.serverPassw") as string}
            value={request?.rcon_passw}
            state={IxigoTextState.mandatory}
            type={IxigoTextType.password}
            onChange={(value) => onChangeInputHandler("rcon_passw", value)}
            isError={errorFields.includes("rcon_passw")}
          />
        </Grid>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoText
            label={t("page.rcon.inputs.labels.serverCmd") as string}
            value={request?.rcon_command}
            state={IxigoTextState.mandatory}
            endAdornment={
              <InputAdornment position="end">
                <IconButton
                  aria-label="toggle password visibility"
                  edge="end"
                  color="primary"
                  onClick={sendRequestHandler}
                >
                  {queryState === QueryStatus.loading ? <CircularProgress size={20} /> : <SendIcon />}
                </IconButton>
              </InputAdornment>
            }
            onChange={(value) => onChangeInputHandler("rcon_command", value)}
            isError={errorFields.includes("rcon_command")}
          />
        </Grid>
      </Grid>
      <DefaultCommands />
      <RconMaps />
      <IxigoDialog isOpen={dialogOpen} onClose={dialogButtonHandler} onContinue={dialogButtonHandler} title={""}>
        <br />
        <IxigoText
          label={t("page.rcon.inputs.labels.serverResp") as string}
          value={rconResponse?.rcon_response}
          state={IxigoTextState.readonly}
          type={IxigoTextType.text}
          rows={10}
        />
      </IxigoDialog>
    </>
  );
};

export default RconContent;
