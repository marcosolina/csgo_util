import React from 'react';
import { PieChart } from 'react-minimal-pie-chart';

interface PieChartProps {
    percentage: number;
    color: string;
    size: number;
}

const PieChartMini: React.FC<PieChartProps> = ({ percentage, color, size }) => {
    percentage=percentage>100?100:percentage;
    const data = [
        { title: percentage+'%', value: percentage, color: color },
        { title: '', value: 100 - percentage, color: 'dimgrey' },
    ];

    return (
        <div style={{ width: size, height: size, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <PieChart
                data={data}
                lineWidth={100} // This makes it a donut chart
                totalValue={100}
                startAngle={-90}
                lengthAngle={360}
                animate
            />
        </div>
    );
};

export default PieChartMini;
