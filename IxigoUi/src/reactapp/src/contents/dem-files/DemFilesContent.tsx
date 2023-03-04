import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  IconButton,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Typography,
} from "@mui/material";
import Case from "../../common/switch-case/Case";
import Switch from "../../common/switch-case/Switch";
import { QueryStatus } from "../../lib/http-requests";
import { useGetDemFiles } from "../../services";
import Loading from "./Loading";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { useEffect, useState } from "react";
import { IGamingNight } from "./interfaces";
import CloudDownloadIcon from "@mui/icons-material/CloudDownload";
import { SERVICES_URLS } from "../../lib/constants/paths";

function downloadFile(url: string, fileName: string) {
  fetch(url, { method: "get", mode: "no-cors", referrerPolicy: "no-referrer" })
    .then((res) => res.blob())
    .then((res) => {
      const aElement = document.createElement("a");
      aElement.setAttribute("download", fileName);
      const href = URL.createObjectURL(res);
      aElement.href = href;
      // aElement.setAttribute('href', href);
      aElement.setAttribute("target", "_blank");
      aElement.click();
      URL.revokeObjectURL(href);
    });
}

const DemFilesContent = () => {
  const [nights, setNights] = useState<IGamingNight[]>([]);
  const { status, data } = useGetDemFiles();
  const files = data?.data?.files;

  useEffect(() => {
    if (status === QueryStatus.success && !!files) {
      const arr: IGamingNight[] = [];
      Object.keys(files).forEach(function (key, index) {
        arr.push({
          date: key,
          mapsPlayed: files[key],
        });
      });
      setNights(arr);
    }
  }, [status, files]);

  const downloadFileHandler = (fileName: string) => {
    console.log(fileName);
    const url = `${SERVICES_URLS["dem-manager"]["get-dem-file"]}/${fileName}`;
    //window.open(url, "_blank", "noreferrer");
    downloadFile(url, fileName);
  };

  return (
    <Switch value={status}>
      <Case case={QueryStatus.loading}>
        <Loading />
      </Case>
      <Case case={QueryStatus.success}>
        {nights.map((n) => (
          <Accordion key={n.date}>
            <AccordionSummary expandIcon={<ExpandMoreIcon />} aria-controls="panel1a-content" id="panel1a-header">
              <Typography>{n.date}</Typography>
            </AccordionSummary>
            <AccordionDetails>
              <List>
                {n.mapsPlayed.map((m, index) => (
                  <ListItem
                    disablePadding
                    key={index}
                    secondaryAction={
                      <>
                        {m.size}
                        <IconButton edge="end" aria-label="donwload" onClick={() => downloadFileHandler(m.name)}>
                          <CloudDownloadIcon />
                        </IconButton>
                      </>
                    }
                  >
                    <ListItemButton>
                      <ListItemText primary={m.map_name} />
                    </ListItemButton>
                  </ListItem>
                ))}
              </List>
            </AccordionDetails>
          </Accordion>
        ))}
      </Case>
    </Switch>
  );
};

export default DemFilesContent;
