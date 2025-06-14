// Lee la URL base de la API desde las variables de entorno
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

/**
 * Función principal para realizar peticiones a la API.
 * Maneja la autenticación y el formato de las respuestas.
 * @param {string} endpoint - El endpoint de la API al que se llamará (ej. '/auth/login').
 * @param {string} method - El método HTTP ('GET', 'POST', 'PUT', 'DELETE', etc.).
 * @param {object} [body=null] - El cuerpo de la petición para POST, PUT, etc.
 * @returns {Promise<any>} - La respuesta de la API en formato JSON.
 */
async function apiFetch(endpoint, method, body = null) {
    const headers = {
        'Content-Type': 'application/json',
    };

    // Obtenemos el token del localStorage
    const token = localStorage.getItem('jwt');
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    // Obtenemos el token CSRF de las cookies si existe
    const csrfToken = getCsrfCookie();
    if (csrfToken) {
        headers['X-XSRF-TOKEN'] = csrfToken;
    }

    const config = {
        method: method,
        headers: headers,
    };

    if (body) {
        config.body = JSON.stringify(body);
    }

    try {
        // --- ¡CORRECCIÓN IMPORTANTE AQUÍ! ---
        // Asegúrate de que la siguiente línea use BACKTICKS (`) y no comillas simples (')
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || `Error ${response.status}: ${response.statusText}`);
        }

        if (response.status === 204) {
            return null;
        }

        return await response.json();

    } catch (error) {
        console.error('Error en la petición API:', error.message);
        throw error;
    }
}

function getCsrfCookie() {
    const p = document.cookie.split("; ").find(v => v.startsWith("XSRF-TOKEN="));
    return p ? p.substring("XSRF-TOKEN=".length) : "";
}

export const api = {
    get: (endpoint) => apiFetch(endpoint, 'GET'),
    post: (endpoint, body) => apiFetch(endpoint, 'POST', body),
    put: (endpoint, body) => apiFetch(endpoint, 'PUT', body),
    patch: (endpoint, body) => apiFetch(endpoint, 'PATCH', body), // <-- LÍNEA AÑADIDA
    delete: (endpoint) => apiFetch(endpoint, 'DELETE'),
};