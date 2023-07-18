
import React from 'react';

interface MatchScoreboardContentProps {
  match_id: string;
}

const MatchScoreboardContent: React.FC<MatchScoreboardContentProps> = ({ match_id }) => {
  return <div>Match Scoreboard Content for { match_id }</div>;
};

export default MatchScoreboardContent;
