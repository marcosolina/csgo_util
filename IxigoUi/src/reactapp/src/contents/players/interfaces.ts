import { QueryStatus } from "react-query";
import { IxigoPossibleValue } from "../../common/select";
import { ICsgoUser } from "../../services";
import { ITeam } from "../../services/players-manager";

export interface IPlayersContent {
  state: QueryStatus;
  penaltyWeight: number;
  percPlayed: number;
  matchesToConsider: number;
  scoreType: string;
  listOfSelectedPlayers: string[];
  possiblePercPlayedValues: IxigoPossibleValue[];
  possibleScoreTypesValues: IxigoPossibleValue[];

  setPenaltyWeight: (p: number) => void;
  setPercPlayed: (p: number) => void;
  setMatchesToConsider: (r: number) => void;
  setScoreType: (s: string) => void;
  setListOfSelectedPlayers: (l: string[]) => void;

  scoreTypes?: Record<string, string>;
  csgoPlayers?: ICsgoUser[];
}

export interface IIxigoTeamProps {
  picture: string;
  title: string;
  team?: ITeam;
  status: QueryStatus;
}
