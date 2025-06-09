package grupo5.gestion_inventario.security;

import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.model.Role;
import grupo5.gestion_inventario.repository.EmployeeRepository;
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
    private final EmployeeRepository    employeeRepo;

    public CustomUserDetailsService(AdminUserRepository adminRepo,
                                    EmployeeRepository employeeRepo) {
        this.adminRepo  = adminRepo;
        this.employeeRepo = employeeRepo;
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

        /* -------- ¿Employee? --------
           Busca por email; si quisieras aceptar “name”, agrega otro findBy… */
        Employee employee = employeeRepo.findByEmail(username)
                .orElseGet(() -> employeeRepo.findByName(username)
                        .orElse(null));

        if (employee != null) {
            String role = employee.getRole() != null ? employee.getRole().name() : "CASHIER";
            return User.builder()
                    .username(employee.getEmail())      // clave del JWT
                    .password(employee.getPasswordHash())
                    .roles(role)
                    .build();
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}
