const clientId = localStorage.getItem("clientId");
const API_BASE = `/clients/${clientId}/providers`;

async function cargarProveedores() {
  const res = await fetch(API_BASE);
  const proveedores = await res.json();
  const tbody = document.querySelector("#tabla-proveedores tbody");
  tbody.innerHTML = "";
  proveedores.forEach(p => {
    const fila = document.createElement("tr");
    fila.innerHTML = `
      <td>${p.id}</td>
      <td>${p.name}</td>
      <td>${p.contactInfo}</td>
      <td>${p.paymentTerms}</td>
      <td>${new Date(p.createdAt).toLocaleDateString()}</td>
      <td>
        <button onclick="editarProveedor(${p.id})">Editar</button>
        <button onclick="eliminarProveedor(${p.id})">Eliminar</button>
      </td>
    `;
    tbody.appendChild(fila);
  });
}

function editarProveedor(id) {
  window.location.href = `form-proveedor.html?id=${id}`;
}

async function eliminarProveedor(id) {
  if (confirm("¿Eliminar proveedor?")) {
    await fetch(`${API_BASE}/${id}`, { method: "DELETE" });
    cargarProveedores();
  }
}

document.addEventListener("DOMContentLoaded", cargarProveedores);
