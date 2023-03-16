export interface IIxigoMultipleSelectProps {
  variant?: IxigoMultipleSelectVariant;
  state?: IxigoMultipleSelectState;
  label?: string;
  minWidth?: number;
  width?: IxigoMultipleSelectWidth;
  isError?: boolean;
  helperText?: string;
  selectedValue: string;
  possibleValues: IxigoPossibleValue[];
  onChange?: (newValue: string) => void;
}

export enum IxigoMultipleSelectWidth {
  fitParent = "fitParent",
  fitContent = "fitContent",
}

export enum IxigoMultipleSelectVariant {
  outlined = "outlined",
  filled = "filled",
  standard = "standard",
}

export enum IxigoMultipleSelectState {
  disabled = "disabled",
  readonly = "readonly",
  editable = "editable",
  mandatory = "mandatory",
}

export interface IxigoPossibleValue {
  value: string;
  label: string;
}
