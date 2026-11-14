import React from "react";
import { MRT_ColumnDef } from "material-react-table";
import { useMatchTeamTable } from "./useMatchTeamTable";
import { ITeamMatchContentRequest } from "./interfaces";
import IxigoTable from "../../../../common/material-table/IxigoTable";

const MatchTeamTable: React.FC<ITeamMatchContentRequest> = ({ match_id, team }) => {
  const { columns, data, state } = useMatchTeamTable({ match_id, team });

  return (
    <IxigoTable
      state={state}
      columns={columns as MRT_ColumnDef<object>[]}
      data={data}
      enableColumnActions={false}
      enableColumnFilters={false}
      enableSorting={true}
      enableTopToolbar={true}
      enableBottomToolbar={false}
      enableGlobalFilter={false}
      enableFullScreenToggle={false}
      enableHiding={true}
      muiTableBodyRowProps={true}
      sorting={[{ id: "hltv_rating", desc: true }]}
      columnPinning={{ left: ["usernames"] }}
      columnVisibility={{
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
      }}
    />
  );
};

export default MatchTeamTable;
