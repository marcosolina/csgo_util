import { IRestFileInfo } from "../../services/dem-manager";

export interface IGamingNight {
  date: string;
  mapsPlayed: IRestFileInfo[];
}

export interface IDownloadDemFileButtonProps {
  fileName: string;
}
