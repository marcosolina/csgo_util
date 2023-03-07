import { IRconCommand } from "./interfaces";
import { useRconContentProvider } from "./useRconContentProvider";

const RconCommand: React.FC<IRconCommand> = (props) => {
  const { request, setRequest, sendCommand } = useRconContentProvider();

  const clickHandler = () => {
    const newRequest = { ...request };
    newRequest.rcon_command = props.cmd;
    setRequest(newRequest);
    sendCommand(newRequest);
  };

  return <div onClick={clickHandler}>{props.name}</div>;
};

export default RconCommand;
