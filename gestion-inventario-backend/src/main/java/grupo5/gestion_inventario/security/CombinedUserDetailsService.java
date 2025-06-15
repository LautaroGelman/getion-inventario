package grupo5.gestion_inventario.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import grupo5.gestion_inventario.superpanel.service.AdminUserDetailsService;

@Service
public class CombinedUserDetailsService implements UserDetailsService {

    private final AdminUserDetailsService adminUserDetailsService;
    private final CustomUserDetailsService customUserDetailsService;
    private final ClientUserDetailsService clientUserDetailsService;

    @Autowired
    public CombinedUserDetailsService(AdminUserDetailsService adminUserDetailsService,
                                      CustomUserDetailsService customUserDetailsService,
                                      ClientUserDetailsService clientUserDetailsService) {
        this.adminUserDetailsService = adminUserDetailsService;
        this.customUserDetailsService = customUserDetailsService;
        this.clientUserDetailsService = clientUserDetailsService;
    }

    /**
     * Carga un usuario por su nombre de usuario buscando en múltiples servicios.
     * Intenta cargar el usuario en el siguiente orden:
     * 1. Administrador (AdminUser)
     * 2. Empleado (Employee)
     * 3. Cliente (Client)
     * Si no se encuentra en ninguno, lanza UsernameNotFoundException.
     *
     * @param username el nombre de usuario a buscar.
     * @return los detalles del usuario encontrado.
     * @throws UsernameNotFoundException si el usuario no se encuentra en ninguna de las fuentes de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // 1. Intenta encontrar al usuario como Administrador
            return adminUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException adminNotFound) {
            // Si no es admin, intenta como Empleado
            try {
                return customUserDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException employeeNotFound) {
                // Si no es empleado, intenta como Cliente
                try {
                    return clientUserDetailsService.loadUserByUsername(username);
                } catch (UsernameNotFoundException clientNotFound) {
                    // Si no se encuentra en ninguna parte, lanza la excepción final.
                    throw new UsernameNotFoundException("Usuario '" + username + "' no encontrado en el sistema.");
                }
            }
        }
    }
}