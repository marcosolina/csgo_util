export interface MatchScoreboardContentProps {
    match_id: number;
}

export interface ScoreImageProps {
  teamType: "terrorist" | "counterTerrorist";
  score: number;
  opacity?: number;
  color?: string;
}