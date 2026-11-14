import {
  FilledInput,
  FormControl,
  FormHelperText,
  InputLabel,
  ListItemText,
  MenuItem,
  OutlinedInput,
  Select,
  SelectChangeEvent,
} from "@mui/material";
import { IIxigoSelectProps, IxigoSelectState, IxigoSelectVariant, IxigoSelectWidth } from "./interfaces";

const IxigoSelect: React.FC<IIxigoSelectProps> = (props) => {
  const width = props.width === IxigoSelectWidth.fitContent ? "" : "100%";
  let variantElement = undefined;
  let variantText = props.variant;
  switch (props.variant) {
    case IxigoSelectVariant.standard:
      break;
    case IxigoSelectVariant.filled:
      variantElement = <FilledInput />;
      break;
    case IxigoSelectVariant.outlined:
    default:
      variantElement = <OutlinedInput label={props.label} />;
      variantText = IxigoSelectVariant.outlined;
      break;
  }

  const onChange = (event: SelectChangeEvent<string>) => {
    const {
      target: { value },
    } = event;
    if (props.onChange) {
      props.onChange(value);
    }
  };

  return (
    <FormControl
      sx={{ minWidth: props.minWidth || 100, width }}
      size="small"
      variant={variantText}
      required={props.state === IxigoSelectState.mandatory}
      disabled={props.state === IxigoSelectState.disabled}
      error={props.isError}
      focused
    >
      {props.label && <InputLabel id="demo-multiple-checkbox-label">{props.label}</InputLabel>}
      <Select
        labelId="demo-multiple-checkbox-label"
        id="demo-multiple-checkbox"
        value={props.possibleValues.find((e) => props.selectedValue === e.value)?.value || ""}
        onChange={onChange}
        input={variantElement}
        renderValue={(selected) => props.possibleValues.filter((e) => selected === e.value).map((e) => e.label)}
        inputProps={{
          readOnly: props.state === IxigoSelectState.readonly,
        }}
      >
        {props.possibleValues.map((arrVal) => (
          <MenuItem key={arrVal.value} value={arrVal.value}>
            <ListItemText primary={arrVal.label} />
          </MenuItem>
        ))}
      </Select>
      {props.helperText ? <FormHelperText>{props.helperText}</FormHelperText> : null}
    </FormControl>
  );
};

export default IxigoSelect;
