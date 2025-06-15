package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.config.JwtUtil;
import grupo5.gestion_inventario.model.Employee; // <-- AÑADIDO
import grupo5.gestion_inventario.repository.EmployeeRepository; // <-- AÑADIDO
import grupo5.gestion_inventario.clientpanel.dto.AuthRequest;
import grupo5.gestion_inventario.clientpanel.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmployeeRepository employeeRepository; // <-- AÑADIDO

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // AHORA BUSCAMOS AL EMPLEADO PARA OBTENER TODOS SUS DATOS
        final Employee employee = employeeRepository.findByEmail(authRequest.getUsername())
                .orElseThrow(() -> new Exception("Error al buscar empleado post-autenticación"));

        // USAMOS EL NUEVO MÉTODO PARA GENERAR EL TOKEN
        final String jwt = jwtUtil.generateToken(employee);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}