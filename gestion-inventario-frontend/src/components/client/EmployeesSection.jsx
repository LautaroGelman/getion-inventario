import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../../services/api';

function EmployeesSection() {
    const [employees, setEmployees] = useState([]);

    useEffect(() => {
        const load = async () => {
            const data = await api.get('/client/employees');
            setEmployees(data);
        };
        load();
    }, []);

    return (
        <div>
            <h2>Gestión de Empleados</h2>
            <Link to="/empleados/nuevo">Nuevo Empleado</Link>
            <ul>
                {employees.map((e) => (
                    <li key={e.id}>{e.name} - {e.role}</li>
                ))}
            </ul>
        </div>
    );
}

export default EmployeesSection;
