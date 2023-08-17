import { ChartData, ChartOptions } from "chart.js";
import { usePlayerGraphContentProvider } from "./usePlayersGraphContentProvider";
import { BinningLevel } from "./interfaces";
import { Line } from "react-chartjs-2";

const DATA: ChartData<"line", number[], string> = {
  labels: [], // Map name
  datasets: [
    {
      label: "",
      data: [],
      fill: false,
      borderColor: "rgb(75, 192, 192)",
      tension: 0.1,
    },
    // This is your trendline
    {
      label: "Trendline",
      data: [],
      fill: false,
      pointRadius: 0,
      borderColor: "rgba(255, 255, 255, 0.5)",
      borderDash: [5, 5],
      tension: 0.1,
    },
  ],
};

const OPTIONS: ChartOptions<"line"> = {
  maintainAspectRatio: false,
  scales: {
    x: {
      type: "time",
      time: {
        unit: BinningLevel.week,
      },
      display: false,
      ticks: {
        autoSkip: false,
        maxRotation: 90,
        minRotation: 90,
      },
    },
  },
};

const PlayersGraphStatsChart = () => {
  const contentProvider = usePlayerGraphContentProvider();
  console.log(contentProvider);

  return null;
  return <Line data={DATA} options={OPTIONS} />;
};

export default PlayersGraphStatsChart;
