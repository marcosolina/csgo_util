import React, { useContext } from "react";
import { useMemo } from "react";
import { Tooltip, Box } from "@mui/material";
import { MaterialReactTable } from "material-react-table";
import { Link } from "react-router-dom";
import { QueryStatus } from "../../../../lib/http-requests";
import PieChartMini from "../../../../common/pie-chart-mini/PieChartMini";
import { useTranslation } from "react-i18next";
import { useMatchTeamTable } from "./useMatchTeamTable";
import { ITeamMatchContentRequest } from "./interfaces";

const MatchTeamTable: React.FC<ITeamMatchContentRequest> = ({ match_id, team }) => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = useMatchTeamTable({ match_id, team});

  function createCustomHeader(tooltipText: string) {
    return ({ column }: { column: any }) => (
      <Tooltip title={tooltipText}>
        <span>{column.columnDef.header}</span>
      </Tooltip>
    );
  }
const smallColSize=5;
  const columnsOld = useMemo(
    () => [
      {
        accessorKey: "usernames" as const,
        header: `${team.toUpperCase()} PLAYERS`,
        minSize: 100,
        Header: createCustomHeader("Player"),
        Cell: ({ cell }: { cell: any }) => {
          const username = cell.getValue() as string;
          const steamid = cell.row.original.steamid;
          return (
            <Box
              component={Link}
              to={`/player/${steamid}`}
              sx={{
                color: cell.row.original.last_round_team === "team1" ? "#90caf9" : "orange",
                fontWeight: "bold",
                textDecoration: "none",
                cursor: "pointer",
                "&:hover": {
                  textDecoration: "underline",
                },
              }}
              onClick={(e) => {
              }}
            >
              {username}
            </Box>
          );
        },
      },
      {
        accessorKey: "roundsplayed" as const,
        header: "R",
        size: smallColSize,
        Header: createCustomHeader("Rounds Played"),
      },
      {
        accessorKey: "last_round_team" as const,
        header: "TEAM",
        size: smallColSize,
        Header: createCustomHeader("Team"),
      },
      {
        accessorKey: "rounds_on_team1" as const,
        header: "RNDT1",
        size: smallColSize,
        Header: createCustomHeader("Rounds on Team 1"),
      },
      {
        accessorKey: "rounds_on_team2" as const,
        header: "RNDT2",
        size: smallColSize,
        Header: createCustomHeader("Rounds on Team 2"),
      },
      { accessorKey: "kills" as const, header: "K", size: smallColSize, Header: createCustomHeader("Total kills") },
      { accessorKey: "deaths" as const, header: "D", size: smallColSize, Header: createCustomHeader("Total deaths") },
      { accessorKey: "assists" as const, header: "A", size: smallColSize, Header: createCustomHeader("Total assists") },
      { accessorKey: "score" as const, header: "SCR", size: smallColSize, Header: createCustomHeader("Score") },
      { accessorKey: "rws" as const, header: "RWS", size: smallColSize, Header: createCustomHeader("Round Win Share") },

      { accessorKey: "headshots" as const, header: "HS", size: smallColSize, Header: createCustomHeader("Headshots") },
      {
        accessorKey: "headshot_percentage" as const,
        header: "HS%",
        size: smallColSize,
        Header: createCustomHeader("Headshot Percentage"),
        Cell: ({ cell, row }: { cell: any; row: { index: number } }) => {
          return <PieChartMini percentage={cell.getValue()} color="darkturquoise" size={22} />;
        },
      },
      {
        accessorKey: "mvp" as const,
        header: "MVP",
        size: smallColSize,
        Header: createCustomHeader("Most Valuable Player"),
      },
      {
        accessorKey: "hltv_rating" as const,
        header: "HLTV",
        Header: createCustomHeader(
          "HLTV Rating - a weighted sum of K/D ratio, rounds survived ratio and rounds with multiple kills. This is the default rating used in the auto team balancing."
        ),
        size: smallColSize,
        Cell: ({ cell }: { cell: any }) => {
          const rating = cell.getValue() as number;
          let backgroundColor = "#D0021B";
          if (rating >= 0.85 && rating < 1.1) {
            backgroundColor = "#D39121"; // Amber in hexadecimal
          } else if (rating >= 1.1 && rating < 1.5) {
            backgroundColor = "#7ED321";
          } else if (rating >= 1.5) {
            backgroundColor = "#90caf9";
          }
          return (
            <span
              style={{
                borderRadius: "4px",
                padding: "2px",
                display: "block",
                color: "#fff",
                width: "43px",
                textAlign: "center",
                backgroundColor: backgroundColor,
              }}
            >
              {rating}
            </span>
          );
        },
      },
      {
        accessorKey: "adr" as const,
        header: "ADR",
        size: smallColSize,
        Header: createCustomHeader("Average Damage per Round"),
      },
      { accessorKey: "kpr" as const, header: "KPR", size: smallColSize, Header: createCustomHeader("Kills Per Round") },
      {
        accessorKey: "dpr" as const,
        header: "DPR",
        size: smallColSize,
        Header: createCustomHeader("Deaths Per Round"),
      },
      {
        accessorKey: "kdr" as const,
        header: "K/D",
        size: smallColSize,
        Header: createCustomHeader("Kill/Death Ratio"),
      },
      { accessorKey: "hr" as const, header: "HR", size: smallColSize, Header: createCustomHeader("Hostages Rescued") },
      { accessorKey: "bp" as const, header: "BP", size: smallColSize, Header: createCustomHeader("Bomb Planted") },
      { accessorKey: "ud" as const, header: "UD", size: smallColSize, Header: createCustomHeader("Utility Damage") },
      {
        accessorKey: "ffd" as const,
        header: "FFD",
        size: smallColSize,
        Header: createCustomHeader("Friendly Fire Damage"),
      },
      { accessorKey: "td" as const, header: "TD", size: smallColSize, Header: createCustomHeader("Trade Deaths") },
      {
        accessorKey: "tda" as const,
        header: "TDA",
        size: smallColSize,
        Header: createCustomHeader("Total Damage Armour"),
      },
      {
        accessorKey: "tdh" as const,
        header: "TDH",
        size: smallColSize,
        Header: createCustomHeader("Total Damage Health"),
      },
      { accessorKey: "fa" as const, header: "FA", size: smallColSize, Header: createCustomHeader("Flash Assists") },
      {
        accessorKey: "ebt" as const,
        header: "EBT",
        size: smallColSize,
        Header: createCustomHeader("Enemy Blind Time"),
      },
      {
        accessorKey: "fbt" as const,
        header: "FBT",
        size: smallColSize,
        Header: createCustomHeader("Friendly Blind Time"),
      },
      { accessorKey: "ek" as const, header: "EK", size: smallColSize, Header: createCustomHeader("Entry Kills") },
      { accessorKey: "tk" as const, header: "TK", size: smallColSize, Header: createCustomHeader("Trade Kills") },
      { accessorKey: "_1k" as const, header: "1K", size: smallColSize, Header: createCustomHeader("One Kill") },
      { accessorKey: "_2k" as const, header: "2K", size: smallColSize, Header: createCustomHeader("Two Kills") },
      { accessorKey: "_3k" as const, header: "3K", size: smallColSize, Header: createCustomHeader("Three Kills") },
      { accessorKey: "_4k" as const, header: "4K", size: smallColSize, Header: createCustomHeader("Four Kills") },
      { accessorKey: "_5k" as const, header: "5K", size: smallColSize, Header: createCustomHeader("Five Kills") },
      { accessorKey: "_1v1" as const, header: "1v1", size: smallColSize, Header: createCustomHeader("1v1 Clutches") },
      { accessorKey: "_1v2" as const, header: "1v2", size: smallColSize, Header: createCustomHeader("1v2 Clutches") },
      { accessorKey: "_1v3" as const, header: "1v3", size: smallColSize, Header: createCustomHeader("1v3 Clutches") },
      { accessorKey: "_1v4" as const, header: "1v4", size: smallColSize, Header: createCustomHeader("1v4 Clutches") },
      { accessorKey: "_1v5" as const, header: "1v5", size: smallColSize, Header: createCustomHeader("1v5 Clutches") },
    ],
    [team]
  );

  return (
    <MaterialReactTable
      columns={columns}
      data={data ?? []}
      initialState={{
        showColumnFilters: false,
        columnVisibility: {
          steamid: false,
          last_round_team: false,
          rounds_on_team1: false,
          rounds_on_team2: false,
          kpr: false,
          dpr: false,
          kdr: false,
          ek: false,
          tk: false,
          _1v1: false,
          _1v2: false,
          _1v3: false,
          _1v4: false,
          _1v5: false,
          _1k: false,
          _2k: false,
          _3k: false,
          _4k: false,
          _5k: false,
          hr: false,
          bp: false,
          ffd: false,
          td: false,
          tda: false,
          tdh: false,
          fbt: false,
          fa: false,
          headshots: false,
        },
        density: "compact",
        sorting: [{ id: "hltv_rating", desc: true }], //sort by state by default
        columnPinning: { left: ["usernames"] },
      }}
      enableColumnActions={false}
      enableColumnFilters={false}
      enableDensityToggle={false}
      enableSorting={true}
      enableTopToolbar={true}
      enableBottomToolbar={false}
      enableGlobalFilter={false}
      enableFullScreenToggle={false}
      enableHiding={true}
      muiTableBodyRowProps={{ hover: false }}
      rowCount={data?.length ?? 0}
      state={{
        isLoading: state === QueryStatus.loading,
        showAlertBanner: state === QueryStatus.error,
        showProgressBars: state === QueryStatus.loading,
      }}
    />
  );
};

export default MatchTeamTable;
