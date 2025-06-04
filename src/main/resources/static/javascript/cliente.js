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
    "Content-Type":  "application/json",
    "X-XSRF-TOKEN":  csrf,
    "Authorization": `Bearer ${jwt}`
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
        window.location.href = "index.html";
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
      if (!showSection.dashLoaded) {
        loadDashboard();
        initChart();         // inicializa el gráfico en dashboard
        showSection.dashLoaded = true;
      }
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
      stacked: false,
      scales: {
        x: {
          title: {
            display: true,
            text: 'Fecha'
          },
          ticks: {
            autoSkip: true,
            maxTicksLimit: 10
          }
        },
        yVentas: {
          type: 'linear',
          display: true,
          position: 'left',
          title: {
            display: true,
            text: 'Cantidad de Ventas'
          },
          beginAtZero: true
        },
        yImporte: {
          type: 'linear',
          display: true,
          position: 'right',
          title: {
            display: true,
            text: 'Importe Total ($)'
          },
          beginAtZero: true,
          grid: {
            drawOnChartArea: false
          }
        }
      },
      plugins: {
        legend: {
          position: 'top'
        },
        tooltip: {
          callbacks: {
            label: function(context) {
              let label = context.dataset.label || '';
              if (context.dataset.yAxisID === 'yImporte') {
                return label + ': $' + context.formattedValue;
              }
              return label + ': ' + context.formattedValue;
            }
          }
        }
      }
    }
  });

  // Carga inicial y refresco cada 15 segundos
  loadChartData();
  setInterval(loadChartData, 15000);
}

async function loadChartData() {
  try {
    // Ajustamos la ruta al endpoint que existe en tu backend:
    const clientId = localStorage.getItem("clientId");
    if (!clientId) {
      console.error("No se encontró clientId en localStorage");
      return;
    }
    const URL = `/clients/${clientId}/sales/summary?days=30`;

    const data = await fetchJson(URL, { headers: authHeaders() });

    // mapeamos fechas, conteo e importe
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

