import { SvgIconTypeMap } from "@mui/material";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import { QueryStatus } from "react-query";
import { IRconRequest, IRconResponse } from "../../services";
import { IIxigoServerConfig } from "../../services/ixigo-server";

export interface IDefaultCommandsProps {
  isCs2?: boolean;
}

export interface IUseRconContentResult {
  sendCommand: (request: IRconRequest) => void;
  errorFields: string[];
  request: IRconRequest;
  setRequest: (request: IRconRequest) => void;
  rconResponse?: IRconResponse;
  queryState: QueryStatus;
  csgoConfig?: IIxigoServerConfig;
}

export interface IRconCommand {
  cmd: string;
  cmdKey: string;
  image?: string;
  icon?: () => React.ReactNode;
}

export interface IRconCommandProps {
  cmd: string;
  label: string;
  image?: string;
  icon?: OverridableComponent<SvgIconTypeMap<{}, "svg">> & { muiName: string };
  isCs2?: boolean;
}
