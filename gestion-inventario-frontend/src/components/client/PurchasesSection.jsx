import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
import './InventorySection.css';

function PurchasesSection() {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const clientId = localStorage.getItem('clientId');
        if (!clientId) {
            setError('Cliente no autenticado');
            setLoading(false);
            return;
        }
        api.get(`/clients/${clientId}/purchase-orders`)
            .then(data => setOrders(data))
            .catch(err => setError(err.message || 'No se pudo cargar las órdenes'))
            .finally(() => setLoading(false));
    }, []);

    const handleNew = () => {
        navigate('/form-orden-compra');
    };

    const handleView = (id) => {
        navigate(`/detalle-orden-compra/${id}`);
    };

    if (loading) {
        return <div>Cargando órdenes de compra...</div>;
    }

    if (error) {
        return <div className="error-message">Error: {error}</div>;
    }

    return (
        <div className="inventory-section">
            <div className="section-header">
                <h2>Órdenes de Compra</h2>
                <button className="btn-new" onClick={handleNew}>Nueva Orden</button>
            </div>
            <table>
                <thead>
                <tr>
                    <th>Proveedor</th>
                    <th>Estado</th>
                    <th>Fecha</th>
                    <th>Costo Total</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                {orders.map(o => (
                    <tr key={o.id}>
                        <td>{o.provider.name}</td>
                        <td>{o.status}</td>
                        <td>{new Date(o.orderDate).toLocaleDateString()}</td>
                        <td>${o.totalCost}</td>
                        <td>
                            <button className="btn-edit" onClick={() => handleView(o.id)}>Ver</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default PurchasesSection;
