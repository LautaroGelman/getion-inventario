package grupo5.gestion_inventario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaForwardingController {

    /**
     * Este método reenvía todas las solicitudes de rutas que no son de la API
     * y no son para archivos estáticos (es decir, no contienen un punto)
     * al punto de entrada de la aplicación de página única (SPA).
     * Esto permite que el enrutador del lado del cliente (React Router)
     * maneje la ruta.
     */
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String forward() {
        // Reenvía a la raíz, que servirá el index.html de la SPA
        return "forward:/";
    }
}