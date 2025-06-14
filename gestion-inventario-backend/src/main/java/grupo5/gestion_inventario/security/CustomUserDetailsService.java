package grupo5.gestion_inventario.security;

import grupo5.gestion_inventario.model.Employee; // <-- CAMBIO: Importamos Employee
import grupo5.gestion_inventario.repository.EmployeeRepository; // <-- CAMBIO: Importamos el nuevo repositorio
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
    private final EmployeeRepository employeeRepo; // <-- CAMBIO: Usamos el repositorio de empleados

    public CustomUserDetailsService(AdminUserRepository adminRepo,
                                    EmployeeRepository employeeRepo) { // <-- CAMBIO: Inyectamos el nuevo repo
        this.adminRepo = adminRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) // username es el email
            throws UsernameNotFoundException {

        /* -------- ¿Admin? -------- */
        AdminUser admin = adminRepo.findByUsername(username).orElse(null);
        if (admin != null) {
            String[] roles = admin.getRoles().toArray(new String[0]);
            return User.builder()
                    .username(admin.getUsername())
                    .password(admin.getPasswordHash())
                    .roles(roles)
                    .build();
        }

        /* -------- ¿Employee? -------- */
        // Buscamos un empleado por su email.
        // Si lo encuentra, lo devuelve. Como Employee implementa UserDetails, Spring Security lo entiende directamente.
        Employee employee = employeeRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return employee; // <-- CAMBIO: Devolvemos el objeto Employee directamente
    }
}