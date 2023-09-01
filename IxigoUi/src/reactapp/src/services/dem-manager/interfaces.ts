export interface IRestGetDemFilesResponse {
  files: Record<string, IRestFileInfo[]>;
}

export interface IRestFileInfo {
  name: string;
  map_name: string;
  size: string;
  url: string;
}

export interface IRestGetCsgoUsersResponse {
  users: ICsgoUser[];
}

export interface ICsgoUser {
  steam_id: string;
  user_name: string;
}
