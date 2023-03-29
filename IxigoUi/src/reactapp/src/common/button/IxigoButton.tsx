import { LoadingButton } from "@mui/lab";
import { Tooltip } from "@mui/material";
import React from "react";
import { IxigoButtonColor, IxigoButtonSize, IxigoButtonStatus, IxigoButtonVariant, IxigoButtonWidth } from ".";
import { IIxigoButton } from "./interfaces";

const IxigoButton: React.FC<IIxigoButton> = (props) => {
  const width = props.width === IxigoButtonWidth.fitParent ? "100%" : "";
  const variant = props.variant ? props.variant : IxigoButtonVariant.contained;
  const size = props.size ? props.size : IxigoButtonSize.medium;
  const color = props.color ? props.color : IxigoButtonColor.default;
  const onClick = () => {
    if (props.loading) {
      return;
    }
    if (!!props.onClick) {
      props.onClick();
    }
  };

  return (
    <Tooltip title={props.toolTip || ""}>
      <LoadingButton
        variant={variant}
        color={color}
        style={{ width }}
        size={size}
        disabled={props.status === IxigoButtonStatus.disabled}
        onClick={onClick}
        loading={props.loading}
      >
        {props.text}
      </LoadingButton>
    </Tooltip>
  );
};

export default IxigoButton;
