import { ImageList, ImageListItem, ImageListItemBar, ListSubheader } from "@mui/material";
import { useTranslation } from "react-i18next";
import { UI_CONTEXT_PATH } from "../../lib/constants";

const IMG_INFO_PREFIX = `${UI_CONTEXT_PATH}/jointheserver`;

const TRANSLATIONS_BASE_PATH = "page.serverInfo.pictures";

const ServerInfoContent = () => {
  const { t } = useTranslation();
  const arr = [1, 2, 3, 4, 5, 6];

  return (
    <ImageList gap={30} cols={1}>
      <ImageListItem key="Subheader" cols={1}>
        <ListSubheader component="div">Steps to join the server</ListSubheader>
      </ImageListItem>
      {arr.map((number) => (
        <ImageListItem cols={1} key={number}>
          <img src={`${IMG_INFO_PREFIX}/${number}.png`} width={"100%"} alt="" />
          <ImageListItemBar
            title={t(`${TRANSLATIONS_BASE_PATH}.${number}.title`)}
            subtitle={t(`${TRANSLATIONS_BASE_PATH}.${number}.subtitle`)}
          />
        </ImageListItem>
      ))}
    </ImageList>
  );
};

export default ServerInfoContent;
