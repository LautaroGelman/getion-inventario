package grupo5.gestion_inventario.controller;

import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.service.EmployeeService;
import grupo5.gestion_inventario.clientpanel.dto.EmployeeDto;
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
    public ResponseEntity<List<EmployeeDto>> list(Authentication auth) {
        Long clientId = (Long) auth.getDetails();
        List<EmployeeDto> list = service.listByClient(clientId).stream()
                .map(emp -> new EmployeeDto(emp.getId(), emp.getName(), emp.getEmail(), emp.getRole()))
                .toList();
    public ResponseEntity<List<Employee>> list(Authentication auth) {
        Long clientId = (Long) auth.getDetails();
        List<Employee> list = service.listByClient(clientId);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<EmployeeDto> create(@RequestBody Employee e,
                                              Authentication auth) {
        Long clientId = (Long) auth.getDetails();
        Employee created = service.create(clientId, e);
        EmployeeDto dto = new EmployeeDto(created.getId(), created.getName(), created.getEmail(), created.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
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
