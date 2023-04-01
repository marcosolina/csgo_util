import { CircularProgress, Fab, Tooltip } from "@mui/material";
import React from "react";
import { IxigoFloatingButtonColor, IxigoFloatingButtonFixedPosition, IxigoFloatingButtonProps } from ".";
import SaveIcon from "@mui/icons-material/Save";
import { DEFAULT_SPACING } from "../../lib/constants";

const IxigoFloatingButton: React.FC<IxigoFloatingButtonProps> = (props) => {
  const color = props.color ? props.color : IxigoFloatingButtonColor.primary;
  let fixedPosition = {};
  switch (props.fixedPosition) {
    case IxigoFloatingButtonFixedPosition.bottomLeft:
      fixedPosition = {
        position: "fixed",
        bottom: DEFAULT_SPACING,
        left: DEFAULT_SPACING,
        marginLeft: DEFAULT_SPACING,
        marginBottom: DEFAULT_SPACING,
      };
      break;
    case IxigoFloatingButtonFixedPosition.topLeft:
      fixedPosition = {
        position: "fixed",
        top: DEFAULT_SPACING,
        left: DEFAULT_SPACING,
        marginLeft: DEFAULT_SPACING,
        marginTop: DEFAULT_SPACING,
      };
      break;
    case IxigoFloatingButtonFixedPosition.topRight:
      fixedPosition = {
        position: "fixed",
        top: DEFAULT_SPACING,
        right: DEFAULT_SPACING,
        marginRight: DEFAULT_SPACING,
        marginTop: DEFAULT_SPACING,
      };
      break;
    case IxigoFloatingButtonFixedPosition.bottomRight:
    default:
      fixedPosition = {
        position: "fixed",
        bottom: DEFAULT_SPACING,
        right: DEFAULT_SPACING,
        marginRight: DEFAULT_SPACING,
        marginBottom: DEFAULT_SPACING,
      };
      break;
  }

  return (
    <>
      {props.loading ? (
        <CircularProgress color={color} sx={{ ...fixedPosition }} />
      ) : (
        <Tooltip title={props.tooltip}>
          <Fab color={color} sx={{ ...fixedPosition }} onClick={props.onClick}>
            {props.icon || <SaveIcon />}
          </Fab>
        </Tooltip>
      )}
    </>
  );
};

export default IxigoFloatingButton;
