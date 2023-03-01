import { QueryClientProvider, QueryClient } from "react-query";
import BaseLayout from "./common/layout/BaseLayout";

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
    <QueryClientProvider client={queryClient}>
      <BaseLayout />
    </QueryClientProvider>
  );
};

export default App;
