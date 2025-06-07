import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './AdminPanelPage.css';
import AccountsSection from '../components/admin/AccountsSection';
import AdminDashboard from '../components/admin/AdminDashboard'; // <-- IMPORTAMOS
import { api } from '../services/api'; // <-- IMPORTAMOS LA API

function AdminPanelPage() {
    const navigate = useNavigate();
    const location = useLocation();

    const [activeSection, setActiveSection] = useState(location.hash.replace('#', '') || 'dashboard');

    // Estados para los datos, carga y error
    const [clientData, setClientData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    // Función para cargar o recargar todos los datos
    const fetchData = () => {
        setLoading(true);
        api.get('/admin/clients')
            .then(data => {
                setClientData(data);
            })
            .catch(err => {
                setError(err.message || 'Error al cargar datos del panel.');
            })
            .finally(() => setLoading(false));
    };

    // Cargar datos al montar el componente
    useEffect(() => {
        fetchData();
    }, []);

    // Actualizar la sección activa cuando cambia el hash
    useEffect(() => {
        const currentHash = location.hash.replace('#', '');
        setActiveSection(currentHash || 'dashboard');
    }, [location.hash]);

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    const handleSectionChange = (section) => {
        navigate(`#${section}`);
    };

    const renderSection = () => {
        if (loading) return <div>Cargando...</div>;
        if (error) return <div style={{color: 'red'}}>Error: {error}</div>;
        if (!clientData) return <div>No hay datos disponibles.</div>;

        switch (activeSection) {
            case 'cuentas':
                // Pasamos la lista de clientes y la función de recarga como props
                return <AccountsSection initialAccounts={clientData.clients} onUpdate={fetchData} />;
            case 'dashboard':
            default:
                // Pasamos el objeto completo de datos al dashboard
                return <AdminDashboard dashboardData={clientData} />;
        }
    };

    return (
        <div className="admin-container">
            <aside className="sidebar">
                <h2>Panel Admin</h2>
                <nav>
                    <button onClick={() => handleSectionChange('dashboard')}>Dashboard</button>
                    <button onClick={() => handleSectionChange('cuentas')}>Cuentas</button>
                </nav>
                <button id="logout-btn-admin" onClick={handleLogout}>Cerrar sesión</button>
            </aside>
            <main className="main-content">
                {renderSection()}
            </main>
        </div>
    );
}

export default AdminPanelPage;