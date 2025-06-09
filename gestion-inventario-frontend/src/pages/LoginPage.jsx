// gestion-inventario-frontend/src/pages/LoginPage.jsx

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode'; // <-- PASO 1: Importamos la librería
import { api } from '../services/api';
import './LoginPage.css';

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false); // Añadido para feedback al usuario
    const navigate = useNavigate();

    /**
     * Función que maneja el envío del formulario.
     * Orquesta los intentos de login secuencialmente.
     */
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        // Intenta primero el login como Super Admin. Si tiene éxito, la función retorna.
        const superAdminSuccess = await trySuperAdminLogin();
        if (superAdminSuccess) {
            setIsLoading(false);
            return;
        }

        // Si el login de Super Admin falla, intenta como Empleado.
        const employeeSuccess = await tryEmployeeLogin();
        if (employeeSuccess) {
            setIsLoading(false);
            return;
        }

        // Si ambos intentos fallan, mostramos un error genérico.
        setError('Usuario o contraseña incorrectos. Por favor, verifique sus credenciales.');
        setIsLoading(false);
        localStorage.clear(); // Limpiamos cualquier dato residual
    };

    /**
     * Intenta autenticar como SUPER_ADMIN.
     * @returns {boolean} - true si el login es exitoso, false si falla.
     */
    const trySuperAdminLogin = async () => {
        try {
            const response = await api.post('/api/superpanel/auth/login', { username, password });

            // --- MEJORA 1: Accedemos a response.data ---
            // Axios envuelve la respuesta en una propiedad 'data'. Aquí está el JSON del backend.
            const { token, roles } = response.data;

            if (token && roles && roles.includes('SUPER_ADMIN')) {
                // Login exitoso como Super Admin
                localStorage.setItem('jwt', token);
                localStorage.setItem('role', 'SUPER_ADMIN');
                localStorage.removeItem('clientId'); // Un Super Admin no tiene clientId
                navigate('/panel-admin');
                return true;
            }
            return false;
        } catch (error) {
            // Si hay un error (ej. 401 Unauthorized), simplemente lo ignoramos y retornamos false
            // para que el flujo principal continúe con el siguiente intento de login.
            console.log("Intento de login como Super Admin falló (esperado si no es Super Admin):", error.message);
            return false;
        }
    };

    /**
     * Intenta autenticar como Empleado (ADMIN/CASHIER).
     * @returns {boolean} - true si el login es exitoso, false si falla.
     */
    const tryEmployeeLogin = async () => {
        try {
            const response = await api.post('/api/auth/login', { username, password });

            // --- MEJORA 1 (de nuevo): Accedemos a response.data ---
            const { token, roles } = response.data;

            if (token && roles && (roles.includes('ADMIN') || roles.includes('CASHIER'))) {
                // --- MEJORA 2: Decodificación segura con jwt-decode ---
                // Usamos la librería para parsear el token de forma segura y robusta.
                const decodedToken = jwtDecode(token);

                localStorage.setItem('jwt', token);
                localStorage.setItem('role', roles[0]); // Guardamos el primer rol válido

                if (decodedToken.clientId) {
                    localStorage.setItem('clientId', decodedToken.clientId);
                }

                navigate('/panel-cliente');
                return true;
            }
            return false;
        } catch (error) {
            console.log("Intento de login como Empleado falló:", error.message);
            return false;
        }
    };

    return (
        <div className="login-page-container">
            <div className="login-form-container">
                <h2>Comercializa S.A.</h2>
                <form id="loginForm" onSubmit={handleSubmit}>
                    <div className="input-group">
                        <label htmlFor="username">Usuario</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            placeholder="Ingrese su usuario"
                            required
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            disabled={isLoading} // Deshabilitamos el input mientras carga
                        />
                    </div>
                    <div className="input-group">
                        <label htmlFor="password">Contraseña</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            placeholder="Ingrese su contraseña"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            disabled={isLoading} // Deshabilitamos el input mientras carga
                        />
                    </div>
                    <button type="submit" disabled={isLoading}>
                        {isLoading ? 'Iniciando...' : 'Iniciar Sesión'}
                    </button>
                    {error && <p id="error-message" className="error-msg">{error}</p>}
                </form>
            </div>
        </div>
    );
}

export default LoginPage;