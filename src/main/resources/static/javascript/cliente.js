document.addEventListener('DOMContentLoaded', () => {
  const form = document.querySelector('form');
  form.addEventListener('submit', async e => {
    e.preventDefault();

    const formData = new FormData(form);
    const payload = {
      name:       formData.get('nombre'),
      email:      formData.get('email'),
      telefono:   formData.get('telefono'),
      plan:       formData.get('plan'),
      estado:     formData.get('estado')
    };

    try {
      const res = await fetch('/clients', {
        method:  'POST',
        headers: { 'Content-Type': 'application/json' },
        body:    JSON.stringify(payload)
      });

      if (!res.ok) throw new Error(await res.text());
      alert('Cliente guardado correctamente');
      form.reset();
    } catch (err) {
      alert('Error al guardar cliente: ' + err.message);
    }
  });
});
