const gastoId = new URLSearchParams(window.location.search).get("id");

if (gastoId) {
  fetch(`${GASTOS_API}/${gastoId}`)
    .then(r => r.json())
    .then(g => {
      document.getElementById("description").value = g.description;
      document.getElementById("amount").value = g.amount;
      document.getElementById("type").value = g.type;
      document.getElementById("date").value = g.date.split("T")[0];
    });
}

document.getElementById("form-gasto").addEventListener("submit", async e => {
  e.preventDefault();
  const data = {
    description: document.getElementById("description").value,
    amount: parseFloat(document.getElementById("amount").value),
    type: document.getElementById("type").value,
    date: document.getElementById("date").value
  };
  const metodo = gastoId ? "PUT" : "POST";
  const url = gastoId ? `${GASTOS_API}/${gastoId}` : GASTOS_API;
  await fetch(url, {
    method: metodo,
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  });
  window.location.href = "list-gastos.html";
});
