import { Bar } from "react-chartjs-2";
import { Box, Skeleton } from "@mui/material";
import { ChartData, ChartOptions, registerables, Chart, ChartDataset } from "chart.js";
import { useGetMapsPlayedCount } from "../../services/charts";
import { useEffect, useState } from "react";
import { QueryStatus } from "../../lib/http-requests";
import Switch from "../../common/switch-case/Switch";
import Case from "../../common/switch-case/Case";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";

Chart.register(...registerables);

const DATA: ChartData<"bar", (number | [number, number] | null)[], unknown> = {
  labels: [],
  datasets: [],
};

const OPTIONS: ChartOptions<"bar"> = {
  responsive: true,
  maintainAspectRatio: false,

  indexAxis: "y" as const,
  plugins: {
    legend: {
      position: "right" as const,
      display: false,
    },
    title: {
      display: true,
      text: "Number of times a map was played",
    },
  },
  scales: {
    x: {
      position: "top",
      grid: {
        display: true,
        color: "rgba(255,255,255,0.3)",
      },
    },
    y: {
      grid: {
        display: true,
        color: "rgba(255,255,255,0.3)",
      },
    },
  },
};

const MapsPlayedCounter = () => {
  const [data, setData] = useState<ChartData<"bar", (number | [number, number] | null)[], unknown>>(DATA);
  const [chartHeight, setChartHeight] = useState<number>(0);

  const { status, data: ixigoResp } = useGetMapsPlayedCount();

  useEffect(() => {
    if (status === QueryStatus.success) {
      const newData = { ...data };
      const count = ixigoResp.data?.maps || [];

      newData.labels = count.map((m) => m.map_name);
      newData.datasets = [
        {
          data: count.map((m) => m.count),
        },
      ];
      setData(newData);
      setChartHeight(count.length * 20);
    }
  }, [status, ixigoResp]);

  return (
    <Switch value={status}>
      <Case case={QueryStatus.success}>
        <Box sx={{ width: "100%", height: `${chartHeight}px` }}>
          <Bar data={data} options={OPTIONS} />
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
  );
};

export default MapsPlayedCounter;
