import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import './SaleFormPage.css'; // Crearemos este archivo para los estilos

function SaleFormPage() {
    const navigate = useNavigate();

    // Estados para el formulario
    const [articles, setArticles] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [selectedCustomerId, setSelectedCustomerId] = useState('');
    const [items, setItems] = useState([
        { productId: '', quantity: 1, tax: 0, discount: 0 }
    ]);
    const [paymentMethod, setPaymentMethod] = useState('');
    const [saleDate, setSaleDate] = useState('');

    // Estados para carga y errores
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    // Cargar los artículos para el menú desplegable cuando el componente se monta
    useEffect(() => {
        const fetchData = async () => {
            try {
                const [itemsData, custData] = await Promise.all([
                    api.get('/client/items'),
                    api.get('/client/end-customers')
                ]);
                setArticles(itemsData);
                setCustomers(custData);
            } catch (err) {
                setError('No se pudieron cargar los datos. ' + err.message);
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
        setItems(prev => [...prev, { productId: '', quantity: 1, tax: 0, discount: 0 }]);
    };

    const removeItem = (index) => {
        setItems(prev => prev.filter((_, i) => i !== index));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!paymentMethod || !selectedCustomerId || items.some(it => !it.productId || !it.quantity)) {
            setError('Por favor, completa todos los campos.');
            return;
        }

        const payload = {
            paymentMethod: paymentMethod,
            endCustomerId: parseInt(selectedCustomerId, 10),
            saleDate: saleDate ? `${saleDate}T12:00:00` : null,
            items: items.map(it => {
                const art = articles.find(a => a.id === parseInt(it.productId, 10));
                return {
                    productId: parseInt(it.productId, 10),
                    quantity: parseInt(it.quantity, 10),
                    unitPrice: art ? art.price : 0,
                    tax: parseFloat(it.tax),
                    discount: parseFloat(it.discount),
                };
            })
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
                    {items.map((item, idx) => (
                        <div key={idx} className="form-group">
                            <label>Artículo:</label>
                            <select value={item.productId} onChange={e => handleItemChange(idx, 'productId', e.target.value)} required>
                                <option value="">Seleccione...</option>
                                {articles.map(it => (
                                    <option key={it.id} value={it.id}>
                                        {it.name} (stock: {it.stock})
                                    </option>
                                ))}
                            </select>
                            <input type="number" min="1" value={item.quantity} onChange={e => handleItemChange(idx, 'quantity', e.target.value)} />
                            <input type="number" min="0" step="0.01" value={item.tax} onChange={e => handleItemChange(idx, 'tax', e.target.value)} placeholder="IVA" />
                            <input type="number" min="0" step="0.01" value={item.discount} onChange={e => handleItemChange(idx, 'discount', e.target.value)} placeholder="Descuento" />
                            {items.length > 1 && <button type="button" className="btn-delete" onClick={() => removeItem(idx)}>Eliminar</button>}
                        </div>
                    ))}
                    <div className="form-actions">
                        <button type="button" className="btn-new" onClick={addItem}>Agregar Ítem</button>
                    </div>

                    <div className="form-group">
                        <label htmlFor="customer">Cliente:</label>
                        <select id="customer" required value={selectedCustomerId} onChange={e => setSelectedCustomerId(e.target.value)}>
                            <option value="">Seleccione...</option>
                            {customers.map(c => (
                                <option key={c.id} value={c.id}>{c.name}</option>
                            ))}
                        </select>
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