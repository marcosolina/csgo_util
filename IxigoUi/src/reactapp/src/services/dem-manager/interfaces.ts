export interface IRestGetDemFilesResponse {
  files: Record<string, IRestFileInfo[]>;
}

export interface IRestFileInfo {
  name: string;
  map_name: string;
  size: string;
  url: string;
}
