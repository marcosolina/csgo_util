import { ImageList, ImageListItem, ImageListItemBar } from "@mui/material";
import { useTranslation } from "react-i18next";
import { QUERY_PARAMS, UI_CONTEXT_PATH } from "../../lib/constants";
import { useSearchParams } from "react-router-dom";
import { useEffect, useState } from "react";
import IxigoDialog from "../../common/dialog/IxigoDialog";

const IMG_INFO_PREFIX = `${UI_CONTEXT_PATH}/jointheserver`;

const TRANSLATIONS_BASE_PATH = "page.serverInfo";
const TRANSLATIONS_BASE_PATH_PICTURES = `${TRANSLATIONS_BASE_PATH}.pictures`;
const ARR = ["ixigo-logo", "discord-logo", "telegram-logo"];

const ServerInfoContent = () => {
  const { t } = useTranslation();
  const [searchParams] = useSearchParams();
  const [popupBlocked, setPopupBlocked] = useState<boolean>(false);

  const clickHandler = (name: string) => {
    let url;
    switch (name) {
      case "ixigo-logo":
        url = "steam://connect/ixigo.selfip.net";
        break;
      case "discord-logo":
        url = "https://discord.gg/8Rxu2xRxc";
        break;
      case "telegram-logo":
        url = "https://t.me/+pCBbGwP78UkwZDI0";
        break;
    }

    if (url) {
      const newWindow = window.open(url, "_blank");
      setPopupBlocked(!newWindow);
    }
  };

  const dialogButtonHandler = () => setPopupBlocked(false);

  useEffect(() => {
    if (
      searchParams.has(QUERY_PARAMS.TAB) &&
      parseInt(searchParams.get(QUERY_PARAMS.TAB) as string) === 5 &&
      searchParams.get(QUERY_PARAMS.JOIN_IXIGO) === "true"
    ) {
      clickHandler("ixigo-logo");
    }
  }, [searchParams]);

  return (
    <>
      <ImageList
        gap={8}
        sx={{
          gridTemplateColumns: {
            xs: "repeat(1, 1fr) !important",
            sm: "repeat(2, 1fr) !important",
            md: "repeat(3, 1fr) !important",
            lg: "repeat(3, 1fr) !important",
            xl: "repeat(3, 1fr) !important",
          },
        }}
      >
        {ARR.map((name) => (
          <ImageListItem key={name} style={{ cursor: "pointer" }} onClick={() => clickHandler(name)}>
            <img
              src={`${IMG_INFO_PREFIX}/${name}.png`}
              width={"100%"}
              alt=""
              style={{ backgroundColor: "#FFFFFF", borderRadius: "5px" }}
            />
            <ImageListItemBar
              title={t(`${TRANSLATIONS_BASE_PATH_PICTURES}.${name}.title`)}
              subtitle={t(`${TRANSLATIONS_BASE_PATH_PICTURES}.${name}.subtitle`)}
            />
          </ImageListItem>
        ))}
      </ImageList>
      <IxigoDialog
        isOpen={popupBlocked}
        onClose={dialogButtonHandler}
        onContinue={dialogButtonHandler}
        title={t(`${TRANSLATIONS_BASE_PATH}.modalPopup.title`)}
      >
        <br />
        <img
          src={`${IMG_INFO_PREFIX}/popup-blocked.png`}
          width={"100%"}
          alt=""
          style={{ backgroundColor: "#FFFFFF", borderRadius: "5px" }}
        />
      </IxigoDialog>
    </>
  );
};

export default ServerInfoContent;
