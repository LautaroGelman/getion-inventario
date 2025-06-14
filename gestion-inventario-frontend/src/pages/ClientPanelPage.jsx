import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useCashSession } from '../hooks/useCashSession';
import CashSessionModal from '../components/client/CashSessionModal';
import Notifications from '../components/Notifications'; // Ruta corregida
import DashboardSection from '../components/client/DashboardSection';
import InventorySection from '../components/client/InventorySection';
import SalesSection from '../components/client/SalesSection';
import ProvidersSection from '../components/client/ProvidersSection';
import ReportsSection from '../components/client/ReportsSection';
import './ClientPanelPage.css';

function ClientPanelPage() {
    const { user, logout } = useAuth();
    const { session, isModalOpen, modalMode, handleOpenSession, handleCloseSession, showCloseModal, setModalOpen } = useCashSession();
    const [activeSection, setActiveSection] = useState('dashboard');

    const ROLES = {
        ADMINISTRADOR: ['dashboard', 'inventory', 'sales', 'providers', 'reports'],
        CAJERO: ['dashboard', 'sales'],
        MULTIFUNCION: ['dashboard', 'inventory', 'sales', 'providers', 'reports']
    };

    const userCanView = (section) => {
        if (!user || !user.role) return false;
        return ROLES[user.role]?.includes(section);
    };

    // Efecto para asegurar que la sección activa por defecto sea válida para el rol del usuario
    useEffect(() => {
        if (!userCanView(activeSection)) {
            const firstAvailableSection = ROLES[user?.role]?.[0] || 'dashboard';
            setActiveSection(firstAvailableSection);
        }
    }, [user]); // Se ejecuta cuando el usuario carga

    const renderSection = () => {
        switch (activeSection) {
            case 'dashboard':
                return <DashboardSection />;
            case 'inventory':
                return userCanView('inventory') ? <InventorySection /> : null;
            case 'sales':
                return userCanView('sales') ? <SalesSection /> : null;
            case 'providers':
                return userCanView('providers') ? <ProvidersSection /> : null;
            case 'reports':
                return userCanView('reports') ? <ReportsSection /> : null;
            default:
                return <DashboardSection />;
        }
    };

    const isCashierRole = user?.role === 'CAJERO' || user?.role === 'MULTIFUNCION';
    const isAdminRole = user?.role === 'ADMINISTRADOR' || user?.role === 'MULTIFUNCION';

    return (
        <>
            {isModalOpen && isCashierRole && (
                <CashSessionModal
                    mode={modalMode}
                    onOpen={handleOpenSession}
                    onClose={handleCloseSession}
                    onCancel={() => setModalOpen(false)}
                    expectedAmount={session?.expectedAmount}
                />
            )}

            <div className="client-panel">
                <aside className="sidebar">
                    <div className="sidebar-header">
                        <h3>Hola, {user ? user.employeeName : 'Usuario'}</h3>
                        <div className="header-actions">
                            {isAdminRole && <Notifications />}
                        </div>
                    </div>
                    <nav className="sidebar-nav">
                        {userCanView('dashboard') && (
                            <button onClick={() => setActiveSection('dashboard')} className={activeSection === 'dashboard' ? 'active' : ''}>
                                Dashboard
                            </button>
                        )}
                        {userCanView('inventory') && (
                            <button onClick={() => setActiveSection('inventory')} className={activeSection === 'inventory' ? 'active' : ''}>
                                Inventario
                            </button>
                        )}
                        {userCanView('sales') && (
                            <button onClick={() => setActiveSection('sales')} className={activeSection === 'sales' ? 'active' : ''}>
                                Ventas
                            </button>
                        )}
                        {userCanView('providers') && (
                            <button onClick={() => setActiveSection('providers')} className={activeSection === 'providers' ? 'active' : ''}>
                                Proveedores
                            </button>
                        )}
                        {userCanView('reports') && (
                            <button onClick={() => setActiveSection('reports')} className={activeSection === 'reports' ? 'active' : ''}>
                                Reportes
                            </button>
                        )}
                    </nav>
                    <div className="sidebar-footer">
                        {isCashierRole && session && (
                            <button onClick={showCloseModal} className="cash-button">Cerrar Caja</button>
                        )}
                        <button onClick={logout} className="logout-button">Cerrar Sesión</button>
                    </div>
                </aside>
                <main className="main-content">
                    {renderSection()}
                </main>
            </div>
        </>
    );
}

export default ClientPanelPage;