export interface MatchScoreboardContentProps {
  match_id: number;
}

export interface ScoreImageProps {
  teamType: "terrorist" | "counterTerrorist";
  score: number;
  opacity?: number;
  color?: string;
}

export interface TeamScoreCardProps {
  totalWins: number;
  winsAsT: number;
  winsAsCt: number;
  teamName: string;
  color: string;
}
