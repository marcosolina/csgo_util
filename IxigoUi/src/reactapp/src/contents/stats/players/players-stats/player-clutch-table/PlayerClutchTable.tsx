import React from "react";
import { usePlayerClutch } from "./usePlayerClutch";
import { IPlayerClutchTableProps } from "./interfaces";
import Switch from "../../../../../common/switch-case/Switch";
import Case from "../../../../../common/switch-case/Case";
import { QueryStatus } from "../../../../../lib/http-requests";
import { Paper, Skeleton } from "@mui/material";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";

const PlayerClutchTable: React.FC<IPlayerClutchTableProps> = ({ steamid }) => {
  const { state } = usePlayerClutch({ steamid });
  // TODO implement the rest
  return (
    <Switch value={state}>
      <Case case={QueryStatus.success}>
        <Paper>clutch</Paper>
      </Case>
      <Case case={QueryStatus.loading}>
        <Skeleton animation="wave" style={{ height: "100%" }} />
      </Case>
      <Case case={QueryStatus.error}>
        <ErrorOutlineIcon color="error" fontSize="large" />
      </Case>
    </Switch>
  );
};

export default PlayerClutchTable;
