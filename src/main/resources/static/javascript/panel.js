/* ─── helpers ──────────────────────────────────────────────── */
function getCsrfCookie() {
    // Devuelve exactamente lo que hay después del '=' (sin decode)
    const pair = document.cookie
        .split("; ")
        .find(v => v.startsWith("XSRF-TOKEN="));
    return pair ? pair.substring("XSRF-TOKEN=".length) : "";
}

/** Devuelve headers con JWT + CSRF (ambos nombres) */
function authHeaders() {
    const csrf = getCsrfCookie();
    return {
        "Authorization": `Bearer ${localStorage.getItem("jwt")}`,
        "X-CSRF-TOKEN":  csrf,
        "X-XSRF-TOKEN": csrf  // compatibilidad con distintos backends
    };
}

/* ───── Carga contadores + tabla ───────────────────────────── */
async function loadDashboard() {
    try {
        const res = await fetch("/admin/clients", {
            credentials: "include",    // para enviar cookies (CSRF, sesión, etc.)
            headers: authHeaders()     // incluye JWT y token CSRF
        });
        if (!res.ok) {
            alert("No se pudieron cargar las cuentas");
            return;
        }
        const dto = await res.json();

        /* 1) Actualizar los contadores */
        document.getElementById("totalAccounts").textContent     = dto.total;
        document.getElementById("freeTrialAccounts").textContent = dto.basico;
        document.getElementById("standardAccounts").textContent  = dto.intermedio;
        document.getElementById("premiumAccounts").textContent   = dto.premium;

        /* 2) Llenar la tabla “Cuentas” */
        const tbody = document.getElementById("accountsTableBody");
        tbody.innerHTML = "";  // Limpia filas anteriores

        dto.clients.forEach(c => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${c.name}</td>
                <td>${c.email}</td>
                <td>${c.telefono ?? ""}</td>
                <td>${c.plan}</td>
                <td class="estado">${c.estado}</td>
                <td>
                  <button class="state-toggle-btn" data-id="${c.id}">
                    ${c.estado === "ACTIVO" ? "Inactivar" : "Activar"}
                  </button>
                </td>`;
            tbody.appendChild(tr);
        });

        /* 3) Asignar listener a cada botón “Activar”/“Inactivar” */
        tbody.querySelectorAll(".state-toggle-btn").forEach(btn => {
            btn.addEventListener("click", async () => {
                const id     = btn.dataset.id;
                const action = btn.textContent === "Inactivar" ? "inactive" : "activate";
                try {
                    const resp = await fetch(`/admin/clients/${id}/${action}`, {
                        method:      "PATCH",
                        credentials: "include",
                        headers:     authHeaders()
                    });
                    if (!resp.ok) throw new Error(await resp.text());

                    // Actualiza el DOM de la fila correspondiente
                    const estadoCell = btn.closest("tr").querySelector(".estado");
                    if (action === "inactive") {
                        estadoCell.textContent = "INACTIVO";
                        btn.textContent = "Activar";
                    } else {
                        estadoCell.textContent = "ACTIVO";
                        btn.textContent = "Inactivar";
                    }
                } catch (e) {
                    alert("No se pudo actualizar estado: " + e.message);
                }
            });
        });
    } catch (error) {
        alert("Error al cargar datos: " + error.message);
    }
}

/* ───── Inicialización DOM ─────────────────────────────────── */
document.addEventListener("DOMContentLoaded", () => {
    // 1) Alternar visibilidad de secciones (Dashboard vs. Cuentas)
    const buttons  = document.querySelectorAll('nav button[data-section]');
    const sections = document.querySelectorAll('.panel-section');

    buttons.forEach(btn =>
        btn.addEventListener("click", () => {
            const sec = btn.getAttribute("data-section");
            sections.forEach(s => {
                s.style.display = (s.id === sec) ? "block" : "none";
            });
        })
    );

    // 2) Botón “Cerrar sesión”
    const logoutBtn = document.getElementById("logout-btn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            window.location.href = "index.html";
        });
    }

    // 3) Botón “Nueva Cuenta” (redirige a form-cliente.html)
    const nuevaBtn = document.getElementById("btn-nueva-cuenta");
    if (nuevaBtn) {
        nuevaBtn.addEventListener("click", () => {
            window.location.href = "form-cliente.html";
        });
    }

    // 4) Carga inicial de datos en “Dashboard”
    loadDashboard();
});
