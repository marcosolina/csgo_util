import React, { useEffect, useMemo, useState } from "react";
import { Radar } from "react-chartjs-2";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import { ChartData, ChartOptions, TooltipItem, registerables } from "chart.js";
import { IPlayerRadarChartProps } from "./interfaces";
import { usePlayerRadarChart } from "./usePlayerRadardChart";
import { QueryStatus } from "../../../../../lib/http-requests";
import Switch from "../../../../../common/switch-case/Switch";
import Case from "../../../../../common/switch-case/Case";
import { Paper, Skeleton } from "@mui/material";
import { useTranslation } from "react-i18next";
import { Chart } from "chart.js";

const LANG_BASE_PATH = "page.stats.player.content.overall.radar-chart";

const LABELS = ["KPR", "HLTV", "ADR", "KAST", "DPR", "HSP", "UD", "EBT"];

Chart.register(...registerables);

const DATA: ChartData<"radar", number[], string> = {
  labels: [],
  datasets: [
    {
      label: "",
      data: [], // avg
      backgroundColor: "rgba(75,192,192,0.2)",
      borderColor: "rgba(75,192,192,1)",
      pointBackgroundColor: "rgba(75,192,192,1)",
      pointBorderColor: "#fff",
      pointHoverBackgroundColor: "#fff",
      pointHoverBorderColor: "rgba(75,192,192,1)",
    },
  ],
};

const OPTIONS: ChartOptions<"radar"> = {
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      callbacks: {
        label: function (tooltipItem: TooltipItem<"radar">) {
          return `${tooltipItem.label}`;
        },
      },
    },
  },
  scales: {
    r: {
      grid: {
        display: true, // Show grid lines
        color: "dimgray", // Set grid lines color to dark grey
      },
      angleLines: {
        display: true,
        color: "dimgray",
      },
      min: -2,
      max: 2,
      ticks: {
        display: false,
      },
    },
  },
};

const PlayerRadarChart: React.FC<IPlayerRadarChartProps> = ({ steamid }) => {
  const { t } = useTranslation();
  const [data, setData] = useState<ChartData<"radar", number[], string>>(DATA);
  const [options, setOptions] = useState<ChartOptions<"radar">>();
  const { state, chartData, originalData } = usePlayerRadarChart({ steamid });

  const chartLabel = useMemo(() => {
    return t(`${LANG_BASE_PATH}.data-set-label"`);
  }, [t]);

  const descriptions = useMemo(() => {
    return [
      t(`${LANG_BASE_PATH}.descriptions.kpr`),
      t(`${LANG_BASE_PATH}.descriptions.hltv`),
      t(`${LANG_BASE_PATH}.descriptions.adr`),
      t(`${LANG_BASE_PATH}.descriptions.kast`),
      t(`${LANG_BASE_PATH}.descriptions.dpr`),
      t(`${LANG_BASE_PATH}.descriptions.hsp`),
      t(`${LANG_BASE_PATH}.descriptions.ud`),
      t(`${LANG_BASE_PATH}.descriptions.ebt`),
    ];
  }, [t]);

  useEffect(() => {
    if (state === QueryStatus.success && chartData && originalData) {
      const originalValues = Object.values(originalData);
      const chartDataValues = Object.values(chartData);

      const newData = { ...DATA };
      newData.labels = LABELS;
      newData.datasets[0].label = chartLabel;
      newData.datasets[0].data = chartDataValues;

      const newOptions = { ...OPTIONS };
      if (newOptions.plugins?.tooltip?.callbacks?.label) {
        newOptions.plugins.tooltip.callbacks.label = (tooltipItem: TooltipItem<"radar">) => {
          // Use the original value directly
          const rawValue = originalValues[tooltipItem.dataIndex!];
          const desc = descriptions[tooltipItem.dataIndex!];
          return `${desc}: ${rawValue.toFixed(2)}`;
        };
      }

      setData(newData);
      setOptions(newOptions);
    }
  }, [state, chartData, originalData, chartLabel, descriptions]);

  return (
    <Switch value={state}>
      <Case case={QueryStatus.success}>
        <Paper>{data.datasets[0].data.length > 0 && <Radar data={data} options={options} />}</Paper>
      </Case>
      <Case case={QueryStatus.loading}>
        <Skeleton animation="wave" style={{ height: "100%" }} />
      </Case>
      <Case case={QueryStatus.error}>
        <ErrorOutlineIcon color="error" fontSize="large" />
      </Case>
    </Switch>
  );
};

export default PlayerRadarChart;
