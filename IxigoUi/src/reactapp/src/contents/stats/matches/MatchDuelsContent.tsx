
import React from 'react';

interface MatchDuelsContentProps {
  match_id: number;
}

const MatchDuelsContent: React.FC<MatchDuelsContentProps> = ({ match_id }) => {
  return <div>Match Duels Content for { match_id }</div>;
};

export default MatchDuelsContent;
