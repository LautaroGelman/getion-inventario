// src/main/java/grupo5/gestion_inventario/security/ClientUserDetailsService.java
package grupo5.gestion_inventario.security;

import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class ClientUserDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client c = repo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Cliente no encontrado: " + email));

        return User.builder()
                .username(c.getEmail())
                .password(c.getPasswordHash())
                .authorities("ROLE_CLIENT")   // ← única authority
                .build();
    }
}
