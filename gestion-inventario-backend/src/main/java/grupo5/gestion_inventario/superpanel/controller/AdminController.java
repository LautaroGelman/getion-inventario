package grupo5.gestion_inventario.superpanel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import grupo5.gestion_inventario.superpanel.model.CustomerAccount;
import grupo5.gestion_inventario.superpanel.service.CustomerAccountService;

@RestController
@RequestMapping("/admin/accounts")
public class AdminController {

    private final CustomerAccountService service;

    public AdminController(CustomerAccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CustomerAccount> create(@RequestBody CustomerAccount acc) {
        CustomerAccount created = service.create(acc);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<CustomerAccount>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerAccount> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerAccount> update(@PathVariable Long id,
                                                  @RequestBody CustomerAccount acc) {
        return service.update(id, acc)
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
