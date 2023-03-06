import React from "react";

export interface IxigoDialogProps {
  onClose: () => void;
  onContinue: () => void;
  btnContinueLbl?: React.ReactNode;
  btnCancelLbl?: React.ReactNode;
  title: React.ReactNode;
  isOpen: boolean;
  dividers?: boolean;
  fullScreen?: boolean;
  loading?: boolean;
  children?: React.ReactNode;
}
