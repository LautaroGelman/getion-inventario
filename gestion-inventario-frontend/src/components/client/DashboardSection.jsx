import React, { useEffect, useState } from 'react';
import { api } from '../../services/api';
import SalesChart from './SalesChart';
import ProfitabilityChart from './ProfitabilityChart'; // <-- IMPORTAMOS EL NUEVO GRÁFICO
import './DashboardSection.css';

function DashboardSection() {
    const [dashboardData, setDashboardData] = useState(null);
    const [salesChartData, setSalesChartData] = useState(null);
    const [profitabilityData, setProfitabilityData] = useState(null); // <-- NUEVO ESTADO
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchAllData = async () => {
            try {
                setLoading(true);
                // Añadimos la tercera llamada a la API a nuestro Promise.all
                const [dashData, salesData, profitData] = await Promise.all([
                    api.get('/client/dashboard'),
                    api.get('/client/sales/summary?days=30'),
                    api.get('/client/dashboard/profitability-summary?days=30')
                ]);
                setDashboardData(dashData);
                setSalesChartData(salesData);
                setProfitabilityData(profitData); // <-- GUARDAMOS LOS NUEVOS DATOS
            } catch (err) {
                setError(err.message || 'No se pudo cargar la información del dashboard.');
            } finally {
                setLoading(false);
            }
        };

        fetchAllData();
    }, []);

    if (loading) {
        return <div>Cargando dashboard...</div>;
    }

    if (error) {
        return <div className="error-message">Error: {error}</div>;
    }

    return (
        <div className="dashboard-section">
            <h2>Dashboard Cliente</h2>
            <p>Indicadores como stock bajo, últimas ventas y movimientos recientes.</p>

            <div className="dash-cards-container">
                {dashboardData && (
                    <>
                        <div className="dash-card">
                            <h3>Artículos con stock bajo</h3>
                            <p>{dashboardData.lowStock}</p>
                        </div>
                        <div className="dash-card">
                            <h3>Ventas del día</h3>
                            <p>{dashboardData.salesToday}</p>
                        </div>
                    </>
                )}
            </div>

            <div className="charts-container">
                <div className="chart-wrapper">
                    {salesChartData ? <SalesChart apiData={salesChartData} /> : <p>Cargando gráfico de ventas...</p>}
                </div>

                {/* Reemplazamos el placeholder con el componente real del gráfico de rentabilidad */}
                <div className="chart-wrapper">
                    {profitabilityData ? <ProfitabilityChart apiData={profitabilityData} /> : <p>Cargando gráfico de rentabilidad...</p>}
                </div>
            </div>
        </div>
    );
}

export default DashboardSection;