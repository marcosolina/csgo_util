import React, { useContext } from "react";
import { useMemo } from "react";
import { Tooltip, Box } from "@mui/material";
import { MaterialReactTable } from "material-react-table";
import { Link } from "react-router-dom";
import { QueryStatus } from "../../../../lib/http-requests";
import PieChartMini from "../../../../common/pie-chart-mini/PieChartMini";
import { useTranslation } from "react-i18next";
import { useMatchTeamTable } from "./useMatchTeamTable";
import { ITeamMatchContentRequest } from "./interfaces";

const MatchTeamTable: React.FC<ITeamMatchContentRequest> = ({ match_id, team }) => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = useMatchTeamTable({ match_id, team});

  return (
    <MaterialReactTable
      columns={columns}
      data={data ?? []}
      initialState={{
        showColumnFilters: false,
        columnVisibility: {
          steamid: false,
          last_round_team: false,
          rounds_on_team1: false,
          rounds_on_team2: false,
          kpr: false,
          dpr: false,
          kdr: false,
          ek: false,
          tk: false,
          _1v1: false,
          _1v2: false,
          _1v3: false,
          _1v4: false,
          _1v5: false,
          _1k: false,
          _2k: false,
          _3k: false,
          _4k: false,
          _5k: false,
          hr: false,
          bp: false,
          ffd: false,
          td: false,
          tda: false,
          tdh: false,
          fbt: false,
          fa: false,
          headshots: false,
        },
        density: "compact",
        sorting: [{ id: "hltv_rating", desc: true }], //sort by state by default
        columnPinning: { left: ["usernames"] },
      }}
      enableColumnActions={false}
      enableColumnFilters={false}
      enableDensityToggle={false}
      enableSorting={true}
      enableTopToolbar={true}
      enableBottomToolbar={false}
      enableGlobalFilter={false}
      enableFullScreenToggle={false}
      enableHiding={true}
      muiTableBodyRowProps={{ hover: false }}
      rowCount={data?.length ?? 0}
      state={{
        isLoading: state === QueryStatus.loading,
        showAlertBanner: state === QueryStatus.error,
        showProgressBars: state === QueryStatus.loading,
      }}
    />
  );
};

export default MatchTeamTable;
