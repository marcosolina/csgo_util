import React from "react";
import { IxigoDialogProps } from ".";
import { useTranslation } from "react-i18next";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import IxigoButton from "../button/IxigoButton";
import { IxigoButtonColor, IxigoButtonSize } from "../button";

const IxigoDialog: React.FC<IxigoDialogProps> = (props) => {
  const { t } = useTranslation();
  return (
    <Dialog open={props.isOpen} maxWidth="md" fullWidth={true} fullScreen={props.fullScreen}>
      <DialogTitle>{props.title}</DialogTitle>
      <DialogContent dividers={props.dividers}>{props.children}</DialogContent>
      <DialogActions>
        <IxigoButton
          text={props.btnCancelLbl || t("dialog.btn.cancel")}
          size={IxigoButtonSize.small}
          onClick={props.onClose}
        />
        <IxigoButton
          text={props.btnContinueLbl || t("dialog.btn.continue")}
          size={IxigoButtonSize.small}
          onClick={props.onContinue}
          color={IxigoButtonColor.primary}
          loading={props.loading}
        />
      </DialogActions>
    </Dialog>
  );
};

export default IxigoDialog;
