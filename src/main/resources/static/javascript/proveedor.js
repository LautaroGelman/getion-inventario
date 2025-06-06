/* ───────── utilidades JWT + CSRF ───────── */
function getCsrf() {
  const p = document.cookie.split("; ").find(v => v.startsWith("XSRF-TOKEN="));
  return p ? p.substring("XSRF-TOKEN=".length) : "";
}

function authHeaders() {
  return {
    "Content-Type":   "application/json",
    "Authorization":  `Bearer ${localStorage.getItem("jwt")}`,
    "X-XSRF-TOKEN":   getCsrf(),
    "X-CSRF-TOKEN":   getCsrf()
  };
}

/* ───────── lógica formulario proveedor ───────── */

let providerId = null;

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("form-proveedor");
  if (!form) return;

  // 1. Comprobar si hay un ID en la URL para modo edición
  const params = new URLSearchParams(window.location.search);
  providerId = params.get("id");

  if (providerId) {
    // --- MODO EDICIÓN ---
    document.querySelector(".form-header h1").textContent = "Editar Proveedor";
    document.querySelector(".btn-submit").textContent = "Guardar Cambios";

    // Buscamos los datos del proveedor en la API
    fetch(`/client/providers/${providerId}`, { headers: authHeaders() })
        .then(res => {
          if (!res.ok) throw new Error("No se pudo cargar el proveedor para editar.");
          return res.json();
        })
        .then(provider => {
          // Rellenamos el formulario
          document.getElementById("name").value = provider.name;
          document.getElementById("contactInfo").value = provider.contactInfo;
          document.getElementById("paymentTerms").value = provider.paymentTerms;
        })
        .catch(err => {
          alert(err.message);
          window.location.href = "panel-cliente.html#proveedores";
        });
  }

  // 2. Manejar el envío del formulario (para CREAR o EDITAR)
  form.addEventListener("submit", async e => {
    e.preventDefault();

    const payload = {
      name: document.getElementById("name").value,
      contactInfo: document.getElementById("contactInfo").value,
      paymentTerms: document.getElementById("paymentTerms").value,
    };

    const method = providerId ? "PUT" : "POST";
    const url = providerId ? `/client/providers/${providerId}` : "/client/providers";
    const successMessage = providerId ? "Proveedor actualizado con éxito" : "Proveedor guardado correctamente";

    try {
      const res = await fetch(url, {
        method: method,
        credentials: "include",
        headers: authHeaders(),
        body: JSON.stringify(payload)
      });

      if (!res.ok) {
        const msg = (await res.text()) || res.statusText;
        throw new Error(`${res.status} – ${msg}`);
      }

      alert(successMessage);
      window.location.href = "panel-cliente.html#proveedores";

    } catch (err) {
      alert("Error al guardar proveedor: " + err.message);
      console.error(err);
    }
  });
});