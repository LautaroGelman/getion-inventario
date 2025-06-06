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

// Variable global para saber si estamos en modo edición y cuál es el ID
let productId = null;

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("form-articulo");
  if (!form) return;

  // 1. Comprobar si hay un ID en la URL
  const params = new URLSearchParams(window.location.search);
  productId = params.get("id");

  if (productId) {
    // --- MODO EDICIÓN ---
    // Cambiamos los textos de la página
    document.querySelector(".form-header h1").textContent = "Editar Artículo";
    document.querySelector(".btn-submit").textContent = "Guardar Cambios";

    // Buscamos los datos del producto en la API
    fetch(`/client/products/${productId}`, { headers: authHeaders() })
        .then(res => {
          if (!res.ok) {
            throw new Error("No se pudo cargar el artículo para editar.");
          }
          return res.json();
        })
        .then(product => {
          // Rellenamos el formulario con los datos recibidos
          document.getElementById("codigo").value = product.code;
          document.getElementById("nombre").value = product.name;
          document.getElementById("descripcion").value = product.description;
          document.getElementById("stock").value = product.stock;
          document.getElementById("costo").value = product.cost;
          document.getElementById("precio").value = product.price;
        })
        .catch(err => {
          alert(err.message);
          window.location.href = "panel-cliente.html#inventario"; // Volvemos si hay error
        });
  }

  // 2. Manejar el envío del formulario (para CREAR o EDITAR)
  form.addEventListener("submit", async e => {
    e.preventDefault();

    const data = new FormData(form);
    const payload = {
      code:              data.get("codigo"),
      name:              data.get("nombre"),
      description:       data.get("descripcion"),
      stockQuantity:     parseInt(data.get("stock"), 10),
      cost:              parseFloat(data.get("costo")),
      price:             parseFloat(data.get("precio")),
      lowStockThreshold: 0
    };

    // Determinamos el método y la URL según si estamos en modo edición o no
    const method = productId ? "PUT" : "POST";
    const url = productId ? `/client/products/${productId}` : "/client/products";
    const successMessage = productId ? "Artículo actualizado con éxito" : "Artículo guardado correctamente";

    try {
      const res = await fetch(url, {
        method:      method,
        credentials: "include",
        headers:     authHeaders(),
        body:        JSON.stringify(payload)
      });

      if (!res.ok) {
        const msg = (await res.text()) || res.statusText;
        throw new Error(`${res.status} – ${msg}`);
      }

      alert(successMessage);
      form.reset();
      window.location.href = "panel-cliente.html#inventario";

    } catch (err) {
      alert("Error al guardar artículo: " + err.message);
      console.error(err);
    }
  });
});