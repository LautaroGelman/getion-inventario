// src/main/java/grupo5/gestion_inventario/security/CustomUserDetailsService.java
package grupo5.gestion_inventario.security;

import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscamos por email (no username)
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Empleado no encontrado: " + email));

        // Convertimos rol (enum) a GrantedAuthority con prefijo ROLE_
        String authority = "ROLE_" + employee.getRole().name();
        return User.builder()
                .username(employee.getEmail())
                .password(employee.getPassword())
                .authorities(authority)
                .build();
    }
}
