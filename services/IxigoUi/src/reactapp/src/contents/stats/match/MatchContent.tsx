import { useTranslation } from "react-i18next";
import { useMatchContent } from "./useMatchContent";
import IxigoTable from "../../../common/material-table/IxigoTable";

const STRING_PREFIX = "page.stats.match";

const MatchContent = () => {
  const { t } = useTranslation();
  const { columns, data, state, refetch } = useMatchContent();

  return (
    <IxigoTable
      title={t(`${STRING_PREFIX}.title`) as string}
      columns={columns}
      data={data}
      sorting={[{ id: "match_date", desc: true }]}
      state={state}
      refetch={refetch}
      enableGrouping={true}
      enableColumnFilters={true}
      enableFilterMatchHighlighting={false}
      errorMsg={t(`${STRING_PREFIX}.error-loading-data`) as string}
      refreshMsg={t(`${STRING_PREFIX}.refresh-data`) as string}
    />
  );
};

export default MatchContent;
