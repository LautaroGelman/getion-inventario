import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import './ArticleFormPage.css';

function PurchaseOrderDetailPage() {
    const { purchaseOrderId } = useParams();
    const navigate = useNavigate();
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        api.get(`/purchase-orders/${purchaseOrderId}`)
            .then(data => setOrder(data))
            .catch(err => setError(err.message || 'No se pudo cargar la orden'))
            .finally(() => setLoading(false));
    }, [purchaseOrderId]);

    const handleReceive = async () => {
        try {
            await api.post(`/purchase-orders/${purchaseOrderId}/receive`);
            const updated = await api.get(`/purchase-orders/${purchaseOrderId}`);
            setOrder(updated);
            alert('Orden recibida');
        } catch (err) {
            alert(err.message || 'Error al recibir la orden');
        }
    };

    if (loading) {
        return <div>Cargando detalle...</div>;
    }

    if (error) {
        return <div className="error-message">Error: {error}</div>;
    }

    if (!order) {
        return <div>No encontrado</div>;
    }

    return (
        <div className="container-form">
            <header className="form-header">
                <h1>Detalle Orden #{order.id}</h1>
            </header>
            <main>
                <p>Proveedor: {order.provider.name}</p>
                <p>Estado: {order.status}</p>
                <p>Fecha: {new Date(order.orderDate).toLocaleDateString()}</p>
                {order.receptionDate && <p>Recibida: {new Date(order.receptionDate).toLocaleDateString()}</p>}
                <table>
                    <thead>
                        <tr>
                            <th>Producto</th>
                            <th>Cantidad</th>
                            <th>Costo</th>
                        </tr>
                    </thead>
                    <tbody>
                        {order.items.map(it => (
                            <tr key={it.id}>
                                <td>{it.product.name}</td>
                                <td>{it.quantity}</td>
                                <td>${it.cost}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                {order.status === 'PENDING' && (
                    <div className="form-actions">
                        <button className="btn-submit" onClick={handleReceive}>Marcar como Recibida</button>
                    </div>
                )}
                <div className="form-actions">
                    <button className="btn-cancel" onClick={() => navigate('/panel-cliente#compras')}>Volver</button>
                </div>
            </main>
        </div>
    );
}

export default PurchaseOrderDetailPage;
