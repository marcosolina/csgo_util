import { ChartData, ChartOptions } from "chart.js";
import { usePlayerGraphContentProvider } from "./usePlayersGraphContentProvider";
import { BinningLevel } from "./interfaces";
import { Line } from "react-chartjs-2";
import { QueryStatus } from "../../../../lib/http-requests";
import Switch from "../../../../common/switch-case/Switch";
import Case from "../../../../common/switch-case/Case";
import { Grid } from "@mui/material";

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

const XS = 12;

const PlayersGraphStatsChart = () => {
  const contentProvider = usePlayerGraphContentProvider();

  return (
    <Switch value={contentProvider.state}>
      <Case case={QueryStatus.success}>
        <Grid container spacing={3}>
          {contentProvider.chartsData.map((chartData, index) => {
            const data: ChartData<"line", number[], string> = {
              labels: chartData.labels,
              datasets: chartData.datasets,
            };

            const isLastChart = index + 1 === contentProvider.chartsData.length;

            // Dirty but I need a deep copy
            const options: ChartOptions<"line"> = JSON.parse(JSON.stringify(OPTIONS));

            options!.scales!.x!.display = isLastChart;

            return (
              <Grid key={index} item xs={XS} height={isLastChart ? 260 : 200}>
                <Line data={data} options={options} />
              </Grid>
            );
          })}
        </Grid>
      </Case>
      <Case case={QueryStatus.loading}>loading</Case>
    </Switch>
  );
};

export default PlayersGraphStatsChart;
