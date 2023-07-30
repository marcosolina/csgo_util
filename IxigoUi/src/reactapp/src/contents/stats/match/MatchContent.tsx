import { IconButton, Tooltip, Box, Typography } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import { MaterialReactTable } from "material-react-table";
import { useTranslation } from "react-i18next";
import { useMatchContent } from "./useMatchContent";
import { QueryStatus } from "../../../lib/http-requests";
import TableLoading from "../../../common/loading/table-loading/TableLoading";
import Switch from "../../../common/switch-case/Switch";
import Case from "../../../common/switch-case/Case";

const STRING_PREFIX = "page.stats.match";

const MatchContent = () => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = useMatchContent();

  return (
    <Switch value={state}>
      <Case case={QueryStatus.loading}>
        <TableLoading />
      </Case>
      <Case case={QueryStatus.success}>
        <Box textAlign="center">
          <Typography variant="h5">{t(`${STRING_PREFIX}.title`)}</Typography>
        </Box>
        <MaterialReactTable
          columns={columns}
          data={data ?? []} //data is undefined on first render  ?.data ?? []
          initialState={{
            showColumnFilters: false,
            density: "compact",
            pagination: { pageIndex: 0, pageSize: 10 },
            //grouping: ['mapname'],
            sorting: [{ id: "match_date", desc: true }], //sort by state by default
          }}
          enableGrouping
          enablePagination
          enableDensityToggle={false}
          muiToolbarAlertBannerProps={
            state === QueryStatus.error
              ? {
                  color: "error",
                  children: "Error loading data",
                }
              : undefined
          }
          renderTopToolbarCustomActions={() => (
            <Tooltip arrow title="Refresh Data">
              <IconButton onClick={() => refetch()}>
                <RefreshIcon />
              </IconButton>
            </Tooltip>
          )}
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

export default MatchContent;
