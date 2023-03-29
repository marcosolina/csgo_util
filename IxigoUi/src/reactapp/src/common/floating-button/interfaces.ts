export interface IxigoFloatingButtonProps {
  color?: IxigoFloatingButtonColor;
  fixedPosition?: IxigoFloatingButtonFixedPosition;
  onClick?: () => void;
  loading?: boolean;
}

export enum IxigoFloatingButtonFixedPosition {
  bottomRight = "br",
  bottomLeft = "bl",
  topLeft = "tl",
  topRight = "tr",
}

export enum IxigoFloatingButtonColor {
  default = "inherit",
  primary = "primary",
  secondary = "secondary",
  inherit = "inherit",
}
