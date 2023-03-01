import { useGetDemFiles } from "../../services";

const BaseLayout = () => {
  useGetDemFiles();
  return <div>Base layout</div>;
};

export default BaseLayout;
