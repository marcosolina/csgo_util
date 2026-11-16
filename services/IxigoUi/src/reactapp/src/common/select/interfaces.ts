export interface IIxigoSelectProps {
  variant?: IxigoSelectVariant;
  state?: IxigoSelectState;
  label?: string;
  minWidth?: number;
  width?: IxigoSelectWidth;
  isError?: boolean;
  helperText?: string;
  selectedValue: string;
  possibleValues: IxigoPossibleValue[];
  onChange?: (newValue: string) => void;
}

export interface IIxigoMultipleSelectProps extends Omit<IIxigoSelectProps, "onChange" | "selectedValue"> {
  onChange?: (newValue: string[]) => void;
  selectedValues: string[];
}

export enum IxigoSelectWidth {
  fitParent = "fitParent",
  fitContent = "fitContent",
}

export enum IxigoSelectVariant {
  outlined = "outlined",
  filled = "filled",
  standard = "standard",
}

export enum IxigoSelectState {
  disabled = "disabled",
  readonly = "readonly",
  editable = "editable",
  mandatory = "mandatory",
}

export interface IxigoPossibleValue {
  value: string;
  label: string;
}
