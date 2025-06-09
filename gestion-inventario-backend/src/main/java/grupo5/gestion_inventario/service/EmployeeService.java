package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import grupo5.gestion_inventario.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;
    private final ClientRepository clientRepo;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository repo,
                           ClientRepository clientRepo,
                           PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.clientRepo = clientRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Employee create(Long clientId, Employee e) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        e.setClient(client);
        e.setPasswordHash(passwordEncoder.encode(e.getPasswordHash()));
        return repo.save(e);
    }

    public Optional<Employee> findById(Long id) {
        return repo.findById(id);
    }

    public Employee update(Long clientId, Long id, Employee data) {
        Employee existing = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        if (!existing.getClient().getId().equals(clientId)) {
            throw new IllegalArgumentException("Sin permiso para modificar este empleado");
        }
        existing.setName(data.getName());
        existing.setEmail(data.getEmail());
        if (data.getPasswordHash() != null && !data.getPasswordHash().isBlank()) {
            existing.setPasswordHash(passwordEncoder.encode(data.getPasswordHash()));
        }
        existing.setRole(data.getRole());
        return repo.save(existing);
    }

    public List<Employee> listByClient(Long clientId) {
        return repo.findAll().stream()
                .filter(emp -> emp.getClient() != null && emp.getClient().getId().equals(clientId))
                .toList();
    }

    public void delete(Long employeeId) {
        repo.deleteById(employeeId);
    }
}
