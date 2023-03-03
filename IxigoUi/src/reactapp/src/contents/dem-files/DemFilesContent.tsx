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
                        <IconButton edge="end" aria-label="donwload">
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
