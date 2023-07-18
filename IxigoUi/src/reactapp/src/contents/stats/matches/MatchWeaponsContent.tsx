
import React from 'react';

interface MatchWeaponsContentProps {
  match_id: string;
}

const MatchWeaponsContent: React.FC<MatchWeaponsContentProps> = ({ match_id }) => {
  return <div>Match Weapons Content for { match_id }</div>;
};

export default MatchWeaponsContent;
