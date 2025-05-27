// /static/javascript/login.js
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
            credentials: 'include',                 // ← recibe la cookie XSRF-TOKEN
            headers:     { 'Content-Type': 'application/json' },
            body:        JSON.stringify(payload)
        });

        // HTTP-error
        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            throw new Error(err.message || res.statusText);
        }

        // expected: { token: "..." }
        const { token } = await res.json();
        if (!token) throw new Error('Respuesta sin token');

        // Guarda el JWT para el resto de peticiones
        localStorage.setItem('jwt', token);

        // ── Obtiene roles del token ──
        const [, payloadB64] = token.split('.');
        const decoded  = JSON.parse(atob(payloadB64));
        const roles    = decoded.roles || [];

        // ── Redirige según rol ──
        if (roles.includes('ROLE_ADMIN')) {
            location.href = '../html/panel-admin.html';
        } else if (roles.includes('ROLE_CLIENT')) {
            location.href = '../html/panel-cliente.html';
        } else {
            errorEl.textContent = 'Su cuenta no tiene un rol válido.';
            localStorage.removeItem('jwt');
        }

    } catch (err) {
        console.error(err);
        errorEl.textContent = 'Error al iniciar sesión: ' + err.message;
    }
});
