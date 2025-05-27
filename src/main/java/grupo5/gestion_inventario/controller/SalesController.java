/// src/main/java/grupo5/gestion_inventario/controller/SalesController.java
package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.SaleDto;
import grupo5.gestion_inventario.clientpanel.dto.SaleRequest;
import grupo5.gestion_inventario.service.SalesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients/{clientId}/sales")
public class SalesController {

    private final SalesService service;

    public SalesController(SalesService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SaleDto> create(@PathVariable Long clientId,
                                          @RequestBody SaleRequest request) {
        // Llama a createSale (que descuenta stock y devuelve DTO)
        SaleDto dto = service.createSale(clientId, request);
        return ResponseEntity.status(201).body(dto);
    }
}
