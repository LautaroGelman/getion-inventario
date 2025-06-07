import React from 'react';
import { Navigate } from 'react-router-dom';

/**
 * Componente para proteger rutas.
 * Verifica si existe un 'jwt' en localStorage.
 * Si no existe, redirige al usuario a la página de login.
 * Si existe, renderiza el componente hijo que se le pasa.
 * @param {{ children: React.ReactNode }} props
 */
function ProtectedRoute({ children }) {
    const token = localStorage.getItem('jwt');

    if (!token) {
        // Si no hay token, redirige a la página de login
        return <Navigate to="/login" replace />;
    }

    // Si hay token, muestra el contenido de la ruta protegida
    return children;
}

export default ProtectedRoute;