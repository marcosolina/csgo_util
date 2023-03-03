import { Skeleton } from "@mui/material";

const Loading = () => {
  return (
    <>
      <Skeleton animation="wave" height={60} />
      <Skeleton animation="wave" height={60} />
    </>
  );
};

export default Loading;
