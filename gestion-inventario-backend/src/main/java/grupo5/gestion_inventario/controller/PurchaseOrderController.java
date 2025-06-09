package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.PurchaseOrderRequest;
import grupo5.gestion_inventario.clientpanel.model.PurchaseOrder;
import grupo5.gestion_inventario.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // Permitir peticiones desde el frontend de React
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping("/clients/{clientId}/purchase-orders")
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders(@PathVariable Long clientId) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders(clientId);
        return ResponseEntity.ok(purchaseOrders);
    }

    @GetMapping("/purchase-orders/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        return purchaseOrderService.getPurchaseOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/purchase-orders")
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrderRequest request) {
        PurchaseOrder newPurchaseOrder = purchaseOrderService.createPurchaseOrder(request);
        return new ResponseEntity<>(newPurchaseOrder, HttpStatus.CREATED);
    }

    @PostMapping("/purchase-orders/{id}/receive")
    public ResponseEntity<PurchaseOrder> receivePurchaseOrder(@PathVariable Long id) {
        PurchaseOrder updatedPurchaseOrder = purchaseOrderService.receivePurchaseOrder(id);
        return ResponseEntity.ok(updatedPurchaseOrder);
    }
}