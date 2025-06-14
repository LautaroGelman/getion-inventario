package grupo5.gestion_inventario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Puedes ser específico…
                .allowedOrigins(
                        "http://localhost:5177",
                        "http://localhost:5176",
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "http://127.0.0.1:5500")
                // …o, en desarrollo, permitir todo:
                // .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS")
                .allowCredentials(false);
    }
}
