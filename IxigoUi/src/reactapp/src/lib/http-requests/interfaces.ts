export enum QueryStatus {
  idle = "idle",
  error = "error",
  loading = "loading",
  success = "success",
}
export type QueryParamType = string | number | boolean | string[];

export interface IxigoResponse<T> {
  errors?: IxigoError[];
  status: number;
  isError: boolean;
  data?: T;
}

export interface IxigoError {
  error_message: string;
  error_field: string;
  error_value: string;
  error_code: string;
}

export interface CheckErrorsFuncReturnType<T> {
  checkResp: (resp: IxigoResponse<T>, successfullMessage?: string) => CheckRespOutcome;
}

export interface CheckRespOutcome {
  errorFields: string[];
  isError: boolean;
}
