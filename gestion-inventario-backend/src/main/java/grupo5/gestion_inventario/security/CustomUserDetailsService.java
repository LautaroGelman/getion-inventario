package grupo5.gestion_inventario.security;

import grupo5.gestion_inventario.model.BusinessAccount;
import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.model.EmployeeRole;
import grupo5.gestion_inventario.model.Role;
import grupo5.gestion_inventario.repository.BusinessAccountRepository;
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
    private final BusinessAccountRepository    businessAccountRepo;
    private final EmployeeRepository  employeeRepo;

    public CustomUserDetailsService(AdminUserRepository adminRepo,
                                    BusinessAccountRepository businessAccountRepo,
                                    EmployeeRepository employeeRepo) {
        this.adminRepo   = adminRepo;
        this.businessAccountRepo  = businessAccountRepo;
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

        /* -------- ¿Employee? -------- */
        Employee emp = employeeRepo.findByEmail(username).orElse(null);
        if (emp != null) {
            String[] roles = new String[]{"CLIENT", emp.getRole().name()};
            return User.builder()
                    .username(emp.getBusinessAccount().getEmail())
                    .password(emp.getPasswordHash())
                    .roles(roles)
                    .build();
        }

        /* -------- ¿BusinessAccount? --------
           Busca por email; si quisieras aceptar “name”, agrega otro findBy… */
        BusinessAccount client = businessAccountRepo.findByEmail(username)
                .orElseGet(() -> businessAccountRepo.findByName(username)
                        .orElse(null));

        if (client != null) {
            String role = client.getRole() != null ? client.getRole().name() : "CLIENT";
            return User.builder()
                    .username(client.getEmail())      // clave del JWT
                    .password(client.getPasswordHash())
                    .roles(role)
                    .build();
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}
