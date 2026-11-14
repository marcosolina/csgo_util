import { useMutation } from "react-query";
import { ISendEventCommand, ISendEventRequest } from "./interfaces";
import { performPost } from "../../lib/http-requests/httpRequests";
import { NotistackVariant, SERVICES_URLS } from "../../lib/constants";
import { useTranslation } from "react-i18next";
import { useSnackbar } from "notistack";

const TRANSLATIONS_BASE_PATH = "services.discordbot.messages";

export const useSendEventCommand = (): ISendEventCommand => {
  const { enqueueSnackbar } = useSnackbar();
  const { t } = useTranslation();

  const mutation = useMutation(
    async (evtReq: ISendEventRequest) => {
      return await performPost<void, ISendEventRequest>(SERVICES_URLS["event-dispatcher"]["post-event"], evtReq);
    },
    {
      onSuccess: (data: any) => {
        console.log(data);
        enqueueSnackbar(t(`${TRANSLATIONS_BASE_PATH}.sendEvent.success`), {
          variant: NotistackVariant.success,
        });
      },
    }
  );

  return {
    sendEvent: mutation.mutate,
    status: mutation.status,
    response: mutation.data,
  };
};
