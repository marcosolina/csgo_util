import { IPieChartProps } from "./interfaces";
import { PieChart } from "react-minimal-pie-chart";

const PieChartMini: React.FC<IPieChartProps> = ({ percentage, color, size }) => {
  percentage = percentage > 100 ? 100 : percentage;
  const data = [
    { title: percentage + "%", value: percentage, color: color },
    { title: "", value: 100 - percentage, color: "dimgrey" },
  ];

  return (
    <div style={{ width: size, height: size, display: "flex", alignItems: "center", justifyContent: "center" }}>
      <PieChart
        data={data}
        lineWidth={100} // This makes it a donut chart
        totalValue={100}
        startAngle={-90}
        lengthAngle={360}
        animate
      />
    </div>
  );
};

export default PieChartMini;
