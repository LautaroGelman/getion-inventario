// src/main/java/grupo5/gestion_inventario/superpanel/service/AdminUserDetailsService.java
package grupo5.gestion_inventario.superpanel.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import grupo5.gestion_inventario.superpanel.repository.AdminUserRepository;
import grupo5.gestion_inventario.superpanel.model.AdminUser;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository repo;

    public AdminUserDetailsService(AdminUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser admin = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        // Convertimos a Spring Security User
        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPasswordHash())
                .roles(admin.getRoles()
                        .stream()
                        .map(r -> r.replace("ROLE_", "")) // User.builder añade ROLE_ automáticamente
                        .toArray(String[]::new))
                .build();
    }
}
