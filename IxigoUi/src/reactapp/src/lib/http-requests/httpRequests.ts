import axios from "axios";
import { useSnackbar } from "notistack";
import { useTranslation } from "react-i18next";
import { NotistackVariant } from "../constants/style";
import { CheckErrorsFuncReturnType, IxigoResponse, QueryParamType } from "./interfaces";

export const createQueryParamString = (input: Record<string, QueryParamType> | undefined): string => {
  if (!input) {
    return "";
  }

  const queryString =
    "?" +
    Object.getOwnPropertyNames(input)
      .map((key) => {
        // no value, return key only
        if (input[key] === undefined) {
          return key;
        }

        let value;
        if (Array.isArray(input[key])) {
          value = (input[key] as string[]).join(",");
        } else {
          value = input[key] as string;
        }

        return `${key}=${encodeURIComponent(value)}`;
      })
      .join("&");

  return queryString;
};

export async function performGet<T>(url: string): Promise<IxigoResponse<T>> {
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
  };

  const result = await axios
    .get(url, { headers })
    .then((result) => result)
    .catch((error) => error.response);

  const resp: IxigoResponse<T> = {
    data: result.data,
    status: result.status,
    errors: result.data.errors,
    isError: result.status >= 400,
  };

  return result.status >= 400 ? Promise.reject(resp) : Promise.resolve(resp);
}

export async function performPost<T, J>(url: string, body: J): Promise<IxigoResponse<T>> {
  const headers = {
    "Content-Type": "application/json",
  };
  const result = await axios
    .post(url, body, { headers })
    .then((result) => result)
    .catch((error) => error.response);
  return {
    data: result.data,
    status: result.status,
    errors: result.data.errors,
    isError: result.status >= 400,
  };
}

export async function performPut<T, J>(url: string, body: J): Promise<IxigoResponse<T>> {
  const headers = {
    "Content-Type": "application/json",
  };
  const result = await axios
    .put(url, body, { headers })
    .then((result) => result)
    .catch((error) => error.response);
  return {
    data: result.data,
    status: result.status,
    errors: result.data.errors,
    isError: result.status >= 400,
  };
}

export function useCheckErrorsInResponse<T>(): CheckErrorsFuncReturnType<T> {
  const { t } = useTranslation();
  const { enqueueSnackbar } = useSnackbar();

  return {
    checkResp: (resp: IxigoResponse<T>, successfullMessage?: string) => {
      if (resp.isError) {
        if (resp.errors) {
          resp.errors.forEach((e) => {
            enqueueSnackbar(`${e.error_code} - ${e.error_message}`, {
              variant: NotistackVariant.error,
            });
          });
        } else {
          const err = resp.data as any;
          if (err.error_code || err.error_msg) {
            enqueueSnackbar(`${err.error_code} - ${err.error_msg}`, {
              variant: NotistackVariant.error,
            });
          } else {
            enqueueSnackbar(t("error.generic.message"), {
              variant: NotistackVariant.error,
            });
          }
        }
      } else {
        if (successfullMessage) {
          enqueueSnackbar(successfullMessage, { variant: NotistackVariant.success });
        }
      }
      return {
        errorFields: resp.errors?.map((e) => e.error_field) || ([] as string[]),
        isError: resp.isError,
      };
    },
  };
}
