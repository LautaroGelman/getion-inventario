package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.model.EndCustomer;
import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import grupo5.gestion_inventario.service.EndCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/end-customers")
public class ClientEndCustomerController {
    private final EndCustomerService service;
    private final EmployeeRepository employeeRepo;

    public ClientEndCustomerController(EndCustomerService service, EmployeeRepository employeeRepo) {
        this.service = service;
        this.employeeRepo = employeeRepo;
    }

    private Employee getAuthClient(Authentication auth) {
        return employeeRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    @GetMapping
    public ResponseEntity<List<EndCustomer>> list(Authentication auth) {
        Employee client = getAuthClient(auth);
        return ResponseEntity.ok(service.listByClient(client.getId()));
    }

    @PostMapping
    public ResponseEntity<EndCustomer> create(@RequestBody EndCustomer data, Authentication auth) {
        Employee client = getAuthClient(auth);
        EndCustomer created = service.create(client, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EndCustomer> get(@PathVariable Long id, Authentication auth) {
        Employee client = getAuthClient(auth);
        return service.findByIdAndClient(id, client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EndCustomer> update(@PathVariable Long id,
                                              @RequestBody EndCustomer data,
                                              Authentication auth) {
        Employee client = getAuthClient(auth);
        return service.update(id, data, client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        Employee client = getAuthClient(auth);
        if (service.delete(id, client)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
