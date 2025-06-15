import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import './ReturnFormPage.css';

function ReturnFormPage() {
    const { saleId } = useParams();
    const navigate = useNavigate();
    const [sale, setSale] = useState(null);
    const [returnItems, setReturnItems] = useState({}); // Objeto para manejar las cantidades a devolver
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchSaleDetails = async () => {
            try {
                // Suponiendo que tienes un endpoint para obtener una venta por ID
                const response = await api.get(`/api/sales/${saleId}`);
                setSale(response.data);
            } catch (err) {
                setError('No se pudieron cargar los detalles de la venta.');
            } finally {
                setLoading(false);
            }
        };
        fetchSaleDetails();
    }, [saleId]);

    const handleQuantityChange = (productId, quantity) => {
        const maxQuantity = sale.items.find(item => item.product.id === productId).cantidad;
        const newQuantity = Math.max(0, Math.min(quantity, maxQuantity)); // No permitir más de lo comprado ni menos de 0
        setReturnItems(prev => ({
            ...prev,
            [productId]: newQuantity,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const itemsToReturn = Object.entries(returnItems)
            .filter(([, quantity]) => quantity > 0)
            .map(([productId, quantity]) => ({
                productId: parseInt(productId),
                quantity,
            }));

        if (itemsToReturn.length === 0) {
            alert('Por favor, selecciona la cantidad de al menos un producto a devolver.');
            return;
        }

        const returnRequest = {
            originalSaleId: saleId,
            items: itemsToReturn,
        };

        try {
            await api.post('/api/returns', returnRequest);
            alert('Devolución procesada con éxito.');
            navigate('/client-panel');
        } catch (err) {
            setError(err.response?.data?.message || 'Error al procesar la devolución.');
        }
    };

    if (loading) return <div>Cargando...</div>;
    if (error) return <div className="error-message">{error}</div>;
    if (!sale) return <div>Venta no encontrada.</div>;

    return (
        <div className="return-form-container">
            <h2>Procesar Devolución de Venta #{sale.id}</h2>
            <p><strong>Cliente:</strong> {sale.cliente}</p>
            <p><strong>Fecha de Venta:</strong> {new Date(sale.fecha).toLocaleString()}</p>

            <form onSubmit={handleSubmit}>
                <table className="return-table">
                    <thead>
                    <tr>
                        <th>Producto</th>
                        <th>Cantidad Comprada</th>
                        <th>Cantidad a Devolver</th>
                    </tr>
                    </thead>
                    <tbody>
                    {sale.items.map(item => (
                        <tr key={item.product.id}>
                            <td>{item.product.nombre}</td>
                            <td>{item.cantidad}</td>
                            <td>
                                <input
                                    type="number"
                                    min="0"
                                    max={item.cantidad}
                                    value={returnItems[item.product.id] || 0}
                                    onChange={(e) => handleQuantityChange(item.product.id, parseInt(e.target.value, 10))}
                                />
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <button type="submit" className="submit-button">Confirmar Devolución</button>
            </form>
        </div>
    );
}

export default ReturnFormPage;