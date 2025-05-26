document.getElementById('guardar-tema').addEventListener('click', () => {
  const tema = document.getElementById('tema-select').value;
  alert(`Tema "${tema}" guardado correctamente.`);
  // Aquí se puede guardar en localStorage o hacer un POST al servidor
});

document.getElementById('guardar-api').addEventListener('click', () => {
  const apiKey = document.getElementById('api-key').value.trim();
  if (apiKey) {
    alert("API Key guardada correctamente.");
    // Guardar en backend si fuera necesario
  } else {
    alert("Por favor, ingresá una API Key válida.");
  }
});

document.getElementById('btn-nuevo-usuario').addEventListener('click', () => {
  alert("Funcionalidad de agregar nuevo usuario aún no implementada.");
});

document.getElementById('tabla-usuarios').addEventListener('click', (e) => {
  if (e.target.classList.contains('editar-usuario')) {
    const id = e.target.dataset.id;
    alert(`Editar usuario con ID ${id} - Funcionalidad pendiente`);
  } else if (e.target.classList.contains('eliminar-usuario')) {
    const id = e.target.dataset.id;
    const confirmar = confirm(`¿Estás seguro de eliminar el usuario con ID ${id}?`);
    if (confirmar) {
      e.target.closest('tr').remove();
    }
  }
});
