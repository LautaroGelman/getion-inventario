package grupo5.gestion_inventario.security;

import grupo5.gestion_inventario.superpanel.model.AdminUser;
import grupo5.gestion_inventario.superpanel.repository.AdminUserRepository;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepo;
    private final ClientRepository clientRepo;

    public CustomUserDetailsService(AdminUserRepository adminUserRepo,
                                    ClientRepository clientRepo) {
        this.adminUserRepo = adminUserRepo;
        this.clientRepo = clientRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Intentamos cargar como administrador
        AdminUser admin = adminUserRepo.findByUsername(username)
                .orElse(null);
        if (admin != null) {
            String[] roles = admin.getRoles().toArray(new String[0]);
            return User.builder()
                    .username(admin.getUsername())
                    .password(admin.getPasswordHash())
                    .roles(roles)
                    .build();
        }

        // Intentamos cargar como cliente
        Client client = clientRepo.findByEmail(username);
        if (client != null) {
            return User.builder()
                    .username(client.getEmail())
                    .password(client.getPassword())
                    .roles("CLIENT")
                    .build();
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}
