export const CONTEXT_PATH_UI =
  (window as { [key: string]: any })["proxyUiContextPath"] ||
  "http://localhost:8763/ixigoproxy";

const PROXY_DEM_MANAGER = `${CONTEXT_PATH_UI}/ixigo-dem-manager`;

export const SERVICES_URLS = {
  DEM_MANAGER: {
    GET_DEM_FILES: `${PROXY_DEM_MANAGER}/demmanager/files`,
  },
};
