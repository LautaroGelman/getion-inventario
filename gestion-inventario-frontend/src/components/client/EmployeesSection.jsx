import React, { useEffect, useState } from 'react';
import { api } from '../../services/api';

function EmployeesSection() {
    const [employees, setEmployees] = useState([]);

    useEffect(() => {
        async function load() {
            try {
                const data = await api.get('/client/employees');
                setEmployees(data);
            } catch (err) {
                console.error(err);
            }
        }
        load();
    }, []);

    return (
        <div>
            <h2>Gestión de Empleados</h2>
            <ul>
                {employees.map(e => (
                    <li key={e.id}>{e.name} - {e.role}</li>
                ))}
            </ul>
        </div>
    );
}

export default EmployeesSection;
