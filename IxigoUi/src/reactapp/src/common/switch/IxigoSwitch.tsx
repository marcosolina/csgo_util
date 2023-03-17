import { FormControlLabel, Switch } from "@mui/material";
import { IIxigoSwitchProps, IxigoSwitchColor, IxigoSwitchState } from "./interfaces";

const IxigoSwitch: React.FC<IIxigoSwitchProps> = (props) => {
  return (
    <FormControlLabel
      value={props.value}
      control={<Switch color={props.color || IxigoSwitchColor.primary} defaultChecked={props.checked} size="small" />}
      label={props.label}
      disabled={props.state === IxigoSwitchState.disabled}
      labelPlacement={props.labelPosition}
    />
  );
};

export default IxigoSwitch;
