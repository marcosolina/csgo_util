import { ICaseProps } from "./interface";

export default function Case<T>(props: ICaseProps<T>): JSX.Element | null {
  if (props.value !== props.case) {
    return null;
  }

  return <>{props.children}</>;
}
