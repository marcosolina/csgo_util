import { AppBar, Tab, Tabs, Typography } from "@mui/material";
import { Box } from "@mui/system";
import { useState } from "react";
import { useGetDemFiles } from "../../services";

const BaseLayout = () => {
  const [value, setValue] = useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  useGetDemFiles();

  return (
    <>
      <AppBar position="sticky">
        <Box padding={"5px"}>
          <Typography variant="h5">IXI-GO: Monday Nights</Typography>
        </Box>
      </AppBar>
      <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
        <Tabs value={value} onChange={handleChange} aria-label="basic tabs example" centered>
          <Tab label="Item One" />
          <Tab label="Item Two" />
          <Tab label="Item Three" />
        </Tabs>
      </Box>
      {value === 0 && "One"}
      {value === 1 && "Two"}
      {value === 2 && "Three"}
    </>
  );
};

export default BaseLayout;
