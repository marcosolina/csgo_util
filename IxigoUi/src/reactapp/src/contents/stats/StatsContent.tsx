// StatsContent.tsx
import React, { ReactNode } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import LeaderboardContent from './leaderboard/LeaderboardContent';
import MatchContent from './matches/MatchContent';
import { Typography, Box } from '@mui/material';

interface LayoutProps {
  children: ReactNode;
}

interface StatsContentProps {
  setSelectedTab: React.Dispatch<React.SetStateAction<number | null>>;
  selectedTab: number | null;  // Make sure this line is included in the interface
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '1em' }}>
      {children}
    </div>
  );
};

const queryClient = new QueryClient();

const StatsContentWithReactQueryProvider: React.FC<StatsContentProps> = ({ setSelectedTab, selectedTab }) => (
  <QueryClientProvider client={queryClient}>
  <Layout>
    <Box textAlign="center">
      <Typography variant="h5">Leaderboard</Typography>
    </Box>
    <LeaderboardContent setSelectedTab={setSelectedTab} selectedTab={selectedTab} />
    <Box textAlign="center">
      <Typography variant="h5">Recent Matches</Typography>
    </Box>
    <MatchContent setSelectedTab={setSelectedTab} selectedTab={selectedTab} />
  </Layout>
</QueryClientProvider>
);

export default StatsContentWithReactQueryProvider;
