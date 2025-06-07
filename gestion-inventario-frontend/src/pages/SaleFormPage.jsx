import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import './SaleFormPage.css'; // Crearemos este archivo para los estilos

function SaleFormPage() {
    const navigate = useNavigate();

    // Estados para el formulario
    const [articles, setArticles] = useState([]);
    const [selectedArticleId, setSelectedArticleId] = useState('');
    const [quantity, setQuantity] = useState(1);
    const [paymentMethod, setPaymentMethod] = useState('');
    const [saleDate, setSaleDate] = useState('');

    // Estados para carga y errores
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    // Cargar los artículos para el menú desplegable cuando el componente se monta
    useEffect(() => {
        api.get('/client/items')
            .then(data => {
                setArticles(data);
                setLoading(false);
            })
            .catch(err => {
                setError('No se pudieron cargar los artículos. ' + err.message);
                setLoading(false);
            });
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!selectedArticleId || !quantity || !paymentMethod) {
            setError('Por favor, completa todos los campos.');
            return;
        }

        const selectedArticle = articles.find(a => a.id === parseInt(selectedArticleId, 10));

        const payload = {
            paymentMethod: paymentMethod,
            // Si la fecha está vacía, enviamos null para que el backend use la fecha actual
            saleDate: saleDate ? `${saleDate}T12:00:00` : null,
            items: [
                {
                    productId: selectedArticle.id,
                    quantity: parseInt(quantity, 10),
                    unitPrice: selectedArticle.price,
                },
            ],
        };

        try {
            await api.post('/client/sales', payload);
            alert('Venta registrada con éxito');
            navigate('/panel-cliente'); // Vuelve al panel principal
        } catch (err) {
            setError(err.message || 'Error al registrar la venta.');
        }
    };

    if (loading) {
        return <div>Cargando...</div>;
    }

    return (
        <div className="container-form">
            <header className="form-header">
                <h1>Registrar Nueva Venta</h1>
            </header>
            <main>
                <form id="form-venta" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="articulo">Artículo:</label>
                        <select id="articulo" name="articulo" required value={selectedArticleId} onChange={e => setSelectedArticleId(e.target.value)}>
                            <option value="">Seleccione...</option>
                            {articles.map(it => (
                                <option key={it.id} value={it.id}>
                                    {it.name} (stock: {it.stock})
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="cantidad">Cantidad:</label>
                        <input type="number" id="cantidad" name="cantidad" min="1" required value={quantity} onChange={e => setQuantity(e.target.value)} />
                    </div>

                    <div className="form-group">
                        <label htmlFor="metodo-pago">Método de Pago:</label>
                        <select id="metodo-pago" name="metodo-pago" required value={paymentMethod} onChange={e => setPaymentMethod(e.target.value)}>
                            <option value="">Seleccione...</option>
                            <option value="efectivo">Efectivo</option>
                            <option value="tarjeta">Tarjeta</option>
                            <option value="transferencia">Transferencia</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="date">Fecha (opcional):</label>
                        <input type="date" id="date" name="fecha-venta" value={saleDate} onChange={e => setSaleDate(e.target.value)} />
                    </div>

                    {error && <p className="error-message">{error}</p>}

                    <div className="form-actions">
                        <button type="submit" className="btn-submit">Registrar Venta</button>
                        <button type="button" className="btn-cancel" onClick={() => navigate('/panel-cliente')}>Cancelar</button>
                    </div>
                </form>
            </main>
        </div>
    );
}

export default SaleFormPage;