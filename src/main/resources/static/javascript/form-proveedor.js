const params = new URLSearchParams(window.location.search);
const proveedorId = params.get("id");

if (proveedorId) {
  fetch(`${API_BASE}/${proveedorId}`)
    .then(r => r.json())
    .then(p => {
      document.getElementById("name").value = p.name;
      document.getElementById("contactInfo").value = p.contactInfo;
      document.getElementById("paymentTerms").value = p.paymentTerms;
    });
}

document.getElementById("form-proveedor").addEventListener("submit", async e => {
  e.preventDefault();
  const data = {
    name: document.getElementById("name").value,
    contactInfo: document.getElementById("contactInfo").value,
    paymentTerms: document.getElementById("paymentTerms").value,
  };
  const metodo = proveedorId ? "PUT" : "POST";
  const url = proveedorId ? `${API_BASE}/${proveedorId}` : API_BASE;
  await fetch(url, {
    method: metodo,
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  });
  window.location.href = "list-proveedores.html";
});
