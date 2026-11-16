import { LoadingButton } from "@mui/lab";
import { CircularProgress, IconButton, Tooltip } from "@mui/material";
import React from "react";
import { IxigoButtonColor, IxigoButtonSize, IxigoButtonStatus, IxigoButtonVariant, IxigoButtonWidth } from ".";
import { IIxigoButton, IxigoButtonType } from "./interfaces";
import Switch from "../switch-case/Switch";
import Case from "../switch-case/Case";

const IxigoButton: React.FC<IIxigoButton> = (props) => {
  const width = props.width === IxigoButtonWidth.fitParent ? "100%" : "";
  const variant = props.variant ? props.variant : IxigoButtonVariant.contained;
  const size = props.size ? props.size : IxigoButtonSize.medium;
  const color = props.color ? props.color : IxigoButtonColor.default;
  const type = props.type ? props.type : IxigoButtonType.square;

  const onClick = () => {
    if (props.loading) {
      return;
    }
    if (!!props.onClick) {
      props.onClick();
    }
  };

  return (
    <Switch value={type}>
      <Case case={IxigoButtonType.square}>
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
      </Case>
      <Case case={IxigoButtonType.justicon}>
        <Tooltip title={props.toolTip || ""}>
          {props.loading ? (
            <CircularProgress color={color} />
          ) : (
            <IconButton
              color={color}
              size={size}
              disabled={props.status === IxigoButtonStatus.disabled}
              onClick={onClick}
            >
              {props.text}
            </IconButton>
          )}
        </Tooltip>
      </Case>
    </Switch>
  );
};

export default IxigoButton;
