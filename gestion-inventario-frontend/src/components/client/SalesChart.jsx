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

function SalesChart({ apiData }) {
    // Procesamos los datos recibidos de la API para adaptarlos al formato del gráfico
    const chartData = {
        labels: apiData.map(d => d.date),
        datasets: [
            {
                label: 'Cantidad de Ventas',
                data: apiData.map(d => d.salesCount),
                yAxisID: 'yVentas',
                type: 'bar',
                backgroundColor: 'rgba(4,128,163,0.5)',
                borderColor: 'rgba(4,128,163,1)',
                borderWidth: 1,
            },
            {
                label: 'Importe Total ($)',
                data: apiData.map(d => d.totalAmount),
                yAxisID: 'yImporte',
                type: 'line',
                tension: 0.3,
                backgroundColor: 'rgba(75,192,192,0.3)',
                borderColor: 'rgba(75,192,192,1)',
                borderWidth: 2,
                fill: true,
            },
        ],
    };

    // Opciones de configuración del gráfico, incluyendo los dos ejes Y
    const options = {
        responsive: true,
        interaction: {
            mode: 'index',
            intersect: false,
        },
        scales: {
            x: { title: { display: true, text: 'Fecha' } },
            yVentas: {
                type: 'linear',
                display: true,
                position: 'left',
                title: { display: true, text: 'Cantidad de Ventas' },
                beginAtZero: true,
            },
            yImporte: {
                type: 'linear',
                display: true,
                position: 'right',
                title: { display: true, text: 'Importe Total ($)' },
                beginAtZero: true,
                grid: { drawOnChartArea: false },
            },
        },
        plugins: {
            legend: { position: 'top' },
            title: {
                display: true,
                text: 'Ventas e Importe ($) por día (Últimos 30 días)',
                font: { size: 16 }
            }
        },
    };

    return <Bar options={options} data={chartData} />;
}

export default SalesChart;