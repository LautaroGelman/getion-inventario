package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.PurchaseOrderRequest;
import grupo5.gestion_inventario.clientpanel.model.PurchaseOrder;
import grupo5.gestion_inventario.config.JwtUtil; // IMPORT AÑADIDO
import grupo5.gestion_inventario.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // IMPORT AÑADIDO
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MULTIFUNCION')") // Seguridad a nivel de clase
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private JwtUtil jwtUtil; // IMPORT AÑADIDO para extraer datos del token

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders(@RequestHeader("Authorization") String token) {
        // Obtenemos el clientId desde el token para seguridad
        Long clientId = jwtUtil.extractClientId(token.substring(7));
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.findAllByClientId(clientId);
        return ResponseEntity.ok(purchaseOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        // Esta lógica ahora funciona porque el servicio devuelve un Optional
        return purchaseOrderService.getPurchaseOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(
            @RequestBody PurchaseOrderRequest request,
            @RequestHeader("Authorization") String token) {
        // CORRECCIÓN: Se extrae el clientId del token y se pasa al servicio
        Long clientId = jwtUtil.extractClientId(token.substring(7));
        PurchaseOrder newPurchaseOrder = purchaseOrderService.createPurchaseOrder(clientId, request);
        return new ResponseEntity<>(newPurchaseOrder, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/receive")
    public ResponseEntity<PurchaseOrder> receivePurchaseOrder(@PathVariable Long id) {
        PurchaseOrder updatedPurchaseOrder = purchaseOrderService.receivePurchaseOrder(id);
        return ResponseEntity.ok(updatedPurchaseOrder);
    }
}