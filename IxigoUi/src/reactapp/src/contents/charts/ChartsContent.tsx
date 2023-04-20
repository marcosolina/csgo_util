import { Bar } from "react-chartjs-2";
import { Box } from "@mui/material";
import { UI_CONTEXT_PATH } from "../../lib/constants";
import wkp from "../../assets/pictures/work-in-progress.png";
import { ChartData, ChartOptions, registerables, Chart, ChartDataset } from "chart.js";
import { useGetMapsPlayedCount } from "../../services/charts";
import { useEffect, useState } from "react";
import { QueryStatus } from "../../lib/http-requests";

Chart.register(...registerables);

const DATA: ChartData<"bar", (number | [number, number] | null)[], unknown> = {
  labels: ["x1", "x2"],
  datasets: [
    {
      label: "P1",
      data: [1, 2],
    },
    {
      label: "P2",
      data: [4, 5],
    },
  ],
};

export const options: ChartOptions<"bar"> = {
  responsive: true,
  plugins: {
    legend: {
      position: "top" as const,
    },
    title: {
      display: true,
      text: "Chart.js Bar Chart",
    },
  },
};

const ChartsContent = () => {
  const [data, setData] = useState<ChartData<"bar", (number | [number, number] | null)[], unknown>>(DATA);

  const qMapsPlayedCount = useGetMapsPlayedCount();

  useEffect(() => {
    if (qMapsPlayedCount.status === QueryStatus.success) {
      const newData = { ...data };
      const count = qMapsPlayedCount.data.data?.maps.slice(0, 3) || [];
      console.log(count);
      newData.labels = count.map((m) => m.map_name);
      newData.datasets =
        count.map(
          (m): ChartDataset<"bar", (number | [number, number] | null)[]> => ({ label: m.map_name, data: [m.count] })
        ) || [];
      setData(newData);
    }
  }, [qMapsPlayedCount]);

  return (
    <Box sx={{ width: "100%" }}>
      <Bar data={data} options={options} />
    </Box>
  );
};

export default ChartsContent;
