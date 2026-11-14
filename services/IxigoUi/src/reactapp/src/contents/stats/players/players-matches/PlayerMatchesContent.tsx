import { useTranslation } from "react-i18next";
import { IPlayersMatchesContentProps } from "./interfaces";
import { usePlayerMatches } from "./usePlayersMatches";
import IxigoTable from "../../../../common/material-table/IxigoTable";

const BASE_TRANSLATION_KEY = "page.stats.player.content.matches";
const PlayerMatchesContent: React.FC<IPlayersMatchesContentProps> = ({ steamId }) => {
  const { t } = useTranslation();
  const { state, columns, data, refetch } = usePlayerMatches({ steamId });
  return (
    <IxigoTable
      columns={columns}
      data={data}
      refetch={refetch}
      state={state}
      sorting={[{ id: "match_date", desc: true }]}
      columnPinning={{ left: ["match_date"] }}
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
        hr: false,
        bp: false,
        ud: false,
        ffd: false,
        td: false,
        tda: false,
        tdh: false,
        fbt: false,
        fa: false,
        ebt: false,
      }}
      enableColumnActions={false}
      enableColumnFilters
      enableSorting={true}
      enableFilterMatchHighlighting={false}
      enableTopToolbar={true}
      enableBottomToolbar
      enableGlobalFilter={false}
      enableFullScreenToggle={false}
      enableHiding={true}
      muiTableBodyRowProps={true}
      errorMsg={t(`${BASE_TRANSLATION_KEY}.error-loading-data`) as string}
    />
  );
};

export default PlayerMatchesContent;
