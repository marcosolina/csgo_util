import React from "react";
import { IMapMatchRequest } from "./interfaces";
import { useTranslation } from "react-i18next";
import { useMapMatchesContent } from "./useMapMatchesContent";
import IxigoTable from "../../../../common/material-table/IxigoTable";

const STRING_PREFIX = "page.stats.maps.matches";
const MapMatchesContent: React.FC<IMapMatchRequest> = ({ mapName }) => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = useMapMatchesContent({ mapName });

  return (
    <IxigoTable
      state={state}
      columns={columns}
      data={data}
      sorting={[{ id: "match_date", desc: true }]}
      enableGrouping={true}
      enableColumnFilters={true}
      enableFilterMatchHighlighting={false}
      enablePagination={true}
      errorMsg={t(`${STRING_PREFIX}.error-loading-data`) as string}
      refreshMsg={t(`${STRING_PREFIX}.refresh`) as string}
      refetch={refetch}
    />
  );
};

export default MapMatchesContent;
