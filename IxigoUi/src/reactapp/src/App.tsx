import CssBaseline from "@mui/material/CssBaseline";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { I18nextProvider } from "react-i18next";
import { QueryClientProvider, QueryClient } from "react-query";
import BaseLayout from "./common/layout/BaseLayout";
import { i18n } from "./lib/multilanguage";

const darkTheme = createTheme({
  palette: {
    mode: "dark",
  },
});

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
    <I18nextProvider i18n={i18n}>
      <ThemeProvider theme={darkTheme}>
        <CssBaseline />
        <QueryClientProvider client={queryClient}>
          <BaseLayout />
        </QueryClientProvider>
      </ThemeProvider>
    </I18nextProvider>
  );
};

export default App;
