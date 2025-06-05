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

/* ───────── lógica formulario venta ───────── */
document.addEventListener("DOMContentLoaded", () => {
  const form    = document.getElementById("form-venta");
  const selectA = document.getElementById("articulo");

  if (!form || !selectA) return;

  // 1) Cargar artículos en el <select>
  fetch("/client/items", { credentials: "include", headers: authHeaders() })
      .then(res => {
        if (!res.ok) throw new Error("No se pudo cargar inventario");
        return res.json();
      })
      .then(items => {
        items.forEach(it => {
          const opt = document.createElement("option");
          opt.value             = it.code;
          opt.dataset.productId = it.id;
          opt.dataset.unitPrice = it.price;
          opt.textContent       = `${it.name} (stock: ${it.stock})`;
          selectA.appendChild(opt);
        });
      })
      .catch(err => {
        alert(err.message);
        console.error(err);
      });

  // 2) Manejar envío de la venta
  form.addEventListener("submit", async e => {
    e.preventDefault();
    const data = new FormData(form);
    const selected = selectA.selectedOptions[0];

    // --- LEEMOS LA FECHA DEL FORMULARIO ---
    // (Asegúrate de que tu input de fecha en el HTML tenga name="fecha-venta")
    const saleDateValue = data.get("fecha-venta");

    const payload = {
      paymentMethod: data.get("metodo-pago"),
      // --- AÑADIMOS LA FECHA AL PAYLOAD ---
      // Si el campo de fecha no está vacío, lo enviamos en formato ISO (YYYY-MM-DDTHH:MM:SS).
      // Si está vacío, enviamos null y el backend usará la fecha actual.
      saleDate: saleDateValue ? (saleDateValue + "T12:00:00") : null,
      items: [
        {
          productId: parseInt(selected.dataset.productId, 10),
          quantity:  parseInt(data.get("cantidad"), 10),
          unitPrice: parseFloat(selected.dataset.unitPrice)
        }
      ]
    };
    console.log("Payload enviado:", JSON.stringify(payload, null, 2));

    try {
      const res = await fetch("/client/sales", {
        method:      "POST",
        credentials: "include",
        headers:     authHeaders(),
        body:        JSON.stringify(payload)
      });
      if (!res.ok) {
        const msg = (await res.text()) || res.statusText;
        throw new Error(`${res.status} – ${msg}`);
      }
      alert("Venta registrada con éxito");
      form.reset();
      localStorage.setItem("reloadInventory", "true");
      window.location.href = "panel-cliente.html#inventario";
    } catch (err) {
      alert("Error al registrar venta: " + err.message);
      console.error(err);
    }
  });
});