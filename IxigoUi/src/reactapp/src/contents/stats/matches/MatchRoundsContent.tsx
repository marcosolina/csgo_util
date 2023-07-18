
import React from 'react';

interface MatchRoundsContentProps {
  match_id: string;
}

const MatchRoundsContent: React.FC<MatchRoundsContentProps> = ({ match_id }) => {
  return <div>Match Rounds Content for { match_id }</div>;
};

export default MatchRoundsContent;
