package grupo5.gestion_inventario.config;

import grupo5.gestion_inventario.repository.EmployeeRepository;
import grupo5.gestion_inventario.superpanel.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final EmployeeRepository employeeRepository;
    private final AdminUserRepository adminUserRepository;

    /**
     * Define un bean para el UserDetailsService.
     * Este servicio es responsable de cargar los detalles del usuario desde la base de datos.
     * Intenta encontrar primero un SUPER_ADMIN. Si no lo encuentra, busca un Employee.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Primero, intentamos encontrar al usuario como un SUPER_ADMIN por su username.
            // Como ambas entidades (AdminUser y Employee) implementan UserDetails,
            // podemos buscarlas y devolverlas sin problemas de tipo.
            var adminUser = adminUserRepository.findByUsername(username);
            if (adminUser.isPresent()) {
                return adminUser.get();
            }

            // Si no se encontró un SUPER_ADMIN, intentamos encontrarlo como un Employee por su email.
            // La variable 'username' del formulario de login se usa aquí como el email.
            var employee = employeeRepository.findByEmail(username);
            if (employee.isPresent()) {
                return employee.get();
            }

            // Si no se encuentra en ninguna de las dos tablas, ahora sí lanzamos la excepción.
            throw new UsernameNotFoundException("Usuario no encontrado con el identificador: " + username);
        };
    }

    /**
     * Define el bean del Proveedor de Autenticación (AuthenticationProvider).
     * Este es el componente que Spring Security usará para validar las credenciales.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Le asignamos el UserDetailsService para que sepa cómo buscar usuarios.
        authProvider.setUserDetailsService(userDetailsService());
        // Le asignamos el PasswordEncoder para que sepa cómo verificar las contraseñas.
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Define el bean del AuthenticationManager.
     * Es necesario para procesar las solicitudes de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define el bean para el encriptador de contraseñas.
     * Usamos BCrypt, que es el estándar recomendado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}