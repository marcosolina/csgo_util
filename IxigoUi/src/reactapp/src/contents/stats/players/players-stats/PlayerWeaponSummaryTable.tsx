import React from "react";
import { useMemo } from "react";
import { Tooltip } from "@mui/material";
import { WEAPONG_IMAGE } from "../../weaponImage";
import { MaterialReactTable } from "material-react-table";
import { useQuery } from "react-query";
import { SERVICES_URLS } from "../../../../lib/constants/paths";
import PieChartMini from "../../../../common/pie-chart-mini/PieChartMini";
import { IPlayerStats } from "./interfaces";

interface WeaponData {
  steamid: string;
  kills: number;
  headshotkills: number;
  damage_per_shot: number;
  accuracy: number;
  hits: number;
  weapon: string;
  weapon_img: string;
  total_damage: number;
  damage_per_hit: number;
  headshotkills_percentage: number;
  shots_fired: number;
  chest_hit_percentage: number;
  leg_hit_percentage: number;
  stomach_hit_percentage: number;
  arm_hit_percentage: number;
}

const smallColSize = 5;
const PlayerWeaponSummaryTable: React.FC<IPlayerStats> = ({ steamid }) => {
  const {
    data: weaponData,
    isError,
    isLoading,
  } = useQuery<WeaponData[], Error>({
    queryKey: ["playerweapon" + steamid],
    queryFn: async () => {
      const url1 = new URL(
        `${SERVICES_URLS["dem-manager"]["get-stats"]}/OVERALL_PLAYER_WEAPON_STATS_CACHE?steamid=${steamid}`
      );

      const responses = await Promise.all([fetch(url1.href)]);

      const jsons = await Promise.all(
        responses.map((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok");
          }
          return response.json();
        })
      );

      const playerOverallStatsExtended = jsons[0].view_data;

      let weaponData = playerOverallStatsExtended.filter((p: WeaponData) => p.steamid === steamid);
      // Add the weapon_img property
      weaponData = weaponData.map((wd: WeaponData) => ({
        ...wd,
        weapon_img: wd.weapon,
      }));
      return weaponData;
    },
    keepPreviousData: true,
  });

  function createCustomHeader(tooltipText: string) {
    return ({ column }: { column: any }) => (
      <Tooltip title={tooltipText}>
        <span>{column.columnDef.header}</span>
      </Tooltip>
    );
  }

  const columns = useMemo(
    () => [
      { accessorKey: "weapon" as const, header: "WEAPON", size: smallColSize, Header: createCustomHeader("Weapon") },
      {
        accessorKey: "weapon_img" as const,
        header: "",
        size: smallColSize,
        Header: createCustomHeader("Weapon"),
        Cell: ({ cell }: { cell: any }) => {
          const weapon = cell.getValue() as string;
          const imageUrl = WEAPONG_IMAGE[weapon];
          return imageUrl ? <img src={imageUrl} alt={weapon} style={{ height: "18px" }} /> : null;
        },
      },
      { accessorKey: "kills" as const, header: "K", size: smallColSize, Header: createCustomHeader("Total kills") },
      {
        accessorKey: "headshotkills" as const,
        header: "HSK",
        size: smallColSize,
        Header: createCustomHeader("Total headshot kills"),
      },
      {
        accessorKey: "damage_per_shot" as const,
        header: "DPS",
        size: smallColSize,
        Header: createCustomHeader("Damage per shot"),
      },
      {
        accessorKey: "accuracy" as const,
        header: "ACC",
        size: smallColSize,
        Header: createCustomHeader("Accuracy"),
        Cell: ({ cell, row }: { cell: any; row: { index: number } }) => {
          return <PieChartMini percentage={cell.getValue()} color="darkturquoise" size={22} />;
        },
      },
      { accessorKey: "hits" as const, header: "HITS", size: smallColSize, Header: createCustomHeader("Total hits") },
      {
        accessorKey: "total_damage" as const,
        header: "TDMG",
        size: smallColSize,
        Header: createCustomHeader("Total damage"),
      },
      {
        accessorKey: "damage_per_hit" as const,
        header: "DPH",
        size: smallColSize,
        Header: createCustomHeader("Damage per hit"),
      },
      {
        accessorKey: "headshotkills_percentage" as const,
        header: "HSKP",
        size: smallColSize,
        Header: createCustomHeader("Headshot kills percentage"),
        Cell: ({ cell, row }: { cell: any; row: { index: number } }) => {
          return <PieChartMini percentage={cell.getValue()} color="darkturquoise" size={22} />;
        },
      },
      {
        accessorKey: "shots_fired" as const,
        header: "SHOTS",
        size: smallColSize,
        Header: createCustomHeader("Shots fired"),
      },
      {
        accessorKey: "chest_hit_percentage" as const,
        header: "CHP",
        size: smallColSize,
        Header: createCustomHeader("Chest hit percentage"),
        Cell: ({ cell, row }: { cell: any; row: { index: number } }) => {
          return <PieChartMini percentage={cell.getValue()} color="darkturquoise" size={22} />;
        },
      },
      {
        accessorKey: "stomach_hit_percentage" as const,
        header: "SHP",
        size: smallColSize,
        Header: createCustomHeader("Stomach hit percentage"),
        Cell: ({ cell, row }: { cell: any; row: { index: number } }) => {
          return <PieChartMini percentage={cell.getValue()} color="darkturquoise" size={22} />;
        },
      },
      {
        accessorKey: "arm_hit_percentage" as const,
        header: "AHP",
        size: smallColSize,
        Header: createCustomHeader("Arm hit percentage"),
        Cell: ({ cell, row }: { cell: any; row: { index: number } }) => {
          return <PieChartMini percentage={cell.getValue()} color="darkturquoise" size={22} />;
        },
      },
      {
        accessorKey: "leg_hit_percentage" as const,
        header: "LHP",
        size: smallColSize,
        Header: createCustomHeader("Leg hit percentage"),
        Cell: ({ cell, row }: { cell: any; row: { index: number } }) => {
          return <PieChartMini percentage={cell.getValue()} color="darkturquoise" size={22} />;
        },
      },
    ],
    []
  );

  return (
    <MaterialReactTable
      columns={columns}
      data={weaponData ?? []} //data is undefined on first render  ?.data ?? []
      initialState={{
        showColumnFilters: false,
        density: "compact",
        pagination: { pageIndex: 0, pageSize: 5 },
        sorting: [{ id: "kills", desc: true }], //sort by state by default
        columnPinning: { left: ["weapon", "weapon_img"] },
      }}
      enableColumnActions={false}
      enableColumnFilters={false}
      enablePagination={true}
      enableSorting={true}
      enableBottomToolbar={true}
      enableTopToolbar={false}
      enableGlobalFilter={false}
      enableFullScreenToggle={false}
      enableHiding={false}
      muiTableBodyRowProps={{ hover: false }}
      muiToolbarAlertBannerProps={
        isError
          ? {
              color: "error",
              children: "Error loading data",
            }
          : undefined
      }
      rowCount={weaponData?.length ?? 0}
      state={{
        isLoading,
        showAlertBanner: isError,
        showProgressBars: isLoading,
      }}
    />
  );
};

export default PlayerWeaponSummaryTable;