import React, { useState } from 'react';
import BestSellersChart from './BestSellersChart';
import SalesByEmployeeTable from './SalesByEmployeeTable';
import './ReportsSection.css';

function ReportsSection() {
    const today = new Date();
    const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);

    const [startDate, setStartDate] = useState(firstDayOfMonth.toISOString().slice(0, 10));
    const [endDate, setEndDate] = useState(today.toISOString().slice(0, 10));

    return (
        <div className="reports-section">
            <div className="section-header">
                <h2>Reportes</h2>
            </div>
            <div className="date-filters">
                <label>
                    Desde:
                    <input type="date" value={startDate} onChange={e => setStartDate(e.target.value)} />
                </label>
                <label>
                    Hasta:
                    <input type="date" value={endDate} onChange={e => setEndDate(e.target.value)} />
                </label>
            </div>

            <div className="report-container">
                <h3>Productos Más Vendidos</h3>
                <BestSellersChart startDate={startDate} endDate={endDate} />
            </div>

            <div className="report-container">
                <h3>Ventas por Empleado</h3>
                <SalesByEmployeeTable startDate={startDate} endDate={endDate} />
            </div>
        </div>
    );
}

export default ReportsSection;