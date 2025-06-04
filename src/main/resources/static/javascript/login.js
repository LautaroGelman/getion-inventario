// /static/javascript/login.js

document.getElementById('loginForm').addEventListener('submit', async e => {
    e.preventDefault();

    const userEl  = document.getElementById('username');
    const passEl  = document.getElementById('password');
    const errorEl = document.getElementById('error-message');
    errorEl.textContent = '';

    const payload = {
        username: userEl.value.trim(),
        password: passEl.value
    };

    try {
        const res = await fetch('http://localhost:8080/auth/login', {
            method:      'POST',
            credentials: 'include',                 // recibe la cookie XSRF-TOKEN
            headers:     { 'Content-Type': 'application/json' },
            body:        JSON.stringify(payload)
        });

        // Si HTTP no es OK, mostramos error
        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            throw new Error(err.message || res.statusText);
        }

        // Esperamos respuesta: { token: "..." }
        const { token } = await res.json();
        if (!token) throw new Error('Respuesta sin token');

        // 1) Guardamos el JWT
        localStorage.setItem('jwt', token);

        // 2) Decodificamos el payload del JWT para extraer clientId y roles
        const [, payloadB64] = token.split('.');
        // Ajuste para Base64URL a Base64 normal
        const padded = payloadB64.replace(/-/g, '+').replace(/_/g, '/')
            + '=='.slice(0, (4 - payloadB64.length % 4) % 4);
        const decoded = JSON.parse(atob(padded));

        // 3) Extraemos clientId del payload (asegúrate de que tu backend lo incluya con ese nombre)
        //    Si en tu JWT el claim se llama distinto (e.g. "id" o "userId"), cámbialo aquí.
        const clientId = decoded.clientId;
        if (clientId) {
            localStorage.setItem('clientId', clientId);
        } else {
            // Si no viene como "clientId", revisa el nombre del claim en tu JWT y ajústalo.
            // Por ejemplo: const clientId = decoded.id;  o decoded.userId;
            console.warn('No se encontró clientId en el JWT; verifica que el token incluya ese claim.');
        }

        // 4) Obtenemos los roles del token
        const roles = decoded.roles || [];

        // 5) Redirigimos según rol
        if (roles.includes('ROLE_ADMIN')) {
            location.href = '../html/panel-admin.html';
        } else if (roles.includes('ROLE_CLIENT')) {
            location.href = '../html/panel-cliente.html';
        } else {
            errorEl.textContent = 'Su cuenta no tiene un rol válido.';
            // Como no redirigimos, limpiamos el JWT
            localStorage.removeItem('jwt');
            localStorage.removeItem('clientId');
        }

    } catch (err) {
        console.error(err);
        errorEl.textContent = 'Error al iniciar sesión: ' + err.message;
    }
});
