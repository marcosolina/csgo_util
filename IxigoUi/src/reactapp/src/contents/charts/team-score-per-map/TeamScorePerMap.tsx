import { Box, Grid, Typography } from "@mui/material";
import { useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { COLOR_PALETTE, DEFAULT_SPACING, generateRgbaString } from "../../../lib/constants";
import { useTeamScorePerMap } from "./useTeamScorePerMap";
import IxigoSelect from "../../../common/select/IxigoSelect";
import { IxigoTextType } from "../../../common/input";
import IxigoText from "../../../common/input/IxigoText";
import { ChartData, ChartOptions, registerables, Chart, ChartDataset, LegendItem } from "chart.js";
import { Bar } from "react-chartjs-2";
import { IUserGotvScore } from "../../../services/charts";

Chart.register(...registerables);

const DATA: ChartData<"bar", number[], string> = {
  labels: [], // Date played
  datasets: [],
};

const OPTIONS: ChartOptions<"bar"> = {
  plugins: {
    title: {
      display: false,
    },
    legend: {
      labels: {
        filter: (item: LegendItem, data: ChartData) => {
          if (!!item.datasetIndex) {
            return data.datasets[item.datasetIndex].stack === "CT";
          }
          return true;
        },
      },
    },
  },
  scales: {
    x: {
      stacked: true,
      grid: {
        display: true,
        color: "rgba(255,255,255,0.3)",
      },
    },
    y: {
      stacked: true,
      grid: {
        display: true,
        color: "rgba(255,255,255,0.3)",
      },
    },
  },
};

const PLACE_HOLDER_USER: IUserGotvScore = {
  user_name: "",
  steam_id: "",
  round_win_share: 0,
  kills: 0,
  assists: 0,
  deaths: 0,
  kill_death_ration: 0,
  head_shots: 0,
  head_shots_percentage: 0,
  team_kill_friendly_fire: 0,
  entry_kill: 0,
  bomb_planted: 0,
  bomb_defused: 0,
  most_valuable_player: 0,
  score: 0,
  half_life_television_rating: 0,
  five_kills: 0,
  four_kills: 0,
  three_kills: 0,
  two_kills: 0,
  one_kill: 0,
  trade_kill: 0,
  trade_death: 0,
  kill_per_round: 0,
  assists_per_round: 0,
  death_per_round: 0,
  average_damage_per_round: 0,
  total_damage_health: 0,
  total_damage_armor: 0,
  one_versus_one: 0,
  one_versus_two: 0,
  one_versus_three: 0,
  one_versus_four: 0,
  one_versus_five: 0,
  grenades_thrown_count: 0,
  flashes_thrown_count: 0,
  smokes_thrown_count: 0,
  fire_thrown_count: 0,
  high_explosive_damage: 0,
  fire_damage: 0,
  match_played: 0,
  side: "",
};

const XS = 12;
const SM = 6;
const MD = 6;
const LG = 6;
const XL = 6;

const TeamScorePerMap = () => {
  const { t } = useTranslation();

  const [map, setMap] = useState<string>("");
  const [matchesToConsider, setMatchesToConsider] = useState<string>("0");
  const [chartData, setChartData] = useState<ChartData<"bar", number[], string>>(DATA);

  const { mapsPlayed, matches, users } = useTeamScorePerMap({ map, matchesToConsider });
  const steamUsers = useRef(users);

  useEffect(() => {
    const dates = Object.keys(matches);
    if (dates.length < 1) {
      return;
    }

    dates.sort((a, b) => a.localeCompare(b));

    // Save all the steam IDs
    const steamIdsSet = new Set<string>();
    dates.forEach((d) => {
      matches[d].sort((p1, p2) => p1.user_name.toLocaleLowerCase().localeCompare(p2.user_name.toLocaleLowerCase()));
      matches[d].forEach((player) => steamIdsSet.add(player.steam_id));
    });

    // If a player did not play on a specific date, add an empty data record
    steamIdsSet.forEach((steamId) => {
      dates.forEach((d) => {
        const player = matches[d].find((player) => player.steam_id === steamId);
        if (!player) {
          matches[d].push({ ...Object.assign({}, PLACE_HOLDER_USER), ...{ steam_id: steamId } });
        }
      });
    });

    const CTs: ChartDataset<"bar", number[]>[] = [];
    const Ts: ChartDataset<"bar", number[]>[] = [];

    const colors = Array.from(steamIdsSet).map((id, index) => COLOR_PALETTE[index % COLOR_PALETTE.length]);

    // Prepare the dataset
    Array.from(steamIdsSet).forEach((steamId, index) => {
      const ctScores: number[] = [];
      const tScores: number[] = [];

      // Add the score to the 2 data set. Real score if you in the right team, otherwise 0 as placeholder
      dates.forEach((d) => {
        const p = matches[d].find((player) => player.steam_id === steamId) as IUserGotvScore;
        ctScores.push(p.side === "CT" ? p.half_life_television_rating : 0);
        tScores.push(p.side === "T" ? p.half_life_television_rating : 0);
      });

      const playerName = steamUsers.current.find((p) => p.steam_id === steamId)?.user_name as string;

      CTs.push({
        label: playerName,
        data: ctScores,
        backgroundColor: generateRgbaString(colors[index], 1),
        borderColor: generateRgbaString(colors[index], 1),
        borderWidth: 0,
        stack: "CT",
      });
      Ts.push({
        label: playerName,
        data: tScores,
        backgroundColor: generateRgbaString(colors[index], 1),
        borderColor: generateRgbaString(colors[index], 1),
        borderWidth: 0,
        stack: "T",
      });
    });

    const ds = [...CTs, ...Ts];

    const data: ChartData<"bar", number[], string> = {
      labels: dates,
      datasets: ds,
    };
    setChartData(data);
  }, [matches]);

  return (
    <>
      <Typography align="center">{t("page.charts.teamScorePerMap.title")}</Typography>
      <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoSelect
            label={t("page.charts.teamScorePerMap.lblDpMap") as string}
            possibleValues={mapsPlayed.map((m) => ({ value: m.map_name, label: m.map_name }))}
            selectedValue={map}
            onChange={setMap}
          />
        </Grid>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoText
            label={t("page.charts.teamScorePerMap.lblMatchCount") as string}
            type={IxigoTextType.number}
            value={matchesToConsider}
            onChange={setMatchesToConsider}
            helperText={t("page.charts.teamScorePerMap.txtHelpMatchCount") as string}
          />
        </Grid>
        <Grid item xs={XS}>
          <Box sx={{ width: "100%", height: "100%" }}>
            <Bar data={chartData} options={OPTIONS} />
          </Box>
        </Grid>
      </Grid>
    </>
  );
};

export default TeamScorePerMap;
