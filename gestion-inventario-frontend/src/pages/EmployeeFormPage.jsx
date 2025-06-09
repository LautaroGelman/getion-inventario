import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api';

function EmployeeFormPage() {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('CASHIER');
    const navigate = useNavigate();
    const { employeeId } = useParams();

    useEffect(() => {
        if (employeeId) {
            api.get(`/client/employees/${employeeId}`).then((data) => {
                setName(data.name);
                setEmail(data.email);
                setRole(data.role);
            });
        }
    }, [employeeId]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const payload = { name, email, role };
        if (password) {
            payload.passwordHash = password;
        }
        if (employeeId) {
            await api.put(`/client/employees/${employeeId}`, payload);
        } else {
            await api.post('/client/employees', payload);
        }

    const handleSubmit = async (e) => {
        e.preventDefault();
        await api.post('/client/employees', { name, email, passwordHash: password, role });
        navigate(-1);
    };

    return (
        <div className="form-container">
            <h2>{employeeId ? 'Editar Empleado' : 'Nuevo Empleado'}</h2>
            <h2>Nuevo Empleado</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Nombre</label>
                    <input value={name} onChange={(e) => setName(e.target.value)} required />
                </div>
                <div>
                    <label>Email</label>
                    <input value={email} onChange={(e) => setEmail(e.target.value)} required />
                </div>
                <div>
                    <label>Contraseña{employeeId ? ' (dejar en blanco para mantener)' : ''}</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required={!employeeId}
                    />
                    <label>Contraseña</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                </div>
                <div>
                    <label>Rol</label>
                    <select value={role} onChange={(e) => setRole(e.target.value)}>
                        <option value="MANAGER">Manager</option>
                        <option value="CASHIER">Cajero</option>
                    </select>
                </div>
                <button type="submit">Guardar</button>
            </form>
        </div>
    );
}

export default EmployeeFormPage;
