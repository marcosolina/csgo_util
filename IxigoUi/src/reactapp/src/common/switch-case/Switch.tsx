import React from "react";
import { ISwitchProps } from ".";

export default function Switch<T>({ value, children }: ISwitchProps<T>): JSX.Element {
  const childrenWithProps = React.Children.map(children, (child) => {
    if (React.isValidElement(child)) {
      return React.cloneElement(child as React.ReactElement<any>, { value });
    }

    return child;
  });

  return <>{childrenWithProps}</>;
}
