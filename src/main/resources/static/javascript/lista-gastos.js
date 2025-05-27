const GASTOS_API = `/clients/${clientId}/expenses`;

async function cargarGastos() {
  const res = await fetch(GASTOS_API);
  const gastos = await res.json();
  const tbody = document.querySelector("#tabla-gastos tbody");
  tbody.innerHTML = "";
  gastos.forEach(g => {
    const fila = document.createElement("tr");
    fila.innerHTML = `
      <td>${g.description}</td>
      <td>${g.amount}</td>
      <td>${g.type}</td>
      <td>${new Date(g.date).toLocaleDateString()}</td>
      <td>
        <button onclick="editarGasto(${g.id})">Editar</button>
        <button onclick="eliminarGasto(${g.id})">Eliminar</button>
      </td>
    `;
    tbody.appendChild(fila);
  });
}

function editarGasto(id) {
  window.location.href = `form-gasto.html?id=${id}`;
}

async function eliminarGasto(id) {
  if (confirm("¿Eliminar gasto?")) {
    await fetch(`${GASTOS_API}/${id}`, { method: "DELETE" });
    cargarGastos();
  }
}

document.addEventListener("DOMContentLoaded", cargarGastos);