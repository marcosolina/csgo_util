import React from "react";

export interface IIxigoButton {
  width?: IxigoButtonWidth;
  variant?: IxigoButtonVariant;
  size?: IxigoButtonSize;
  text: React.ReactNode;
  status?: IxigoButtonStatus;
  color?: IxigoButtonColor;
  onClick?: () => void;
  loading?: boolean;
  toolTip?: string;
}

export enum IxigoButtonWidth {
  fitParent = "fitParent",
  fitContent = "fitContent",
}

export enum IxigoButtonVariant {
  contained = "contained",
  text = "",
  outlined = "outlined",
}

export enum IxigoButtonSize {
  small = "small",
  medium = "medium",
  large = "large",
}

export enum IxigoButtonStatus {
  enabled = "true",
  disabled = "false",
}

export enum IxigoButtonColor {
  default = "inherit",
  primary = "primary",
  secondary = "secondary",
  success = "success",
  error = "error",
  info = "info",
  warning = "warning",
}
