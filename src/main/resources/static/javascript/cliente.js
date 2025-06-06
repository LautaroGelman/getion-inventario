/* ─────────────────── utilidades ──────────────── */
function getCookie(name) {
  const pair = document.cookie
      .split("; ")
      .find(row => row.startsWith(name + "="));
  return pair ? decodeURIComponent(pair.split("=")[1]) : null;
}

function authHeaders() {
  // Corregido para usar la función getCsrf que ya tenías en otros archivos
  const p = document.cookie.split("; ").find(v => v.startsWith("XSRF-TOKEN="));
  const csrf = p ? p.substring("XSRF-TOKEN=".length) : "";
  const jwt  = localStorage.getItem("jwt");
  return {
    "Content-Type":  "application/json",
    "X-XSRF-TOKEN":  csrf,
    "Authorization": `Bearer ${jwt}`
  };
}

/* ──────────────── inicio ──────────────── */
document.addEventListener("DOMContentLoaded", () => {
  if (localStorage.getItem("reloadInventory") === "true") {
    showSection.invLoaded = false;
    localStorage.removeItem("reloadInventory");
  }

  // --- ¡BLOQUE AÑADIDO AQUÍ! ---
  if (localStorage.getItem("reloadProviders") === "true") {
    showSection.provLoaded = false; // Forza la recarga de proveedores
    localStorage.removeItem("reloadProviders"); // Limpia la bandera
  }

  const hash = window.location.hash.replace(/^#/, "");
  if (hash) {
    showSection(hash);
  } else {
    showSection("dashboard");
  }

  document.querySelectorAll("nav button[data-section]")
      .forEach(btn =>
          btn.addEventListener("click", () =>
              showSection(btn.dataset.section)
          )
      );

  document.getElementById("logout-btn")
      .addEventListener("click", () => {
        localStorage.removeItem("jwt");
        window.location.href = "index.html";
      });

  const btnNuevo = document.getElementById("btn-nuevo-articulo");
  if (btnNuevo) {
    btnNuevo.addEventListener("click", () => {
      window.location.href = "form-articulo.html";
    });
  }

  const btnNuevaVenta = document.getElementById("btn-nueva-venta");
  if (btnNuevaVenta) {
    btnNuevaVenta.addEventListener("click", () => {
      window.location.href = "form-venta.html";
    });
  }

  // --- ¡NUEVO LISTENER PARA EL BOTÓN DE PROVEEDOR! ---
  const btnNuevoProveedor = document.getElementById("btn-nuevo-proveedor");
  if (btnNuevoProveedor) {
    btnNuevoProveedor.addEventListener("click", () => {
      window.location.href = "form-proveedor.html";
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
      if (!showSection.dashLoaded) {
        loadDashboard();
        initChart();
        initProfitabilityChart();
        showSection.dashLoaded = true;
      }
      break;
      // --- ¡NUEVO CASE PARA LA SECCIÓN DE PROVEEDORES! ---
    case "proveedores":
      if (!showSection.provLoaded) {
        loadProveedores();
        showSection.provLoaded = true;
      }
      break;
  }

  history.replaceState(null, "", `#${id}`);
}

/* ──────────────── fetchers ──────────────── */
async function fetchJson(url, opts = {}) {
  const res = await fetch(url, { credentials: "include", ...opts, headers: authHeaders() });
  if (!res.ok) {
    const msg = (await res.text()) || res.statusText;
    throw new Error(`${res.status} – ${msg}`);
  }
  return res.json();
}
/* DASHBOARD */
async function loadDashboard() {
  try {
    const data = await fetchJson("/client/dashboard");
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
/* ──────────────── Gráfico de Ventas (Chart.js) ──────────────── */
let ventasChartInstance;

function initChart() {
  const ctx = document.getElementById('ventasChart').getContext('2d');
  ventasChartInstance = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: [],
      datasets: [
        {
          label: 'Cantidad de Ventas',
          data: [],
          yAxisID: 'yVentas',
          type: 'bar',
          backgroundColor: 'rgba(4,128,163,0.5)',
          borderColor: 'rgba(4,128,163,1)',
          borderWidth: 1
        },
        {
          label: 'Importe Total ($)',
          data: [],
          yAxisID: 'yImporte',
          type: 'line',
          tension: 0.3,
          backgroundColor: 'rgba(75,192,192,0.3)',
          borderColor: 'rgba(75,192,192,1)',
          borderWidth: 2
        }
      ]
    },
    options: {
      responsive: true,
      interaction: {
        mode: 'index',
        intersect: false
      },
      scales: {
        x: { title: { display: true, text: 'Fecha' }, ticks: { autoSkip: true, maxTicksLimit: 10 } },
        yVentas: { type: 'linear', display: true, position: 'left', title: { display: true, text: 'Cantidad de Ventas' }, beginAtZero: true },
        yImporte: { type: 'linear', display: true, position: 'right', title: { display: true, text: 'Importe Total ($)' }, beginAtZero: true, grid: { drawOnChartArea: false } }
      },
      plugins: {
        legend: { position: 'top' },
        tooltip: { callbacks: { label: function(context) { let label = context.dataset.label || ''; if (context.dataset.yAxisID === 'yImporte') { return label + ': $' + context.formattedValue; } return label + ': ' + context.formattedValue; } } }
      }
    }
  });

  // Carga inicial y refresco cada 15 segundos
  loadChartData();
  setInterval(loadChartData, 15000);
}

async function loadChartData() {
  try {
    // --- CORREGIDO: Ya no necesitamos el clientId, lo obtiene el backend
    const URL = `/client/sales/summary?days=30`;
    const data = await fetchJson(URL);

    const labels       = data.map(o => o.date);
    const ventasData   = data.map(o => o.salesCount);
    const importeData  = data.map(o => o.totalAmount);

    ventasChartInstance.data.labels               = labels;
    ventasChartInstance.data.datasets[0].data     = ventasData;
    ventasChartInstance.data.datasets[1].data     = importeData;
    ventasChartInstance.update();

  } catch (e) {
    console.error("Error cargando datos del gráfico:", e);
  }
}


// --- ¡NUEVO CÓDIGO PARA EL GRÁFICO DE RENTABILIDAD! ---
let profitabilityChartInstance;

function initProfitabilityChart() {
  const ctx = document.getElementById('profitabilityChart').getContext('2d');
  profitabilityChartInstance = new Chart(ctx, {
    type: 'bar', // El tipo base es barra, pero una serie será de tipo línea
    data: {
      labels: [],
      datasets: [
        {
          label: 'Ingresos ($)',
          data: [],
          backgroundColor: 'rgba(75, 192, 192, 0.5)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1
        },
        {
          label: 'Costos ($)',
          data: [],
          backgroundColor: 'rgba(255, 99, 132, 0.5)',
          borderColor: 'rgba(255, 99, 132, 1)',
          borderWidth: 1
        },
        {
          label: 'Ganancia ($)',
          data: [],
          type: 'line', // <-- Este dataset se renderiza como una línea
          borderColor: 'rgba(54, 162, 235, 1)',
          backgroundColor: 'rgba(54, 162, 235, 0.2)',
          tension: 0.3,
          fill: false
        }
      ]
    },
    options: {
      responsive: true,
      scales: {
        x: { title: { display: true, text: 'Fecha' } },
        y: { title: { display: true, text: 'Monto ($)' }, beginAtZero: true }
      }
    }
  });

  // Carga inicial de datos
  loadProfitabilityChartData();
}

async function loadProfitabilityChartData() {
  try {
    const data = await fetchJson("/client/dashboard/profitability-summary?days=30");

    // Extraemos los datos para cada serie del gráfico
    const labels = data.map(d => d.date);
    const revenueData = data.map(d => d.revenue);
    const costData = data.map(d => d.costOfGoods);
    const profitData = data.map(d => d.profit);

    // Actualizamos los datos del gráfico
    profitabilityChartInstance.data.labels = labels;
    profitabilityChartInstance.data.datasets[0].data = revenueData;
    profitabilityChartInstance.data.datasets[1].data = costData;
    profitabilityChartInstance.data.datasets[2].data = profitData;
    profitabilityChartInstance.update();
  } catch (e) {
    console.error("Error cargando datos del gráfico de rentabilidad:", e);
  }
}
// --- FIN DEL NUEVO CÓDIGO ---


/* INVENTARIO */
async function loadInventario() {
  try {
    const items = await fetchJson("/client/items");
    const tbody = document.getElementById("inv-body");
    tbody.innerHTML = items.map(it => `
      <tr>
        <td>${it.code}</td>
        <td>${it.name}</td>
        <td>${it.description}</td>
        <td>${it.stock}</td>
        <td>${it.price}</td>
        <td>
          <button class="btn-editar" data-id="${it.id}">Editar</button>
          <button class="btn-eliminar" data-id="${it.id}">Eliminar</button>
        </td>
      </tr>
    `).join("");

    // Lógica para los botones de Editar (existente)
    tbody.querySelectorAll(".btn-editar").forEach(btn => {
      btn.addEventListener("click", () => {
        const productId = btn.dataset.id;
        window.location.href = `form-articulo.html?id=${productId}`;
      });
    });

    // --- NUEVA LÓGICA PARA LOS BOTONES DE ELIMINAR ---
    tbody.querySelectorAll(".btn-eliminar").forEach(btn => {
      btn.addEventListener("click", async () => {
        const productId = btn.dataset.id;

        // Pedimos confirmación antes de una acción destructiva
        if (confirm(`¿Estás seguro de que quieres eliminar el producto?`)) {
          try {
            const res = await fetch(`/client/products/${productId}`, {
              method: 'DELETE',
              headers: authHeaders()
            });

            if (res.ok) {
              // Si el borrado fue exitoso (respuesta 204), eliminamos la fila de la tabla.
              btn.closest('tr').remove();
            } else {
              // Si el backend devuelve un error (ej: 404), lo mostramos.
              throw new Error(`Error ${res.status}: No se pudo eliminar el producto.`);
            }
          } catch (e) {
            alert("Error al eliminar el producto: " + e.message);
            console.error(e);
          }
        }
      });
    });

  } catch (e) {
    alert("Error cargando inventario: " + e.message);
    console.error(e);
  }
}

/* VENTAS */
async function loadVentas() {
  try {
    const ventas = await fetchJson("/client/sales");
    const tbody  = document.getElementById("ventas-body");
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
