import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import './ClientFormPage.css'; // Crearemos este archivo para los estilos

function ClientFormPage() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        telefono: '',
        plan: 'BASICO', // Valor por defecto
        estado: 'ACTIVO', // Valor por defecto
    });
    const [error, setError] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        if (!formData.password) {
            setError('La contraseña es obligatoria para nuevos clientes.');
            return;
        }

        try {
            await api.post('/admin/clients', formData);
            alert('Cliente creado con éxito');
            // Navegamos de vuelta a la sección 'Cuentas' del panel de admin
            navigate('/panel-admin#cuentas');
        } catch (err) {
            setError(err.message || 'Error al crear el cliente.');
        }
    };

    return (
        <div className="client-form-page-container">
            <main className="main-content">
                <header><h1>Registro de Cliente</h1></header>
                <form id="clienteForm" onSubmit={handleSubmit}>
                    <label htmlFor="name">Nombre completo</label>
                    <input type="text" id="name" name="name" onChange={handleChange} value={formData.name} required />

                    <label htmlFor="email">Correo electrónico</label>
                    <input type="email" id="email" name="email" onChange={handleChange} value={formData.email} required />

                    <label htmlFor="password">Contraseña</label>
                    <input type="password" id="password" name="password" onChange={handleChange} value={formData.password} required />

                    <label htmlFor="telefono">Teléfono</label>
                    <input type="text" id="telefono" name="telefono" onChange={handleChange} value={formData.telefono} />

                    <label htmlFor="plan">Plan asignado</label>
                    <select id="plan" name="plan" onChange={handleChange} value={formData.plan}>
                        <option value="BASICO">Básico</option>
                        <option value="INTERMEDIO">Intermedio</option>
                        <option value="PREMIUM">Premium</option>
                    </select>

                    <label htmlFor="estado">Estado</label>
                    <select id="estado" name="estado" onChange={handleChange} value={formData.estado}>
                        <option value="ACTIVO">Activo</option>
                        <option value="INACTIVO">Inactivo</option>
                    </select>

                    {error && <p className="error-message">{error}</p>}
                    <div className="form-actions">
                        <button type="submit">Guardar Cliente</button>
                        <button type="button" onClick={() => navigate('/panel-admin#cuentas')}>Cancelar</button>
                    </div>
                </form>
            </main>
        </div>
    );
}

export default ClientFormPage;