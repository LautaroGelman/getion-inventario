// src/main/java/grupo5/gestion_inventario/security/CombinedUserDetailsService.java
package grupo5.gestion_inventario.security;

import grupo5.gestion_inventario.superpanel.service.AdminUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * Bean principal que intenta buscar el usuario
 * primero en admins, luego empleados, luego clients.
 * Lo usamos desde el JwtAuthenticationFilter.
 */
@Service
@Primary
public class CombinedUserDetailsService implements UserDetailsService {

    private final AdminUserDetailsService adminSvc;
    private final CustomUserDetailsService employeeSvc;
    private final ClientUserDetailsService clientSvc;

    @Autowired
    public CombinedUserDetailsService(AdminUserDetailsService adminSvc,
                                      CustomUserDetailsService employeeSvc,
                                      ClientUserDetailsService clientSvc) {
        this.adminSvc = adminSvc;
        this.employeeSvc = employeeSvc;
        this.clientSvc = clientSvc;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return adminSvc.loadUserByUsername(username);
        } catch (UsernameNotFoundException ignored) { }
        try {
            return employeeSvc.loadUserByUsername(username);
        } catch (UsernameNotFoundException ignored) { }
        return clientSvc.loadUserByUsername(username);
    }
}
