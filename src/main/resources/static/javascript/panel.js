document.addEventListener("DOMContentLoaded", () => {
  console.log("Panel de administración cargado");
});
const buttons = document.querySelectorAll('nav button[data-section]');
    const sections = document.querySelectorAll('.panel-section');

    buttons.forEach(button => {
      button.addEventListener('click', () => {
        const target = button.getAttribute('data-section');
        sections.forEach(s => {
          s.style.display = s.id === target ? 'block' : 'none';
        });
      });
    });

    // Logout simple (redireccionar a login)
    document.getElementById('logout-btn').addEventListener('click', () => {
      window.location.href = 'index.html';
    });