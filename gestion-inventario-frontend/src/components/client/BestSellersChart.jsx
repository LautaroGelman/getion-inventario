import React, { useState, useEffect } from 'react';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';
import { api } from '../../services/api';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

function BestSellersChart({ startDate, endDate }) {
    const [chartData, setChartData] = useState(null);

    useEffect(() => {
        if (startDate && endDate) {
            const fetchBestSellers = async () => {
                try {
                    const response = await api.get('/api/metrics/bestsellers', {
                        params: {
                            startDate: `${startDate}T00:00:00`,
                            endDate: `${endDate}T23:59:59`,
                        }
                    });
                    const data = response.data;
                    setChartData({
                        labels: data.map(item => item.productName),
                        datasets: [{
                            label: 'Cantidad Vendida',
                            data: data.map(item => item.totalQuantitySold),
                            backgroundColor: 'rgba(4, 128, 163, 0.6)',
                        }]
                    });
                } catch (error) {
                    console.error("Error fetching best sellers:", error);
                }
            };
            fetchBestSellers();
        }
    }, [startDate, endDate]);

    if (!chartData) {
        return <div>Cargando datos del gráfico...</div>;
    }

    return <Bar data={chartData} />;
}

export default BestSellersChart;