import { TextField } from "@mui/material";
import React from "react";
import { IIxigoText, IxigoTextState, IxigoTextType, IxigoTextVariant, IxigoTextWidth } from "./interfaces";

const IxigoText: React.FC<IIxigoText> = (props) => {
  const variant = props.variant ? props.variant : IxigoTextVariant.outlined;
  const width = props.width === IxigoTextWidth.fitContent ? "" : "100%";
  const type = props.type ? props.type : IxigoTextType.text;

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (props.onChange) {
      props.onChange(e.target.value);
    }
  };

  return (
    <TextField
      focused
      error={props.isError}
      label={props.label}
      variant={variant}
      value={props.value || ""}
      onChange={onChange}
      size="small"
      style={{ width }}
      required={props.state === IxigoTextState.mandatory}
      disabled={props.state === IxigoTextState.disabled}
      InputProps={{
        readOnly: props.state === IxigoTextState.readonly,
        endAdornment: props.endAdornment,
      }}
      helperText={props.helperText}
      type={type}
      rows={props.rows || undefined}
      multiline={props.rows ? true : false}
    />
  );
};

export default IxigoText;
