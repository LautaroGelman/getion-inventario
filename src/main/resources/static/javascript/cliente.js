/* ──────────────── utilidades ──────────────── */
function getCookie(name) {
  const pair = document.cookie
      .split("; ")
      .find(row => row.startsWith(name + "="));
  return pair ? decodeURIComponent(pair.split("=")[1]) : null;
}

function authHeaders() {
  const csrf = getCookie("XSRF-TOKEN");
  const jwt  = localStorage.getItem("jwt");
  return {
    "Content-Type":   "application/json",
    "X-XSRF-TOKEN":   csrf,
    "Authorization":  `Bearer ${jwt}`
  };
}

/* ──────────────── inicio ──────────────── */
document.addEventListener("DOMContentLoaded", () => {
  // 1) Si venimos de registrar una venta, forzamos recarga de inventario
  if (localStorage.getItem("reloadInventory") === "true") {
    showSection.invLoaded = false;
    localStorage.removeItem("reloadInventory");
  }

  // 2) Si hay hash en la URL, mostramos esa sección al cargar
  const hash = window.location.hash.replace(/^#/, "");
  if (hash) {
    showSection(hash);
  } else {
    showSection("dashboard");
  }

  /* navegación entre secciones */
  document.querySelectorAll("nav button[data-section]")
      .forEach(btn =>
          btn.addEventListener("click", () =>
              showSection(btn.dataset.section)
          )
      );

  /* logout */
  document.getElementById("logout-btn")
      .addEventListener("click", () => {
        localStorage.removeItem("jwt");
        window.location.href = "../login.html";
      });

  /* botón Nuevo Artículo */
  const btnNuevo = document.getElementById("btn-nuevo-articulo");
  if (btnNuevo) {
    btnNuevo.addEventListener("click", () => {
      window.location.href = "form-articulo.html";
    });
  }

  /* botón Registrar nueva Venta */
  const btnNuevaVenta = document.getElementById("btn-nueva-venta");
  if (btnNuevaVenta) {
    btnNuevaVenta.addEventListener("click", () => {
      window.location.href = "form-venta.html";
    });
  }

  /* carga inicial del dashboard */
  loadDashboard();
});

/* ──────────────── helpers UI ──────────────── */
function showSection(id) {
  document.querySelectorAll(".panel-section").forEach(sec => {
    sec.style.display = sec.id === id ? "block" : "none";
  });

  switch (id) {
    case "inventario":
      if (!showSection.invLoaded) {
        loadInventario();
        showSection.invLoaded = true;
      }
      break;
    case "ventas":
      if (!showSection.venLoaded) {
        loadVentas();
        showSection.venLoaded = true;
      }
      break;
    case "dashboard":
      break;
  }

  // Actualizamos el hash de la URL para navegación/back
  history.replaceState(null, "", `#${id}`);
}

/* ──────────────── fetchers ──────────────── */
async function fetchJson(url, opts = {}) {
  const res = await fetch(url, { credentials: "include", ...opts });
  if (!res.ok) {
    const msg = (await res.text()) || res.statusText;
    throw new Error(`${res.status} – ${msg}`);
  }
  return res.json();
}

/* DASHBOARD */
async function loadDashboard() {
  try {
    const data = await fetchJson("/client/dashboard", { headers: authHeaders() });
    const dash = document.getElementById("dash-cards");
    dash.innerHTML = `
      <div class="card">Artículos con stock bajo: <strong>${data.lowStock}</strong></div>
      <div class="card">Ventas del día: <strong>${data.salesToday}</strong></div>
    `;
  } catch (e) {
    alert("Error cargando dashboard: " + e.message);
    console.error(e);
  }
}

/* INVENTARIO */
async function loadInventario() {
  try {
    const items = await fetchJson("/client/items", { headers: authHeaders() });
    const tbody = document.getElementById("inv-body");
    tbody.innerHTML = items.map(it => `
      <tr>
        <td>${it.code}</td>
        <td>${it.name}</td>
        <td>${it.description}</td>
        <td>${it.stock}</td>
        <td>${it.price}</td>
      </tr>
    `).join("");
  } catch (e) {
    alert("Error cargando inventario: " + e.message);
    console.error(e);
  }
}

/* VENTAS */
async function loadVentas() {
  try {
    const ventas = await fetchJson("/client/sales", { headers: authHeaders() });
    const tbody = document.getElementById("ventas-body");
    tbody.innerHTML = ventas.map(v => `
      <tr>
        <td>${v.clientName}</td>
        <td>${v.itemName}</td>
        <td>${v.quantity}</td>
        <td>${v.price}</td>
        <td>${v.paymentMethod}</td>
        <td>${new Date(v.createdAt).toLocaleDateString()}</td>
      </tr>
    `).join("");
  } catch (e) {
    alert("Error cargando ventas: " + e.message);
    console.error(e);
  }
}

