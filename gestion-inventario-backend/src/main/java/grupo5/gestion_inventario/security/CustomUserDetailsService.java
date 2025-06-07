package grupo5.gestion_inventario.security;

import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.superpanel.model.AdminUser;
import grupo5.gestion_inventario.superpanel.repository.AdminUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminRepo;
    private final ClientRepository    clientRepo;

    public CustomUserDetailsService(AdminUserRepository adminRepo,
                                    ClientRepository clientRepo) {
        this.adminRepo  = adminRepo;
        this.clientRepo = clientRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        /* -------- ¿Admin? -------- */
        AdminUser admin = adminRepo.findByUsername(username).orElse(null);
        if (admin != null) {
            String[] roles = admin.getRoles().toArray(new String[0]); // p.e. ["ADMIN"]
            return User.builder()
                    .username(admin.getUsername())
                    .password(admin.getPasswordHash())
                    .roles(roles)
                    .build();
        }

        /* -------- ¿Client? --------
           Busca por email; si quisieras aceptar “name”, agrega otro findBy… */
        Client client = clientRepo.findByEmail(username)
                .orElseGet(() -> clientRepo.findByName(username)
                        .orElse(null));

        if (client != null) {
            return User.builder()
                    .username(client.getEmail())      // clave del JWT
                    .password(client.getPasswordHash())
                    .roles("CLIENT")
                    .build();
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}
