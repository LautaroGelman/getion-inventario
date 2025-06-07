import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../services/api';
// Reutilizaremos los estilos del formulario de artículos
import './ArticleFormPage.css';

function ProviderFormPage() {
    const { providerId } = useParams();
    const navigate = useNavigate();
    const isEditing = Boolean(providerId);

    const [formData, setFormData] = useState({
        name: '',
        contactInfo: '',
        paymentTerms: '',
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(isEditing);

    useEffect(() => {
        if (isEditing) {
            api.get(`/client/providers/${providerId}`)
                .then(provider => {
                    setFormData({
                        name: provider.name,
                        contactInfo: provider.contactInfo,
                        paymentTerms: provider.paymentTerms,
                    });
                })
                .catch(err => setError('No se pudieron cargar los datos del proveedor.'))
                .finally(() => setLoading(false));
        }
    }, [providerId, isEditing]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        try {
            if (isEditing) {
                await api.put(`/client/providers/${providerId}`, formData);
                alert('Proveedor actualizado con éxito');
            } else {
                await api.post('/client/providers', formData);
                alert('Proveedor creado con éxito');
            }
            // Navegamos de vuelta a la sección correcta usando el HASH
            navigate('/panel-cliente#proveedores');
        } catch (err) {
            setError(err.message || 'Error al guardar el proveedor.');
        }
    };

    if (loading) {
        return <div>Cargando formulario...</div>;
    }

    return (
        <div className="container-form">
            <header className="form-header">
                <h1>{isEditing ? 'Editar Proveedor' : 'Nuevo Proveedor'}</h1>
            </header>
            <main>
                <form id="form-proveedor" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="name">Nombre:</label>
                        <input type="text" id="name" name="name" value={formData.name} onChange={handleChange} required />
                    </div>
                    <div className="form-group">
                        <label htmlFor="contactInfo">Información de Contacto:</label>
                        <textarea id="contactInfo" name="contactInfo" value={formData.contactInfo} onChange={handleChange} rows="4"></textarea>
                    </div>
                    <div className="form-group">
                        <label htmlFor="paymentTerms">Términos de Pago:</label>
                        <textarea id="paymentTerms" name="paymentTerms" value={formData.paymentTerms} onChange={handleChange} rows="4"></textarea>
                    </div>
                    {error && <p className="error-message">{error}</p>}
                    <div className="form-actions">
                        <button type="submit" className="btn-submit">Guardar</button>
                        <button type="button" className="btn-cancel" onClick={() => navigate('/panel-cliente#proveedores')}>Cancelar</button>
                    </div>
                </form>
            </main>
        </div>
    );
}

export default ProviderFormPage;