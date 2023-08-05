import { Link as RouterLink } from 'react-router-dom';
import MuiLink from '@mui/material/Link';
import { ITableLinkProps } from "./interfaces";

const TableLink: React.FC<ITableLinkProps> = ({ text, to, color="white" }) => {
  return (
    <MuiLink
      component={RouterLink}
      to={to}
      sx={{
        color: color,
        fontWeight: "bold",
        textDecoration: "none",
        cursor: "pointer",
        "&:hover": {
          textDecoration: "underline",
        },
      }}
    >
      {text}
    </MuiLink>
  );
};

export default TableLink;
