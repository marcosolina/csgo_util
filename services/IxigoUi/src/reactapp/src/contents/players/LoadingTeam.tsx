import { Skeleton } from "@mui/material";

const LoadingTeam = () => {
  return (
    <>
      <Skeleton animation="wave" height={60} />
      <Skeleton animation="wave" height={60} />
    </>
  );
};

export default LoadingTeam;
