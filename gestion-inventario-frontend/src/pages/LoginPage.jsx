import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext'; // <-- El cambio clave: importamos useAuth
import './LoginPage.css';

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { login } = useAuth(); // <-- Obtenemos la función login de nuestro contexto

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        try {
            // La llamada a la API es la misma
            const response = await api.post('/api/auth/login', { username, password });

            // Esperamos una respuesta como { "token": "ey..." }
            const token = response.data.token;

            if (!token) {
                throw new Error('No se recibió un token del servidor.');
            }

            // !! TODA LA LÓGICA COMPLEJA SE REEMPLAZA POR ESTA LÍNEA !!
            // La función login() del contexto se encarga de decodificar,
            // guardar en localStorage y actualizar el estado de la app.
            login(token);

            // Redirigimos al panel principal. El panel decidirá qué mostrar.
            navigate('/client-panel');

        } catch (err) {
            // Mantenemos tu excelente manejo de errores
            setError(err.response?.data?.message || 'Error al iniciar sesión. Revisa tus credenciales.');
            console.error(err);
        }
    };

    return (
        // Tu JSX se mantiene exactamente igual, no es necesario cambiarlo.
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