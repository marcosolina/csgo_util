import React from "react";
import { IMatchKillMatrixRequest } from "./interfaces";
import { useMatchKillMatrixContent } from "./useMatchKillMatrixContent";
import { useTranslation } from "react-i18next";
import IxigoTable from "../../../../common/material-table/IxigoTable";

const STRING_PREFIX = "page.stats.match.killmatrix";

const MatchKillMatrixContent: React.FC<IMatchKillMatrixRequest> = ({ match_id }) => {
  const { t } = useTranslation();
  const { columns, flattenedData, state, refetch } = useMatchKillMatrixContent({ match_id });

  return (
    <IxigoTable
      state={state}
      columns={columns}
      data={flattenedData}
      sorting={[{ id: "Team", desc: false }]}
      pagination={{ pageIndex: 0, pageSize: 10 }}
      columnPinning={{ left: ["Killer/Victim"] }}
      columnVisibility={{
        Team: false,
      }}
      enableHiding={true}
      enablePagination={true}
      errorMsg={t(`${STRING_PREFIX}.error-loading-data`) as string}
      refetch={refetch}
    />
  );
};

export default MatchKillMatrixContent;
