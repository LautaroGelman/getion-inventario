import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import './ArticleFormPage.css';

function EndCustomerFormPage() {
    const { customerId } = useParams();
    const navigate = useNavigate();
    const isEditing = Boolean(customerId);

    const [formData, setFormData] = useState({
        name: '',
        contactInfo: ''
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(isEditing);

    useEffect(() => {
        if (isEditing) {
            api.get(`/client/end-customers/${customerId}`)
                .then(data => setFormData({ name: data.name, contactInfo: data.contactInfo }))
                .catch(err => setError(err.message || 'No se pudieron cargar los datos'))
                .finally(() => setLoading(false));
        }
    }, [customerId, isEditing]);

    const handleChange = e => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async e => {
        e.preventDefault();
        setError('');
        try {
            if (isEditing) {
                await api.put(`/client/end-customers/${customerId}`, formData);
            } else {
                await api.post('/client/end-customers', formData);
            }
            navigate('/panel-cliente#clientes');
        } catch (err) {
            setError(err.message || 'Error al guardar');
        }
    };

    if (loading) return <div>Cargando formulario...</div>;

    return (
        <div className="container-form">
            <header className="form-header">
                <h1>{isEditing ? 'Editar Cliente' : 'Nuevo Cliente'}</h1>
            </header>
            <main>
                <form id="form-end-customer" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="name">Nombre:</label>
                        <input id="name" name="name" value={formData.name} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label htmlFor="contactInfo">Contacto:</label>
                        <textarea id="contactInfo" name="contactInfo" rows="3" value={formData.contactInfo} onChange={handleChange}></textarea>
                    </div>
                    {error && <p className="error-message">{error}</p>}
                    <div className="form-actions">
                        <button type="submit" className="btn-submit">Guardar</button>
                        <button type="button" className="btn-cancel" onClick={() => navigate('/panel-cliente#clientes')}>Cancelar</button>
                    </div>
                </form>
            </main>
        </div>
    );
}

export default EndCustomerFormPage;
