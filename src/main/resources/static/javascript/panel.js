document.addEventListener('DOMContentLoaded', () => {
    console.log("Panel.js cargado");

    // Alternar secciones desde la barra de navegación
    const sectionButtons = document.querySelectorAll('nav button[data-section]');
    const sections = document.querySelectorAll('.panel-section');
    sectionButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const target = btn.getAttribute('data-section');
            sections.forEach(s => {
                s.style.display = (s.id === target) ? 'block' : 'none';
            });
            // Si activamos sección 'cuentas', cargamos datos de cuentas
            if (target === 'cuentas') loadCuentas();
        });
    });

    // Logout (redireccionar a login)
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            window.location.href = 'index.html';
        });
    }

    // Funciones de redirección para botones estáticos
    const nuevoArticuloBtn = document.getElementById('btn-nuevo-articulo');
    if (nuevoArticuloBtn) {
        nuevoArticuloBtn.addEventListener('click', () => {
            window.location.href = 'form-articulo.html';
        });
    }

    const nuevaVentaBtn = document.getElementById('btn-nueva-venta');
    if (nuevaVentaBtn) {
        nuevaVentaBtn.addEventListener('click', () => {
            window.location.href = 'form-venta.html';
        });
    }

    const nuevaCuentaBtn = document.getElementById('btn-nueva-cuenta');
    if (nuevaCuentaBtn) {
        nuevaCuentaBtn.addEventListener('click', () => {
            window.location.href = 'form-cliente.html';
        });
    }

    // Carga inicial de cuentas si estamos en panel-admin y sección cuentas visible
    if (document.getElementById('cuentas') && document.getElementById('cuentas').style.display !== 'none') {
        loadCuentas();
    }
});

/**
 * Trae la lista de cuentas y las renderiza en la tabla con acciones.
 */
async function loadCuentas() {
    try {
        // Llamada a API para obtener cuentas
        const cuentas = await apiFetch('/admin/cuentas');
        const table = document.querySelector('#cuentas table');
        // Limpiar tbody si existe
        let tbody = table.querySelector('tbody');
        if (!tbody) {
            tbody = document.createElement('tbody');
            table.appendChild(tbody);
        }
        tbody.innerHTML = '';
        cuentas.forEach(c => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
        <td>${c.nombre}</td>
        <td>${c.correo}</td>
        <td>${c.telefono}</td>
        <td>${c.plan}</td>
        <td>${c.estado}</td>
        <td class="acciones">
          <button class="edit-btn" data-id="${c.id}">Editar</button>
          <button class="delete-btn" data-id="${c.id}">Eliminar</button>
        </td>`;
            tbody.appendChild(tr);
        });
        attachCuentaActions();
    } catch (err) {
        console.error('Error cargando cuentas:', err);
    }
}

/**
 * Agrega los event listeners a los botones Editar y Eliminar.
 */
function attachCuentaActions() {
    // Editar
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const id = btn.getAttribute('data-id');
            window.location.href = `form-edit-cliente.html?id=${id}`;
        });
    });
    // Eliminar
    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            const id = btn.getAttribute('data-id');
            if (confirm('¿Seguro que quieres eliminar esta cuenta?')) {
                try {
                    await apiFetch(`/admin/cuentas/${id}`, { method: 'DELETE' });
                    loadCuentas(); // recargar lista
                } catch (err) {
                    console.error('Error eliminando cuenta:', err);
                }
            }
        });
    });
}
