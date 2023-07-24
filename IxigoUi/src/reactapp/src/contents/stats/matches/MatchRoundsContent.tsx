import { useState, useMemo } from "react";
import { IconButton, Tooltip, Typography } from '@mui/material';
import { Box } from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { MaterialReactTable } from 'material-react-table';
import {
  useQuery,
} from 'react-query';
import { PieChart } from 'react-minimal-pie-chart';
import terroristLogo from '../../../assets/icons/T.png';
import ctLogo from '../../../assets/icons/CT.png';
import bomb from '../../../assets/icons/bomb.png';
import death from '../../../assets/icons/death.png';
import defuse from '../../../assets/icons/defuse.png';
import rescue from '../../../assets/icons/rescue.png';
import time from '../../../assets/icons/time.png';
import eco from '../../../assets/icons/eco.png';
import force from '../../../assets/icons/forcebuy.png';
import full from '../../../assets/icons/fullbuy.png';
import pistol from '../../../assets/icons/pistol.png';
import headshot from '../../../assets/icons/hs.png';
import flashbang from '../../../assets/icons/flashbang.png';
import { weaponImage } from "../weaponImage";
import { group } from "console";

const roundIconImage: { [key: number]: string } = {
  1: bomb,
  7: defuse,
  8: death,
  9: death,
  11: rescue,
  12: time,
  13: time
}

const roundTypeIconImage: { [key: string]: string } = {
  'pistol': pistol,
  'eco': eco,
  'force buy': force,
  'full buy': full
}

const eventNameDesc: { [key: string]: string } = {
  'hostage_rescued': 'rescued the hostage',
  'bomb_planted': 'planted the bomb',
  'bomb_defused': 'defused the bomb'
}

interface MatchRoundsContentProps {
  match_id: number;
}

