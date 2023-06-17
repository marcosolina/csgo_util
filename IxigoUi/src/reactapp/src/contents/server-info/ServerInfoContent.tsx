import { ImageList, ImageListItem, ImageListItemBar } from "@mui/material";
import { useTranslation } from "react-i18next";
import { QUERY_PARAMS, UI_CONTEXT_PATH } from "../../lib/constants";
import { useSearchParams } from "react-router-dom";
import { useEffect } from "react";

const IMG_INFO_PREFIX = `${UI_CONTEXT_PATH}/jointheserver`;

const TRANSLATIONS_BASE_PATH = "page.serverInfo.pictures";
const ARR = ["ixigo-logo", "discord-logo", "telegram-logo"];

const clickHandler = (name: string) => {
  switch (name) {
    case "ixigo-logo":
      window.open("steam://connect/ixigo.selfip.net", "_blank");
      break;
    case "discord-logo":
      window.open("https://discord.gg/8Rxu2xRxc", "_blank");
      break;
    case "telegram-logo":
      window.open("https://t.me/+pCBbGwP78UkwZDI0", "_blank");
      break;
  }
};

const ServerInfoContent = () => {
  const { t } = useTranslation();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    if (
      searchParams.has(QUERY_PARAMS.TAB) &&
      searchParams.has(QUERY_PARAMS.TAB) &&
      parseInt(searchParams.get(QUERY_PARAMS.TAB) as string) === 5 &&
      searchParams.get(QUERY_PARAMS.JOIN_IXIGO) === "true"
    ) {
      clickHandler("ixigo-logo");
    }
  }, [searchParams]);

  return (
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
            title={t(`${TRANSLATIONS_BASE_PATH}.${name}.title`)}
            subtitle={t(`${TRANSLATIONS_BASE_PATH}.${name}.subtitle`)}
          />
        </ImageListItem>
      ))}
    </ImageList>
  );
};

export default ServerInfoContent;
