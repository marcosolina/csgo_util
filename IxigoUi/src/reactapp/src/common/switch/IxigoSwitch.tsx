import { FormControlLabel, Switch } from "@mui/material";
import { IIxigoSwitchProps, IxigoSwitchColor, IxigoSwitchState } from "./interfaces";

const IxigoSwitch: React.FC<IIxigoSwitchProps> = (props) => {
  const onChangeHandler = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (props.onChange) {
      props.onChange(props.value, event.target.checked);
    }
  };

  return (
    <FormControlLabel
      value={props.value}
      control={
        <Switch
          color={props.color || IxigoSwitchColor.primary}
          checked={props.checked}
          size="small"
          onChange={onChangeHandler}
        />
      }
      label={props.label}
      disabled={props.state === IxigoSwitchState.disabled}
      labelPlacement={props.labelPosition}
    />
  );
};

export default IxigoSwitch;
