/* ─── utilidades JWT + CSRF ─────────────────────────────────── */
function getCsrf() {
    const p = document.cookie.split("; ")
        .find(v => v.startsWith("XSRF-TOKEN="));
    return p ? p.substring("XSRF-TOKEN=".length) : "";
}
function authHeaders() {
    return {
        "Content-Type" : "application/json",
        "Authorization": `Bearer ${localStorage.getItem("jwt")}`,
        "X-XSRF-TOKEN" : getCsrf(),
        "X-CSRF-TOKEN" : getCsrf()
    };
}

/* ─── lógica del formulario ────────────────────────────────── */
document.addEventListener("DOMContentLoaded", () => {
    const form  = document.getElementById("clienteForm");
    const plano = window.location.pathname.endsWith("form-cliente.html");

    if (!form) return;      // seguridad: si alguien mete el script en otra página

    form.addEventListener("submit", async e => {
        e.preventDefault();

        const data = Object.fromEntries(new FormData(form).entries());

        try {
            const res = await fetch("/admin/clients", {
                method      : "POST",
                credentials : "include",
                headers     : authHeaders(),
                body        : JSON.stringify(data)
            });

            if (!res.ok) {
                const msg = (await res.text()) || res.statusText;
                throw new Error(`${res.status} – ${msg}`);
            }

            alert("Cliente creado con éxito");
            // Vuelves al panel de cuentas
            window.location.href = "panel-admin.html#cuentas";

        } catch (err) {
            alert("Error al crear cliente: " + err.message);
            console.error(err);
        }
    });
});
