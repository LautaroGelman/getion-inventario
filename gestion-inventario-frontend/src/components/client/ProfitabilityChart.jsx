import React from 'react';
import { Bar } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    LineElement,
    PointElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';

// Registramos los componentes de Chart.js que vamos a utilizar
ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    LineElement,
    PointElement,
    Title,
    Tooltip,
    Legend
);

function ProfitabilityChart({ apiData }) {
    // Adaptamos los datos de la API al formato que espera el gráfico
    const chartData = {
        labels: apiData.map(d => d.date),
        datasets: [
            {
                label: 'Ingresos ($)',
                data: apiData.map(d => d.revenue),
                backgroundColor: 'rgba(75, 192, 192, 0.5)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
            },
            {
                label: 'Costos ($)',
                data: apiData.map(d => d.costOfGoods),
                backgroundColor: 'rgba(255, 99, 132, 0.5)',
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1,
            },
            {
                label: 'Ganancia ($)',
                data: apiData.map(d => d.profit),
                type: 'line', // Este dataset se muestra como una línea sobre las barras
                borderColor: 'rgba(54, 162, 235, 1)',
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                tension: 0.3,
                fill: false,
            },
        ],
    };

    // Opciones de configuración del gráfico
    const options = {
        responsive: true,
        scales: {
            x: { title: { display: true, text: 'Fecha' } },
            y: { title: { display: true, text: 'Monto ($)' }, beginAtZero: true },
        },
        plugins: {
            legend: { position: 'top' },
            title: {
                display: true,
                text: 'Análisis de Rentabilidad (Últimos 30 días)',
                font: { size: 16 }
            }
        },
    };

    return <Bar options={options} data={chartData} />;
}

export default ProfitabilityChart;