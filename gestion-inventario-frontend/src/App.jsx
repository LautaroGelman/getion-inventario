import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import ClientPanelPage from './pages/ClientPanelPage';
import ArticleFormPage from './pages/ArticleFormPage';
import SaleFormPage from './pages/SaleFormPage';
import ReturnFormPage from './pages/ReturnFormPage'; // <-- IMPORTAMOS LA NUEVA PÁGINA
import ProtectedRoute from './components/ProtectedRoute';

function App() {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/" element={<LoginPage />} />
                    <Route path="/login" element={<LoginPage />} />

                    {/* Rutas Protegidas */}
                    <Route path="/client-panel" element={<ProtectedRoute><ClientPanelPage /></ProtectedRoute>} />
                    <Route path="/add-article" element={<ProtectedRoute><ArticleFormPage /></ProtectedRoute>} />
                    <Route path="/edit-article/:id" element={<ProtectedRoute><ArticleFormPage /></ProtectedRoute>} />
                    <Route path="/add-sale" element={<ProtectedRoute><SaleFormPage /></ProtectedRoute>} />
                    {/* AÑADIMOS LA NUEVA RUTA DE DEVOLUCIONES */}
                    <Route path="/return-sale/:saleId" element={<ProtectedRoute><ReturnFormPage /></ProtectedRoute>} />

                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;