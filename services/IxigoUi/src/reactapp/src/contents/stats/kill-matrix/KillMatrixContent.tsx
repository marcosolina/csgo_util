import React from "react";
import { useTranslation } from "react-i18next";
import { useKillMatrixContent } from "./useKillMatrixContent";
import IxigoTable from "../../../common/material-table/IxigoTable";

const STRING_PREFIX = "page.stats.killmatrix";

const KillMatrixContent: React.FC = () => {
  const { t } = useTranslation();
  const { columns, flattenedData, state, refetch } = useKillMatrixContent();

  return (
    <IxigoTable
      title={t(`${STRING_PREFIX}.title`) as string}
      columns={columns}
      data={flattenedData}
      sorting={[{ id: "Team", desc: false }]}
      state={state}
      refetch={refetch}
      columnPinning={{ left: ["Killer/Victim"] }}
      errorMsg={t(`${STRING_PREFIX}.error-loading-data`) as string}
      refreshMsg={t(`${STRING_PREFIX}.refresh-data`) as string}
      columnVisibility={{ Team: false }}
    />
  );
};

export default KillMatrixContent;
