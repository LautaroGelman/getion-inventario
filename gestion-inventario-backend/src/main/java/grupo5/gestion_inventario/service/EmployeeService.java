package grupo5.gestion_inventario.service;

import grupo5.gestion_inventario.model.Employee;
import grupo5.gestion_inventario.model.Client;
import grupo5.gestion_inventario.repository.EmployeeRepository;
import grupo5.gestion_inventario.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public EmployeeService(EmployeeRepository repo, ClientRepository clientRepo) {
        this.repo = repo;
        this.clientRepo = clientRepo;
    }

    public Employee create(Long clientId, Employee e) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        e.setClient(client);
        e.setPasswordHash(passwordEncoder.encode(e.getPasswordHash()));
        return repo.save(e);
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
