import i18n, { InitOptions } from "i18next";
import { initReactI18next } from "react-i18next";
import HttpApi from "i18next-http-backend";
import { UI_CONTEXT_PATH } from "../constants/paths";

const i18nOptions: InitOptions = {
  debug: false,
  backend: {
    loadPath: `${UI_CONTEXT_PATH}/languages/{{lng}}.json`,
    crossDomain: false,
  },
  lng: "en-GB",
  fallbackLng: "en-GB",
  load: "currentOnly",
  react: {
    useSuspense: false,
  },
};

i18n.use(HttpApi).use(initReactI18next).init(i18nOptions);

export { i18n };
