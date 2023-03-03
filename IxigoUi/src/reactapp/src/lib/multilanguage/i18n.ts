import i18n, { InitOptions } from "i18next";
import { initReactI18next } from "react-i18next";
import HttpApi from "i18next-http-backend";

const i18nOptions: InitOptions = {
  debug: false,
  backend: {
    loadPath: `/languages/{{lng}}.json`,
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
