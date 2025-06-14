package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.SaleReturnRequest;
import grupo5.gestion_inventario.clientpanel.model.SaleReturn;
import grupo5.gestion_inventario.service.SaleReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/returns")
@CrossOrigin(origins = "http://localhost:5173")
public class SaleReturnController {

    @Autowired
    private SaleReturnService saleReturnService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'CAJERO', 'MULTIFUNCION')")
    public ResponseEntity<SaleReturn> createReturn(@RequestBody SaleReturnRequest saleReturnRequest) {
        // se maneja con @PreAuthorize.
        SaleReturn createdReturn = saleReturnService.createSaleReturn(saleReturnRequest);
        return ResponseEntity.ok(createdReturn);
    }
}