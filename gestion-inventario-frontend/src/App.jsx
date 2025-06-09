import { Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import ProtectedRoute from './components/ProtectedRoute';
import ClientPanelPage from './pages/ClientPanelPage';
import ArticleFormPage from './pages/ArticleFormPage';
import SaleFormPage from './pages/SaleFormPage';
import ProviderFormPage from './pages/ProviderFormPage';
import AdminPanelPage from './pages/AdminPanelPage';
import ClientFormPage from "./pages/ClientFormPage.jsx"; // <-- IMPORTAMOS
import EndCustomerFormPage from './pages/EndCustomerFormPage';
import PurchaseOrderFormPage from './pages/PurchaseOrderFormPage';
import PurchaseOrderDetailPage from './pages/PurchaseOrderDetailPage';
import EmployeeFormPage from './pages/EmployeeFormPage';

function App() {
    return (
        <Routes>
            <Route path="/" element={<LoginPage />} />
            <Route path="/login" element={<LoginPage />} />

            <Route path="/panel-cliente" element={<ProtectedRoute><ClientPanelPage /></ProtectedRoute>} />
            <Route path="/form-articulo" element={<ProtectedRoute><ArticleFormPage /></ProtectedRoute>} />
            <Route path="/form-articulo/:productId" element={<ProtectedRoute><ArticleFormPage /></ProtectedRoute>} />
            <Route path="/form-venta" element={<ProtectedRoute><SaleFormPage /></ProtectedRoute>} />
            <Route path="/form-proveedor" element={<ProtectedRoute><ProviderFormPage /></ProtectedRoute>} />
            <Route path="/form-proveedor/:providerId" element={<ProtectedRoute><ProviderFormPage /></ProtectedRoute>} />
            {/* --- NUEVA RUTA PARA EL FORMULARIO DE CLIENTE --- */}
            <Route path="/form-cliente" element={<ProtectedRoute><ClientFormPage /></ProtectedRoute>} />
            <Route path="/form-end-customer" element={<ProtectedRoute><EndCustomerFormPage /></ProtectedRoute>} />
            <Route path="/form-end-customer/:customerId" element={<ProtectedRoute><EndCustomerFormPage /></ProtectedRoute>} />
            <Route path="/form-orden-compra" element={<ProtectedRoute><PurchaseOrderFormPage /></ProtectedRoute>} />
            <Route path="/detalle-orden-compra/:purchaseOrderId" element={<ProtectedRoute><PurchaseOrderDetailPage /></ProtectedRoute>} />
            <Route path="/empleados/nuevo" element={<ProtectedRoute><EmployeeFormPage /></ProtectedRoute>} />
            {/* --- RUTA ACTUALIZADA PARA EL PANEL DE ADMIN --- */}
            <Route path="/panel-admin" element={<ProtectedRoute><AdminPanelPage /></ProtectedRoute>} />

            <Route path="*" element={<div>Error 404: Página no encontrada</div>} />
        </Routes>
    );
}

export default App;
