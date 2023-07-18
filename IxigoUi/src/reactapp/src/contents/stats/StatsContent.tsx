// StatsContent.tsx
import React, { ReactNode } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import LeaderBoardContent from './leaderboard/LeaderboardContent';
import MatchContent from './matches/MatchContent';
import { Typography, Box } from '@mui/material';

interface LayoutProps {
  children: ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '1em' }}>
      {children}
    </div>
  );
};

const queryClient = new QueryClient();

const StatsContentWithReactQueryProvider: React.FC = () => (
  <QueryClientProvider client={queryClient}>
  <Layout>
    <Box textAlign="center">
      <Typography variant="h5">Leaderboard</Typography>
    </Box>
    <LeaderBoardContent />
    <Box textAlign="center">
      <Typography variant="h5">Recent Matches</Typography>
    </Box>
    <MatchContent />
  </Layout>
</QueryClientProvider>
);

export default StatsContentWithReactQueryProvider;
