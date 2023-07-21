import { useState, useMemo } from "react";
import { IconButton, Tooltip } from '@mui/material';
import { Box } from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { MaterialReactTable } from 'material-react-table';
import {
  useQuery,
} from 'react-query';
import { PieChart } from 'react-minimal-pie-chart';
import terroristLogo from '../../../assets/icons/T.png';
import ctLogo from '../../../assets/icons/CT.png';

interface MatchRoundsContentProps {
  match_id: number;
}

interface MatchRound {
  match_id: number,
  round: number,
  team: number,
  player_count: number,
  death_count: number,
  winner_team: string,
  total_money_spent: number,
  total_equipment_value: number,
  round_end_reason: string,
  round_type: string,
  t_score: number,
  ct_score: number,
  team1_score: number,
  team2_score: number
}

// Define a type that represents a row in your table
interface TableRound {
  round: number;
  team1?: MatchRound;
  team2?: MatchRound;
}

const round_end_reasons = {
  1: "Target Successfully Bombed!",
  7: "The bomb has been defused!",
  8: "Counter-Terrorists Win!",
  9: "Terrorists Win!",
  11: "All Hostages have been rescued!",
  12: "Target has been saved!",
  13: "Hostages have not been rescued!",
}


const smallColSize = 5;
const MatchRoundsContent: React.FC<MatchRoundsContentProps> = ({ match_id }) => {
  const [maxValues, setMaxValues] = useState<{ total_equipment_value: number, total_money_spent: number }>({ total_equipment_value: 0, total_money_spent: 0 });

  const { data: matchRounds, isError, isFetching, isLoading, refetch } = useQuery<TableRound[]>({
    queryKey: ['matchrounds' + match_id],
    queryFn: async () => {
      const url1 = new URL("https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/ROUND_SCORECARD");

      const responses = await Promise.all([
        fetch(url1.href),
      ]);

      const jsons = await Promise.all(responses.map(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      }));

      const matchRounds = jsons[0].view_data;

      // filter matchRounds based on match_id
      const filteredMatchRounds = matchRounds.filter((round: MatchRound) => round.match_id === match_id);



      // Group by round, then by team
      const groupedData: TableRound[] = filteredMatchRounds.reduce((acc: TableRound[], round: MatchRound) => {
        let foundRound = acc.find(r => r.round === round.round);
        if (!foundRound) {
          foundRound = { round: round.round };
          acc.push(foundRound);
        }
        if (round.team === 2) {
          foundRound.team1 = round;
        } else if (round.team === 3) {
          foundRound.team2 = round;
        }
        return acc;
      }, []);

      // Calculate maximum values
      function getMaxValues() {
        let maxEquipmentValue = 0;
        let maxMoneySpent = 0;

        for (const round of filteredMatchRounds) {
          if (round.team === 2) {
            maxEquipmentValue = Math.max(maxEquipmentValue, round.total_equipment_value);
            maxMoneySpent = Math.max(maxMoneySpent, round.total_money_spent);
          } else if (round.team === 3) {
            maxEquipmentValue = Math.max(maxEquipmentValue, round.total_equipment_value);
            maxMoneySpent = Math.max(maxMoneySpent, round.total_money_spent);
          }
        }

        return { total_equipment_value: maxEquipmentValue, total_money_spent: maxMoneySpent };
      }

      setMaxValues(getMaxValues());

      return groupedData;

    },
    keepPreviousData: true,
  });



  function createCustomHeader(tooltipText: string) {
    return ({ column }: { column: any }) => (
      <Tooltip title={tooltipText}>
        <span>{column.columnDef.header}</span>
      </Tooltip>
    );
  }

  const FinanceCell: React.FC<{ cell: any, row: { index: number }, team: 'team1' | 'team2' }> = ({ cell, row, team }) => {

    const teamData = cell.row.original[team];
    if (!teamData) {
      // The data for this team does not exist, possibly because the match is not over yet
      return null;
    }

    const equipmentValuePercentage = teamData.total_equipment_value / maxValues.total_equipment_value * 100;
    const moneySpentPercentage = teamData.total_money_spent / maxValues.total_money_spent * 100;

    return (
      <div style={{ direction: team === 'team1' ? 'rtl' : 'ltr' }}>
        <Tooltip title={`Equipment Value: $${teamData.total_equipment_value}`}>
          <div style={{
            backgroundColor: '#90caf9',
            borderRadius: '10px',
            width: `${equipmentValuePercentage}%`,
            height: '10px'
          }} />
        </Tooltip>
        <Tooltip title={`Cash Spent: $${teamData.total_money_spent}`}>
          <div style={{
            backgroundColor: 'orange',
            borderRadius: '10px',
            width: `${moneySpentPercentage}%`,
            height: '10px'
          }} />
        </Tooltip>
      </div>
    );
  }


  const PlayerBlob: React.FC<{ isAlive: boolean }> = ({ isAlive }) => {
    const data = [
      { title: 'Alive', value: isAlive ? 100 : 0, color: '#90caf9' },
      { title: 'Dead', value: isAlive ? 0 : 100, color: 'dimgrey' },
    ];
  
    return (
      <div style={{ width: 20, height: 20, display: 'flex', alignItems: 'center', justifyContent: 'flex-end' }}>
        <PieChart
          data={data}
          lineWidth={isAlive ? 30 : 10} // This makes it a donut chart
          totalValue={100}
          startAngle={-90}
          lengthAngle={360}
          animate
        />
      </div>
    );
  }
  const PlayerCountCell: React.FC<{ cell: any, row: { index: number }, team: 'team1' | 'team2' }> = ({ cell, row, team }) => {
    const teamData = cell.row.original[team];
    if (!teamData) {
      // The data for this team does not exist, possibly because the match is not over yet
      return null;
    }

    const { player_count, death_count } = teamData;
    const playerBlobs = [];

    for (let i = 0; i < player_count; i++) {
      playerBlobs.push(
        <PlayerBlob key={i} isAlive={i >= death_count} />
      );
    }

    return (
      <div style={{ display: 'flex', justifyContent: team === 'team1' ? 'flex-end' : 'flex-start' }}>
        {team === 'team1' ? playerBlobs : playerBlobs.reverse()}
      </div>
    );
  }


  const ScoreCell: React.FC<{ cell: any, row: { index: number }, team: 'team1' | 'team2' }> = ({ cell, row, team }) => {
    const teamData = cell.row.original[team];
    if (!teamData) {
      // The data for this team does not exist, possibly because the match is not over yet
      return null;
    }

    const { round, team1_score, team2_score, winner_team } = teamData;
    const isTerrorist = team === 'team1' && round <= 7 || team === 'team2' && round > 7;
    const logo = isTerrorist ? terroristLogo : ctLogo;
    const isWinner = (winner_team === 2 && isTerrorist || winner_team ===3 && !isTerrorist);
    const imageOpacity = isWinner ? .5 : 0.05;
    const colour = isWinner ? 'white' : 'dimgrey';

    return (
        <div style={{ position: 'relative', width: 25, height: 25 }}>
            <img src={logo} alt={isTerrorist ? 'Terrorist logo' : 'CT logo'} style={{ width: '100%', height: '100%', opacity: imageOpacity }} />
            <div style={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', color: colour, fontWeight: 'bold' }}>
                {team === 'team1' ? team1_score: team2_score}
            </div>
        </div>
    );
}

  const columns = useMemo(
    () => [
      { accessorKey: 'round' as const, header: 'R', size: smallColSize, Header: createCustomHeader('Round number') },
      {
        accessorKey: 'team1.total_equipment_value' as const, header: '$', size: 100, Header: createCustomHeader('Finance'),
        Cell: (props: { cell: any, row: { index: number } }) => <FinanceCell {...props} team='team1' />
      },
      {
        accessorKey: 'team1.player_count' as const, header: 'Players', size: smallColSize, Header: createCustomHeader('Player Count'),
        Cell: (props: { cell: any, row: { index: number } }) => <PlayerCountCell {...props} team='team1' />
      },
      { accessorKey: 'team1.team1_score' as const, header: 'T1', size: smallColSize, Header: createCustomHeader('Team 1 Score') ,
        Cell: (props: { cell: any, row: { index: number } }) => <ScoreCell {...props} team='team1'/>},
      { accessorKey: 'team1.round_end_reason' as const, header: 'W', size: 1, Header: createCustomHeader('Round End Reason') },
      { accessorKey: 'team2.team2_score' as const, header: 'T2', size: smallColSize, Header: createCustomHeader('Team 2 Score') ,
      Cell: (props: { cell: any, row: { index: number } }) => <ScoreCell {...props} team='team2'/>},
      { accessorKey: 'team2.player_count' as const, header: 'Players', size: smallColSize, Header: createCustomHeader('Player Count'),
      Cell: (props: { cell: any, row: { index: number } }) => <PlayerCountCell {...props} team='team2' /> },
      {
        accessorKey: 'team2.total_equipment_value' as const, header: '$', size: 100, Header: createCustomHeader('Finance'),
        Cell: (props: { cell: any, row: { index: number } }) => <FinanceCell {...props} team='team2' />
      },
    ],
    [],
  );


  return (
    <MaterialReactTable
      columns={columns}
      data={matchRounds ?? []}
      initialState={{
        showColumnFilters: false,
        density: 'compact',
        sorting: [{ id: 'round', desc: false }],
      }}
      enableColumnActions={false}
      enableColumnFilters={false}
      enableSorting={false}
      enableTableHead={true}
      enableTopToolbar={false}
      enableBottomToolbar={false}
      enableGlobalFilter={false}
      enableFullScreenToggle={false}
      enableHiding={false}
      enablePagination={false}
      muiToolbarAlertBannerProps={
        isError
          ? {
            color: 'error',
            children: 'Error loading data',
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
      rowCount={matchRounds?.length ?? 0}
      state={{
        isLoading,
        showAlertBanner: isError,
        showProgressBars: isFetching,
      }}
    />
  );
};

export default MatchRoundsContent;