// src/main/java/grupo5/gestion_inventario/superpanel/service/AdminUserDetailsService.java
package grupo5.gestion_inventario.superpanel.service;

import grupo5.gestion_inventario.superpanel.model.AdminUser;
import grupo5.gestion_inventario.superpanel.repository.AdminUserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository repo;

    public AdminUserDetailsService(AdminUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser admin = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin no encontrado: " + username));

        // Aquí usamos .roles(...) porque .roles() añade automáticamente "ROLE_"
        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPasswordHash())
                .roles(admin.getRoles()
                        .stream()
                        .map(r -> r.replace("ROLE_", ""))
                        .toArray(String[]::new))
                .build();
    }
}
