package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.AuthRequest;
import grupo5.gestion_inventario.clientpanel.dto.AuthResponse;
import grupo5.gestion_inventario.config.JwtUtil;
import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para autenticación y generación de JWT.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmployeeRepository employeeRepo;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          EmployeeRepository employeeRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.employeeRepo = employeeRepo;
    }

    /**
     * Autentica al usuario y genera un JWT:
     *  - Para admins: token con roles únicamente.
     *  - Para clientes: token con roles + clientId.
     */
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        // 1) Autenticar
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2) Extraer roles
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String username = authentication.getName();
        String token;
        String role = roles.isEmpty() ? null : roles.get(0).replace("ROLE_", "");

        // 3) Si es un empleado (ADMIN o CASHIER), incluyo su ID en el token
        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_CASHIER")) {
            Employee employee = employeeRepo.findByEmail(username)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Empleado no encontrado: " + username));
            Long clientId = employee.getId();
            token = jwtUtil.generateToken(username, roles, clientId);
        } else {
            // Para cualquier otro rol (ej. usuarios del superpanel) no se incluye ID
            token = jwtUtil.generateToken(username, roles);
        }

        return new AuthResponse(token, role);
    }
}
