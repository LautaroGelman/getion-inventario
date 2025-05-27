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

/* ───────── lógica formulario artículo ───────── */
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("form-articulo");
  if (!form) return;

  form.addEventListener("submit", async e => {
    e.preventDefault();

    const data = new FormData(form);
    const payload = {
      code:              data.get("codigo"),
      name:              data.get("nombre"),
      description:       data.get("descripcion"),
      stockQuantity:     parseInt(data.get("stock"), 10),
      price:             parseFloat(data.get("precio")),
      lowStockThreshold: 0
    };

    try {
      const res = await fetch("/products", {
        method:      "POST",
        credentials: "include",
        headers:     authHeaders(),
        body:        JSON.stringify(payload)
      });
      if (!res.ok) {
        const msg = (await res.text()) || res.statusText;
        throw new Error(`${res.status} – ${msg}`);
      }
      alert("Artículo guardado correctamente");
      form.reset();
      // Volvemos al panel de inventario
      window.location.href = "panel-cliente.html#inventario";
    } catch (err) {
      alert("Error al guardar artículo: " + err.message);
      console.error(err);
    }
  });
});

