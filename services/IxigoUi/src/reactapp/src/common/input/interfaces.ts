export interface IIxigoText {
  value?: string;
  label?: string;
  onChange?: (newValue: string) => void;
  variant?: IxigoTextVariant;
  state?: IxigoTextState;
  width?: IxigoTextWidth;
  isError?: boolean;
  helperText?: string;
  type?: IxigoTextType;
  rows?: number;
  endAdornment?: React.ReactNode;
  step?: number;
}

export enum IxigoTextWidth {
  fitParent = "fitParent",
  fitContent = "fitContent",
}

export enum IxigoTextVariant {
  outlined = "outlined",
  filled = "filled",
  standard = "standard",
}

export enum IxigoTextState {
  disabled = "disabled",
  readonly = "readonly",
  editable = "editable",
  mandatory = "mandatory",
}

export enum IxigoTextType {
  text = "text",
  number = "number",
  password = "password",
}
