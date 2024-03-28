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
];

export const useCs2ServerContent = (): IUseCs2ServerContentResult => {
  return {
    maps: CS2_MAPS,
  };
};
