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
import { IIxigoMultipleSelectProps, IxigoSelectState, IxigoSelectVariant, IxigoSelectWidth } from "./interfaces";

const IxigoSelectMultiple: React.FC<IIxigoMultipleSelectProps> = (props) => {
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

  const onChange = (event: SelectChangeEvent<string[]>) => {
    const {
      target: { value },
    } = event;
    if (props.onChange) {
      props.onChange(typeof value === "string" ? value.split(",") : value);
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
      <InputLabel id="demo-multiple-checkbox-label">{props.label}</InputLabel>
      <Select
        labelId="demo-multiple-checkbox-label"
        id="demo-multiple-checkbox"
        multiple
        value={props.selectedValues}
        onChange={onChange}
        input={variantElement}
        renderValue={(selected) =>
          props.possibleValues
            .filter((e) => selected.includes(e.value))
            .map((e) => e.label)
            .join(", ")
        }
        inputProps={{
          readOnly: props.state === IxigoSelectState.readonly,
        }}
      >
        {props.possibleValues.map((arrVal) => (
          <MenuItem key={arrVal.value} value={arrVal.value}>
            <Checkbox checked={props.selectedValues.indexOf(arrVal.value) > -1} />
            <ListItemText primary={arrVal.label} />
          </MenuItem>
        ))}
      </Select>
      {props.helperText ? <FormHelperText>{props.helperText}</FormHelperText> : null}
    </FormControl>
  );
};

export default IxigoSelectMultiple;
