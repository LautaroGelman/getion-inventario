import React from 'react';
import { Route, Routes } from 'react-router-dom'; // Eliminado 'BrowserRouter as Router'
import { AuthProvider } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import ClientPanelPage from './pages/ClientPanelPage';
import AdminPanelPage from './pages/AdminPanelPage'; // Añadido: Asumiendo que esta página existe y se usará en futuras rutas
import ArticleFormPage from './pages/ArticleFormPage';
import ProviderFormPage from './pages/ProviderFormPage'; // Añadido: Asumiendo que esta página existe y se usará en futuras rutas
import SaleFormPage from './pages/SaleFormPage';
import ReturnFormPage from './pages/ReturnFormPage';
import ClientFormPage from './pages/ClientFormPage'; // Añadido: Asumiendo que esta página existe y se usará en futuras rutas
import ProtectedRoute from './components/ProtectedRoute';

function App() {
    return (
        <AuthProvider>
            {/* Eliminado <Router>, ya está en main.jsx */}
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                {/* La ruta raíz '/' debería ser la página principal después del login, posiblemente protegida */}
                <Route path="/" element={<ProtectedRoute allowedRoles={['ADMINISTRADOR', 'CAJERO', 'MULTIFUNCION']}><ClientPanelPage /></ProtectedRoute>} />

                {/* Rutas Protegidas del Client Panel */}
                {/* La ruta de ClientPanelPage ya está en "/", así que "/client-panel" podría ser redundante a menos que tengas un caso de uso específico para ello. */}
                {/* Si necesitas una ruta explícita para el panel del cliente, asegúrate de que no cause conflictos con la raíz. Por ahora, lo dejo para ilustrar, pero la raíz ya lo maneja. */}
                <Route path="/client-panel" element={<ProtectedRoute allowedRoles={['ADMINISTRADOR', 'CAJERO', 'MULTIFUNCION']}><ClientPanelPage /></ProtectedRoute>} />

                <Route path="/inventory-form/:id?" element={<ProtectedRoute allowedRoles={['ADMINISTRADOR', 'MULTIFUNCION']}><ArticleFormPage /></ProtectedRoute>} />
                <Route path="/provider-form/:id?" element={<ProtectedRoute allowedRoles={['ADMINISTRADOR', 'MULTIFUNCION']}><ProviderFormPage /></ProtectedRoute>} /> {/* Añadida ruta para proveedores */}
                <Route path="/sale-form" element={<ProtectedRoute allowedRoles={['ADMINISTRADOR', 'CAJERO', 'MULTIFUNCION']}><SaleFormPage /></ProtectedRoute>} />
                <Route path="/return-sale/:saleId" element={<ProtectedRoute allowedRoles={['ADMINISTRADOR', 'CAJERO', 'MULTIFUNCION']}><ReturnFormPage /></ProtectedRoute>} />

                {/* Rutas Protegidas del Admin Panel (Super_Admin) */}
                <Route path="/admin" element={<ProtectedRoute allowedRoles={['SUPER_ADMIN']}><AdminPanelPage /></ProtectedRoute>} />
                <Route path="/register-client" element={<ProtectedRoute allowedRoles={['SUPER_ADMIN']}><ClientFormPage /></ProtectedRoute>} />
                {/* Puedes añadir más rutas de administración aquí */}

            </Routes>
        </AuthProvider>
    );
}

export default App;