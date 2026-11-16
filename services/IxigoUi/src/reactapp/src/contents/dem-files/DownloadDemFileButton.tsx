import { LoadingButton } from "@mui/lab";
import { IDownloadDemFileButtonProps } from ".";
import CloudDownloadIcon from "@mui/icons-material/CloudDownload";
import { SERVICES_URLS } from "../../lib/constants/paths";
import { useState } from "react";

const DownloadDemFileButton: React.FC<IDownloadDemFileButtonProps> = (props) => {
  const [isDownloading, setIsDownloading] = useState<boolean>(false);

  const downloadFileHandler = async () => {
    setIsDownloading(true);
    const url = `${SERVICES_URLS["dem-manager"]["get-dem-file"]}/${props.fileName}`;
    const resp = await fetch(url, { method: "get", mode: "no-cors", referrerPolicy: "no-referrer" });
    if (!resp.ok) {
      setIsDownloading(false);
      return;
    }
    const blob = await resp.blob();
    const aElement = document.createElement("a");
    aElement.setAttribute("download", props.fileName);
    const href = URL.createObjectURL(blob);
    aElement.href = href;
    aElement.setAttribute("target", "_blank");
    aElement.click();
    URL.revokeObjectURL(href);
    setIsDownloading(false);
  };

  return (
    <LoadingButton variant="text" loading={isDownloading} onClick={downloadFileHandler}>
      <CloudDownloadIcon />
    </LoadingButton>
  );
};

export default DownloadDemFileButton;
