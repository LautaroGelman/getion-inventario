import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
import './InventorySection.css';

function CustomersSection() {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchCustomers = async () => {
            try {
                setLoading(true);
                const data = await api.get('/client/end-customers');
                setCustomers(data);
            } catch (err) {
                setError(err.message || 'No se pudo cargar la lista de clientes.');
            } finally {
                setLoading(false);
            }
        };
        fetchCustomers();
    }, []);

    const handleDelete = async (id) => {
        if (window.confirm('¿Eliminar cliente?')) {
            try {
                await api.delete(`/client/end-customers/${id}`);
                setCustomers(list => list.filter(c => c.id !== id));
            } catch (err) {
                alert(err.message || 'Error al eliminar');
            }
        }
    };

    const handleEdit = (id) => {
        navigate(`/form-end-customer/${id}`);
    };

    const handleNew = () => {
        navigate('/form-end-customer');
    };

    if (loading) return <div>Cargando clientes...</div>;
    if (error) return <div className="error-message">Error: {error}</div>;

    return (
        <div className="inventory-section">
            <div className="section-header">
                <h2>Clientes</h2>
                <button className="btn-new" onClick={handleNew}>Nuevo Cliente</button>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Contacto</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {customers.map(c => (
                        <tr key={c.id}>
                            <td>{c.name}</td>
                            <td>{c.contactInfo}</td>
                            <td>
                                <button className="btn-edit" onClick={() => handleEdit(c.id)}>Editar</button>
                                <button className="btn-delete" onClick={() => handleDelete(c.id)}>Eliminar</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default CustomersSection;
