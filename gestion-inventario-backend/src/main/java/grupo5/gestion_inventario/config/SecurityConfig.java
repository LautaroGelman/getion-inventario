package grupo5.gestion_inventario.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Configuración de CORS para permitir la comunicación con el frontend.
                .cors(cors -> cors.configure(http))

                // 2. Se deshabilita CSRF. Esta es la causa del error 403 y es una
                //    práctica estándar para APIs stateless que usan tokens JWT.
                .csrf(csrf -> csrf.disable())

                // 3. Definición de las reglas de autorización para cada ruta.
                .authorizeHttpRequests(authorize -> authorize
                        // Permite el acceso sin autenticación a los endpoints de login.
                        .requestMatchers("/api/auth/login", "/api/superpanel/auth/login").permitAll()

                        // Permite las peticiones OPTIONS pre-vuelo de CORS a todas las rutas.
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Requiere el rol SUPER_ADMIN para cualquier ruta dentro de /api/superpanel/
                        .requestMatchers("/api/superpanel/**").hasAuthority("SUPER_ADMIN")

                        // Requiere rol ADMIN o CASHIER para cualquier ruta dentro de /api/clientpanel/
                        .requestMatchers("/api/clientpanel/**").hasAnyAuthority("ADMIN", "CASHIER")

                        // Todas las demás peticiones deben estar autenticadas.
                        .anyRequest().authenticated()
                )

                // 4. Configuración del manejo de sesiones.
                // Se establece como STATELESS porque usamos tokens JWT, no sesiones de servidor.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 5. Se define el proveedor de autenticación.
                .authenticationProvider(authenticationProvider)

                // 6. Se añade nuestro filtro personalizado de JWT antes del filtro de usuario/contraseña.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}