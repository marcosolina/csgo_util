export interface IIxigoDate {
  value: Date | null;
  label?: string;
  onChange?: (newValue: Date | null) => void;
  variant?: IxigoDateVariant;
  state?: IxigoDateState;
  width?: IxigoDateWidth;
  isError?: boolean;
  helperText?: string;
  endAdornment?: React.ReactNode;
}

export enum IxigoDateWidth {
  fitParent = "fitParent",
  fitContent = "fitContent",
}

export enum IxigoDateVariant {
  outlined = "outlined",
  filled = "filled",
  standard = "standard",
}

export enum IxigoDateState {
  disabled = "disabled",
  readonly = "readonly",
  editable = "editable",
  mandatory = "mandatory",
}
