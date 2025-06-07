
// src/main/java/grupo5/gestion_inventario/superpanel/controller/PaymentPlanController.java
package grupo5.gestion_inventario.superpanel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import grupo5.gestion_inventario.superpanel.model.PaymentPlan;
import grupo5.gestion_inventario.superpanel.service.PaymentPlanService;

@RestController
@RequestMapping("/admin/plans")
public class PaymentPlanController {

    private final PaymentPlanService service;

    public PaymentPlanController(PaymentPlanService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentPlan> create(@RequestBody PaymentPlan plan) {
        PaymentPlan created = service.create(plan);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<PaymentPlan>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentPlan> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentPlan> update(@PathVariable Long id,
                                              @RequestBody PaymentPlan plan) {
        return service.update(id, plan)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}