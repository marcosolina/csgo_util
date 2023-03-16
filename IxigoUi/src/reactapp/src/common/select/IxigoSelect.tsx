import {
  Checkbox,
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
import {
  IIxigoMultipleSelectProps,
  IxigoMultipleSelectState,
  IxigoMultipleSelectVariant,
  IxigoMultipleSelectWidth,
} from "./interfaces";

const IxigoSelect: React.FC<IIxigoMultipleSelectProps> = (props) => {
  const width = props.width === IxigoMultipleSelectWidth.fitContent ? "" : "100%";
  let variantElement = undefined;
  let variantText = props.variant;
  switch (props.variant) {
    case IxigoMultipleSelectVariant.standard:
      break;
    case IxigoMultipleSelectVariant.filled:
      variantElement = <FilledInput />;
      break;
    case IxigoMultipleSelectVariant.outlined:
    default:
      variantElement = <OutlinedInput label={props.label} />;
      variantText = IxigoMultipleSelectVariant.outlined;
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
      required={props.state === IxigoMultipleSelectState.mandatory}
      disabled={props.state === IxigoMultipleSelectState.disabled}
      error={props.isError}
      focused
    >
      <InputLabel id="demo-multiple-checkbox-label">{props.label}</InputLabel>
      <Select
        labelId="demo-multiple-checkbox-label"
        id="demo-multiple-checkbox"
        value={props.selectedValue as ""}
        onChange={onChange}
        input={variantElement}
        renderValue={(selected) => props.possibleValues.filter((e) => selected === e.value).map((e) => e.label)}
        inputProps={{
          readOnly: props.state === IxigoMultipleSelectState.readonly,
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
