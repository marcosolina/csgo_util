import { useTranslation } from "react-i18next";
import { IMapWeaponContent, IMapWeaponContentRequest } from "./interfaces";
import { useCallback, useEffect, useMemo, useState } from "react";
import { MRT_Cell, MRT_ColumnDef } from "material-react-table";
import { IWeaponMapData, useGetStats, USERS_REQUEST, MAP_PLAYER_WEAPON_STATS_CACHE } from "../../../../services";
import { combineQueryStatuses } from "../../../../lib/queries/queriesFunctions";
import { QueryStatus } from "../../../../lib/http-requests";
import TableLink from "../../../../common/table-link/TableLink";
import { TFunction } from "i18next";
import { WEAPONG_IMAGE } from "../../weaponImage";
import PieChartMini from "../../../../common/pie-chart-mini/PieChartMini";
import customHeader from "../../../../common/material-table/custom-header/customHeader";
import { UI_CONTEXT_PATH } from "../../../../lib/constants";

const COL_HEADERS_BASE_TRANSLATION_KEY = "page.stats.match.weapons.column-headers";
const SMALL_COL_SIZE = 5;

const COLUMNS_ORDER: string[] = [
  "username",
  "weapon",
  "weapon_img",
  "kills",
  "headshotkills",
  "damage_per_shot",
  "accuracy",
  "hits",
  "total_damage",
  "damage_per_hit",
  "headshotkills_percentage",
  "shots_fired",
  "headshot_percentage",
  "chest_hit_percentage",
  "stomach_hit_percentage",
  "arm_hit_percentage",
  "leg_hit_percentage",
];

function createColumnDefinition(
  key: string,
  t: TFunction<"translation", undefined, "translation">,
  playerPathUpdater: (steamid: string) => string
): MRT_ColumnDef<IWeaponMapData> {
  const cell: MRT_ColumnDef<IWeaponMapData> = {
    id: key,
    accessorFn: (row) => row[key as keyof IWeaponMapData],
    header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
    size: SMALL_COL_SIZE,
    Header: customHeader<IWeaponMapData>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
  };

  if (key === "username") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IWeaponMapData> }) => {
      const username = cell.getValue() as string;
      const steamid = cell.row.original.steamid;
      return <TableLink text={username} to={playerPathUpdater(steamid)} />;
    };
  }

  if (key === "weapon_img") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IWeaponMapData> }) => {
      const weapon = cell.getValue() as string;
      const imageUrl = WEAPONG_IMAGE[weapon];
      return imageUrl ? <img src={imageUrl} alt={weapon} style={{ height: "18px" }} /> : null;
    };
  }

  if (
    key === "accuracy" ||
    key === "headshotkills_percentage" ||
    key === "headshot_percentage" ||
    key === "chest_hit_percentage" ||
    key === "stomach_hit_percentage" ||
    key === "arm_hit_percentage" ||
    key === "leg_hit_percentage"
  ) {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IWeaponMapData> }) => {
      const value = cell.getValue() as number;
      return <PieChartMini percentage={value} color="darkturquoise" size={22} />;
    };
  }

  if (key === "kills" || key === "total_damage") {
    cell.aggregationFn = "sum" as any;
    cell.AggregatedCell = ({ cell }: { cell: any }) => <div>{cell.getValue()}</div>;
  }

  return cell;
}

export const useMapWeaponsContent = (request: IMapWeaponContentRequest): IMapWeaponContent => {
  const getStatsRequest = useMemo(() => {
    const copy = { ...MAP_PLAYER_WEAPON_STATS_CACHE };
    copy.queryParams = { mapname: request.mapName };
    return copy;
  }, [request]);

  const { t } = useTranslation();
  const [data, setData] = useState<IWeaponMapData[]>([]);

  const playerPathUpdater = useCallback((steamid: string) => {
    return `${UI_CONTEXT_PATH}/stats/player/${steamid}`;
  }, []);

  // Get the data
  const qUsersRequest = useGetStats(USERS_REQUEST);
  const qMatchWeaponsRequest = useGetStats(getStatsRequest);

  // Merge the queries statuses into one
  const queriesState = combineQueryStatuses([qUsersRequest, qMatchWeaponsRequest]);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<IWeaponMapData>[]>(() => {
    const cols: MRT_ColumnDef<IWeaponMapData>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, t, playerPathUpdater));
    });
    return cols;
  }, [t, playerPathUpdater]);

  const refetch = useCallback(() => {
    qUsersRequest.refetch();
    qMatchWeaponsRequest.refetch();
  }, [qUsersRequest, qMatchWeaponsRequest]);

  // Create the data
  useEffect(() => {
    if (queriesState === QueryStatus.success) {
      const users = qUsersRequest.data?.data?.view_data;
      let matchPlayerWeaponStats = qMatchWeaponsRequest.data?.data?.view_data;
      if (!users || !matchPlayerWeaponStats) {
        return;
      }

      // Add the weapon_img property
      matchPlayerWeaponStats = matchPlayerWeaponStats.map((wd: IWeaponMapData) => ({
        ...wd,
        weapon_img: wd.weapon,
      }));

      const data: IWeaponMapData[] = [];
      matchPlayerWeaponStats.forEach((playerStat) => {
        const user = users.find((user) => user.steam_id === playerStat.steamid);
        if (!user) {
          return;
        }

        data.push({
          ...playerStat,
          username: user.user_name,
        });
      });

      setData(data);
    }
  }, [queriesState, qUsersRequest.data, qMatchWeaponsRequest.data]);

  return {
    columns,
    state: queriesState,
    data,
    refetch,
  };
};
