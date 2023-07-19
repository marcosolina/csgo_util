import React from 'react';
import { Pie } from 'react-chartjs-2';

interface PieChartProps {
    percentage: number;
    color: string;
    size: number;
}

const PieChartMini: React.FC<PieChartProps> = ({ percentage, color, size }) => {
    const data = {
        datasets: [
            {
                data: [percentage, 100 - percentage],
                backgroundColor: [color, '#555'],
            },
        ],
    };

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
            enabled: false,
        },
        legend: {
            display: false,
        },
        elements: {
            arc: {
                borderWidth: 0,
            },
        },
    };

    return (
        <div style={{ width: size, height: size }}>  {/* Set the size of the div */}
            <Pie data={data} options={options} />
        </div>
    );
};

export default PieChartMini;
