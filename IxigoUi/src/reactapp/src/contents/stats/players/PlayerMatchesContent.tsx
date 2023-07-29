import React, { useContext } from "react";
import { useMemo } from "react";
import { Tooltip, Box } from "@mui/material";
import { MaterialReactTable } from "material-react-table";
import { useQuery } from "react-query";
import { Link } from "react-router-dom";
import { SERVICES_URLS } from "../../../lib/constants/paths";
import PieChartMini from "../../../common/pie-chart-mini/PieChartMini";

interface PlayerMatchesContentProps {
  steamid: string;
}

interface PlayerMatch {
  usernames: string;
  steamid: string;
  mapName: string;
  roundsplayed: number;
  rounds_on_team1: number;
  rounds_on_team2: number;
  last_round_team: string;

  kills: number;
  deaths: number;
  assists: number;
  kpr: number;
  dpr: number;
  kdr: number;
  ek: number;
  tk: number;

  _1v1: number;
  _1v2: number;
  _1v3: number;
  _1v4: number;
  _1v5: number;

  _1k: number;
  _2k: number;
  _3k: number;
  _4k: number;
  _5k: number;

  headshots: number;
  headshot_percentage: number;
  hr: number;

  mvp: number;
  hltv_rating: number;

  adr: number;
  kast: number;

  bp: number;
  ud: number;
  ffd: number;

  score: number;
  rws: number;

  td: number;
  tda: number;
  tdh: number;

  fbt: number;
  fa: number;
  ebt: number;

  match_date: string;
  match_id: number;
  team: number;
  ff: number;
  bd: number;
}

type Match = {
  team1_wins_as_ct: number;
  match_id: number;
  team2_wins_as_ct: number;
  team2_wins_as_t: number;
  mapname: string;
  total_t_wins: number;
  total_ct_wins: number;
  team1_total_wins: number;
  match_date: string;
  team1_wins_as_t: number;
  team2_total_wins: number;
};

const smallColSize = 5;
const PlayerMatchesContent: React.FC<PlayerMatchesContentProps> = ({ steamid }) => {
  const {
    data: matchData,
    isError,
    isLoading,
  } = useQuery<PlayerMatch[], Error>({
    queryKey: ["playermatch" + steamid],
    queryFn: async () => {
      const url1 = new URL(
        `${SERVICES_URLS["dem-manager"]["get-stats"]}PLAYER_MATCH_STATS_EXTENDED_CACHE?steamid=${steamid}`
      );
      const url2 = new URL(`${SERVICES_URLS["dem-manager"]["get-stats"]}MATCH_RESULTS_CACHE`);
      const responses = await Promise.all([fetch(url1.href), fetch(url2.href)]);

      const jsons = await Promise.all(
        responses.map((response) => {
          if (!response.ok) {
            throw new Error("Network response was not ok");
          }
          return response.json();
        })
      );

      const playerMatchStatsExtended = jsons[0].view_data;
      const matches = jsons[1].view_data;

      // Create a lookup for mapName from match_id in matches
      const mapNameLookup = matches.reduce((lookup: { [key: number]: string }, match: Match) => {
        lookup[match.match_id] = match.mapname;
        return lookup;
      }, {});

      let matchData = playerMatchStatsExtended
        .map((p: PlayerMatch) => {
          p.mapName = mapNameLookup[p.match_id];
          return p;
        })
        .filter((p: PlayerMatch) => p.steamid === steamid);

      return matchData;
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
      {
        accessorKey: "match_date" as const,
        header: "DATE",
        minSize: 100,
        Header: createCustomHeader("Match Date"),
        Cell: ({ cell }: { cell: any }) => {
          const date = cell.getValue() as string;
          const match_id = cell.row.original.match_id;
          const dateF = new Date(date);
          const formattedDate = dateF.toLocaleDateString("en-GB", { year: "numeric", month: "long", day: "numeric" });
          const formattedTime = dateF.toLocaleTimeString("en-GB", { hour: "2-digit", minute: "2-digit" });
          /*
          const selectedStatsContext = useContext(SelectedStatsContext);
          if (!selectedStatsContext) {
            throw new Error("useContext was called outside of the selectedStatsContext provider");
          }
          const { setSelectedMatch, setSelectedSubpage } = selectedStatsContext;
          */
          return (
            <Box
              component={Link}
              to={`/matches/${match_id}`}
              sx={{
                color: "white",
                fontWeight: "bold",
                textDecoration: "none",
                cursor: "pointer",
                "&:hover": {
                  textDecoration: "underline",
                },
              }}
              onClick={(e) => {
                /*
                e.preventDefault(); // Prevent the link from navigating
                setSelectedMatch(match_id);
                setSelectedSubpage("match");
                */
              }}
            >
              {`${formattedDate}, ${formattedTime}`}
            </Box>
          );
        },
      },
      {
        accessorKey: "mapName" as const,
        header: "Map",
        Cell: ({ cell }: { cell: any }) => {
          const mapName = cell.getValue() as string;
          /*
          const selectedStatsContext = useContext(SelectedStatsContext);
          if (!selectedStatsContext) {
            throw new Error("useContext was called outside of the selectedStatsContext provider");
          }
          const { setSelectedSubpage, setSelectedMap } = selectedStatsContext;
          */
          return (
            <Box
              component={Link}
              to={`/map/${mapName}`}
              sx={{
                color: "white",
                fontWeight: "bold",
                textDecoration: "none",
                cursor: "pointer",
                "&:hover": {
                  textDecoration: "underline",
                },
              }}
              onClick={(e) => {
                /*
                e.preventDefault(); // Prevent the link from navigating
                setSelectedMap(mapName);
                setSelectedSubpage("map");
                */
              }}
            >
              {mapName}
            </Box>
          );
        },
      },
      {
        accessorKey: "roundsplayed" as const,
        header: "RNDS",
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
    []
  );

  return (
    <MaterialReactTable
      columns={columns}
      data={matchData ?? []}
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
          hr: false,
          bp: false,
          ud: false,
          ffd: false,
          td: false,
          tda: false,
          tdh: false,
          fbt: false,
          fa: false,
          ebt: false,
        },
        density: "compact",
        sorting: [{ id: "match_date", desc: true }], //sort by state by default
        columnPinning: { left: ["match_date"] },
      }}
      enableColumnActions={false}
      enableColumnFilters
      enableColumnFilterModes
      enableSorting={true}
      enableFilterMatchHighlighting={false}
      enableTopToolbar={true}
      enableBottomToolbar
      enableDensityToggle={false}
      enableGlobalFilter={false}
      enableFullScreenToggle={false}
      enableHiding={true}
      muiTableBodyRowProps={{ hover: false }}
      muiToolbarAlertBannerProps={
        isError
          ? {
              color: "error",
              children: "Error loading data",
            }
          : undefined
      }
      rowCount={matchData?.length ?? 0}
      state={{
        isLoading,
        showAlertBanner: isError,
        showProgressBars: isLoading,
      }}
    />
  );
};

export default PlayerMatchesContent;
