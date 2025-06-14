package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.ClientDashboardDto;
import grupo5.gestion_inventario.clientpanel.dto.ProductDto;
import grupo5.gestion_inventario.clientpanel.dto.ProfitabilitySummaryDto;
import grupo5.gestion_inventario.clientpanel.dto.SaleDto;
import grupo5.gestion_inventario.clientpanel.dto.SalesDailySummaryDto; // <-- IMPORT AÑADIDO
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.ClientRepository;
import grupo5.gestion_inventario.service.ProductService;
import grupo5.gestion_inventario.service.SalesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@PreAuthorize("hasRole('CLIENT')")
public class ClientDashboardController {

    private final ClientRepository clientRepo;
    private final ProductService   productService;
    private final SalesService     salesService;

    public ClientDashboardController(
            ClientRepository clientRepo,
            ProductService productService,
            SalesService salesService) {
        this.clientRepo     = clientRepo;
        this.productService = productService;
        this.salesService   = salesService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ClientDashboardDto> dashboard(Authentication auth) {
        Client client = clientRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        long lowStock   = productService.countLowStock(client.getId());
        long salesToday = salesService.countSalesToday(client.getId());

        ClientDashboardDto dto = new ClientDashboardDto(lowStock, salesToday);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/dashboard/profitability-summary")
    public ResponseEntity<List<ProfitabilitySummaryDto>> getProfitabilitySummary(
            @RequestParam(defaultValue = "30") int days,
            Authentication auth) {
        Client client = clientRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<ProfitabilitySummaryDto> summary = salesService.getProfitabilitySummaryLastDays(client.getId(), days);
        return ResponseEntity.ok(summary);
    }

    // --- ¡NUEVO ENDPOINT AÑADIDO PARA CORREGIR EL BUG! ---
    /**
     * Datos para el gráfico de volumen de ventas e importe.
     * GET /client/sales/summary?days=30
     */
    @GetMapping("/sales/summary")
    public ResponseEntity<List<SalesDailySummaryDto>> getSalesSummary(
            @RequestParam(defaultValue = "30") int days,
            Authentication auth) {
        Client client = clientRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<SalesDailySummaryDto> summary = salesService.summaryLastDays(client.getId(), days);
        return ResponseEntity.ok(summary);
    }


    @GetMapping("/items")
    public ResponseEntity<List<ProductDto>> items(Authentication auth) {
        Client client = clientRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<ProductDto> products = productService.findByClientId(client.getId());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/sales")
    public ResponseEntity<List<SaleDto>> sales(Authentication auth) {
        Client client = clientRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<SaleDto> sales = salesService.findByClientId(client.getId());
        return ResponseEntity.ok(sales);
    }
}