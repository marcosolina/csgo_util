import Grid from "@mui/material/Grid";
import { IxigoTextState, IxigoTextType } from "../../common/input";
import IxigoText from "../../common/input/IxigoText";
import { DEFAULT_SPACING, NotistackVariant } from "../../lib/constants";
import SendIcon from "@mui/icons-material/Send";
import { CircularProgress, IconButton, InputAdornment } from "@mui/material";
import { useEffect, useRef, useState } from "react";
import { IRconRequest, useSendRconCommand } from "../../services";
import { QueryStatus } from "../../lib/http-requests";
import { useTranslation } from "react-i18next";
import { useCheckErrorsInResponse } from "../../lib/http-requests/httpRequests";
import { useSnackbar } from "notistack";
import IxigoDialog from "../../common/dialog/IxigoDialog";

const XS = 12;
const SM = 12;
const MD = 6;
const LG = 4;
const XL = 3;

const RconContent = () => {
  const { t } = useTranslation();
  const { enqueueSnackbar } = useSnackbar();
  const rconHook = useSendRconCommand();
  const { checkResp } = useCheckErrorsInResponse();
  const checkRespFunc = useRef(checkResp);

  const [errorFields, setErrorFields] = useState<string[]>([]);
  const [dialogOpen, setDialogOpen] = useState<boolean>(false);
  const [request, setRequest] = useState<IRconRequest>();

  const dialogButtonHandler = () => setDialogOpen(false);

  const onChangeInputHandler = (inputField: string, value: string) => {
    const newRequest = { ...request } as any;
    newRequest[inputField] = value;
    setRequest(newRequest as IRconRequest);
  };

  const onSubmitHandler = () => {
    if (!request) {
      return;
    }
    rconHook.sendCommand(request);
  };

  useEffect(() => {
    if (rconHook.response && rconHook.status === QueryStatus.success) {
      const successMessage = t("pages.rcon.successRcon");
      const checkOutput = checkRespFunc.current(rconHook.response, successMessage);
      setErrorFields(checkOutput.errorFields);
      if (rconHook.response?.data?.rcon_response) {
        setDialogOpen(true);
      }
      return;
    }

    if (rconHook.status === QueryStatus.error) {
      enqueueSnackbar(t("error.generic.message"), { variant: NotistackVariant.error });
      return;
    }
  }, [rconHook.status, rconHook.response]);

  return (
    <>
      <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoText
            label={"RCON Server Address"}
            value={request?.rcon_host}
            state={IxigoTextState.mandatory}
            onChange={(value) => onChangeInputHandler("rcon_host", value)}
            isError={errorFields.includes("rcon_host")}
          />
        </Grid>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoText
            label={"RCON Server Port"}
            value={request?.rcon_port as string | undefined}
            state={IxigoTextState.mandatory}
            type={IxigoTextType.number}
            onChange={(value) => onChangeInputHandler("rcon_port", value)}
            isError={errorFields.includes("rcon_port")}
          />
        </Grid>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoText
            label={"RCON Server Password"}
            value={request?.rcon_passw}
            state={IxigoTextState.mandatory}
            type={IxigoTextType.password}
            onChange={(value) => onChangeInputHandler("rcon_passw", value)}
            isError={errorFields.includes("rcon_passw")}
          />
        </Grid>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoText
            label={"RCON command to send"}
            value={request?.rcon_command}
            state={IxigoTextState.mandatory}
            endAdornment={
              <InputAdornment position="end">
                <IconButton
                  aria-label="toggle password visibility"
                  edge="end"
                  color="primary"
                  onClick={onSubmitHandler}
                >
                  {rconHook.status === QueryStatus.loading ? <CircularProgress size={20} /> : <SendIcon />}
                </IconButton>
              </InputAdornment>
            }
            onChange={(value) => onChangeInputHandler("rcon_command", value)}
            isError={errorFields.includes("rcon_command")}
          />
        </Grid>
      </Grid>
      <IxigoDialog
        isOpen={dialogOpen}
        onClose={dialogButtonHandler}
        onContinue={dialogButtonHandler}
        title={t("page.rcon.dialog.title")}
      >
        {rconHook.response?.data?.rcon_response}
      </IxigoDialog>
    </>
  );
};

export default RconContent;
