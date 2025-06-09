import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
import './InventorySection.css';

function InventorySection() {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    // --- NUEVO: Estado para el término de búsqueda ---
    const [searchTerm, setSearchTerm] = useState('');

    const fetchItems = async () => {
        try {
            setLoading(true);
            const data = await api.get('/client/items');
            setItems(data);
        } catch (err) {
            setError(err.message || 'No se pudo cargar el inventario.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchItems();
    }, []);

    const handleDelete = async (productId) => {
        if (window.confirm('¿Estás seguro de que quieres eliminar el producto?')) {
            try {
                await api.delete(`/client/products/${productId}`);
                setItems(currentItems => currentItems.filter(item => item.id !== productId));
            } catch (err) {
                alert('Error al eliminar el producto: ' + (err.message || 'Error desconocido.'));
            }
        }
    };

    const handleEdit = (productId) => {
        navigate(`/form-articulo/${productId}`);
    };

    const handleNew = () => {
        navigate('/form-articulo');
    };

    // --- NUEVO: Lógica de filtrado ---
    // Filtramos la lista de items basándonos en el searchTerm
    // Se busca tanto en el nombre como en el código del producto.
    const filteredItems = items.filter(item =>
        item.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        item.code.toLowerCase().includes(searchTerm.toLowerCase())
    );

    if (loading) {
        return <div>Cargando inventario...</div>;
    }

    if (error) {
        return <div className="error-message">Error: {error}</div>;
    }

    return (
        <div className="inventory-section">
            <div className="section-header">
                <h2>Inventario</h2>
                <button className="btn-new" onClick={handleNew}>Nuevo artículo</button>
            </div>

            {/* --- NUEVO: Barra de búsqueda --- */}
            <div className="search-bar-container">
                <input
                    type="text"
                    placeholder="Buscar por nombre o código..."
                    className="search-input"
                    value={searchTerm}
                    onChange={e => setSearchTerm(e.target.value)}
                />
            </div>

            <table>
                <thead>
                <tr>
                    <th>Código</th>
                    <th>Nombre</th>
                    <th>Descripción</th>
                    <th>Stock</th>
                    <th>Precio</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                {/* --- MODIFICADO: Mapeamos sobre la lista filtrada --- */}
                {filteredItems.map(it => (
                    <tr key={it.id}>
                        <td>{it.code}</td>
                        <td>{it.name}</td>
                        <td>{it.description}</td>
                        <td>{it.stock}</td>
                        <td>${it.price}</td>
                        <td>
                            <button className="btn-edit" onClick={() => handleEdit(it.id)}>Editar</button>
                            <button className="btn-delete" onClick={() => handleDelete(it.id)}>Eliminar</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default InventorySection;