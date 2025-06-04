package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.SalesDailySummaryDto;
import grupo5.gestion_inventario.service.SalesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients/{clientId}/sales")
public class SalesMetricsController {

    private final SalesService service;

    public SalesMetricsController(SalesService service) {
        this.service = service;
    }

    @GetMapping("/summary")
    public ResponseEntity<List<SalesDailySummaryDto>> getSummary(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "30") int days) {

        return ResponseEntity.ok(service.summaryLastDays(clientId, days));
    }
}

