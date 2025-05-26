
const ctx = document.getElementById('statsChart').getContext('2d');
const statsChart = new Chart(ctx, {
  type: 'bar',
  data: {
    labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May'],
    datasets: [{
      label: 'Ventas',
      data: [50000, 75000, 60000, 90000, 125000],
      backgroundColor: '#007BFF'
    }]
  },
  options: { responsive: true, scales: { y: { beginAtZero: true } } }
});


document.addEventListener('DOMContentLoaded', () => {
  const form    = document.getElementById('formProducto');
  const mensaje = document.getElementById('mensaje');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const data = {
      name:           document.getElementById('nombre').value,
      description:    document.getElementById('descripcion').value,
      price:          parseFloat(document.getElementById('precio').value),
      stockQuantity:  parseInt(document.getElementById('stock').value, 10)
    };
    const clientId = document.getElementById('cliente').value;

    try {
      const res = await fetch(`http://localhost:8080/clients/${clientId}/products`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });

      if (!res.ok) throw new Error(`Error ${res.status}`);

      const product = await res.json();
      mensaje.textContent = `✅ Producto #${product.id} guardado`;
      mensaje.style.color = 'green';
      form.reset();
    } catch (err) {
      console.error(err);
      mensaje.textContent = '❌ No se pudo guardar el producto';
      mensaje.style.color = 'red';
    }
  });
});

