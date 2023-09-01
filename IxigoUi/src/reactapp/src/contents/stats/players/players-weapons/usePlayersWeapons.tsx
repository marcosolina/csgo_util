import { useTranslation } from "react-i18next";
import { IPlayerWeaponContentRequest, IPlayerWeaponContentResponse, IWeaponDataDetails } from "./interfaces";
import { useEffect, useMemo, useState } from "react";
import { IWeaponData, OVERALL_PLAYER_WEAPON_STATS_REQUEST, useGetStats } from "../../../../services/stats";
import { QueryStatus } from "../../../../lib/http-requests";
import { TFunction } from "i18next";
import { MRT_Cell, MRT_ColumnDef } from "material-react-table";
import customHeader from "../../../../common/material-table/custom-header/customHeader";
import { WEAPONG_IMAGE } from "../../weaponImage";
import PieChartMini from "../../../../common/pie-chart-mini/PieChartMini";

const BASE_TRANSLATION_KEY = "page.stats.player.content.weapons";
const COL_HEADERS_BASE_TRANSLATION_KEY = `${BASE_TRANSLATION_KEY}.column-headers`;
const SMALL_COL_SIZE = 5;

const COLUMNS_ORDER: string[] = [
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
  t: TFunction<"translation", undefined, "translation">
): MRT_ColumnDef<IWeaponDataDetails> {
  const cell: MRT_ColumnDef<IWeaponDataDetails> = {
    id: key,
    accessorFn: (row) => row[key as keyof IWeaponDataDetails],
    header: t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.header`),
    size: SMALL_COL_SIZE,
    Header: customHeader<IWeaponDataDetails>(t(`${COL_HEADERS_BASE_TRANSLATION_KEY}.${key}.tooltip`)),
  };

  if (key === "weapon_img") {
    cell.Cell = ({ cell }: { cell: MRT_Cell<IWeaponDataDetails> }) => {
      const value = cell.getValue() as string;
      const imageUrl = WEAPONG_IMAGE[value];
      return imageUrl ? <img src={imageUrl} alt={value} style={{ height: "18px" }} /> : null;
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
    cell.Cell = ({ cell }: { cell: MRT_Cell<IWeaponDataDetails> }) => {
      const value = cell.getValue() as number;
      return <PieChartMini percentage={value} color="darkturquoise" size={22} />;
    };
  }

  return cell;
}

export function usePlayersWeapons(request: IPlayerWeaponContentRequest): IPlayerWeaponContentResponse {
  const { t } = useTranslation();
  const getStatsRequest = useMemo(() => {
    const copy = { ...OVERALL_PLAYER_WEAPON_STATS_REQUEST };
    copy.queryParams = { steamid: request.steamId };
    return copy;
  }, [request]);

  const qGetStats = useGetStats(getStatsRequest);

  const [data, setData] = useState<IWeaponData[]>([]);

  // Create the columns
  const columns = useMemo<MRT_ColumnDef<IWeaponDataDetails>[]>(() => {
    const cols: MRT_ColumnDef<IWeaponDataDetails>[] = [];
    COLUMNS_ORDER.forEach((key) => {
      cols.push(createColumnDefinition(key, t));
    });
    return cols;
  }, [t]);

  // Create the data
  useEffect(() => {
    if (qGetStats.status === QueryStatus.success) {
      const weaponData = qGetStats.data?.data?.view_data;
      if (!weaponData) {
        return;
      }

      setData(weaponData.map((weapon) => ({ ...weapon, weapon_img: weapon.weapon })));
    }
  }, [qGetStats.status, qGetStats.data?.data]);

  return {
    state: qGetStats.status,
    columns,
    data,
  };
}
