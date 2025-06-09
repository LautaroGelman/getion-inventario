package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.SaleReturnRequest;
import grupo5.gestion_inventario.clientpanel.model.SaleReturn;
import grupo5.gestion_inventario.clientpanel.repository.SaleReturnRepository;
import grupo5.gestion_inventario.repository.BusinessAccountRepository;
import grupo5.gestion_inventario.service.SaleReturnService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/sale-returns")
public class ClientSaleReturnController {

    private final SaleReturnService saleReturnService;
    private final BusinessAccountRepository businessAccountRepo;

    public ClientSaleReturnController(SaleReturnService saleReturnService,
                                      BusinessAccountRepository businessAccountRepo) {
        this.saleReturnService = saleReturnService;
        this.businessAccountRepo = businessAccountRepo;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MANAGER','CASHIER')")
    public ResponseEntity<SaleReturn> createReturn(@RequestBody SaleReturnRequest request,
                                                   Authentication auth) {
        Long clientId = businessAccountRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"))
                .getId();
        SaleReturn created = saleReturnService.createReturn(clientId, request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER','CASHIER')")
    public ResponseEntity<List<SaleReturn>> list(Authentication auth) {
        Long clientId = businessAccountRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"))
                .getId();
        List<SaleReturn> returns = saleReturnService.findByClientId(clientId);
        return ResponseEntity.ok(returns);
    }
}