interface PlayerStat {
  steamid: string;
  usernames: string;
  last_round_team: string;
  // add more fields here as necessary
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

interface FinanceCellProps {
  cell: any;
  row: { index: number };
  team: 'team1' | 'team2';
  maxValues: { total_equipment_value: number, total_money_spent: number } | undefined;
}

interface RoundEvent {
  steamid: string;
  round: number;
  match_id: number;
  eventtype: string;
  eventtime: number;
}

interface KillEvent {
  victimsteamid: string;
  isfirstkill: boolean;
  match_id: number;
  eventtime: number;
  istradekill: boolean;
  assister: string;
  steamid: string;
  weapon: string;
  flashassister: string;
  round: number;
  killerflashed: string;
  headshot: boolean;
  istradedeath: boolean;
}


// Define a type that represents a row in your table
interface TableRound {
  round: number;
  team1?: MatchRound;
  team2?: MatchRound;
  killEvents?: any[];
  roundEvents?: any[];
}

const round_end_reasons: { [key: number]: string } = {
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

  const { data, isError, isFetching, isLoading, refetch } = useQuery<{
    matchRounds: TableRound[],
    maxValues: { total_equipment_value: number, total_money_spent: number },
    playerStats: any[]
  }>({
    queryKey: ['matchrounds' + match_id],
    queryFn: async () => {
      const url1 = new URL(`https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/ROUND_SCORECARD_CACHE?match_id=${match_id}`);
      const url2 = new URL(`https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/round_kill_events?match_id=${match_id}`);
      const url3 = new URL(`https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/round_events?match_id=${match_id}`);
      const url4 = new URL(`https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/PLAYER_MATCH_STATS_EXTENDED_CACHE?match_id=${match_id}`);

      const responses = await Promise.all([
        fetch(url1.href),
        fetch(url2.href),
        fetch(url3.href),
        fetch(url4.href),
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
      const killEvents = jsons[1].view_data.filter((event: any) => event.match_id === match_id);
      const roundEvents = jsons[2].view_data.filter((event: any) => event.match_id === match_id);
      const playerStats = jsons[3].view_data.filter((stats: any) => stats.match_id === match_id);



      const groupedData: TableRound[] = filteredMatchRounds.reduce((acc: TableRound[], round: MatchRound) => {
        let foundRound = acc.find(r => r.round === round.round);
        if (!foundRound) {
          foundRound = { round: round.round, killEvents: [], roundEvents: [] };
          acc.push(foundRound);
        }
        foundRound = foundRound!;
        if ((round.team === 2 && round.round <= 7) || (round.team === 3 && round.round > 7)) {
          foundRound.team1 = round;
          if (foundRound.killEvents) {
            foundRound.killEvents.push(...killEvents.filter((event: any) => event.round === round.round));
          }
          if (foundRound.roundEvents) {
            foundRound.roundEvents.push(...roundEvents.filter((event: any) => event.round === round.round));
          }
        } else {
          foundRound.team2 = round;
        }

        return acc;
      }, []);

      // Calculate maximum values
      function getMaxValues() {
        let maxEither = 0;

        for (const round of filteredMatchRounds) {
          maxEither = Math.max(maxEither, round.total_equipment_value);
          maxEither = Math.max(maxEither, round.total_money_spent);
        }

        return { total_equipment_value: maxEither, total_money_spent: maxEither };
      }

      setMaxValues(getMaxValues());
      const maxValues = getMaxValues();
      return { matchRounds: groupedData, maxValues, playerStats };

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

  const FinanceCell: React.FC<FinanceCellProps> = ({ cell, row, team, maxValues }) => {
    if (!maxValues) {
      return null;  // or some placeholder
    }
    const teamData = cell.row.original[team];
    if (!data) {
      return null;  // or some placeholder
    }
    if (!teamData) {
      return null;  // or some placeholder
    }

    const equipmentValuePercentage = teamData.total_equipment_value / data.maxValues.total_equipment_value * 100;
    const moneySpentPercentage = teamData.total_money_spent / data.maxValues.total_money_spent * 100;

    return (
      <div style={{ direction: team === 'team1' ? 'rtl' : 'ltr' }}>
        <Tooltip title={`Equipment Value: $${teamData.total_equipment_value}`}>
          <div style={{
            backgroundColor: 'rgb(75, 192, 192)',
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



  const PlayerBlob: React.FC<{ isAlive: boolean, team: 'team1' | 'team2' }> = ({ isAlive, team }) => {
    const data = [
      { title: 'Alive', value: isAlive ? 100 : 0, color: team == 'team1' ? '#90caf9' : 'orange' },
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
        <PlayerBlob key={i} isAlive={i >= death_count} team={team} />
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
    const isTerrorist = ((team === 'team1' && round <= 7) || (team === 'team2' && round > 7));
    const logo = isTerrorist ? terroristLogo : ctLogo;
    const isWinner = ((winner_team === 2 && isTerrorist) || (winner_team === 3 && !isTerrorist));
    const imageOpacity = isWinner ? .5 : 0.05;
    const colour = isWinner ? 'white' : 'dimgrey';

    return (
      <div style={{ position: 'relative', width: 25, height: 25 }}>
        <img src={logo} alt={isTerrorist ? 'Terrorist logo' : 'CT logo'} style={{ width: '100%', height: '100%', opacity: imageOpacity }} />
        <div style={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', color: colour, fontWeight: 'bold' }}>
          {team === 'team1' ? team1_score : team2_score}
        </div>
      </div>
    );
  }

  const columns = useMemo(
    () => [
      {
        accessorKey: 'team1.total_equipment_value' as const, header: 'Equipment/Spend', size: 150, Header: createCustomHeader('Starting equipment value/cash spent'),
        Cell: (props: { cell: any, row: { index: number } }) => <FinanceCell {...props} team='team1' maxValues={data?.maxValues} />
      },
      {
        accessorKey: 'team1.round_type' as const, header: 'Type', size: smallColSize, Header: createCustomHeader('Round Type'),
        Cell: ({ cell }: { cell: any }) => {
          const type = cell.getValue() as string;
          const imageUrl = roundTypeIconImage[type];
          return imageUrl ? <Tooltip title={type}><img src={imageUrl} alt={type} style={{ height: '30px' }} /></Tooltip> : null;
        }
      },
      {
        accessorKey: 'team1.player_count' as const, header: 'Players', size: 100, Header: createCustomHeader('Player Count'),
        Cell: (props: { cell: any, row: { index: number } }) => <PlayerCountCell {...props} team='team1' />
      },
      {
        accessorKey: 'team1.team1_score' as const, header: 'T1', minSize: 5, maxSize: 5, size: smallColSize, Header: createCustomHeader('Team 1 Score'),
        Cell: (props: { cell: any, row: { index: number } }) => <ScoreCell {...props} team='team1' />
      },
      {
        accessorKey: 'team1.round_end_reason' as const, header: '', minSize: 5, maxSize: 5, size: 1, Header: createCustomHeader('Round End Reason'),
        Cell: ({ cell }: { cell: any }) => {
          const reason = cell.getValue() as number;
          const imageUrl = roundIconImage[reason];
          const reasonText = round_end_reasons[reason];
          return imageUrl ? <Tooltip title={reasonText}><img src={imageUrl} alt={reasonText} style={{ height: '30px' }} /></Tooltip> : null;
        }
      },
      {
        accessorKey: 'team2.team2_score' as const, header: 'T2', minSize: 5, maxSize: 5, size: smallColSize, Header: createCustomHeader('Team 2 Score'),
        Cell: (props: { cell: any, row: { index: number } }) => <ScoreCell {...props} team='team2' />
      },
      {
        accessorKey: 'team2.player_count' as const, header: 'Players', size: 100, Header: createCustomHeader('Player Count'),
        Cell: (props: { cell: any, row: { index: number } }) => <PlayerCountCell {...props} team='team2' />
      },
      {
        accessorKey: 'team2.round_type' as const, header: 'Type', size: smallColSize, Header: createCustomHeader('Round Type'),
        Cell: ({ cell }: { cell: any }) => {
          const type = cell.getValue() as string;
          const imageUrl = roundTypeIconImage[type];
          return imageUrl ? <Tooltip title={type}><img src={imageUrl} alt={type} style={{ height: '30px' }} /></Tooltip> : null;
        }
      },
      {
        accessorKey: 'team2.total_equipment_value' as const, header: 'Equipment/Spend', size: 150, Header: createCustomHeader('Starting equipment value/cash spent'),
        Cell: (props: { cell: any, row: { index: number } }) => <FinanceCell {...props} team='team2' maxValues={data?.maxValues} />
      },
      { accessorKey: 'round' as const, header: 'R', size: smallColSize, Header: createCustomHeader('Round number') },
    ],
    [data],
  );


  const renderDetailPanel = (playerStats: PlayerStat[]) => ({ row }: { row: any }) => {
    const { killEvents, roundEvents } = row.original;
    const events = [...killEvents, ...roundEvents];

    events.sort((a, b) => a.eventtime - b.eventtime);

    const getUsernameAndTeam = (steamid: string) => {
      const player = playerStats.find((player: any) => player.steamid === steamid);
      return player ? { username: player.usernames, team: player.last_round_team } : { username: "Bot", team: 'unknown' };
    };

    return (
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          margin: 'auto',
          width: '100%',
        }}
      >
        {events.map((event: any, i) => {
          const { username: steamidUsername, team: steamidTeam } = getUsernameAndTeam(event.steamid);
          if (event.victimsteamid) {
            const { username: victimsteamidUsername, team: victimsteamidTeam } = getUsernameAndTeam(event.victimsteamid);
            const { username: assisterUsername, team: assisterTeam } = getUsernameAndTeam(event.assister);
            const { username: flashAssisterUsername, team: flashAssisterTeam } = getUsernameAndTeam(event.flashassister);
            return (
              <Box key={i} sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
                <Typography>
                  {`${new Date(event.eventtime * 1000).toISOString().substr(14, 5)}: `}
                  <span style={{ color: steamidTeam == 'team1' ? '#90caf9' : 'orange' }}>{steamidUsername}</span>
                  <span style={{ color: assisterTeam == 'team1' ? '#90caf9' : 'orange' }}>{event.assister && ` + ${assisterUsername}`} </span>
                  {event.flashassist && <img height="20px" style={{ padding: '0 5px' }} src={flashbang} alt="Flash Assist" />}
                  <span style={{ color: flashAssisterTeam == 'team1' ? '#90caf9' : 'orange' }}>{event.flashassist && ` + ${flashAssisterUsername}`}</span>
                </Typography>
                <img height="20px" style={{ transform: 'scaleX(-1)', padding: '0 5px' }} src={weaponImage[event.weapon]} alt={event.weapon} />
                {event.headshot && <img height="20px" style={{ padding: '0 5px' }} src={headshot} alt="Headshot" />}
                <Typography>
                  <span style={{ color: victimsteamidTeam == 'team1' ? '#90caf9' : 'orange' }}>{victimsteamidUsername}</span>
                </Typography>
              </Box>
            );
          } else {
            return (
              <Typography key={i}>
                {`${new Date(event.eventtime * 1000).toISOString().substr(14, 5)}: `}
                <span style={{ color: steamidTeam == 'team1' ? '#90caf9' : 'orange' }}>{steamidUsername + " "}</span>
                {eventNameDesc[event.eventtype] ? eventNameDesc[event.eventtype] : event.eventtype}
              </Typography>
            );
          }
        })}
      </Box>
    );
  };


  return (
    <MaterialReactTable
      columns={columns}
      data={data?.matchRounds ?? []}
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
      renderDetailPanel={renderDetailPanel(data?.playerStats ?? [])}
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
      rowCount={data?.matchRounds?.length ?? 0}
      state={{
        isLoading,
        showAlertBanner: isError,
        showProgressBars: isFetching,
      }}
    />
  );
};

export default MatchRoundsContent;