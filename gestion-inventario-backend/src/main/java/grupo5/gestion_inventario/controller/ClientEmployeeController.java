package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/employees")
@PreAuthorize("hasRole('CLIENT')")
public class ClientEmployeeController {

    private final EmployeeService service;

    public ClientEmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> list(Authentication auth) {
        Long clientId = (Long) auth.getDetails();
        List<Employee> list = service.listByClient(clientId);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Employee> create(@RequestBody Employee e,
                                           Authentication auth) {
        Long clientId = (Long) auth.getDetails();
        Employee created = service.create(clientId, e);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
