import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
import './SalesSection.css'; // Crearemos este archivo para los estilos

function SalesSection() {
    const [sales, setSales] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchSales = async () => {
            try {
                setLoading(true);
                const data = await api.get('/client/sales');
                setSales(data);
            } catch (err) {
                setError(err.message || 'No se pudo cargar el historial de ventas.');
            } finally {
                setLoading(false);
            }
        };

        fetchSales();
    }, []);

    const handleNewSale = () => {
        navigate('/form-venta'); // Iremos a esta ruta en el siguiente paso
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
                    <th>Cliente</th>
                    <th>Comprador</th>
                    <th>Artículo</th>
                    <th>Cantidad</th>
                    <th>Precio Total</th>
                    <th>Método de pago</th>
                    <th>Fecha</th>
                </tr>
                </thead>
                <tbody>
                {sales.map((sale, index) => (
                    <tr key={index}> {/* Usamos index como key si no hay un ID único por venta en el DTO */}
                        <td>{sale.clientName}</td>
                        <td>{sale.endCustomerName}</td>
                        <td>{sale.itemName}</td>
                        <td>{sale.quantity}</td>
                        <td>${sale.price}</td>
                        <td>{sale.paymentMethod}</td>
                        <td>{new Date(sale.createdAt).toLocaleDateString()}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default SalesSection;