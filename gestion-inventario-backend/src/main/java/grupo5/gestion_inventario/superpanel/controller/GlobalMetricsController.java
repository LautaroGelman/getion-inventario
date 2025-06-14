// src/main/java/grupo5/gestion_inventario/superpanel/controller/GlobalMetricsController.java
package grupo5.gestion_inventario.superpanel.controller;

import grupo5.gestion_inventario.superpanel.dto.GlobalMetricsDTO;
import grupo5.gestion_inventario.superpanel.service.GlobalMetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/reports")
public class GlobalMetricsController {

    private final GlobalMetricsService service;

    public GlobalMetricsController(GlobalMetricsService service) {
        this.service = service;
    }

    /** GET /admin/reports/summary */
    @GetMapping("/summary")
    public ResponseEntity<GlobalMetricsDTO> summary() {
        return ResponseEntity.ok(service.summary());
    }
}
