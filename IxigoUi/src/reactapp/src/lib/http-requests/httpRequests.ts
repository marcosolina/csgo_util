import axios from "axios";
import { IxigoResponse, QueryParamType } from "./interfaces";

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

  return {
    data: result.data,
    status: result.status,
    errors: result.data.errors,
    isError: result.status >= 400,
  };
}
