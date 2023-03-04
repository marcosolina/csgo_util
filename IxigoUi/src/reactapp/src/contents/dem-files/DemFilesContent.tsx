import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
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
import DownloadDemFileButton from "./DownloadDemFileButton";

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
      arr.sort((a, b) => a.date.localeCompare(b.date) * -1);
      setNights(arr);
    }
  }, [status, files]);

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
                  <ListItem disablePadding key={index} secondaryAction={<DownloadDemFileButton fileName={m.name} />}>
                    <ListItemButton>
                      <ListItemText primary={m.map_name} secondary={m.size} />
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
