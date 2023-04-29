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
import { useTeamsAvgScoresPerMapData } from "./useTeamsAvgScoresPerMapData";
import IxigoSelect from "../../../common/select/IxigoSelect";
import IxigoSelectMultiple from "../../../common/select/IxigoSelectMultiple";
import IxigoText from "../../../common/input/IxigoText";
import { IxigoTextType } from "../../../common";

Chart.register(...registerables);

const DATA: ChartData<"radar", number[], string> = {
  labels: [], // Map name
  datasets: [
    {
      label: "", //Team name
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

const XS = 12;
const SM = 6;
const MD = 4;
const LG = 3;
const XL = 3;

const TeamsAvgScorePerMap = () => {
  const { t } = useTranslation();

  const [scoresForMaps, setScoresForMaps] = useState<string[]>([]);
  const [scoreType, setScoreType] = useState<string>("HLTV");
  const [matchesToConsider, setMatchesToConsider] = useState<string>("0");
  const [data, setData] = useState<ChartData<"radar", number[], string>>(DATA);

  const { status, avgScores, scoreTypes, mapsPlayed } = useTeamsAvgScoresPerMapData({
    scoreType,
    maps: scoresForMaps,
    matchesToConsider,
  });

  useEffect(() => {
    if (status === QueryStatus.success && !!avgScores) {
      const scores = avgScores.scores;
      const mapsWithDuplicates = Object.keys(scores)
        .map((k) => scores[k].map((list) => list.map_name))
        .flat();
      const maps = Array.from(new Set(mapsWithDuplicates)).sort((a, b) => a.localeCompare(b));

      const newData: ChartData<"radar", number[], string> = {
        labels: maps,
        datasets: [
          {
            label: scores[maps[0]][0].team, //Team name
            data: maps.map((m) => scores[m][0].avg_score), // avg
            backgroundColor: generateRgbaString(COLOR_PALETTE[0], 0.2),
            borderColor: generateRgbaString(COLOR_PALETTE[0], 1),
            borderWidth: 1,
          },
          {
            label: scores[maps[0]][1].team, //Team name
            data: maps.map((m) => scores[m][1].avg_score), // avg
            backgroundColor: generateRgbaString(COLOR_PALETTE[1], 0.2),
            borderColor: generateRgbaString(COLOR_PALETTE[1], 1),
            borderWidth: 1,
          },
        ],
      };
      setData(newData);
    }
  }, [status, avgScores]);

  return (
    <>
      <Typography align="center">{t("page.charts.avgTeamsScorePerMap.title")}</Typography>
      <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoSelect
            label={t("page.charts.avgTeamsScorePerMap.lblScoreTypes") as string}
            possibleValues={scoreTypes}
            selectedValue={scoreType}
            onChange={(v) => setScoreType(v)}
          />
        </Grid>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoSelectMultiple
            label={t("page.charts.avgTeamsScorePerMap.lblDpMaps") as string}
            selectedValues={scoresForMaps}
            possibleValues={mapsPlayed.map((m) => ({ value: m.map_name, label: m.map_name }))}
            onChange={setScoresForMaps}
          />
        </Grid>
        <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <IxigoText
            label={t("page.charts.avgTeamsScorePerMap.lblMatchCount") as string}
            type={IxigoTextType.number}
            value={matchesToConsider}
            onChange={setMatchesToConsider}
            helperText={t("page.charts.avgTeamsScorePerMap.txtHelpMatchCount") as string}
          />
        </Grid>
      </Grid>
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

export default TeamsAvgScorePerMap;
