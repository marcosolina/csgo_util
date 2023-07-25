import CssBaseline from "@mui/material/CssBaseline";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { I18nextProvider } from "react-i18next";
import { QueryClientProvider, QueryClient } from "react-query";
import BaseLayout from "./common/layout/BaseLayout";
import { i18n } from "./lib/multilanguage";
import { SnackbarKey, SnackbarProvider, useSnackbar } from "notistack";
import { IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { RconContentProvider } from "./contents/rcon/indext";
import { BrowserRouter } from "react-router-dom";
import { PlayersContentProvider } from "./contents/players/indext";

const darkTheme = createTheme({
  palette: {
    mode: "dark",
  },
});

const SnackbarCloseButton: React.FC<{ snackbarKey: SnackbarKey }> = ({ snackbarKey }) => {
  const { closeSnackbar } = useSnackbar();

  return (
    <IconButton onClick={() => closeSnackbar(snackbarKey)}>
      <CloseIcon />
    </IconButton>
  );
};


const App = () => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        refetchOnWindowFocus: false,
        // ms * seconds * minutes
        staleTime: 1000 * 30,
        cacheTime: 1000 * 60 * 5,
      },
    },
  });


  return (
      <BrowserRouter>
        <SnackbarProvider maxSnack={10} action={(snackbarKey) => <SnackbarCloseButton snackbarKey={snackbarKey} />}>
          <I18nextProvider i18n={i18n}>
            <ThemeProvider theme={darkTheme}>
              <CssBaseline />
              <QueryClientProvider client={queryClient}>
                <RconContentProvider>
                  <PlayersContentProvider>
                    <BaseLayout />
                  </PlayersContentProvider>
                </RconContentProvider>
              </QueryClientProvider>
            </ThemeProvider>
          </I18nextProvider>
        </SnackbarProvider>
      </BrowserRouter>
  );
};

export default App;
