package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.AuthRequest;
import grupo5.gestion_inventario.clientpanel.dto.AuthResponse;
import grupo5.gestion_inventario.config.JwtUtil;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
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
    private final ClientRepository clientRepo;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          ClientRepository clientRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.clientRepo = clientRepo;
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

        // 3) Si es ROLE_CLIENT, busco el clientId y uso generateToken con clientId
        if (roles.contains("ROLE_CLIENT")) {
            Client client = clientRepo.findByEmail(username) // o findByName, según tu entidad
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
}
