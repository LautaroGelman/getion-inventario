package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.ClientDashboardDto;
import grupo5.gestion_inventario.clientpanel.dto.ProductDto;
import grupo5.gestion_inventario.clientpanel.dto.ProfitabilitySummaryDto;
import grupo5.gestion_inventario.clientpanel.dto.SaleDto;
import grupo5.gestion_inventario.clientpanel.dto.SalesDailySummaryDto; // <-- IMPORT AÑADIDO
import grupo5.gestion_inventario.model.BusinessAccount;
import grupo5.gestion_inventario.repository.BusinessAccountRepository;
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

    private final BusinessAccountRepository businessAccountRepo;
    private final ProductService   productService;
    private final SalesService     salesService;

    public ClientDashboardController(
            BusinessAccountRepository businessAccountRepo,
            ProductService productService,
            SalesService salesService) {
        this.businessAccountRepo     = businessAccountRepo;
        this.productService = productService;
        this.salesService   = salesService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<ClientDashboardDto> dashboard(Authentication auth) {
        BusinessAccount client = businessAccountRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        long lowStock   = productService.countLowStock(client.getId());
        long salesToday = salesService.countSalesToday(client.getId());

        ClientDashboardDto dto = new ClientDashboardDto(lowStock, salesToday);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/dashboard/profitability-summary")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<List<ProfitabilitySummaryDto>> getProfitabilitySummary(
            @RequestParam(defaultValue = "30") int days,
            Authentication auth) {
        BusinessAccount client = businessAccountRepo.findByEmail(auth.getName())
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
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<List<SalesDailySummaryDto>> getSalesSummary(
            @RequestParam(defaultValue = "30") int days,
            Authentication auth) {
        BusinessAccount client = businessAccountRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<SalesDailySummaryDto> summary = salesService.summaryLastDays(client.getId(), days);
        return ResponseEntity.ok(summary);
    }


    @GetMapping("/items")
    public ResponseEntity<List<ProductDto>> items(Authentication auth) {
        BusinessAccount client = businessAccountRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<ProductDto> products = productService.findByClientId(client.getId());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/sales")
    public ResponseEntity<List<SaleDto>> sales(Authentication auth) {
        BusinessAccount client = businessAccountRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<SaleDto> sales = salesService.findByClientId(client.getId());
        return ResponseEntity.ok(sales);
    }
}