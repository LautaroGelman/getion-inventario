document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('form-articulo');
  form.addEventListener('submit', async e => {
    e.preventDefault();
    const data = new FormData(form);
    const clientId = parseInt(data.get('clientId'), 10);

    const payload = {
      code:              data.get('codigo'),
      name:              data.get('nombre'),
      description:       data.get('descripcion'),
      stockQuantity:     parseInt(data.get('stock'), 10),
      price:             parseFloat(data.get('precio')),
      // lowStockThreshold no existe en form → queda null y el servicio lo setea a 0
      lowStockThreshold: null
    };

    try {
      const res = await fetch(`/clients/${clientId}/products`, {
        method:  'POST',
        headers: { 'Content-Type': 'application/json' },
        body:    JSON.stringify(payload)
      });
      if (!res.ok) throw new Error(await res.text());
      alert('Artículo guardado correctamente');
      form.reset();
    } catch (err) {
      alert('Error al guardar artículo: ' + err.message);
    }
  });
});
