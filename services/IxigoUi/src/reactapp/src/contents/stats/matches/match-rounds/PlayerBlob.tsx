import React from 'react';
import { PieChart } from 'react-minimal-pie-chart'; // Import PieChart as needed

const PlayerBlob: React.FC<{ isAlive: boolean; team: "team1" | "team2" }> = ({ isAlive, team }) => {
    const data = [
        { title: "Survived", value: isAlive ? 100 : 0, color: team === "team1" ? "#90caf9" : "orange" },
        { title: "Died", value: isAlive ? 0 : 100, color: "dimgrey" },
      ];
  
      return (
        <div style={{ width: 20, height: 20, display: "flex", alignItems: "center", justifyContent: "flex-end" }}>
          <PieChart
            data={data}
            lineWidth={isAlive ? 30 : 10} // This makes it a donut chart
            totalValue={100}
            startAngle={-90}
            lengthAngle={360}
            animate
          />
        </div>
      );
};

export default PlayerBlob;
