package grupo5.gestion_inventario.superpanel.controller;

import grupo5.gestion_inventario.clientpanel.dto.AuthRequest;
import grupo5.gestion_inventario.clientpanel.dto.AuthResponse;
import grupo5.gestion_inventario.config.JwtUtil;
import grupo5.gestion_inventario.superpanel.model.AdminUser;
import grupo5.gestion_inventario.superpanel.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/superpanel/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService service;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        // CORRECCIÓN: Extraemos los datos de UserDetails para pasarlos al método correcto.
        String username = userDetails.getUsername();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // Llamamos a la versión de generateToken para administradores (sin clientId).
        final String jwt = jwtUtil.generateToken(username, roles);

        return ResponseEntity.ok(new AuthResponse(jwt, "SUPER_ADMIN"));
    }

    @PostMapping
    public AdminUser createAdminUser(@RequestBody Map<String, String> payload) {
        // El controlador ahora solo necesita username y password
        return service.create(payload.get("username"), payload.get("password"));
    }

    @GetMapping
    public List<AdminUser> getAllAdminUsers() {
        // Llama al método correcto en el servicio
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUser> getAdminUserById(@PathVariable Long id) {
        // Llama al método correcto en el servicio
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUser> updateAdminUser(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        // El controlador ahora solo actualiza la contraseña
        AdminUser updatedUser = service.update(id, payload.get("password"));
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminUser(@PathVariable Long id) {
        // Llama al método correcto en el servicio
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}