export interface IIxigoSwitchProps {
  labelPosition?: IIxigoSwitchLabelPlacement;
  state?: IxigoSwitchState;
  label?: string;
  value: string;
  checked: boolean;
  color?: IxigoSwitchColor;
  onChange?: (value: string, checked: boolean) => void;
}

export enum IIxigoSwitchLabelPlacement {
  top = "top",
  right = "end",
  left = "start",
  bottom = "bottom",
}

export enum IxigoSwitchState {
  disabled = "disabled",
  readonly = "readonly",
  editable = "editable",
}

export enum IxigoSwitchColor {
  default = "default",
  primary = "primary",
  secondary = "secondary",
  success = "success",
  error = "error",
  info = "info",
  warning = "warning",
}
