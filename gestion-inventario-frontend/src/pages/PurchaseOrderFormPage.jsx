import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import './ArticleFormPage.css';

function PurchaseOrderFormPage() {
    const navigate = useNavigate();

    const [providers, setProviders] = useState([]);
    const [products, setProducts] = useState([]);
    const [items, setItems] = useState([
        { productId: '', quantity: 1, cost: 0, tax: 0, discount: 0 }
    ]);
    const [providerId, setProviderId] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [prov, prod] = await Promise.all([
                    api.get('/client/providers'),
                    api.get('/client/items')
                ]);
                setProviders(prov);
                setProducts(prod);
            } catch (err) {
                setError(err.message || 'No se pudieron cargar datos');
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    const handleItemChange = (index, field, value) => {
        setItems(prev => prev.map((it, i) => i === index ? { ...it, [field]: value } : it));
    };

    const addItem = () => {
        setItems(prev => [...prev, { productId: '', quantity: 1, cost: 0, tax: 0, discount: 0 }]);
    };

    const removeItem = (index) => {
        setItems(prev => prev.filter((_, i) => i !== index));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        const clientId = localStorage.getItem('clientId');
        const payload = {
            providerId: parseInt(providerId, 10),
            clientId: clientId ? parseInt(clientId, 10) : null,
            items: items.map(it => ({
                productId: parseInt(it.productId, 10),
                quantity: parseInt(it.quantity, 10),
                cost: parseFloat(it.cost),
                tax: parseFloat(it.tax),
                discount: parseFloat(it.discount)
            }))
        };
        try {
            await api.post('/purchase-orders', payload);
            alert('Orden de compra creada con éxito');
            navigate('/panel-cliente#compras');
        } catch (err) {
            setError(err.message || 'Error al crear la orden');
        }
    };

    if (loading) {
        return <div>Cargando formulario...</div>;
    }

    return (
        <div className="container-form">
            <header className="form-header">
                <h1>Nueva Orden de Compra</h1>
            </header>
            <main>
                <form id="form-orden-compra" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="provider">Proveedor:</label>
                        <select id="provider" value={providerId} onChange={e => setProviderId(e.target.value)} required>
                            <option value="">Seleccione...</option>
                            {providers.map(p => (
                                <option key={p.id} value={p.id}>{p.name}</option>
                            ))}
                        </select>
                    </div>
                    {items.map((item, idx) => (
                        <div key={idx} className="form-group">
                            <label>Producto:</label>
                            <select value={item.productId} onChange={e => handleItemChange(idx, 'productId', e.target.value)} required>
                                <option value="">Seleccione...</option>
                                {products.map(pr => (
                                    <option key={pr.id} value={pr.id}>{pr.name}</option>
                                ))}
                            </select>
                            <input type="number" min="1" value={item.quantity} onChange={e => handleItemChange(idx, 'quantity', e.target.value)} />
                            <input type="number" min="0" step="0.01" value={item.cost} onChange={e => handleItemChange(idx, 'cost', e.target.value)} />
                            <input type="number" min="0" step="0.01" value={item.tax} onChange={e => handleItemChange(idx, 'tax', e.target.value)} placeholder="IVA" />
                            <input type="number" min="0" step="0.01" value={item.discount} onChange={e => handleItemChange(idx, 'discount', e.target.value)} placeholder="Descuento" />
                            {items.length > 1 && <button type="button" className="btn-delete" onClick={() => removeItem(idx)}>Eliminar</button>}
                        </div>
                    ))}
                    <div className="form-actions">
                        <button type="button" className="btn-new" onClick={addItem}>Agregar Ítem</button>
                    </div>
                    {error && <p className="error-message">{error}</p>}
                    <div className="form-actions">
                        <button type="submit" className="btn-submit">Guardar</button>
                        <button type="button" className="btn-cancel" onClick={() => navigate('/panel-cliente#compras')}>Cancelar</button>
                    </div>
                </form>
            </main>
        </div>
    );
}

export default PurchaseOrderFormPage;
