import { IAvgScorePerMap, useGetAvgScoresPerMap, useGetCsgoPlayers } from "../../../services";
import { ChartData, ChartOptions, registerables, Chart } from "chart.js";
import { Radar } from "react-chartjs-2";
import { QueryStatus } from "../../../lib/http-requests";
import Switch from "../../../common/switch-case/Switch";
import Case from "../../../common/switch-case/Case";
import { Box, Grid, Skeleton, Typography } from "@mui/material";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import { useTranslation } from "react-i18next";
import { useEffect, useState } from "react";
import { COLOR_PALETTE, DEFAULT_SPACING, generateRgbaString } from "../../../lib/constants";
import { combineQueryStatuses } from "../../../lib/queries";
import { useAvgScoresPerMapData } from "./useAvgScoresPerMapData";
import IxigoSwitch from "../../../common/switch/IxigoSwitch";
import IxigoSelect from "../../../common/select/IxigoSelect";

Chart.register(...registerables);

const fillMissingMaps = (allMaps: string[], avgScores: IAvgScorePerMap[], steamId: string): IAvgScorePerMap[] => {
  const filledArray = allMaps.map((mapName): IAvgScorePerMap => {
    const avgScore = avgScores.find((avg) => avg.map_name === mapName);
    if (!avgScore) {
      return {
        avg_score: 0,
        map_name: mapName,
        steam_id: steamId,
      };
    }
    return avgScore;
  });
  return filledArray;
};

const DATA: ChartData<"radar", number[], string> = {
  labels: [], // Map name
  datasets: [
    {
      label: "", //Steam ID
      data: [], // avg
      backgroundColor: "rgba(255, 99, 132, 0.2)",
      borderColor: "rgba(255, 99, 132, 1)",
      borderWidth: 1,
    },
  ],
};

const OPTIONS: ChartOptions<"radar"> = {
  plugins: {
    tooltip: {
      mode: "index",
    },
  },
  scales: {
    r: {
      angleLines: {
        display: true,
        color: "rgba(255,255,255,0.3)",
      },
      grid: {
        display: true,
        color: "rgba(255,255,255,0.3)",
      },
      pointLabels: {
        color: "#FFFFFF",
      },
    },
  },
};

const AvgScorePerMap = () => {
  const { t } = useTranslation();

  const [steamIds, setSteamIds] = useState<string[]>([]);
  const [data, setData] = useState<ChartData<"radar", number[], string>>(DATA);
  const [scoreType, setScoreType] = useState<string>("HLTV");

  const { status, users, avgScores, scoreTypes } = useAvgScoresPerMapData({ steamIds, scoreType });

  const onSelectedPlayer = (stramId: string, addUser: boolean) => {
    const players = [...steamIds];

    const index = players.indexOf(stramId);
    if (addUser && index < 0) {
      players.push(stramId);
    }

    if (index >= 0) {
      players.splice(index, 1);
    }

    setSteamIds(players);
  };

  useEffect(() => {
    if (status === QueryStatus.success && !!avgScores && users) {
      const scores = avgScores.scores;
      const mapsWithDuplicates = Object.keys(scores)
        .map((k) => scores[k].map((list) => list.map_name))
        .flat();
      const maps = Array.from(new Set(mapsWithDuplicates)).sort((a, b) => a.localeCompare(b));

      const colors = steamIds.map((id, index) => COLOR_PALETTE[index % COLOR_PALETTE.length]);
      const usersDef = users;

      const newData: ChartData<"radar", number[], string> = {
        labels: maps,
        datasets: Object.keys(scores).map((steamId, index) => {
          const avgs = fillMissingMaps(maps, scores[steamId], steamId);
          avgs.sort((a, b) => a.map_name.localeCompare(b.map_name));
          console.log(avgs);
          return {
            label: usersDef.find((u) => u.steam_id === steamId)?.user_name, //Steam ID
            data: avgs.map((avg) => avg.avg_score), // avg
            backgroundColor: generateRgbaString(colors[index], 0.2),
            borderColor: generateRgbaString(colors[index], 1),
            borderWidth: 1,
          };
        }),
      };
      setData(newData);
    }
  }, [status]);

  return (
    <>
      <Typography align="center">{t("page.charts.avgScorePerMap.title")}</Typography>
      <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
        {users.map((player) => (
          <Grid item xs={6} sm={4} md={3} lg={3} xl={3} key={player.steam_id}>
            <IxigoSwitch
              label={player.user_name}
              value={player.steam_id}
              checked={steamIds.includes(player.steam_id)}
              onChange={onSelectedPlayer}
            />
          </Grid>
        ))}
      </Grid>
      <IxigoSelect
        label={t("page.charts.avgScorePerMap.lblScoreTypes") as string}
        possibleValues={scoreTypes}
        selectedValue={scoreType}
        onChange={(v) => setScoreType(v)}
      />
      <Switch value={status}>
        <Case case={QueryStatus.success}>
          <Box sx={{ width: "100%", height: "100%" }}>
            <Radar data={data} options={OPTIONS} />
          </Box>
        </Case>
        <Case case={QueryStatus.loading}>
          <Skeleton animation="wave" height={200} />
        </Case>
        <Case case={QueryStatus.error}>
          <Box textAlign={"center"}>
            <ErrorOutlineIcon color="error" fontSize="large" />
          </Box>
        </Case>
      </Switch>
    </>
  );
};

export default AvgScorePerMap;
