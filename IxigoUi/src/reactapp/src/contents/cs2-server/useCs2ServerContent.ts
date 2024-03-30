import { IUseCs2ServerContentResult } from ".";
import { IMapResponse } from "../../services/rcon/interfaces";

const CS2_MAPS: IMapResponse[] = [
  {
    is_workshop_map: true,
    map_name: "Assembly",
    workshop_id: "3071005299",
  },
  {
    is_workshop_map: true,
    map_name: "Brewery",
    workshop_id: "3070290240",
  },
  {
    is_workshop_map: true,
    map_name: "Mutiny",
    workshop_id: "3070766070",
  },
  {
    is_workshop_map: true,
    map_name: "Vandal",
    workshop_id: "3071899764",
  },
  {
    is_workshop_map: true,
    map_name: "Biome CS2",
    workshop_id: "3075706807",
  },
  {
    is_workshop_map: true,
    map_name: "Bunker",
    workshop_id: "3085200029",
  },
  {
    is_workshop_map: true,
    map_name: "Mission",
    workshop_id: "3084661017",
  },
  {
    is_workshop_map: true,
    map_name: "Rush",
    workshop_id: "3077752384",
  },
  {
    is_workshop_map: true,
    map_name: "Codewise",
    workshop_id: "3100864853",
  },
  {
    is_workshop_map: true,
    map_name: "Minecraft",
    workshop_id: "3095875614",
  },
  {
    is_workshop_map: true,
    map_name: "St Marc",
    workshop_id: "3070562370",
  },
  {
    is_workshop_map: true,
    map_name: "Short Dust",
    workshop_id: "3070612859",
  },
  {
    is_workshop_map: true,
    map_name: "Ravine CS2",
    workshop_id: "3121051997",
  },
  {
    is_workshop_map: true,
    map_name: "Dust",
    workshop_id: "3127729110",
  },
  {
    is_workshop_map: true,
    map_name: "Thera",
    workshop_id: "3121217565",
  },
  {
    is_workshop_map: true,
    map_name: "Foroglio",
    workshop_id: "3132854332",
  },
  {
    is_workshop_map: true,
    map_name: "Memento",
    workshop_id: "3165559377",
  },
  {
    is_workshop_map: true,
    map_name: "Lost Coast",
    workshop_id: "3181655247",
  },
  {
    is_workshop_map: true,
    map_name: "Whiterun",
    workshop_id: "3114023815",
  },
  {
    is_workshop_map: true,
    map_name: "Maginot",
    workshop_id: "3195399109",
  },
  {
    is_workshop_map: true,
    map_name: "Highland",
    workshop_id: "3150246494",
  },
  {
    is_workshop_map: true,
    map_name: "Sevirno",
    workshop_id: "3157804628",
  },
];

export const useCs2ServerContent = (): IUseCs2ServerContentResult => {
  return {
    maps: CS2_MAPS,
  };
};
