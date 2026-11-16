import React from 'react';
import { Tooltip } from "@mui/material";

interface FinanceCellProps {
  cell: any;
  row: any;
  team: "team1" | "team2";
  maxValues: any; // define this with the proper type
}

const FinanceCell: React.FC<FinanceCellProps> = ({ cell, row, team, maxValues }) => {
    if (!maxValues) {
        return null; // or some placeholder
      }
      const teamData = cell.row.original[team];
      if (!teamData) {
        return null; // or some placeholder
      }

      const equipmentValuePercentage = (teamData.total_equipment_value / maxValues) * 100;
      const moneySpentPercentage = (teamData.total_money_spent / maxValues) * 100;

      return (
        <div style={{ direction: team === "team1" ? "rtl" : "ltr" }}>
          <Tooltip title={`Equipment Value: $${teamData.total_equipment_value}`}>
            <div
              style={{
                backgroundColor: "rgb(75, 192, 192)",
                borderRadius: "10px",
                width: `${equipmentValuePercentage}%`,
                height: "10px",
              }}
            />
          </Tooltip>
          <Tooltip title={`Cash Spent: $${teamData.total_money_spent}`}>
            <div
              style={{
                backgroundColor: "orange",
                borderRadius: "10px",
                width: `${moneySpentPercentage}%`,
                height: "10px",
              }}
            />
          </Tooltip>
        </div>
      );
};

export default FinanceCell;
