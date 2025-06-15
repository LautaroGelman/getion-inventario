import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
import './SalesSection.css';

function SalesSection() {
    const [sales, setSales] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchSales = async () => {
            try {
                setLoading(true);
                // El endpoint ahora es /api/sales, protegido por rol
                const response = await api.get('/api/sales');
                setSales(response.data);
            } catch (err) {
                setError(err.response?.data?.message || 'No se pudo cargar el historial de ventas.');
            } finally {
                setLoading(false);
            }
        };

        fetchSales();
    }, []);

    const handleNewSale = () => {
        // La ruta del POS que creamos en el paso anterior
        navigate('/add-sale');
    };

    // Nueva función para manejar la devolución
    const handleReturn = (saleId) => {
        navigate(`/return-sale/${saleId}`);
    };

    if (loading) {
        return <div>Cargando ventas...</div>;
    }

    if (error) {
        return <div className="error-message">Error: {error}</div>;
    }

    return (
        <div className="sales-section">
            <div className="section-header">
                <h2>Ventas</h2>
                <button className="btn-new" onClick={handleNewSale}>Registrar nueva venta</button>
            </div>
            <table>
                <thead>
                <tr>
                    <th>ID Venta</th>
                    <th>Cliente</th>
                    <th>Total</th>
                    <th>Fecha</th>
                    <th>Acciones</th> {/* <-- Nueva columna */}
                </tr>
                </thead>
                <tbody>
                {sales.map((sale) => (
                    <tr key={sale.id}>
                        <td>{sale.id}</td>
                        <td>{sale.cliente}</td>
                        {/* Usamos el totalAmount que calcula el backend */}
                        <td>${sale.totalAmount.toFixed(2)}</td>
                        <td>{new Date(sale.fecha).toLocaleString()}</td>
                        <td>
                            {/* <-- Nuevo botón de devolución --> */}
                            <button className="btn-action" onClick={() => handleReturn(sale.id)}>
                                Devolver
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default SalesSection;