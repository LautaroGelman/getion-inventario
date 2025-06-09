import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom'; // <-- AÑADIMOS useLocation
import './ClientPanelPage.css';
import DashboardSection from '../components/client/DashboardSection';
import InventorySection from '../components/client/InventorySection';
import SalesSection from '../components/client/SalesSection';
import ProvidersSection from '../components/client/ProvidersSection';
import PurchasesSection from '../components/client/PurchasesSection';
import CustomersSection from '../components/client/CustomersSection';

function ClientPanelPage() {
    const navigate = useNavigate();
    const location = useLocation(); // Hook para leer la info de la URL actual

    // La sección activa ahora se determina por el HASH de la URL, o es 'dashboard' por defecto
    const [activeSection, setActiveSection] = useState(location.hash.replace('#', '') || 'dashboard');

    // Este useEffect se ejecutará cada vez que el HASH de la URL cambie
    useEffect(() => {
        const currentHash = location.hash.replace('#', '');
        setActiveSection(currentHash || 'dashboard');
    }, [location.hash]);


    const handleLogout = () => {
        localStorage.removeItem('jwt');
        localStorage.removeItem('clientId');
        navigate('/login');
    };

    // Función para cambiar de sección. Ahora cambia la URL.
    const handleSectionChange = (section) => {
        navigate(`#${section}`);
    };

    const renderSection = () => {
        switch (activeSection) {
            case 'inventario':
                return <InventorySection />;
            case 'ventas':
                return <SalesSection />;
            case 'compras':
                return <PurchasesSection />;
            case 'clientes':
                return <CustomersSection />;
            case 'proveedores':
                return <ProvidersSection />;
            case 'dashboard':
            default:
                return <DashboardSection />;
        }
    };

    return (
        <div className="client-panel-container">
            <header className="panel-header">
                <h1>Panel Cliente</h1>
                <nav>
                    {/* Los botones ahora llaman a handleSectionChange para actualizar la URL */}
                    <button onClick={() => handleSectionChange('dashboard')}>Dashboard</button>
                    <button onClick={() => handleSectionChange('inventario')}>Inventario</button>
                    <button onClick={() => handleSectionChange('ventas')}>Ventas</button>
                    <button onClick={() => handleSectionChange('compras')}>Compras</button>
                    <button onClick={() => handleSectionChange('clientes')}>Clientes</button>
                    <button onClick={() => handleSectionChange('proveedores')}>Proveedores</button>
                    <button id="logout-btn" onClick={handleLogout}>Cerrar sesión</button>
                </nav>
            </header>

            <main className="panel-main-content">
                {renderSection()}
            </main>
        </div>
    );
}

export default ClientPanelPage;