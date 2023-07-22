import React, { useEffect, useRef } from 'react';
import {
  useQuery,
} from 'react-query';
import Chart from 'chart.js/auto';
import 'chartjs-adapter-date-fns';
import { startOfWeek, startOfMonth, format } from 'date-fns';

interface PlayerGraphsStatsContentProps {
  steamid: string;
  fieldName: string;
  label: string;
  binningLevel: 'week' | 'month';
  showXAxisLabels: boolean;
}

const PlayerGraphsStatsContent: React.FC<PlayerGraphsStatsContentProps> = ({ steamid, fieldName, label, binningLevel, showXAxisLabels }) => {
  const canvasRef = useRef(null);

  const { data, isError, isFetching, isLoading, refetch } = useQuery({
    queryKey: ['playerradar'],
    queryFn: async () => {
      const url1 = new URL("https://marco.selfip.net/ixigoproxy/ixigo-dem-manager/demmanager/charts/view/PLAYER_MATCH_STATS_EXTENDED_CACHE");
  
      const responses = await Promise.all([
        fetch(url1.href),
      ]);
  
      const jsons = await Promise.all(responses.map(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      }));
  
      const matchResults = jsons[0].view_data;
  
      return matchResults;
    },
    keepPreviousData: true,
  });

  useEffect(() => {
    let chart: Chart | null = null;
    if (data) {
      // Filter the data based on the steamid
      const filteredData = data.filter((item: any) => item.steamid === steamid);
      
      // Sort the data by date
      filteredData.sort((a: any, b: any) => new Date(a.match_date).getTime() - new Date(b.match_date).getTime());

      // Bin the data by week or month
      const binnedData = new Map();
      filteredData.forEach((item: any) => {
        const dateFn = binningLevel === 'week' ? startOfWeek : startOfMonth; // Choose the correct date function
        const binnedDate = format(dateFn(new Date(item.match_date)), 'yyyy-MM-dd');
        if (!binnedData.has(binnedDate)) {
          binnedData.set(binnedDate, []);
        }
        binnedData.get(binnedDate).push(item);
      });

      // Get the average field value for each bin
      const dates = Array.from(binnedData.keys());
      const fieldValues = Array.from(binnedData.values()).map(items => {
        const total = items.reduce((sum: number, item: any) => sum + item[fieldName], 0);
        return total / items.length;
      });

      // Calculate the trendline
      const xMean = dates.reduce((sum, _, i) => sum + i, 0) / dates.length;
      const yMean = fieldValues.reduce((sum, y) => sum + y, 0) / fieldValues.length;

      let numerator = 0;
      let denominator = 0;
      for (let i = 0; i < dates.length; i++) {
        numerator += (i - xMean) * (fieldValues[i] - yMean);
        denominator += (i - xMean) ** 2;
      }

      const slope = numerator / denominator;
      const yIntercept = yMean - slope * xMean;

      // Generate points for the trendline
      const trendline = dates.map((_, i) => slope * i + yIntercept);

      if (canvasRef.current) {
        chart = new Chart(canvasRef.current, {
          type: 'line',
          data: {
            labels: dates,
            datasets: [
              // This is your original data
              {
                label: label,
                data: fieldValues,
                fill: false,
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.1,
              },
              // This is your trendline
              {
                label: 'Trendline',
                data: trendline,
                fill: false,
                pointRadius: 0,
                borderColor: 'rgba(255, 255, 255, 0.5)', 
                borderDash: [5, 5], 
                tension: 0.1,
              },
            ],
          },
          options: {
            maintainAspectRatio: false,
            scales: {
              x: {
                type: 'time',
                time: {
                  unit: binningLevel
                },
                display: showXAxisLabels,
                ticks: {
                    autoSkip: false,
                    maxRotation: 90,
                    minRotation: 90
                }
              }
            }
          }
        });
      }
    }
    return () => {
      if (chart) {
        chart.destroy();
      }
    };
  }, [data, steamid, fieldName, label, binningLevel]);

  if (isLoading) return <div>Loading...</div>;
  if (isError) return <div>Error loading data</div>;

  return <canvas ref={canvasRef} />;
};

export default PlayerGraphsStatsContent;