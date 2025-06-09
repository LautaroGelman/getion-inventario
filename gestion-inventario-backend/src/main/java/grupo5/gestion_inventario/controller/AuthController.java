package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.AuthRequest;
import grupo5.gestion_inventario.clientpanel.dto.AuthResponse;
import grupo5.gestion_inventario.config.JwtUtil;
import grupo5.gestion_inventario.model.BusinessAccount;
import grupo5.gestion_inventario.repository.BusinessAccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
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
    private final BusinessAccountRepository businessAccountRepo;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          BusinessAccountRepository businessAccountRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.businessAccountRepo = businessAccountRepo;
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
        String role = roles.stream()
                .filter(r -> !r.equals("ROLE_CLIENT"))
                .findFirst()
                .orElse(roles.isEmpty() ? null : roles.get(0))
                .replace("ROLE_", "");

        // 3) Si es ROLE_CLIENT, busco el clientId y uso generateToken con clientId
        if (roles.contains("ROLE_CLIENT")) {
            BusinessAccount client = businessAccountRepo.findByEmail(username) // o findByName, según tu entidad
                    .orElseThrow(() ->
                            new IllegalArgumentException("Cliente no encontrado: " + username));
            Long clientId = client.getId();
            token = jwtUtil.generateToken(username, roles, clientId);
        }
        // 4) Si es ROLE_ADMIN (o cualquier otro rol), genero token sin clientId
        else {
            token = jwtUtil.generateToken(username, roles);
        }

        return new AuthResponse(token, role);
    }

    /**
     * Devuelve la información del usuario autenticado.
     */
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(Authentication auth) {
        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(r -> !r.equals("ROLE_CLIENT") && !r.equals("ROLE_ADMIN"))
                .findFirst()
                .orElse("ROLE_CLIENT")
                .replace("ROLE_", "");
        return ResponseEntity.ok(new AuthResponse(null, role));
    }
}
