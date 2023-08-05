import { Link } from "@mui/material";
import { ITableLinkProps } from "./interfaces"; // adjust the path according to your project structure

const TableLink: React.FC<ITableLinkProps> = ({ text, onClickHandler, color }) => {
  return (
    <Link
      component="button"
      sx={{
        color: color,
        fontWeight: "bold",
        textDecoration: "none",
        cursor: "pointer",
        "&:hover": {
          textDecoration: "underline",
        },
      }}
      onClick={(e) => {
        e.preventDefault();
        onClickHandler();
      }}
    >
      {text}
    </Link>
  );
};

export default TableLink;
