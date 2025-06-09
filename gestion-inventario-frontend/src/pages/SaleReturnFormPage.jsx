import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import './SaleFormPage.css';

function SaleReturnFormPage() {
    const navigate = useNavigate();
    const [articles, setArticles] = useState([]);
    const [selectedArticleId, setSelectedArticleId] = useState('');
    const [quantity, setQuantity] = useState(1);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        api.get('/client/items')
            .then(data => setArticles(data))
            .catch(err => setError(err.message || 'No se pudieron cargar los productos'))
            .finally(() => setLoading(false));
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        const article = articles.find(a => a.id === parseInt(selectedArticleId, 10));
        if (!article) {
            setError('Debes seleccionar un producto');
            return;
        }
        const payload = {
            items: [
                {
                    productId: article.id,
                    quantity: parseInt(quantity, 10),
                    unitPrice: article.price
                }
            ]
        };
        try {
            await api.post('/client/sale-returns', payload);
            alert('Devolución registrada');
            navigate('/panel-cliente#ventas');
        } catch (err) {
            setError(err.message || 'Error al registrar la devolución');
        }
    };

    if (loading) {
        return <div>Cargando...</div>;
    }

    return (
        <div className="container-form">
            <header className="form-header">
                <h1>Registrar Devolución</h1>
            </header>
            <main>
                <form id="form-venta" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="articulo">Artículo:</label>
                        <select id="articulo" value={selectedArticleId} onChange={e => setSelectedArticleId(e.target.value)} required>
                            <option value="">Seleccione...</option>
                            {articles.map(a => (
                                <option key={a.id} value={a.id}>{a.name}</option>
                            ))}
                        </select>
                    </div>
                    <div className="form-group">
                        <label htmlFor="cantidad">Cantidad:</label>
                        <input type="number" id="cantidad" min="1" value={quantity} onChange={e => setQuantity(e.target.value)} required />
                    </div>
                    {error && <p className="error-message">{error}</p>}
                    <div className="form-actions">
                        <button type="submit" className="btn-submit">Registrar</button>
                        <button type="button" className="btn-cancel" onClick={() => navigate('/panel-cliente#ventas')}>Cancelar</button>
                    </div>
                </form>
            </main>
        </div>
    );
}

export default SaleReturnFormPage;
