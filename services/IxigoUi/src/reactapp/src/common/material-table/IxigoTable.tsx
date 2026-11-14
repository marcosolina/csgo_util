import { Box, IconButton, Tooltip, Typography } from "@mui/material";
import { IxigoTableProps } from ".";
import RefreshIcon from "@mui/icons-material/Refresh";
import MaterialReactTable from "material-react-table";
import { QueryStatus } from "../../lib/http-requests";
import { useTranslation } from "react-i18next";

const IxigoTable = <T extends object>(props: IxigoTableProps<T>) => {
  const { t } = useTranslation();
  const {
    columns,
    data,
    state,
    refetch,
    title,
    errorMsg,
    refreshMsg,
    sorting,
    columnPinning,
    enableGrouping,
    enableColumnFilters,
    enableFilterMatchHighlighting,
    columnVisibility,

    enableColumnActions,
    enablePagination,
    enableSorting,
    enableBottomToolbar,
    enableTopToolbar,
    enableGlobalFilter,
    enableFullScreenToggle,
    enableHiding,
    enableTableHead,
    enableColumnOrdering,
    muiTableBodyRowProps,
    pagination,
    renderTopToolbarCustomActions,
    renderDetailPanel,
    grouping,
    expanded,
  } = props;

  let bodyProps;
  if (muiTableBodyRowProps) {
    bodyProps = { hover: false };
  }

  const customAction = !!refetch
    ? () => (
        <Tooltip arrow title={refreshMsg || t(`generic-info.reload`)}>
          <IconButton onClick={refetch}>
            <RefreshIcon />
          </IconButton>
        </Tooltip>
      )
    : undefined;

  return (
    <>
      {title && (
        <Box textAlign="center">
          <Typography variant="h5">{title}</Typography>
        </Box>
      )}
      <MaterialReactTable
        columns={columns}
        data={data}
        initialState={{
          showColumnFilters: false,
          columnVisibility,
          density: "compact",
          expanded: expanded || {},
          pagination: pagination || { pageIndex: 0, pageSize: 10 },
          sorting: sorting,
          columnPinning: columnPinning || { left: [], right: [] },
          grouping: grouping || [],
        }}
        enableGrouping={enableGrouping}
        enableColumnFilters={enableColumnFilters}
        enableFilterMatchHighlighting={enableFilterMatchHighlighting}
        enableColumnFilterModes
        enableDensityToggle={false}
        enablePinning={enableHiding !== false}
        enableMultiSort
        enableColumnDragging={false}
        enableColumnOrdering={enableColumnOrdering}
        enableFullScreenToggle={enableFullScreenToggle}
        enablePagination={enablePagination}
        enableHiding={enableHiding}
        enableTableHead={enableTableHead}
        renderDetailPanel={renderDetailPanel}
        enableColumnActions={enableColumnActions}
        enableSorting={enableSorting}
        enableBottomToolbar={enableBottomToolbar}
        enableTopToolbar={enableTopToolbar}
        muiTableBodyRowProps={bodyProps}
        enableGlobalFilter={enableGlobalFilter !== undefined ? enableGlobalFilter : true}
        muiToolbarAlertBannerProps={
          state === QueryStatus.error
            ? {
                color: "error",
                children: errorMsg || t(`error.generic.message`),
              }
            : undefined
        }
        renderTopToolbarCustomActions={renderTopToolbarCustomActions || customAction}
        rowCount={data?.length ?? 0}
        state={{
          isLoading: state === QueryStatus.loading,
          showAlertBanner: state === QueryStatus.error,
          showProgressBars: state === QueryStatus.loading,
        }}
      />
    </>
  );
};

export default IxigoTable;
