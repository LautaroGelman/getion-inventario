import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import './ArticleFormPage.css';

function ArticleFormPage() {
    const { productId } = useParams();
    const navigate = useNavigate();
    const isEditing = Boolean(productId);

    const [formData, setFormData] = useState({
        code: '',
        name: '',
        description: '',
        stockQuantity: 0,
        cost: 0,
        price: 0,
    });
    const [error, setError] = useState('');

    useEffect(() => {
        if (isEditing) {
            api.get(`/client/products/${productId}`)
                .then(product => {
                    setFormData({
                        code: product.code,
                        name: product.name,
                        description: product.description,
                        stockQuantity: product.stock,
                        cost: product.cost,
                        price: product.price,
                    });
                })
                // --- ¡CORRECCIÓN AQUÍ! ---
                // Usamos el mensaje del error capturado.
                .catch(err => setError(err.message || 'No se pudieron cargar los datos del artículo.'));
        }
    }, [productId, isEditing]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        const payload = {
            ...formData,
            stockQuantity: parseInt(formData.stockQuantity, 10),
            cost: parseFloat(formData.cost),
            price: parseFloat(formData.price),
        };

        try {
            if (isEditing) {
                await api.put(`/client/products/${productId}`, payload);
                alert('Artículo actualizado con éxito');
            } else {
                await api.post('/client/products', payload);
                alert('Artículo creado con éxito');
            }
            navigate('/panel-cliente');
        } catch (err) {
            // --- ¡CORRECCIÓN AQUÍ! ---
            // Usamos el mensaje del error capturado.
            setError(err.message || 'Error al guardar el artículo.');
            console.error(err); // Es una buena práctica mantener el log del error completo en la consola.
        }
    };

    // ... el resto del código JSX del return se mantiene igual ...
    return (
        <div className="container-form">
            <header className="form-header">
                <h1>{isEditing ? 'Editar Artículo' : 'Nuevo Artículo'}</h1>
            </header>
            <main>
                <form id="form-articulo" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="codigo">Código:</label>
                        <input type="text" id="codigo" name="code" value={formData.code} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="nombre">Nombre:</label>
                        <input type="text" id="nombre" name="name" value={formData.name} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="descripcion">Descripción:</label>
                        <textarea id="descripcion" name="description" value={formData.description} onChange={handleChange} rows="3"></textarea>
                    </div>

                    <div className="form-group">
                        <label htmlFor="stock">Stock:</label>
                        <input type="number" id="stock" name="stockQuantity" value={formData.stockQuantity} onChange={handleChange} min="0" required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="costo">Costo:</label>
                        <input type="number" id="costo" name="cost" value={formData.cost} onChange={handleChange} min="0" step="0.01" required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="precio">Precio:</label>
                        <input type="number" id="precio" name="price" value={formData.price} onChange={handleChange} min="0" step="0.01" required />
                    </div>

                    {error && <p className="error-message">{error}</p>}

                    <div className="form-actions">
                        <button type="submit" className="btn-submit">Guardar</button>
                        <button type="button" className="btn-cancel" onClick={() => navigate('/panel-cliente')}>Cancelar</button>
                    </div>
                </form>
            </main>
        </div>
    );
}

export default ArticleFormPage;