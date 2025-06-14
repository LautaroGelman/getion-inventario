package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.config.JwtUtil;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import grupo5.gestion_inventario.clientpanel.dto.AuthRequest;
import grupo5.gestion_inventario.clientpanel.dto.AuthResponse;
import grupo5.gestion_inventario.superpanel.model.AdminUser;
import grupo5.gestion_inventario.superpanel.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private EmployeeRepository employeeRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest req) throws Exception {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);

        String username = req.getUsername();
        String token;

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isClient = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"));

        if (isAdmin) {
            AdminUser admin = adminUserRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception("Admin no encontrado: " + username));
            token = jwtUtil.generateToken(admin);

        } else if (isClient) {
            Client client = clientRepository.findByEmail(username)
                    .orElseThrow(() -> new Exception("Cliente no encontrado: " + username));
            token = jwtUtil.generateToken(client);

        } else { // Empleado
            Employee emp = employeeRepository.findByEmail(username)
                    .orElseThrow(() -> new Exception("Empleado no encontrado: " + username));
            token = jwtUtil.generateToken(emp);
        }

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
