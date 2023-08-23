import { useTranslation } from "react-i18next";
import { IPlayersMatchesContentProps } from "./interfaces";
import { usePlayerMatches } from "./usePlayersMatches";
import Switch from "../../../../common/switch-case/Switch";
import Case from "../../../../common/switch-case/Case";
import { QueryStatus } from "../../../../lib/http-requests";
import TableLoading from "../../../../common/loading/table-loading/TableLoading";
import MaterialReactTable from "material-react-table";

const BASE_TRANSLATION_KEY = "page.stats.player.content.matches";
const PlayerMatchesContent: React.FC<IPlayersMatchesContentProps> = ({ steamId }) => {
  const { t } = useTranslation();
  const { state, columns, data } = usePlayerMatches({ steamId });
  return (
    <Switch value={state}>
      <Case case={QueryStatus.loading}>
        <TableLoading />
      </Case>
      <Case case={QueryStatus.success}>
        <MaterialReactTable
          columns={columns}
          data={data}
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
            },
            density: "compact",
            sorting: [{ id: "match_date", desc: true }], //sort by state by default
            columnPinning: { left: ["match_date"] },
          }}
          enableColumnActions={false}
          enableColumnFilters
          enableColumnFilterModes
          enableSorting={true}
          enableFilterMatchHighlighting={false}
          enableTopToolbar={true}
          enableBottomToolbar
          enableDensityToggle={false}
          enableGlobalFilter={false}
          enableFullScreenToggle={false}
          enableHiding={true}
          muiTableBodyRowProps={{ hover: false }}
          muiToolbarAlertBannerProps={
            state === QueryStatus.error
              ? {
                  color: "error",
                  children: t(`${BASE_TRANSLATION_KEY}.error-loading-data`),
                }
              : undefined
          }
          rowCount={data?.length ?? 0}
          state={{
            isLoading: state === QueryStatus.loading,
            showAlertBanner: state === QueryStatus.error,
            showProgressBars: state === QueryStatus.loading,
          }}
        />
      </Case>
    </Switch>
  );
};

export default PlayerMatchesContent;
