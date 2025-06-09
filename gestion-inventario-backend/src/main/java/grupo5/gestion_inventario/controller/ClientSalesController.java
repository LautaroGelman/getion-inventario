package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.SaleDto;
import grupo5.gestion_inventario.clientpanel.dto.SaleRequest;
import grupo5.gestion_inventario.service.SalesService;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/sales")
public class ClientSalesController {

    private final SalesService     salesService;
    private final EmployeeRepository employeeRepo;

    public ClientSalesController(SalesService salesService,
                                 EmployeeRepository employeeRepo) {
        this.salesService = salesService;
        this.employeeRepo   = employeeRepo;
    }

    @PostMapping
    public ResponseEntity<SaleDto> createSale(@RequestBody SaleRequest request,
                                              Authentication auth) {
        // Busca el ID del cliente (Long) usando el usuario autenticado
        Long clientId = employeeRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"))
                .getId();

        // Llama al servicio pasándole el clientId (Long), que es lo que espera
        SaleDto dto = salesService.createSale(clientId, request);
        return ResponseEntity.status(201).body(dto);
    }
}