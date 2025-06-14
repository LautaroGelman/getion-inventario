import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
import './AccountsSection.css';

// El componente ahora recibe 'initialAccounts' como una prop
function AccountsSection({ initialAccounts, onUpdate }) {
    const [accounts, setAccounts] = useState(initialAccounts);
    const navigate = useNavigate();

    // useEffect para actualizar la lista si la prop cambia
    useEffect(() => {
        setAccounts(initialAccounts);
    }, [initialAccounts]);


    const handleToggleStatus = async (accountId, currentStatus) => {
        const action = currentStatus === 'ACTIVO' ? 'inactive' : 'activate';
        try {
            await api.patch(`/admin/clients/${accountId}/${action}`, {});
            // En lugar de modificar el estado local, llamamos a la función onUpdate del padre
            onUpdate();
        } catch (err) {
            alert(`Error al cambiar el estado: ${err.message}`);
        }
    };

    // No necesitamos los estados de loading/error aquí porque el padre los maneja

    return (
        <div className="accounts-section">
            <div className="section-header">
                <h2>Cuentas</h2>
                <button className="btn-new" onClick={() => navigate('/form-cliente')}>Nueva Cuenta</button>
            </div>
            <table>
                <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Correo</th>
                    <th>Teléfono</th>
                    <th>Plan</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                {accounts.map(acc => (
                    <tr key={acc.id}>
                        <td>{acc.name}</td>
                        <td>{acc.email}</td>
                        <td>{acc.telefono ?? ''}</td>
                        <td>{acc.plan}</td>
                        <td>
                <span className={`status-badge status-${acc.estado.toLowerCase()}`}>
                  {acc.estado}
                </span>
                        </td>
                        <td>
                            <button
                                className="btn-toggle-status"
                                onClick={() => handleToggleStatus(acc.id, acc.estado)}
                            >
                                {acc.estado === 'ACTIVO' ? 'Inactivar' : 'Activar'}
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default AccountsSection;