import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api'; // Importamos nuestro cliente de API
import './LoginPage.css'; // Crearemos este archivo para los estilos

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(''); // Limpiamos errores anteriores

        try {
            // Usamos nuestro cliente API para hacer la petición POST
            const response = await api.post('/auth/login', { username, password });
            const { token, role } = response;

            if (!token) {
                throw new Error('No se recibió un token del servidor.');
            }

            // Guardamos el JWT y el rol en el almacenamiento local
            localStorage.setItem('jwt', token);
            if (role) {
                localStorage.setItem('role', role);
            }

            // Decodificamos el token para obtener el clientId
            const [, payloadB64] = token.split('.');
            const padded = payloadB64.replace(/-/g, '+').replace(/_/g, '/') + '=='.slice(0, (4 - payloadB64.length % 4) % 4);
            const decoded = JSON.parse(atob(padded));

            const clientId = decoded.clientId;

            // Guardamos el clientId si existe
            if (clientId) {
                localStorage.setItem('clientId', clientId);
            }

            // Redirigimos según el rol del usuario
            if (role === 'ADMIN') {
                navigate('/panel-admin');
            } else if (role === 'CASHIER' || role === 'ADMIN') {
                navigate('/panel-cliente');
            } else {
                setError('Su cuenta no tiene un rol válido.');
                localStorage.removeItem('jwt'); // Limpiamos el token si no hay rol
                localStorage.removeItem('clientId');
            }

        } catch (err) {
            // Si la API devuelve un error, lo mostramos
            setError(err.message || 'Error al iniciar sesión. Revisa tus credenciales.');
            console.error(err);
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
                        />
                    </div>
                    <button type="submit">Iniciar Sesión</button>
                    {error && <p id="error-message" className="error-msg">{error}</p>}
                </form>
            </div>
        </div>
    );
}

export default LoginPage;