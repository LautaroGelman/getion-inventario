package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.clientpanel.dto.EmployeeRequest;
import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.model.Role;
import grupo5.gestion_inventario.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/employees")
@PreAuthorize("hasAuthority('ADMIN')")
public class EmployeeController {

    private final EmployeeService service;
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(EmployeeService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody EmployeeRequest req) {
        Employee e = new Employee();
        e.setName(req.getName());
        e.setEmail(req.getEmail());
        e.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        e.setRole(req.getRole() != null ? req.getRole() : Role.CASHIER);
        return new ResponseEntity<>(service.create(e), HttpStatus.CREATED);
    }
}
