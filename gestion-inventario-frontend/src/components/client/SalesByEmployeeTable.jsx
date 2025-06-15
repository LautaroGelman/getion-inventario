import React, { useState, useEffect } from 'react';
import { api } from '../../services/api';

function SalesByEmployeeTable({ startDate, endDate }) {
    const [salesData, setSalesData] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (startDate && endDate) {
            const fetchSalesByEmployee = async () => {
                setLoading(true);
                try {
                    const response = await api.get('/api/metrics/sales-by-employee', {
                        params: {
                            startDate: `${startDate}T00:00:00`,
                            endDate: `${endDate}T23:59:59`,
                        }
                    });
                    setSalesData(response.data);
                } catch (error) {
                    console.error("Error fetching sales by employee:", error);
                } finally {
                    setLoading(false);
                }
            };
            fetchSalesByEmployee();
        }
    }, [startDate, endDate]);

    if (loading) return <div>Cargando datos...</div>;

    return (
        <table className="report-table">
            <thead>
            <tr>
                <th>Empleado</th>
                <th>N° de Ventas</th>
                <th>Monto Total Vendido</th>
            </tr>
            </thead>
            <tbody>
            {salesData.map(item => (
                <tr key={item.employeeId}>
                    <td>{item.employeeName}</td>
                    <td>{item.salesCount}</td>
                    <td>${item.totalSalesAmount.toFixed(2)}</td>
                </tr>
            ))}
            </tbody>
        </table>
    );
}

export default SalesByEmployeeTable;