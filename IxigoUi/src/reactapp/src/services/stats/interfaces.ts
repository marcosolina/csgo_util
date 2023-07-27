export interface IGetStatsRequest<T> {
  viewName: string;
  queryParams?: T;
}

export interface IGetStatsResponse<T> {
  view_name: string;
  view_data: T;
}
