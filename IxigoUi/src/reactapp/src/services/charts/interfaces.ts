export interface IMapPlayed {
  map_name: string;
  count: number;
}

export interface IMapsPlayed {
  maps: IMapPlayed[];
}

export interface IGetAvgScoresPerMapRequest {
  steamIds: string[];
  scoreType: string;
}

export interface IAvgScoresPerMap {
  scores: Record<string, IAvgScorePerMap[]>;
}

export interface IAvgScorePerMap {
  steam_id: string;
  map_name: string;
  avg_score: number;
}
