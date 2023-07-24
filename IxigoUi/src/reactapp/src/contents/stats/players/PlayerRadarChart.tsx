import React from 'react';
import { Radar } from 'react-chartjs-2';
import * as ss from 'simple-statistics';
import { useQuery } from 'react-query';
import { TooltipItem } from 'chart.js';

// Define your types for the player data
interface PlayerData {
    steamid: string;
    username: string;
    kpr: number;
    hltv_rating: number;
    adr: number;
    kast: number;
    dpr: number;
    headshot_percentage: number;
    ud: number;
    ebt: number;
}

interface RadarChartProps {
    steamid: string;
}

const PlayerRadarChart: React.FC<RadarChartProps> = ({ steamid }) => {
    const { data: playerData, isError, isLoading } = useQuery<PlayerData[], Error>({
        queryKey: ['leaderboard'+steamid],
        queryFn: async (): Promise<PlayerData[]> => {
            const url1 = new URL("https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/PLAYER_OVERALL_STATS_EXTENDED_EXTENDED_CACHE");

            const responses = await Promise.all([
                fetch(url1.href),

            ]);

            const jsons = await Promise.all(responses.map(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            }));

            const playerOverallStatsExtended = jsons[0].view_data as PlayerData[];

            return playerOverallStatsExtended;
        },
        keepPreviousData: true,
    });


    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (isError || !playerData) {
        return <div>Error loading data</div>;
    }

    // Find the specific player
    const player = playerData.find(p => p.steamid === steamid);
    if (!player) {
        return <div>Player not found</div>;
    }

    // Calculate the mean and standard deviation of the metrics
    const kprMean = ss.mean(playerData.map(p => p.kpr));
    const hltv_ratingMean = ss.mean(playerData.map(p => p.hltv_rating));
    const adrMean = ss.mean(playerData.map(p => p.adr));
    const kastMean = ss.mean(playerData.map(p => p.kast));
    const dprMean = ss.mean(playerData.map(p => p.dpr));
    const hspMean = ss.mean(playerData.map(p => p.headshot_percentage));
    const udMean = ss.mean(playerData.map(p => p.ud));
    const ebtMean = ss.mean(playerData.map(p => p.ebt));

    const kprStdDev = ss.standardDeviation(playerData.map(p => p.kpr));
    const hltv_ratingStdDev = ss.standardDeviation(playerData.map(p => p.hltv_rating));
    const adrStdDev = ss.standardDeviation(playerData.map(p => p.adr));
    const kastStdDev = ss.standardDeviation(playerData.map(p => p.kast));
    const dprStdDev = ss.standardDeviation(playerData.map(p => p.dpr));
    const hspStdDev = ss.standardDeviation(playerData.map(p => p.headshot_percentage));
    const udStdDev = ss.standardDeviation(playerData.map(p => p.ud));
    const ebtStdDev = ss.standardDeviation(playerData.map(p => p.ebt));


    // Calculate z-scores for the specific player
    const kprZScore = (player.kpr - kprMean) / kprStdDev;
    const hltv_ratingZScore = (player.hltv_rating - hltv_ratingMean) / hltv_ratingStdDev;
    const adrZScore = (player.adr - adrMean) / adrStdDev;
    const kastZScore = (player.kast - kastMean) / kastStdDev;
    const dprZScore = (dprMean - player.dpr) / dprStdDev;
    const hspZScore = (player.headshot_percentage - hspMean) / hspStdDev;
    const udZScore = (player.ud - udMean) / udStdDev;
    const ebtZScore = (player.ebt - ebtMean) / ebtStdDev;

    const originalValues = [player.kpr, player.hltv_rating, player.adr, player.kast, player.dpr, player.headshot_percentage, player.ud, player.ebt];
    const descriptions = ['Kills per round', 'HLTV rating','Average Damage per Round', 'Kill/Assist/Survive/TD%','Deaths Per Round (lower values give higher z-score)','Headshot kill %','Utility Damage','Average Enemy blind time']

    // Use z-scores in the dataset
    const data = {
        labels: ['KPR', 'HLTV', 'ADR', 'KAST', 'DPR', 'HSP', 'UD', 'EBT'],
        datasets: [
            {
                label: 'Player Stats',
                data: [kprZScore, hltv_ratingZScore, adrZScore, kastZScore, dprZScore, hspZScore, udZScore, ebtZScore],
                backgroundColor: 'rgba(75,192,192,0.2)',
                borderColor: 'rgba(75,192,192,1)',
                pointBackgroundColor: 'rgba(75,192,192,1)',
                pointBorderColor: '#fff',
                pointHoverBackgroundColor: '#fff',
                pointHoverBorderColor: 'rgba(75,192,192,1)'
            }
        ]
    };

    const options = {
        plugins: {
            legend: {
                display: false,
            },
            tooltip: {
                callbacks: {
                    label: function (tooltipItem: TooltipItem<'radar'>) {
                        // Use the original value directly
                        const rawValue = originalValues[tooltipItem.dataIndex!];
                        const desc = descriptions[tooltipItem.dataIndex!];
                        return `${desc}: ${rawValue.toFixed(2)}`;
                    },
                },
            },
        },
        scales: {
            r: {
                grid: {
                    display: true,  // Show grid lines
                    color: 'dimgray',  // Set grid lines color to dark grey
                },
                angleLines: {
                    display: true,
                    color: 'dimgray'
                },
                min: -2,
                max: 2,
                ticks: {
                    display: false
                 }
            }
        },
    };
    return <Radar data={data} options={options} />;
};

export default PlayerRadarChart;
