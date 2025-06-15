import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
// Usaremos los mismos estilos que la sección de inventario para consistencia
import './InventorySection.css';

function ProvidersSection() {
    const [providers, setProviders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProviders = async () => {
            try {
                setLoading(true);
                // Asumiendo que la API estará en /client/providers
                const data = await api.get('/client/providers');
                setProviders(data);
            } catch (err) {
                setError(err.message || 'No se pudo cargar la lista de proveedores.');
            } finally {
                setLoading(false);
            }
        };
        fetchProviders();
    }, []);

    const handleDelete = async (providerId) => {
        if (window.confirm('¿Estás seguro de que quieres eliminar este proveedor?')) {
            try {
                await api.delete(`/client/providers/${providerId}`);
                setProviders(currentProviders => currentProviders.filter(p => p.id !== providerId));
            } catch (err) {
                alert('Error al eliminar el proveedor: ' + (err.message || 'Error desconocido.'));
            }
        }
    };

    const handleEdit = (providerId) => {
        navigate(`/form-proveedor/${providerId}`);
    };

    const handleNew = () => {
        navigate('/form-proveedor');
    };

    if (loading) {
        return <div>Cargando proveedores...</div>;
    }

    if (error) {
        return <div className="error-message">Error: {error}</div>;
    }

    return (
        <div className="inventory-section"> {/* Reutilizamos la clase para estilos */}
            <div className="section-header">
                <h2>Proveedores</h2>
                <button className="btn-new" onClick={handleNew}>Nuevo Proveedor</button>
            </div>
            <table>
                <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Contacto</th>
                    <th>Términos de Pago</th>
                    <th>Fecha de Alta</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                {providers.map(p => (
                    <tr key={p.id}>
                        <td>{p.name}</td>
                        <td>{p.contactInfo}</td>
                        <td>{p.paymentTerms}</td>
                        <td>{new Date(p.createdAt).toLocaleDateString()}</td>
                        <td>
                            <button className="btn-edit" onClick={() => handleEdit(p.id)}>Editar</button>
                            <button className="btn-delete" onClick={() => handleDelete(p.id)}>Eliminar</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default ProvidersSection;