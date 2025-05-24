document.getElementById("loginForm").addEventListener("submit", function (e) {
    e.preventDefault();
  
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    const errorMsg = document.getElementById("error-message");
  
    if (username === "" || password === "") {
      errorMsg.textContent = "Por favor, complete todos los campos.";
      return;
    }
  
    // Ejemplo de validación básica (sólo para pruebas)
    if (username === "admin" && password === "1234") {
      errorMsg.style.color = "green";
      errorMsg.textContent = "Inicio de sesión exitoso. Redirigiendo...";
      setTimeout(() => {
        window.location.href = "dashboard.html"; // Página destino
      }, 1500);
    } else {
      errorMsg.textContent = "Usuario o contraseña incorrectos.";
    }
  });
  