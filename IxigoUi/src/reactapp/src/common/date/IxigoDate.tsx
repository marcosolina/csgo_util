import { DatePicker, DateValidationError } from "@mui/x-date-pickers";
import { IIxigoDate, IxigoDateState, IxigoDateVariant, IxigoDateWidth } from "./interfaces";
import { PickerChangeHandlerContext } from "@mui/x-date-pickers/internals/hooks/usePicker/usePickerValue.types";

const IxigoDate: React.FC<IIxigoDate> = (props) => {
  const variant = props.variant ? props.variant : IxigoDateVariant.outlined;
  const width = props.width === IxigoDateWidth.fitContent ? "" : "100%";

  const onChange = (value: Date | null, context: PickerChangeHandlerContext<DateValidationError>) => {
    if (props.onChange) {
      props.onChange(value);
    }
  };

  return (
    <DatePicker
      label={props.label}
      value={props.value}
      format="dd/MM/yyyy"
      onChange={onChange}
      slotProps={{
        textField: {
          size: "small",
          focused: true,
          variant: variant,
          style: { width },
          required: props.state === IxigoDateState.mandatory,
          disabled: props.state === IxigoDateState.disabled,
        },
      }}
    />
  );
};

export default IxigoDate;
