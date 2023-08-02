import React from "react";
import { Radar } from "react-chartjs-2";
import * as ss from "simple-statistics";
import { TooltipItem } from "chart.js";
import { IPlayerRadarChartProps } from "./interfaces";
import { usePlayerRadarChart } from "./usePlayerRadardChart";

const PlayerRadarChart: React.FC<IPlayerRadarChartProps> = ({ steamid }) => {
  const queryHook = usePlayerRadarChart({ steamid });

  const originalValues = [
    player.kpr,
    player.hltv_rating,
    player.adr,
    player.kast,
    player.dpr,
    player.headshot_percentage,
    player.ud,
    player.ebt,
  ];
  const descriptions = [
    "Kills per round",
    "HLTV rating",
    "Average Damage per Round",
    "Kill/Assist/Survive/TD%",
    "Deaths Per Round (lower values give higher z-score)",
    "Headshot kill %",
    "Utility Damage",
    "Average Enemy blind time",
  ];

  // Use z-scores in the dataset
  const data = {
    labels: ["KPR", "HLTV", "ADR", "KAST", "DPR", "HSP", "UD", "EBT"],
    datasets: [
      {
        label: "Player Stats",
        data: [kprZScore, hltv_ratingZScore, adrZScore, kastZScore, dprZScore, hspZScore, udZScore, ebtZScore],
        backgroundColor: "rgba(75,192,192,0.2)",
        borderColor: "rgba(75,192,192,1)",
        pointBackgroundColor: "rgba(75,192,192,1)",
        pointBorderColor: "#fff",
        pointHoverBackgroundColor: "#fff",
        pointHoverBorderColor: "rgba(75,192,192,1)",
      },
    ],
  };

  const options = {
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        callbacks: {
          label: function (tooltipItem: TooltipItem<"radar">) {
            // Use the original value directly
            const rawValue = originalValues[tooltipItem.dataIndex!];
            const desc = descriptions[tooltipItem.dataIndex!];
            return `${desc}: ${rawValue.toFixed(2)}`;
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
  return <Radar data={data} options={options} />;
};

export default PlayerRadarChart;
